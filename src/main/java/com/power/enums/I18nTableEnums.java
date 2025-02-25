package com.power.enums;


import lombok.Getter;

/**
 * @ClassName I18nTableEnums
 * @Description TODO
 * @Author power
 * @Date 2025/2/26 1:08
 * @Version 1.0
 */
@Getter
public enum I18nTableEnums {
    INTERNATIONAL("international")
    ;

    private I18nTableEnums(String tableName) {
        this.tableName = tableName;
    }

    private final String tableName;
}
