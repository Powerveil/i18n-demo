package com.power.domain.dto;

import com.power.common.PageRequest;
import lombok.Data;

import java.util.List;

@Data
public class I18Test8Dto extends PageRequest {
    private List<Long> idList;
    private String orgIdIkun;
    private String localeIkun;

}
