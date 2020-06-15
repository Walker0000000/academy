package com.academy.mapper;

import com.academy.entity.Chapter;
import com.academy.entity.tree.DTree;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * Chapter 表数据库控制层接口
 */
public interface ChapterMapper extends BaseMapper<Chapter> {

    List<DTree> getChapterTreeList(Map<String, Object> contain);

}