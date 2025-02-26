package com.power.annotation;

import com.power.enums.I18nTableEnums;
import com.power.enums.I18nTypeEnums;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface I18n {

    /**
     * org_id对应对象中的字段名称
     * @return
     */
    String orgIdField() default "orgId";

    /**
     * 语言环境对应对象中的字段名称
     * @return
     */
    String localeField() default "locale";

//    /**
//     * 默认查询有效  0：有效 1：无效 -1：查询全部
//     * @return
//     */
//    int status() default 0;

    /**
     * 是否开启国际化
     * @return
     */
    boolean enable() default true;
}
