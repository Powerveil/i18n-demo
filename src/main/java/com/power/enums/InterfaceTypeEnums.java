package com.power.enums;

import lombok.Getter;

/**
 * 后续可以改为 判断请求路径
 */
@Getter
public enum InterfaceTypeEnums {
    /**
     * 业务唯一标识
     */
    OUTER("outer", ""),
    INNER("inner", ""),
    ;

    private String type;
    private String desc;

    InterfaceTypeEnums(String type, String desc) {
        this.type = type;
        this.type = type;
    }
}
