package com.power.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName organization
 */
@TableName(value ="organization")
@Data
public class Organization {
    /**
     * 组织id
     */
    @TableId
    private String org_id;

    /**
     * 
     */
    private String main_locale;
}