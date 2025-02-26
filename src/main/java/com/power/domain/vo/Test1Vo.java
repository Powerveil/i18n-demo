package com.power.domain.vo;


import com.power.annotation.PowerI18n;
import com.power.enums.I18nTableEnums;
import com.power.enums.I18nTypeEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Test1Vo
 * @Description TODO
 * @Author power
 * @Date 2025/2/26 1:40
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Test1Vo {
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
}
