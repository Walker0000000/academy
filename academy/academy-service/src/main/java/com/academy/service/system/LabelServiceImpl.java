package com.academy.service.system;

import com.academy.config.DataSource;
import com.academy.config.DataSourceEnum;
import com.academy.entity.Label;
import com.academy.mapper.LabelMapper;
import com.academy.utils.ResultUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Label 表数据服务层接口实现类
 */
@Service
public class LabelServiceImpl implements ILabelService {

    @Resource
    private LabelMapper labelMapper;

    @Override
    @DataSource(DataSourceEnum.POST)
    @Transactional
    public ResultUtil getLabelCombobox(Map<String, Object> params) {
        try {
            params.put("status", "1");
            List<Label> labelList = labelMapper.selectByMap(params);
            return ResultUtil.success(labelList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("系统异常");
        }
    }


}