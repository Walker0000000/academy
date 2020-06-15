package com.academy.service.system;

import com.alibaba.fastjson.JSONObject;
import com.academy.utils.ResultUtil;

import java.util.List;
import java.util.Map;

public interface SysMenuService {

    List<JSONObject> getSysMenuList (Map<String, Object> contain);

    ResultUtil insertMenu (JSONObject menu);

    ResultUtil getMenuList(Map<String, Object> params);

    JSONObject getMenuByMap(Map<String, Object> contain);

    List<JSONObject> getSysMenuTreeList(Map<String, Object> contain);

    ResultUtil updateMenu(JSONObject person);

    /**
     *删除菜单
     */
    ResultUtil deleteMenu(JSONObject params);
}
