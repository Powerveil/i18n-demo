package com.power.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
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
    @TableField(value = "org_id")
    private String orgId;

    /**
     * 
     */
    @TableField(value = "main_locale")
    private String mainLocale;
}