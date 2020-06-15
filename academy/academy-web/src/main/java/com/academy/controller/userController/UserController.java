package com.academy.controller.userController;

import com.academy.service.system.UserService;
import com.academy.utils.ResultUtil;
import com.academy.utils.StringUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("用户模块")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登陆页面
     *
     * @param model
     * @return
     */
    @RequiresPermissions("/user/gotoUserListPage")
    @GetMapping(value = "/gotoUserListPage")
    public ModelAndView gotoUserListPage(ModelAndView model) {
        model.setViewName("user/userList");
        return model;
    }


    @GetMapping("/getUserList")
    public ResultUtil getUserList(@RequestParam Map<String, Object> params) {
        return userService.getUserList(params);
    }

    @GetMapping(value = "/gotoUserInsertPage")
    public ModelAndView gotoUserInsertPage(ModelAndView model) {
        model.setViewName("user/userInsert");
        return model;
    }

    @PostMapping(value = "/insertUser")
    public ResultUtil insertUser(@RequestBody JSONObject person) {
        if (person == null) {
            return ResultUtil.error();
        }
        return userService.insertUser(person);
    }

    @GetMapping(value = "/gotoUserUpdatePage")
    public ModelAndView gotoUserUpdatePage(ModelAndView model, @RequestParam(name = "id") String id) {
        if (StringUtil.isNotEmpty(id)) {
            Map<String, Object> contain = new HashMap<>();
            contain.put("id", id);
            JSONObject userData = userService.getUserByMap(contain);
            if (userData != null) {
                model.addObject("userData", userData);
            }
        }
        model.setViewName("user/userUpdate");
        return model;
    }

    @PostMapping(value = "/updateUser")
    public ResultUtil updateUser(@RequestBody JSONObject person) {
        if (person == null) {
            return ResultUtil.error();
        }
        return userService.updateUser(person);
    }

    /**
     *
     *授权用户系统菜单角色
     */
    @GetMapping(value = "/gotoUserAuthorizePage")
    public ModelAndView gotoUserAuthorizePage(ModelAndView model, @RequestParam(name = "id") String id) {

        if (StringUtil.isNotEmpty(id)) {
            Map<String, Object> contain = new HashMap<>();
            contain.put("id", id);
            JSONObject UserRoleData = userService.getUserRoleByMap(contain);
            if (UserRoleData != null) {
                model.addObject("UserRoleData", UserRoleData);
            }
        }
        model.setViewName("user/userAuthorize");
        return model;
    }

    /**
     *用户系统后台授权
     */
    @PostMapping(value = "/userAuthorize")
    public ResultUtil userAuthorize(@RequestBody JSONObject person) {
        if (person == null) {
            return ResultUtil.error();
        }
        return userService.userAuthorize(person);
    }

    /**
     * 查询用户下拉框数据
     *
     * @return java.util.List<com.alibaba.fastjson.JSONObject>
     * @author KY
     * @date 2020/5/8 17:21
     */
    @GetMapping("/getUserTreeList")
    public List<JSONObject> getUserTreeList() {
        Map<String, Object> contain = new HashMap<>();
        return userService.getUserTreeList(contain);
    }
}
