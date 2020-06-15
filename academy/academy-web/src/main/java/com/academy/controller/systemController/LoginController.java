package com.academy.controller.systemController;

import com.academy.service.system.SysPermissionService;
import com.academy.shiro.common.LoginType;
import com.academy.utils.ResultUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private SysPermissionService sysPermissionService;

    /**
     * 登陆页面
     * @param model
     * @return
     */
    @GetMapping(value = "/loginPage")
    public ModelAndView gotoLoginPage(ModelAndView model) {
        model.setViewName("login/login");
        JSONObject captcha = sysPermissionService.getLoginCode();
        model.addObject("captcha",captcha);
        return model;
    }

    /**
     * 用户密码登录
     * @return
     */
    @PostMapping(value = "/login")
    public ResultUtil login(@RequestBody JSONObject person){

        if(person == null || person.isEmpty()){
            return ResultUtil.error("参数缺失");
        }
        return sysPermissionService.userNameAndPasswordLogin(LoginType.USER_PASSWORD,person);
    }

    /**
     * 首页
     * @param model
     * @return
     */
    @GetMapping(value = "/index")
    public ModelAndView gotoIndexPage(ModelAndView model) {
        model.setViewName("system/home");
        return model;
    }

    /**
     * 退出登录
     * @return
     */
    @RequestMapping("/logout")
    public ModelAndView logout(ModelAndView model){
        SecurityUtils.getSubject().logout();
        model.setViewName("login/login");
        return model;
    }

}
