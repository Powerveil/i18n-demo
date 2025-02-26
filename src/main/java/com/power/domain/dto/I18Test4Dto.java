package com.power.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class I18Test4Dto {
    private List<Long> idList;
    private String orgIdIkun;
    private String localeIkun;
}