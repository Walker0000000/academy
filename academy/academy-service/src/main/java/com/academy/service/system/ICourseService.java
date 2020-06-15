package com.academy.service.system;

import com.academy.utils.ResultUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Course 表数据服务层接口
 */
public interface ICourseService {

    ResultUtil getCourseList(Map<String, Object> params);

    ResultUtil updateCourseStatus(JSONObject params);

    ResultUtil insertCourse(JSONObject menu);

    JSONObject getCourseByMap(Map<String, Object> contain);

    ResultUtil updateCourse(JSONObject person);

}