package com.academy.service.system;

import com.academy.utils.ResultUtil;

import java.util.Map;

/**
 * Label 表数据服务层接口
 */
public interface ILabelService {

    ResultUtil getLabelCombobox(Map<String, Object> params);

}