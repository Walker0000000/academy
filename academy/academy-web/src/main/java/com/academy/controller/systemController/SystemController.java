package com.academy.controller.systemController;

import com.academy.service.system.SysMenuService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private SysMenuService sysMenuService;

    @GetMapping("/getMenuList")
    public List<JSONObject> getMenuList(){
        Map<String,Object> contain = new HashMap<>();
        return  sysMenuService.getSysMenuList(contain);
    }


}
