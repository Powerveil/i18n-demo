package com.power.aop;


import cn.hutool.json.JSONUtil;
import com.power.annotation.I18n;
import com.power.annotation.PowerI18n;
import com.power.enums.I18nTableEnums;
import com.power.enums.I18nTypeEnums;
import com.power.mapper.I18nMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName I18nAspect
 * @Description TODO
 * @Author power
 * @Date 2025/2/26 1:23
 * @Version 1.0
 */
@Component
@Aspect
@Slf4j
public class I18nAspect {

    @Autowired
    private I18nMapper i18nMapper;


    @Pointcut("@annotation(com.power.annotation.I18n)")
    public void pt() {
    }

//    @Around("pt()")
//    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
////        log.info("限流开始!");
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

    /// /        log.info("限流通过!");
//        return proceed;
//    }
    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {

        Object proceed = joinPoint.proceed();

        I18n methodI18n = getMethodI18n(joinPoint);
        // 国际化处理
        if (!methodI18n.enable()) {
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            log.info("{} 国际化未启用~~~", method.getName());
            return proceed;
        }
//        String orgId = request.getParameter(methodI18n.orgIdField());
//        String locale = request.getParameter(methodI18n.localeField());
        Object arg = joinPoint.getArgs()[0];

        String orgId = JSONUtil.parseObj(arg).getStr(methodI18n.orgIdField());;
        String locale = JSONUtil.parseObj(arg).getStr(methodI18n.localeField());

        // todo 默认单个对象，非集合或嵌套
        Class<?> clazz = proceed.getClass();

        // 获取所有字段(包括私有字段)
        Field[] fields = clazz.getDeclaredFields();

        Map<String, Field> fieldMap = new HashMap<>();

        for (Field field : fields) {
            fieldMap.put(field.getName(), field);
        }


        for (Field field : fields) {
            // 检查字段是否有@I18n注解
            PowerI18n powerI18n = field.getAnnotation(PowerI18n.class);
            if (powerI18n != null) {
                String tableName = powerI18n.tableName().getTableName();
                Integer type = powerI18n.type().getType();

                Field field1 = fieldMap.get(powerI18n.bizIdField());
                field1.setAccessible(true);

                Long bizId = (Long) field1.get(proceed);

                String content = i18nMapper.queryItemContent(tableName, orgId, bizId, type, locale);
                field.setAccessible(true);
                field.set(proceed, content);
            }
        }

//            // 处理带有@I18n注解的字段
//            field.setAccessible(true);
//            try {
//                Object fieldValue = field.get(proceed);
//                if (fieldValue instanceof String) {
//                    // 获取国际化值
////                    String i18nValue = i18nService.getI18nMessage((String) fieldValue, i18nAnnotation.value());
//                    // 设置国际化后的值
////                    field.set(proceed, i18nValue);
//                }
//            } catch (IllegalAccessException e) {
//                log.error("Failed to process i18n field: {}", field.getName(), e);
//            }








        return proceed;
    }

    private I18n getMethodI18n(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        I18n annotation = methodSignature.getMethod().getAnnotation(I18n.class);
        return annotation;
    }

}
