package com.power.annotation;


import com.power.enums.I18nTableEnums;
import com.power.enums.I18nTypeEnums;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
public @interface PowerI18n {

    I18nTableEnums tableName() default I18nTableEnums.INTERNATIONAL;

    String orgIdField() default "orgId";

    String bizIdField() default "id";

    String localeField() default "locale";

    I18nTypeEnums type();

    /**
     * 默认查询有效  0：有效 1：无效 -1：查询全部
     * @return
     */
    int status() default 0;

    boolean enable() default true;

}
