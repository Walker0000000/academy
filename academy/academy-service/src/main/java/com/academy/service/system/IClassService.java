package com.academy.service.system;

import com.academy.utils.ResultUtil;

import java.util.Map;

/**
 * Class 表数据服务层接口
 */
public interface IClassService {

    ResultUtil getClassCombobox(Map<String, Object> params);

}