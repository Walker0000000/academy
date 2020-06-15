package com.academy.controller.roleController;

import com.academy.entity.SysUser;
import com.academy.service.system.RoleService;
import com.academy.utils.ResultUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 系统角色展示页面
     */
    @GetMapping(value = "/gotoRoleListPage")
    public ModelAndView gotoRoleListPage(ModelAndView model) {
        model.setViewName("role/roleList");
        return model;
    }


    /**
     * 获取系统角色列表
     */
    @GetMapping("/getRoleList")
    public ResultUtil getRoleList(@RequestParam Map<String, Object> params) {

        return roleService.getRoleList(params);
    }


    /**
     * 获取系统角色新增页面
     */
    @GetMapping(value = "/gotoRoleInsertPage")
    public ModelAndView gotoRoleInsertPage(ModelAndView model) {
        model.setViewName("role/roleInsert");
        return model;
    }

    /**
     * 新增系统角色
     */
    @PostMapping(value = "/insertRole")
    public ResultUtil insertUser(@RequestBody JSONObject person) {
        if (person == null) {
            return ResultUtil.error();
        }
        return roleService.insertRole(person);
    }

    /**
     * 授权角色页面
     */
    @GetMapping(value = "/gotoRoleUpdatePage")
    public ModelAndView gotoRoleUpdatePage(ModelAndView model, @RequestParam(name = "id") String id) {
        SysUser sysUser = new SysUser();
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        JSONObject roleData = roleService.getRoleMenuList(params);
        model.setViewName("role/roleUpdate");
        model.addObject("roleData", roleData);
        return model;
    }

    /**
     * 授权系统角色菜单
     */
    @PostMapping(value = "/updateRole")
    public ResultUtil updateRole(@RequestBody JSONObject roleObject) {
        if (roleObject == null) {
            return ResultUtil.error();
        }
        return roleService.updateRole(roleObject);
    }

    /**
     * 授权系统角色菜单
     */
    @PostMapping(value = "/deleteRole")
    public ResultUtil deleteRole(@RequestBody Map<String, Object> params) {
        if (params == null) {
            return ResultUtil.error();
        }
        return roleService.deleteRole(params);
    }

}
