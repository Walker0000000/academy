package com.academy.controller.courseController;

import com.academy.service.system.ICourseService;
import com.academy.service.system.SysMenuService;
import com.academy.utils.ResultUtil;
import com.academy.utils.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/course")
public class CourseController {

    @Resource
    private ICourseService courseService;

    /**
     * 课程列表页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/gotoCourseListPage")
    public ModelAndView gotoCourseListPage(ModelAndView model) {
        model.setViewName("course/courseList");
        return model;
    }

    @GetMapping("/getCourseList")
    public ResultUtil getCourseList(@RequestParam Map<String, Object> params) {
        return courseService.getCourseList(params);
    }

    /**
     * 修改课程状态
     */
    @PostMapping(value = "/updateCourseStatus")
    public ResultUtil updateCourseStatus(@RequestBody JSONObject params) {
        if (params.get("id") == null) {
            return ResultUtil.error();
        }
        return courseService.updateCourseStatus(params);
    }

    /**
     * 前往新增课程页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/gotoCourseInsertPage")
    public ModelAndView gotoCourseInsertPage(ModelAndView model) {
        model.setViewName("course/courseInsert");
        return model;
    }

    /**
     * 新增课程
     *
     * @param person
     * @return
     */
    @PostMapping(value = "/insertCourse")
    public ResultUtil insertCourse(@RequestBody JSONObject person) {
        if (person == null) {
            return ResultUtil.error();
        }
        return courseService.insertCourse(person);
    }

    /**
     * 跳转修改课程页面
     *
     * @param model
     * @param id
     * @return
     */
    @RequestMapping(value = "/gotoCourseUpdatePage")
    public ModelAndView gotoCourseUpdatePage(ModelAndView model, @RequestParam(name = "id") String id) {

        if (StringUtil.isNotEmpty(id)) {
            Map<String, Object> contain = new HashMap<>();
            contain.put("id", id);
            JSONObject menuData = courseService.getCourseByMap(contain);
            if (menuData != null) {
                model.addObject("courseData", menuData);
            }
        }
        model.setViewName("course/courseUpdate");
        return model;
    }

    /**
     * 修改课程
     *
     * @param person
     * @return
     */
    @PostMapping(value = "/updateCourse")
    public ResultUtil updateCourse(@RequestBody JSONObject person) {

        if (person == null) {
            return ResultUtil.error();
        }
        return courseService.updateCourse(person);
    }
}
