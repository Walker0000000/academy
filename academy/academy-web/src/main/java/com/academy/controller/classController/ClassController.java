package com.academy.controller.classController;

import com.academy.service.system.IClassService;
import com.academy.utils.ResultUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Class 控制层
 */
@RestController
@RequestMapping("/class")
public class ClassController {

    @Resource
    private IClassService classService;

    /**
     * 获取课程分类下拉框
     *
     * @param params
     * @return
     */
    @GetMapping("/getClassCombobox")
    public ResultUtil getClassCombobox(@RequestParam Map<String, Object> params) {
        return classService.getClassCombobox(params);
    }

}