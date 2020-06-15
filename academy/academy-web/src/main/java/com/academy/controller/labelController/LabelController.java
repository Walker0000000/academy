package com.academy.controller.labelController;

import com.academy.service.system.ILabelService;
import com.academy.utils.ResultUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Label 控制层
 */
@RestController
@RequestMapping("/label")
public class LabelController {

    @Resource
    private ILabelService labelService;

    /**
     * 获取课程标签下拉框
     *
     * @param params
     * @return
     */
    @GetMapping("/getLabelCombobox")
    public ResultUtil getLabelCombobox(@RequestParam Map<String, Object> params) {
        return labelService.getLabelCombobox(params);
    }

}