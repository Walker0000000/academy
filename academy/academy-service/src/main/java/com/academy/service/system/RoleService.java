package com.academy.service.system;

import com.alibaba.fastjson.JSONObject;
import com.academy.utils.ResultUtil;

import java.util.Map;

/**
 * 用户表
 *
 * @author flyCloud
 * @email
 * @date 2018-08-31 15:01:21
 */
public interface RoleService {

    /**
     * 获取系统角色列表
     */
    ResultUtil getRoleList(Map<String, Object> params);

    /**
     * 新增系统角色
     */
    ResultUtil insertRole(JSONObject person);

    /**
     * 获取当前角色的菜单信息
     */
    JSONObject getRoleMenuList(Map<String, Object> params);

    /**
     * 授权系统角色菜单
     */
    ResultUtil updateRole(JSONObject person);

    /**
     * 删除系统角色
     */
    ResultUtil deleteRole(Map<String, Object> params);
}

