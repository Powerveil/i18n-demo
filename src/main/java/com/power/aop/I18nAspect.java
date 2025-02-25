package com.power.aop;


import com.power.annotation.PowerI18n;
import com.power.enums.I18nTableEnums;
import com.power.enums.I18nTypeEnums;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

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

    @Pointcut("@annotation(com.power.annotation.PowerI18n)")
    public void pt() {}

    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
//        log.info("限流开始!");
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        PowerI18n classPowerI18n = getClassPowerI18n(joinPoint);
        PowerI18n methodPowerI18n = getMethodPowerI18n(joinPoint);


        I18nTableEnums i18nTableEnums = classPowerI18n.tableName();
        String orgIdField = classPowerI18n.orgIdField();
        String bizIdField = classPowerI18n.bizIdField();
        String localeField = classPowerI18n.localeField();
        I18nTypeEnums typeField = classPowerI18n.type();
        int status = classPowerI18n.status();

        Object proceed = joinPoint.proceed();


        // 先处理单个的
        // 处理单个数据和List

        // 国际化处理
        if (classPowerI18n.enable()) {

        }


//        log.info("限流通过!");
        return proceed;
    }



    private PowerI18n getClassPowerI18n(ProceedingJoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getAnnotation(PowerI18n.class);
    }

    private PowerI18n getMethodPowerI18n(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        PowerI18n annotation = methodSignature.getMethod().getAnnotation(PowerI18n.class);
        return annotation;
    }

}
