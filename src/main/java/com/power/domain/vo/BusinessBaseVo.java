package com.power.domain.vo;

import com.power.annotation.PowerI18n;
import com.power.enums.I18nTableEnums;
import com.power.enums.I18nTypeEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessBaseVo {
    private Long bbId;

    @PowerI18n(tableName = I18nTableEnums.INTERNATIONAL, type = I18nTypeEnums.BUSINESS_BASE, bizIdField = "bbId")
    private String content;

    private String description;
}
