package com.academy.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.academy.entity.SysUser;
import com.academy.entity.SysUserRoles;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * SysUser 表数据库控制层接口
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {


    List<JSONObject> getUserList(@Param("params") Map<String, Object> params);

    List<JSONObject> getUserRoleList(Map<String, Object> params);

    int insertUserRoleBatch(List<SysUserRoles> sysRoleMenusList);

    /**
     * 查询用户下拉框数据
     */
    List<JSONObject> getUserTreeList(Map<String, Object> contain);

    List<JSONObject> getUserList(Page page, @Param("params") Map<String, Object> params);

}