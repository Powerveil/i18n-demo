package com.power.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 国际化信息表
 * @TableName international
 */
@TableName(value ="international")
@Data
public class International implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户id
     */
    @TableField(value = "org_id")
    private String orgId;

    /**
     * 业务id
     */
    @TableField(value = "biz_id")
    private Long bizId;

    /**
     * 国际化内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 类型
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 1：停用 0：启用
     */
    @TableField(value = "disabled")
    private Integer disabled;

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