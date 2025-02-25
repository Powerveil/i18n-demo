package com.power.enums;

import lombok.Getter;

@Getter
public enum I18nTypeEnums {
    /**
     * 业务唯一标识
     */
    BUSINESS_BASE("business_base", 1),
    ;

    private String tableName;
    private Integer type;

    I18nTypeEnums(String tableName, Integer type) {
        this.tableName = tableName;
        this.type = type;
    }

}
