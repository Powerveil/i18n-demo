package com.power.aop;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.power.annotation.I18n;
import com.power.annotation.PowerI18n;
import com.power.common.Result;
import com.power.service.I18nService;
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

    @Pointcut("@annotation(com.power.annotation.I18n)")
    public void pt() {
    }

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

        this.extractFieldValue(proceed, orgId, locale);

        return proceed;
    }


    @SneakyThrows
    private void extractFieldValue(Object proceed, String orgId, String locale) {
        // todo 这里如果解析错误，做解析适应自己项目的类就行

        // 可自定义标准返回类
        if (proceed instanceof Result) {
            proceed = ((Result<?>) proceed).getData();
        }

        if (proceed instanceof Collection) {
            // todo 待优化
            this.extractBatchFieldValue((Collection<?>) proceed, orgId, locale);
            return;
        }
        // todo Page -> proceed = (Page) proceed.getRecords()

        // todo 默认支持简单的 单个对象，集合(List)  暂不支持嵌套
        // 获取所有字段(包括私有字段)
        Class<?> clazz = proceed.getClass();
        this.executeClassField(clazz);

        Map<String, Field> fieldMap = classFieldCache.getOrDefault(clazz, new LinkedHashMap<>());

        Map<Field, PowerI18n> hasPowerI18nMap = powerI18nFieldsCache.getOrDefault(clazz, new LinkedHashMap<>());

        for (Map.Entry<Field, PowerI18n> fieldPowerI18nEntry : hasPowerI18nMap.entrySet()) {
            Field field = fieldPowerI18nEntry.getKey();
            PowerI18n powerI18n = fieldPowerI18nEntry.getValue();

            String tableName = powerI18n.tableName().getTableName();
            Integer type = powerI18n.type().getType();
            int disabled = powerI18n.disabled();

            Field biaIdFiled = fieldMap.get(powerI18n.bizIdField());
            biaIdFiled.setAccessible(true);
            Long bizId = (Long) biaIdFiled.get(proceed);

            String content = i18nService.getContent(tableName, orgId, bizId, type, locale, disabled);
            field.setAccessible(true);
            field.set(proceed, content);
        }
    }

    @SneakyThrows
    private void extractBatchFieldValue(Collection<?> proceedCollection, String orgId, String locale) {
        if (CollUtil.isEmpty(proceedCollection)) {
            return;
        }
        List<?> list = CollUtil.newArrayList(proceedCollection);
        Class<?> clazz = list.get(0).getClass();
        this.executeClassField(clazz);
        Map<String, Field> fieldMap = classFieldCache.getOrDefault(clazz, new LinkedHashMap<>());

        // 一个类有多个带这个注解的字段（todo 注意目前支持一层）
        Map<Field, PowerI18n> hasPowerI18nMap = powerI18nFieldsCache.getOrDefault(clazz, new LinkedHashMap<>());

        for (Map.Entry<Field, PowerI18n> fieldPowerI18nEntry : hasPowerI18nMap.entrySet()) {
            Field field = fieldPowerI18nEntry.getKey();
            PowerI18n powerI18n = fieldPowerI18nEntry.getValue();

            // 现在获取了字段的信息，和一些注解里的死值  ====================================================================================================
            String tableName = powerI18n.tableName().getTableName();
            Integer type = powerI18n.type().getType();
            int disabled = powerI18n.disabled();

            Field bizIdFiled = fieldMap.get(powerI18n.bizIdField());
            bizIdFiled.setAccessible(true);

            // 获取变值
            List<Long> bizIdList = CollUtil.newArrayList();

            for (Object object : list) {
                Long bizId = (Long) bizIdFiled.get(object);
                bizIdList.add(bizId);
            }
            //
            Map<String, String> map = i18nService.getContent(tableName, orgId, bizIdList, type, locale, disabled);

            for (Object object : list) {
                Long bizId = (Long) bizIdFiled.get(object);

                String key = orgId + bizId + type + locale;
                String content = map.get(key);

                field.setAccessible(true);
                field.set(object, content);
            }
        }

    }


    private void executeClassField(Class<?> clazz) {
        // 获取所有字段(包括私有字段)

        Map<String, Field> fieldMap = classFieldCache.getOrDefault(clazz, new LinkedHashMap<>());

        Map<Field, PowerI18n> hasPowerI18nMap = powerI18nFieldsCache.getOrDefault(clazz, new LinkedHashMap<>());

        if (MapUtil.isNotEmpty(fieldMap) && MapUtil.isNotEmpty(hasPowerI18nMap)) {
            return;
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            fieldMap.put(field.getName(), field);
            PowerI18n powerI18n = field.getAnnotation(PowerI18n.class);
            if (powerI18n != null) {
                field.setAccessible(true);
                hasPowerI18nMap.put(field, powerI18n);
            }
        }
        classFieldCache.put(clazz, fieldMap);
        powerI18nFieldsCache.put(clazz, hasPowerI18nMap);
    }

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
