package com.academy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Api("学生实体对象")
@Data
@TableName("sc_sys_menu")
public class SysMenu implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**  */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 菜单编码 */
    private String menuCode;

    /** 系统编号 */
    private String systemCode;

    /** 菜单名称 */
    private String menuName;

    /** 上级菜单编码 */
    private String parentCode;

    /** 排序 */
    private String sort;

    /** 图标 */
    private String icon;

    /** 路由地址 */
    private String path;

    /** 菜单等级 0系统1模块、2页面、3按钮 */
    private Integer menuLevel;

    /** 创建时间 */
    private Date createTime;

    /** 创建人编码 */
    private String createCode;

    /** 修改时间 */
    private Date updateTime;

    /** 修改人编码 */
    private String updateCode;

    /** 状态 Y 有效 N 无效 */
    private String status;

}
