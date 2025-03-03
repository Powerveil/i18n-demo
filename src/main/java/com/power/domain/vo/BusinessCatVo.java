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
public class BusinessCatVo {
    private Long bcId;

    @PowerI18n(tableName = I18nTableEnums.INTERNATIONAL, type = I18nTypeEnums.BUSINESS_CAT, bizIdField = "bcId")
    private String content;

    private String description;

}
