package com.academy.service.system;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Chapter 表数据服务层接口
 */
public interface IChapterService {

    /**
     * 获取课程的章节信息
     */
    JSONObject getCourseChapterList(Map<String, Object> params);

}