package com.academy.controller.menuController;

import com.academy.service.system.SysMenuService;
import com.academy.utils.ResultUtil;
import com.academy.utils.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 登陆页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/gotoMenuListPage")
    public ModelAndView gotoMenuListPage(ModelAndView model) {
        model.setViewName("menu/menuList");
        return model;
    }


    @GetMapping("/getMenuList")
    public ResultUtil getMenuList(@RequestParam Map<String,Object> params ){
        return  sysMenuService.getMenuList(params);
    }

    @RequestMapping(value = "/gotoMenuInsertPage")
    public ModelAndView gotoMenuInsertPage(ModelAndView model) {
        model.setViewName("menu/menuInsert");
        return model;
    }

    @PostMapping(value = "/insertMenu")
    public ResultUtil insertMenu(@RequestBody JSONObject person) {
        if(person == null){
            return ResultUtil.error();
        }
        return sysMenuService.insertMenu(person);
    }

    @RequestMapping(value = "/gotoMenuUpdatePage")
    public ModelAndView gotoMenuUpdatePage(ModelAndView model,@RequestParam(name = "id") String id) {

        if(StringUtil.isNotEmpty(id)){
            Map<String,Object> contain = new HashMap<>();
            contain.put("id",id);
            JSONObject menuData = sysMenuService.getMenuByMap(contain);
            if(menuData!=null){
                model.addObject("menuData",menuData);
            }
        }
        model.setViewName("menu/menuUpdate");
        return model;
    }

    @GetMapping("/getSysMenuTreeList")
    public List<JSONObject> getSysMenuTreeList(){
        Map<String,Object> contain = new HashMap<>();
        return  sysMenuService.getSysMenuTreeList(contain);
    }

    @PostMapping(value = "/updateMenu")
    public ResultUtil updateMenu(@RequestBody JSONObject person) {

        if(person == null){
            return ResultUtil.error();
        }
        return sysMenuService.updateMenu(person);
    }

    /**
     *删除菜单
     */
    @PostMapping(value = "/deleteMenu")
    public ResultUtil deleteMenu(@RequestBody JSONObject params) {
        if(params.get("id") == null){
            return ResultUtil.error();
        }
        return sysMenuService.deleteMenu(params);
    }

}
