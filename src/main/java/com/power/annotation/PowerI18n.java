package com.power.annotation;


import com.power.enums.I18nTableEnums;
import com.power.enums.I18nTypeEnums;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface PowerI18n {

    /**
     * 表名
     * @return
     */
    I18nTableEnums tableName() default I18nTableEnums.INTERNATIONAL;

    /**
     * 国际化类型，一般一个业务表对应一个type
     * @return
     */
    I18nTypeEnums type();

//    /**
//     * org_id对应对象中的字段名称
//     * @return
//     */
//    String orgIdField() default "orgId";

    /**
     * 业务id对应对象中的字段名称
     * @return
     */
    String bizIdField();

    /**
     * 默认查询有效  0：有效 1：无效 -1：查询全部
     * @return
     */
    int disabled() default 0;

//    /**
//     * 是否值查询一个语种 一条数据
//     * @return
//     */
//    boolean isSingle() default true;

}
