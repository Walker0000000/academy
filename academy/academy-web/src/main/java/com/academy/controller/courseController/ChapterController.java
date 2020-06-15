package com.academy.controller.courseController;

import com.academy.service.system.IChapterService;
import com.academy.utils.ResultUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Chapter 控制层
 */
@RestController
@RequestMapping("/chapter")
public class ChapterController {

    @Resource
    private IChapterService chapterService;

    /**
     * 课程章节页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/gotoCourseChapterPage")
    public ModelAndView gotoCourseListPage(ModelAndView model, @RequestParam(name = "id") String id) {
        Map<String, Object> params = new HashMap<>();
        model.setViewName("course/courseChapters");
        return model;
    }

    /**
     * 获取课程分类下拉框
     *
     * @param id
     * @return
     */
    @GetMapping("/getChapterTree")
    public ResultUtil getChapterTree(@RequestParam(name = "id") String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        JSONObject chapterData = chapterService.getCourseChapterList(params);
        return ResultUtil.success(chapterData);
    }

}