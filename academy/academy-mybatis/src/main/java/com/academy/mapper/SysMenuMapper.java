package com.academy.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.academy.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Enroll 表数据库控制层接口
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据用户角色获取菜单
     *
     * @param contain
     * @return
     */

    List<JSONObject> getSysMenuList(Map<String, Object> contain);
    /**
     * 根据用户角色获取菜单
     *
     * @param contain
     * @return
     */
    List<JSONObject> getSysMenuList(Page page, Map<String, Object> contain);

    /**
     * 根据条件获取菜单列表
     *
     * @param contain
     * @return
     */
    List<JSONObject> getMenuList(@Param("params")Map<String, Object> contain);


    List<JSONObject> getSysMenuTreeList(Map<String, Object> contain);


    List<JSONObject> getMenuList(Page page, @Param("params") Map<String, Object> params);
}