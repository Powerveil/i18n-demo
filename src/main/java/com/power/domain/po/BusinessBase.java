package com.power.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName business_base
 */
@TableName(value ="business_base")
@Data
public class BusinessBase implements Serializable {
    /**
     * 业务唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属租户ID（与国际化表租户体系一致）
     */
    @TableField(value = "org_id")
    private String orgId;

    /**
     * 业务名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 业务简介
     */
    @TableField(value = "description")
    private String description;

    /**
     * 1:已删除 0:正常
     */
    @TableField(value = "deleted")
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}