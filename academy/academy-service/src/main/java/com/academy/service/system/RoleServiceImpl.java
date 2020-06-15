package com.academy.service.system;

import com.academy.config.DataSource;
import com.academy.config.DataSourceEnum;
import com.academy.entity.SysRole;
import com.academy.entity.SysRoleMenus;
import com.academy.entity.SysUser;
import com.academy.entity.SysUserRoles;
import com.academy.mapper.SysRoleMapper;
import com.academy.mapper.SysRoleMenusMapper;
import com.academy.mapper.SysUserRolesMapper;
import com.academy.utils.ResultUtil;
import com.academy.utils.StringUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleMenusMapper sysRoleMenusMapper;

    @Autowired
    private SysUserRolesMapper sysUserRolesMapper;

    @Autowired
    private SysCodeService sysCodeService;

    /**
     * 获取系统角色列表
     */
    @Override
    @DataSource(DataSourceEnum.GET)
    public ResultUtil getRoleList(Map<String, Object> params) {

        try {
            List<JSONObject> roleList = null;
            //分页查询当前页
            if (StringUtil.isNotEmpty(params.get("page")) && StringUtil.isNotEmpty(params.get("limit"))) {
                Integer curretPage = Integer.parseInt((String) params.get("page"));
                Integer size = Integer.parseInt((String) params.get("limit"));
                Page page = new Page(curretPage, size);
                roleList = sysRoleMapper.getRoleList(page, params);
                //判断是否为空数据
                if (roleList == null || roleList.isEmpty()) {
                    return ResultUtil.success(new ArrayList<>(), 0);
                }
                return ResultUtil.success(roleList, page.getTotal());
            }

            roleList = sysRoleMapper.getRoleList(params);
            if (roleList == null || roleList.isEmpty()) {
                return ResultUtil.success(new ArrayList<>(), 0);
            }
            return ResultUtil.success(roleList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("系统异常");
        }
    }

    /**
     * 新增系统角色
     */
    @Override
    @DataSource(DataSourceEnum.POST)
    @Transactional
    public ResultUtil insertRole(JSONObject role) {

        try {
            String roleCode = sysCodeService.getSysCode("roleCode");

            if (StringUtil.isEmpty(roleCode)) {
                return ResultUtil.error("获取编码失败");
            }

            String roleName = role.getString("roleName");
            if (StringUtil.isEmpty(roleName)) {
                return ResultUtil.error("名称不能为空");
            }

            QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("role_name", roleName);
            List<SysRole> roleList_old = sysRoleMapper.selectList(queryWrapper);

            if (roleList_old != null && roleList_old.size() > 0) {
                return ResultUtil.error("名称重复");
            }

            String roleType = role.getString("roleType");

            if (StringUtil.isEmpty(roleCode) || StringUtil.isEmpty(roleName) || StringUtil.isEmpty(roleType)) {
                return ResultUtil.error("参数缺失");
            }

            SysRole sysRole = new SysRole();
            SysUser currentUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
            sysRole.setCreateCode(currentUser.getUserCode());
            sysRole.setCreateTime(new Date());
            sysRole.setRoleCode(roleCode);
            sysRole.setRoleName(roleName);
            sysRole.setRoleType(roleType);
            sysRole.setStatus("Y");
            sysRoleMapper.insert(sysRole);
            return ResultUtil.success("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error();
        }

    }

    /**
     * 获取当前角色的菜单信息
     */
    @Override
    @DataSource(DataSourceEnum.GET)
    public JSONObject getRoleMenuList(Map<String, Object> params) {

        String id = params.get("id") != null ? String.valueOf(params.get("id")) : null;
        if (StringUtil.isEmpty(id)) {
            JSONObject roleData = new JSONObject();
            roleData.put("roleMenuList", new ArrayList<>());
            return roleData;
        }

        List<JSONObject> roleList = sysRoleMapper.getRoleList(params);
        if (roleList == null || roleList.isEmpty()) {
            JSONObject roleData = new JSONObject();
            roleData.put("roleMenuList", new ArrayList<>());
            return roleData;
        }

        JSONObject role = roleList.get(0);
        params.put("roleCode", role.getString("roleCode"));

        //获取当前用户的全部拥有的全部菜单信息
        List<JSONObject> roleMenuList = sysRoleMapper.getRoleMenuList(params);
        if (roleMenuList == null || roleMenuList.isEmpty()) {
            role.put("roleMenuList", new ArrayList<>());
            return role;
        }

        //判断当前角色拥有的菜单 true 表示在树默认选中
        //并获取一级菜单列表
        List<JSONObject> resultList = new ArrayList<>();
        for (JSONObject menu : roleMenuList) {
            String checked = menu.getString("checked");
            if (StringUtil.isEmpty(checked)) {
                menu.put("checked", false);
            } else {
                menu.put("checked", true);
            }
            String menuLevel = menu.getString("menuLevel");
            if ("0".equals(menuLevel)) {
                resultList.add(menu);
            }
        }

        if (resultList == null || resultList.isEmpty()) {
            role.put("roleMenuList", new ArrayList<>());
            return role;
        }

        this.getRoleMenu(resultList, roleMenuList);

        role.put("roleMenuList", resultList);

        return role;
    }

    /**
     * 构建菜单树结构
     */
    private void getRoleMenu(List<JSONObject> resultList, List<JSONObject> menuList) {

        for (JSONObject resultMenu : resultList) {

            resultMenu.put("data", new ArrayList<>());
            String value = resultMenu.getString("value");

            if (StringUtil.isNotEmpty(value)) {
                for (JSONObject menu : menuList) {
                    String parentCode = menu.getString("parentCode");
                    if (value.equals(parentCode)) {
                        List<JSONObject> subsetList = (List<JSONObject>) resultMenu.get("data");
                        if (subsetList != null && subsetList.size() > 0) {
                            subsetList.add(menu);
                        } else {
                            List<JSONObject> subsetList_new = new ArrayList<>();
                            subsetList_new.add(menu);
                            resultMenu.put("data", subsetList_new);
                        }
                    }
                }
                List<JSONObject> subsetList = (List<JSONObject>) resultMenu.get("data");
                if (subsetList != null && subsetList.size() > 0) {
                    this.getRoleMenu(subsetList, menuList);
                }

            }
        }
    }


    /**
     * 授权系统角色菜单
     */
    @Override
    @DataSource(DataSourceEnum.POST)
    @Transactional
    public ResultUtil updateRole(JSONObject role) {
        try {
            Integer id = role.getInteger("id");

            QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            List<SysRole> roleList = sysRoleMapper.selectList(queryWrapper);
            if (roleList == null || roleList.isEmpty()) {
                return ResultUtil.error("未找的对应的角色信息");
            }
            SysRole sysRole = roleList.get(0);

            String roleCode = sysRole.getRoleCode();

            if (StringUtil.isEmpty(roleCode)) {
                return ResultUtil.error("角色编码缺失");
            }

            QueryWrapper<SysRoleMenus> queryWrapper_menus = new QueryWrapper<>();
            queryWrapper_menus.eq("role_code", roleCode);
            sysRoleMenusMapper.delete(queryWrapper_menus);

            JSONArray roleMenus = role.getJSONArray("checkMenus");
            if (roleMenus == null || roleMenus.isEmpty()) {
                return ResultUtil.success("操作成功");
            }

            List<SysRoleMenus> sysRoleMenusList = new ArrayList<>();
            SysUser currentUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
            String userCode = currentUser.getUserCode();
            Date currentTime = new Date();
            for (Object menuCode : roleMenus) {
                SysRoleMenus sysRoleMenus = new SysRoleMenus();
                sysRoleMenus.setCreateCode(userCode);
                sysRoleMenus.setCreateTime(currentTime);
                sysRoleMenus.setMenuCode(String.valueOf(menuCode));
                sysRoleMenus.setRoleCode(roleCode);
                sysRoleMenus.setStatus("Y");
                sysRoleMenusList.add(sysRoleMenus);

            }

            if (sysRoleMenusList != null && sysRoleMenusList.size() > 0) {
                sysRoleMapper.insertRoleMenusBatch(sysRoleMenusList);
            }
            return ResultUtil.success("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error();
        }
    }


    /**
     * 删除系统角色
     */
    @Override
    @DataSource(DataSourceEnum.POST)
    @Transactional
    public ResultUtil deleteRole(Map<String, Object> params) {
        try {
            String id = params.get("id") != null ? String.valueOf(params.get("id")) : null;
            if (StringUtil.isEmpty(id)) {
                return ResultUtil.error("参数错误");
            }
            SysRole wxRole = sysRoleMapper.selectById(id);
            if (wxRole == null) {
                return ResultUtil.error("不存在该角色");
            }

            //查询有用户属于该角色
            QueryWrapper<SysUserRoles> queryWrapper_user = new QueryWrapper<>();
            queryWrapper_user.eq("role_code", wxRole.getRoleCode());
            queryWrapper_user.eq("status", "Y");
            List<SysUserRoles> sysUserRoles = sysUserRolesMapper.selectList(queryWrapper_user);
            if (sysUserRoles != null && !sysUserRoles.isEmpty()) {
                return ResultUtil.error("存在该角色用户，无法删除角色");
            }

            wxRole.setStatus("N");
            SysUser currentUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
            wxRole.setUpdateCode(currentUser.getUserCode());
            wxRole.setUpdateTime(new Date());

            sysRoleMapper.updateById(wxRole);
            return ResultUtil.success("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("操作失败");
        }

    }


}
