package com.academy.controller.homeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/home")
public class HomeController {

    /**
     * 首页
     * @param model
     * @return
     */
    @RequestMapping(value = "/index")
    public ModelAndView gotoIndexPage(ModelAndView model) {
        model.setViewName("system/home");
        return model;
    }

}
