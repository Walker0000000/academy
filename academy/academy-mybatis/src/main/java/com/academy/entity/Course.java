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
 * 课程表
 */
@Api("课程表")
@Data
@TableName("t_course")
public class Course implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 课程编码
     */
    private String code;

    /**
     * 课程名称
     */
    private String name;

    /**
     * 所属分类编码
     */
    private String classCode;

    /**
     * 标签编码，多个用逗号隔开
     */
    private String labelCode;

    /**
     * 适学年龄段区间，多个用分号隔开
     */
    private String ages;

    /**
     * 课时
     */
    private Integer classHour;

    /**
     * 课次
     */
    private Integer classTime;

    /**
     * 状态（0：删除、1：待提交、2：审核中、3：审核通过、4：审核拒绝、5：已发布、6：已下架）
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
