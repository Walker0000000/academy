package com.academy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程标签表
 */
@Api("课程标签表")
@Data
@TableName("t_label")
public class Label implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标签编码
     */
    private String code;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 状态（0：删除、1：启用）
     */
    private Integer status;

    /**
     * 创建者
     */
    private String createUserCode;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改者
     */
    private String updateUserCode;

    /**
     * 修改时间
     */
    private Date updateTime;

}
