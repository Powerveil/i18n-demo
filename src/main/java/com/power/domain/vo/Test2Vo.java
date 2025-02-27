package com.power.domain.vo;

import com.power.annotation.PowerI18n;
import com.power.enums.I18nTableEnums;
import com.power.enums.I18nTypeEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Test2Vo {
    /**
     * 业务唯一标识
     */
    private Long myId;

    /**
     * 所属租户ID（与国际化表租户体系一致）
     */
    private String orgId;


    @PowerI18n(tableName = I18nTableEnums.INTERNATIONAL, type = I18nTypeEnums.BUSINESS_BASE, bizIdField = "myId")
    private String i18nContent;

    @PowerI18n(tableName = I18nTableEnums.INTERNATIONAL, type = I18nTypeEnums.BUSINESS_BASE, bizIdField = "myId")
    private String i18nContent2;
}