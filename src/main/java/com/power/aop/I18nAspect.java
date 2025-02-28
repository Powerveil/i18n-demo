package com.power.aop;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.power.annotation.I18n;
import com.power.annotation.PowerI18n;
import com.power.common.Result;
import com.power.enums.InterfaceTypeEnums;
import com.power.service.I18nService;
import com.power.service.OrganizationService;
import com.power.utils.ListUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
//import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName I18nAspect
 * @Description todo 写在前面
 * 本质也就是去查表，无非是关系型数据库和非关系型数据库，把这个操作在方法返回时做出来
 * 是去数据库找到数据对应语言环境的
 * 国际化处理切面类，用于处理带有@I18n注解的方法返回结果的字段国际化替换
 * @Author power
 * @Date 2025/2/26 1:23
 * @Version 1.0
 */
@Component
@Aspect
@Slf4j
public class I18nAspect {

    @Autowired
    private I18nService i18nService;

    @Autowired
    private OrganizationService organizationService;

    // 缓存类字段信息，避免重复反射
    /**
     * 每一个类的所有字段映射关系
     */
    private static final ConcurrentHashMap<Class<?>, Map<String, Field>> classFieldCache = new ConcurrentHashMap<>();
    /**
     * 每一个类所有需要国际化的字段
     */
    private static final ConcurrentHashMap<Class<?>, Map<Field, PowerI18n>> powerI18nFieldsCache = new ConcurrentHashMap<>();


//    final String SALT = "this is my salt";
//    final int MIN_HASH_LENGTH = 6;
//
//    Hashids hashids = new Hashids(SALT, MIN_HASH_LENGTH);

    /**
     * 定义切点，拦截所有被@I18n注解标记的方法
     */
    @Pointcut("@annotation(com.power.annotation.I18n)")
    public void pt() {
    }

    /**
     * 环绕通知，处理国际化逻辑
     * @param joinPoint 连接点对象，用于获取方法执行上下文
     * @return 处理后的方法返回值
     * @throws Throwable 可能抛出的异常
     */
    @Around("pt()")
    public Object i18nHandleContent(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = joinPoint.proceed();

        I18n methodI18n = getMethodI18n(joinPoint);
        // 国际化处理
        if (!methodI18n.enable()) {
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            log.info("{} 国际化未启用~~~", method.getName());
            return proceed;
        }

        Object arg = joinPoint.getArgs()[0];

        String orgId = JSONUtil.parseObj(arg).getStr(methodI18n.orgIdField());
        String locale = JSONUtil.parseObj(arg).getStr(methodI18n.localeField());
        boolean isFallback = false;
        if (InterfaceTypeEnums.OUTER.equals(methodI18n.interfaceType())) {
            isFallback = true;
        } else if (InterfaceTypeEnums.INNER.equals(methodI18n.interfaceType())) {
            isFallback = methodI18n.fallback();
        }

        this.extractFieldValue(proceed, orgId, locale, isFallback);

        return proceed;
    }


    /**
     * 提取并设置单个对象的国际化字段值
     * @param proceed 目标方法返回的对象
     * @param orgId 组织ID
     * @param locale 当前语言环境
     * @param isFallback 是否启用回退逻辑
     */
    @SneakyThrows
    private void extractFieldValue(Object proceed, String orgId, String locale, boolean isFallback) {
        // todo 这里如果解析错误，做解析适应自己项目的类就行

        // 可自定义标准返回类
        if (proceed instanceof Result) {
            proceed = ((Result<?>) proceed).getData();
        }

        if (proceed instanceof Collection) {
            // todo 待优化 isFallback
            this.extractBatchFieldValue((Collection<?>) proceed, orgId, locale, isFallback);
            return;
        }
        // todo Page -> proceed = (Page) proceed.getRecords()

        // todo 默认支持简单的 单个对象，集合(List)  暂不支持嵌套
        // 获取所有字段(包括私有字段)
        Class<?> clazz = proceed.getClass();
        this.executeClassField(clazz);

        Map<String, Field> fieldMap = classFieldCache.getOrDefault(clazz, new LinkedHashMap<>());

        Map<Field, PowerI18n> hasPowerI18nMap = powerI18nFieldsCache.getOrDefault(clazz, new LinkedHashMap<>());

        List<String> localeList = CollUtil.newArrayList(locale);
        if (isFallback) {
            localeList.add(organizationService.getMainLocaleByOrgId(orgId));
            localeList = ListUtils.removeDuplicatesAndFilterNulls(localeList);
        }

        for (Map.Entry<Field, PowerI18n> fieldPowerI18nEntry : hasPowerI18nMap.entrySet()) {
            Field field = fieldPowerI18nEntry.getKey();
            PowerI18n powerI18n = fieldPowerI18nEntry.getValue();

            String tableName = powerI18n.tableName().getTableName();
            Integer type = powerI18n.type().getType();
            int disabled = powerI18n.disabled();

            Field bizIdFiled = fieldMap.get(powerI18n.bizIdField());
            Long bizId = (Long) bizIdFiled.get(proceed);

            Map<String, String> contentMap = i18nService.getContent(tableName, orgId, bizId, type, localeList, disabled);
            String content = this.getContentFallback(orgId, localeList, bizId, type, contentMap);
            field.set(proceed, content);
        }
    }

    /**
     * 批量处理集合类型对象的国际化字段值
     * @param proceedCollection 目标方法返回的对象集合
     * @param orgId 组织ID
     * @param locale 当前语言环境
     * @param isFallback 是否启用回退逻辑
     */
    @SneakyThrows
    private void extractBatchFieldValue(Collection<?> proceedCollection, String orgId, String locale, boolean isFallback) {
        if (CollUtil.isEmpty(proceedCollection)) {
            return;
        }
        List<?> list = CollUtil.newArrayList(proceedCollection);
        Class<?> clazz = list.get(0).getClass();
        this.executeClassField(clazz);
        Map<String, Field> fieldMap = classFieldCache.getOrDefault(clazz, new LinkedHashMap<>());

        // 一个类有多个带这个注解的字段（todo 注意目前支持一层）
        Map<Field, PowerI18n> hasPowerI18nMap = powerI18nFieldsCache.getOrDefault(clazz, new LinkedHashMap<>());

        List<String> localeList = CollUtil.newArrayList(locale);
        if (isFallback) {
            localeList.add(organizationService.getMainLocaleByOrgId(orgId));
            localeList = ListUtils.removeDuplicatesAndFilterNulls(localeList);
        }

        for (Map.Entry<Field, PowerI18n> fieldPowerI18nEntry : hasPowerI18nMap.entrySet()) {
            Field field = fieldPowerI18nEntry.getKey();
            PowerI18n powerI18n = fieldPowerI18nEntry.getValue();

            // 现在获取了字段的信息，和一些注解里的死值  ====================================================================================================
            String tableName = powerI18n.tableName().getTableName();
            Integer type = powerI18n.type().getType();
            int disabled = powerI18n.disabled();

            Field bizIdFiled = fieldMap.get(powerI18n.bizIdField());

            // 获取变值
            List<Long> bizIdList = CollUtil.newArrayList();

            for (Object object : list) {
                Long bizId = (Long) bizIdFiled.get(object);
                bizIdList.add(bizId);
            }
            //
            Map<String, String> contentMap = i18nService.getContent(tableName, orgId, bizIdList, type, localeList, disabled);

            for (Object object : list) {
                Long bizId = (Long) bizIdFiled.get(object);
                String content = this.getContentFallback(orgId, localeList, bizId, type, contentMap);
                field.set(object, content);
            }
        }

    }

    /**
     * 根据语言环境列表获取字段内容的回退值
     * @param orgId 组织ID
     * @param localeList 语言环境优先级列表
     * @param bizId 业务ID
     * @param type 国际化类型
     * @param contentMap 国际化内容缓存
     * @return 匹配到的国际化内容
     */
    private String getContentFallback(String orgId, List<String> localeList, Long bizId, Integer type, Map<String, String> contentMap) {
        String content = null;

        for (String localeItem : localeList) {
            String key = orgId + bizId + type + localeItem;
            content = contentMap.get(key);
            if (content != null) {
                break;
            }
        }
        return content;
    }

    /**
     * 解析并缓存类的字段信息（包含@PowerI18n注解处理）
     * @param clazz 要处理的类类型
     */
    private void executeClassField(Class<?> clazz) {
        // 获取所有字段(包括私有字段)

        Map<String, Field> fieldMap = classFieldCache.getOrDefault(clazz, new LinkedHashMap<>());

        Map<Field, PowerI18n> hasPowerI18nMap = powerI18nFieldsCache.getOrDefault(clazz, new LinkedHashMap<>());

        if (MapUtil.isNotEmpty(fieldMap) && MapUtil.isNotEmpty(hasPowerI18nMap)) {
            return;
        }

        List<String> bizIdFieldStrList = CollUtil.newArrayList();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            fieldMap.put(field.getName(), field);
            PowerI18n powerI18n = field.getAnnotation(PowerI18n.class);
            if (powerI18n != null) {
                // 破坏注解的字段的封装性
                field.setAccessible(true);
                hasPowerI18nMap.put(field, powerI18n);

                bizIdFieldStrList.add(powerI18n.bizIdField());
            }
        }
        // 破坏注解中 bizId 的字段的封装性
        for (String bizIdFiledStr : bizIdFieldStrList) {
            fieldMap.get(bizIdFiledStr).setAccessible(true);
        }

        classFieldCache.put(clazz, fieldMap);
        powerI18nFieldsCache.put(clazz, hasPowerI18nMap);
    }

    /**
     * 获取方法上的@I18n注解实例
     * @param joinPoint 连接点对象
     * @return 方法上的I18n注解实例
     */
    private I18n getMethodI18n(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        I18n annotation = methodSignature.getMethod().getAnnotation(I18n.class);
        return annotation;
    }


    //    @Around("pt()")
//    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
//        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = requestAttributes.getRequest();
//
//        PowerI18n classPowerI18n = getClassPowerI18n(joinPoint);
//        PowerI18n methodPowerI18n = getMethodPowerI18n(joinPoint);
//
//        Object proceed = joinPoint.proceed();
//
//        // 先处理单个的
//        // 处理单个数据和List
//
//        // 国际化处理
//        if (!classPowerI18n.enable()) {
//            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
//            log.info("{} 国际化未启用~~~", method.getName());
//            return proceed;
//        }
//
//        // sdk？
//        String locale = "zh_CN";
//
//        // 注意这里处理的是出参
//        String tableName = classPowerI18n.tableName().getTableName();
//
//        String orgIdField = classPowerI18n.orgIdField();
//        String orgId = request.getParameter(orgIdField); // todo
//        //
//        String bizIdField = classPowerI18n.bizIdField();
//
//        Long bizId = Long.valueOf(request.getParameter(bizIdField));
//        String localeField = classPowerI18n.localeField();
//        I18nTypeEnums typeField = classPowerI18n.type();
//        int status = classPowerI18n.status();
//
//        i18nMapper.queryItemContent(tableName, orgId, bizId, typeField.getType(), locale);
//        // todo spel表达式，controller + response class
//        // todo 字段维度的 两个注解 1.用于 aop spel locale 2.具体的信息   暂时计划只支持 单个和 List (分页在思考) ，复杂嵌套目前还没有考虑
//
//

//        return proceed;
//    }
}
