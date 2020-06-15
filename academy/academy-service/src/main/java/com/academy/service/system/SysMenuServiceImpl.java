package com.academy.service.system;

import com.academy.config.DataSource;
import com.academy.config.DataSourceEnum;
import com.academy.entity.SysMenu;
import com.academy.entity.SysUser;
import com.academy.mapper.SysMenuMapper;
import com.academy.utils.ResultUtil;
import com.academy.utils.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("sysMenuService")
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysCodeService sysCodeService;

    @Override
    @DataSource(DataSourceEnum.GET)
    @Transactional
    public List<JSONObject> getSysMenuList(Map<String, Object> contain) {

        List<JSONObject> menuList = sysMenuMapper.getSysMenuList(contain);

        if (menuList == null || menuList.isEmpty()) {
            return new ArrayList<>();
        }
        List<JSONObject> results = new ArrayList<>();
        Map<String, List<JSONObject>> menuListMap = new HashMap<>();
        for (JSONObject menu : menuList) {
            String level = menu.getString("menu_level");
            String menu_code = menu.getString("menu_code");
            String parent_code = menu.getString("parent_code");
            if ("1".equals(level) && StringUtils.isNotEmpty(menu_code)) {
                results.add(menu);
            }
            if ("2".equals(level) && StringUtils.isNotEmpty(parent_code)) {
                List<JSONObject> menuList_map = menuListMap.get(parent_code);
                if (menuList_map == null || menuList_map.isEmpty()) {
                    List<JSONObject> menuList_new = new ArrayList<>();
                    menuList_new.add(menu);
                    menuListMap.put(parent_code, menuList_new);
                } else {
                    menuList_map.add(menu);
                }
            }
        }

        if (results == null || results.isEmpty()) {
            return new ArrayList<>();
        }

        for (JSONObject menu : results) {
            String menu_code = menu.getString("menu_code");
            if (StringUtils.isEmpty(menu_code)) {
                continue;
            }
            menu.put("spread", "true");
            List<JSONObject> menuList_map = menuListMap.get(menu_code);
            if (menuList_map != null && menuList_map.size() > 0) {
                menu.put("children", menuList_map);
            }
        }
        return results;
    }

    @Override
    @DataSource(DataSourceEnum.POST)
    @Transactional
    public ResultUtil insertMenu(JSONObject menu) {

        try {
            String menuCode = sysCodeService.getSysCode("menuCode");
            if (StringUtil.isEmpty(menuCode)) {
                return ResultUtil.error("获取编码失败");
            }
            String path = menu.getString("path");
            QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("path", path);
            List<SysMenu> menuList_old = sysMenuMapper.selectList(queryWrapper);
            if (menuList_old != null && menuList_old.size() > 0) {
                return ResultUtil.error("请求路径重复");
            }
            String menuName = menu.getString("menuName");
            String parentCode = menu.getString("parentCode");
            String sort = menu.getString("sort");
            String icon = menu.getString("icon");
            Integer menuLevel = menu.getInteger("menuLevel");
            Map<String, Object> contain_System = new HashMap<>();
            contain_System.put("menu_level", "0");
            List<SysMenu> menuList_System = sysMenuMapper.selectByMap(contain_System);
            String systemCode = "";
            if (menuList_System != null && menuList_System.size() > 0) {
                SysMenu menu_sys = menuList_System.get(0);
                systemCode = menu_sys.getMenuCode();
                if (StringUtil.isEmpty(systemCode)) {
                    return ResultUtil.error("获取系统编码失败");
                }
            }
            if (StringUtil.isEmpty(menuCode) || StringUtil.isEmpty(menuName) || StringUtil.isEmpty(parentCode) ||
                    StringUtil.isEmpty(sort) || StringUtil.isEmpty(icon) || StringUtil.isEmpty(path) ||
                    menuLevel == null) {
                return ResultUtil.error("参数缺失");
            }
            SysMenu sysMenu = new SysMenu();
            sysMenu.setCreateCode("U00001");
            sysMenu.setCreateTime(new Date());
            sysMenu.setIcon(icon);
            sysMenu.setMenuName(menuName);
            sysMenu.setMenuCode(menuCode);
            sysMenu.setSystemCode(systemCode);
            sysMenu.setParentCode(parentCode);
            sysMenu.setMenuLevel(menuLevel);
            sysMenu.setPath(path);
            sysMenu.setSort(sort);
            sysMenu.setStatus("Y");
            sysMenuMapper.insert(sysMenu);
            return ResultUtil.success("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error();
        }
    }

    @Override
    @DataSource(DataSourceEnum.POST)
    @Transactional
    public ResultUtil getMenuList(Map<String, Object> params) {
        try {
            List<JSONObject> menuList = null;
            if (StringUtil.isNotEmpty(params.get("page")) && StringUtil.isNotEmpty(params.get("limit"))) {
                Integer curretPage = Integer.parseInt((String) params.get("page"));
                Integer size = Integer.parseInt((String) params.get("limit"));
                Page page = new Page(curretPage, size);
                menuList = sysMenuMapper.getMenuList(page, params);
                //判断是否为空数据
                if (menuList == null || menuList.isEmpty()) {
                    return ResultUtil.success(new ArrayList<>(), 0);
                }
                return ResultUtil.success(menuList, page.getTotal());
            }
            menuList = sysMenuMapper.getMenuList(params);
            if (menuList == null || menuList.isEmpty()) {
                return ResultUtil.success(new ArrayList<>());
            }
            return ResultUtil.success(menuList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("系统异常");
        }
    }

    @Override
    public JSONObject getMenuByMap(Map<String, Object> contain) {
        List<JSONObject> menuList = sysMenuMapper.getMenuList(contain);
        if (menuList != null && menuList.size() > 0) {
            return menuList.get(0);
        }
        return new JSONObject();
    }


    @Override
    @DataSource(DataSourceEnum.GET)
    @Transactional
    public List<JSONObject> getSysMenuTreeList(Map<String, Object> contain) {
        List<JSONObject> menuList = sysMenuMapper.getSysMenuTreeList(contain);
        if (menuList == null || menuList.isEmpty()) {
            return new ArrayList<>();
        }
        //判断当前角色拥有的菜单 true 表示在树默认选中
        //并获取一级菜单列表
        List<JSONObject> results = new ArrayList<>();
        for (JSONObject menu : menuList) {
            String menuLevel = menu.getString("menu_level");
            if ("1".equals(menuLevel)) {
                results.add(menu);
            }
        }
        //当一级都为空时直接放回新的list
        if (results == null || results.isEmpty()) {
            return new ArrayList<>();
        }
        this.getMenuTree(results, menuList);

        Map<String, Object> contain_System = new HashMap<>();
        contain_System.put("menuLevel", "0");
        List<JSONObject> menuList_System = sysMenuMapper.getSysMenuTreeList(contain_System);
        if (menuList_System != null && menuList_System.size() > 0) {
            JSONObject menu = menuList_System.get(0);
            menu.put("checked", "true");
            results.add(menu);
        }
        return results;
    }

    private void getMenuTree(List<JSONObject> resultList, List<JSONObject> menuList) {
        for (JSONObject resultMenu : resultList) {
            resultMenu.put("children", new ArrayList<>());
            String value = resultMenu.getString("menu_code");
            if (StringUtil.isNotEmpty(value)) {
                for (JSONObject menu : menuList) {
                    String parentCode = menu.getString("parent_code");
                    if (value.equals(parentCode)) {
                        List<JSONObject> subsetList = (List<JSONObject>) resultMenu.get("children");
                        if (subsetList != null && subsetList.size() > 0) {
                            subsetList.add(menu);
                        } else {
                            List<JSONObject> subsetList_new = new ArrayList<>();
                            subsetList_new.add(menu);
                            resultMenu.put("children", subsetList_new);
                        }
                    }
                }
                List<JSONObject> subsetList = (List<JSONObject>) resultMenu.get("children");
                if (subsetList != null && subsetList.size() > 0) {
                    this.getMenuTree(subsetList, menuList);
                }
            }
        }
    }

    @Override
    @DataSource(DataSourceEnum.POST)
    @Transactional
    public ResultUtil updateMenu(JSONObject menu) {
        try {
            Integer id = menu.getInteger("id");
            QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            List<SysMenu> menuList = sysMenuMapper.selectList(queryWrapper);
            if (menuList == null || menuList.isEmpty()) {
                return ResultUtil.error("未找的对应的菜单信息");
            }
            SysMenu sysMenu = menuList.get(0);
            String menuName = menu.getString("menuName");
            if (StringUtil.isNotEmpty(menuName)) {
                sysMenu.setMenuName(menuName);
            }
            String parentCode = menu.getString("parentCode");
            if (StringUtil.isNotEmpty(parentCode)) {
                sysMenu.setParentCode(parentCode);
            }
            String sort = menu.getString("sort");
            if (StringUtil.isNotEmpty(sort)) {
                sysMenu.setSort(sort);
            }
            String icon = menu.getString("icon");
            if (StringUtil.isNotEmpty(icon)) {
                sysMenu.setIcon(icon);
            }
            String path = menu.getString("path");
            if (StringUtil.isNotEmpty(path)) {
                sysMenu.setPath(path);
            }
            String status = menu.getString("status");
            if (StringUtil.isNotEmpty(status)) {
                sysMenu.setStatus(status);
            }
            Integer menuLevel = menu.getInteger("menuLevel");
            if (StringUtil.isNotEmpty(menuLevel)) {
                sysMenu.setMenuLevel(menuLevel);
            }
            sysMenuMapper.updateById(sysMenu);
            return ResultUtil.success("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error();
        }
    }


    /**
     * 删除菜单
     */
    @Override
    @DataSource(DataSourceEnum.POST)
    @Transactional
    public ResultUtil deleteMenu(JSONObject params) {
        try {
            Integer id = (Integer) params.get("id");
            SysMenu sysMenu = sysMenuMapper.selectById(id);
            if (sysMenu == null) {
                return ResultUtil.error("不存在该菜单");
            }
            //是否存在有效的子菜单
            QueryWrapper<SysMenu> queryWrapper_parent = new QueryWrapper<>();
            queryWrapper_parent.eq("parent_code", sysMenu.getMenuCode());
            queryWrapper_parent.eq("status", "Y");
            List<SysMenu> menuList = sysMenuMapper.selectList(queryWrapper_parent);
            if (menuList != null && !menuList.isEmpty()) {
                return ResultUtil.error("存在子菜单无法删除");
            }

            String status = sysMenu.getStatus();
            if ("N".equals(status)) {
                return ResultUtil.error("请确认菜单状态");
            }

            sysMenu.setStatus("N");

            SysUser currentUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
            sysMenu.setUpdateCode(currentUser.getUserCode());
            sysMenu.setUpdateTime(new Date());
            sysMenuMapper.updateById(sysMenu);
            return ResultUtil.success("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("操作失败");
        }
    }

}
