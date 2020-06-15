package com.academy.service.system;

import com.alibaba.fastjson.JSONObject;
import com.academy.utils.ResultUtil;

import java.util.List;
import java.util.Map;

/**
 * 用户表
 *
 * @author flyCloud
 * @email
 * @date 2018-08-31 15:01:21
 */
public interface UserService {

    /**
     * 获取系统用户列表
     */
    ResultUtil getUserList(Map<String, Object> params);

    /**
     * 新增系统用户
     */
    ResultUtil insertUser(JSONObject person);

    /**
     * 根据条件获取系统用户
     */
    JSONObject getUserByMap(Map<String, Object> contain);

    /**
     * 编辑系统用户
     */
    ResultUtil updateUser(JSONObject person);

    /**
     * 获取用户系统角色
     */
    JSONObject getUserRoleByMap(Map<String, Object> contain);

    /**
     *
     *登录用户认证
     */
    ResultUtil userAuthorize(JSONObject person);

    /**
     * 查询用户下拉框数据
     */
    List<JSONObject> getUserTreeList(Map<String, Object> contain);

}

