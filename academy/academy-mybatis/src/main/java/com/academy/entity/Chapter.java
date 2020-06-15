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
 * 课程章节表
 */
@Api("课程章节表")
@Data
@TableName("t_chapter")
public class Chapter implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 章节编码
     */
    private String code;

    /**
     * 章节名称
     */
    private String name;

    /**
     * 所属课程编码
     */
    private String courseCode;

    /**
     * 正文
     */
    private String chapterBody;

    /**
     * 教案正文
     */
    private String teachingPlanBody;

    /**
     * 章节排序
     */
    private Integer sort;

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
