package com.academy.service.system;

import com.academy.config.DataSource;
import com.academy.config.DataSourceEnum;
import com.academy.entity.Class;
import com.academy.mapper.ClassMapper;
import com.academy.utils.ResultUtil;
import com.academy.utils.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class 表数据服务层接口实现类
 */
@Service
public class ClassServiceImpl implements IClassService {

    @Resource
    private ClassMapper classMapper;

    @Override
    @DataSource(DataSourceEnum.POST)
    @Transactional
    public ResultUtil getClassCombobox(Map<String, Object> params) {
        try {
            params.put("status", "1");
            List<Class> classList = classMapper.selectByMap(params);
            return ResultUtil.success(classList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("系统异常");
        }
    }


}