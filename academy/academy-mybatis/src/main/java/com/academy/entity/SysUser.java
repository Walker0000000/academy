package com.academy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sc_sys_user")
public class SysUser implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**  */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 登陆账号 */
    private String loginName;

    /** 用户编码 */
    private String userType;

    /** 用户编码 */
    private String userCode;

    /** 微信唯一码 */
    private String openId;

    /** 用户名 */
    private String userName;

    /** 密码 */
    private String password;

    /** 电话 */
    private String phone;

    /**
     * 微信头像
     */
    private String avatar;

    /** 昵称 */
    private String nickname;

    /** 邮箱 */
    private String email;

    /** 性别 0男性1女性 */
    private Integer sex;

    /** 状态 N 无效 Y有效 */
    private String status;

    /** 创建人 */
    private String createCode;

    /** 创建时间 */
    private Date createTime;

    /** 更新人 */
    private String updateCode;

    /** 更新时间 */
    private Date updateTime;

    /** 资源服务器 */
    @TableField(exist = false)
    private String showImg;


}
