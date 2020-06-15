package com.academy.service.system;

import com.academy.config.DataSource;
import com.academy.config.DataSourceEnum;
import com.academy.entity.Chapter;
import com.academy.entity.Course;
import com.academy.entity.tree.DTree;
import com.academy.mapper.ChapterMapper;
import com.academy.mapper.CourseMapper;
import com.academy.utils.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Chapter 表数据服务层接口实现类
 */
@Service
public class ChapterServiceImpl implements IChapterService {

    @Resource
    private ChapterMapper chapterMapper;

    @Resource
    private CourseMapper courseMapper;

    /**
     * 获取当前角色的菜单信息
     */
    @Override
    @DataSource(DataSourceEnum.GET)
    public JSONObject getCourseChapterList(Map<String, Object> params) {

        JSONObject chapterData = new JSONObject();

        String id = params.get("id") != null ? String.valueOf(params.get("id")) : null;
        if (StringUtil.isEmpty(id)) {
            chapterData.put("chapterList", new ArrayList<>());
            return chapterData;
        }

        List<DTree> courseList = courseMapper.getCourseTreeDetail(params);
        if (courseList == null || courseList.isEmpty()) {
            chapterData.put("chapterList", new ArrayList<>());
            return chapterData;
        }

        DTree course = courseList.get(0);

        Map<String, Object> chapterParams = new HashMap<>();
        chapterParams.put("course_code", course.getId());

        //获取课程的所有章节
        List<DTree> chapterList = chapterMapper.getChapterTreeList(chapterParams);
        if (chapterList == null || chapterList.isEmpty()) {
            chapterData.put("chapterList", new ArrayList<>());
        } else {
            course.setChildren(chapterList);
            chapterData.put("chapterList", courseList);
        }

        return chapterData;
    }

}