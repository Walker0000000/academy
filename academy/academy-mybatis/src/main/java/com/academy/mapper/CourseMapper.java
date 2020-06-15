package com.academy.mapper;

import com.academy.entity.Course;
import com.academy.entity.tree.DTree;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Course 表数据库控制层接口
 */
public interface CourseMapper extends BaseMapper<Course> {

    /**
     * 根据条件获取菜单列表
     *
     * @param contain
     * @return
     */
    List<JSONObject> getCourseList(@Param("params") Map<String, Object> contain);

    List<JSONObject> getCourseList(Page page, @Param("params") Map<String, Object> params);

    List<DTree> getCourseTreeDetail(@Param("params") Map<String, Object> params);

}