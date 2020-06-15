package com.academy.mapper;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.academy.entity.SysRole;
import com.academy.entity.SysRoleMenus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * SysRole 表数据库控制层接口
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<JSONObject> getRoleList(@Param("params") Map<String, Object> params);

    List<JSONObject> getRoleMenuList(Map<String, Object> contain);

    int insertRoleMenusBatch(List<SysRoleMenus> sysRoleMenusList);

    List<JSONObject> getRoleList(Page page, @Param("params")Map<String, Object> params);
}