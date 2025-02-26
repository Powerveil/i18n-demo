package com.power.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName i18Test1Dto
 * @Description TODO
 * @Author power
 * @Date 2025/2/26 17:39
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class I18Test2Dto {
    private List<Long> idList;
    private String orgId;
    private String locale;
}
