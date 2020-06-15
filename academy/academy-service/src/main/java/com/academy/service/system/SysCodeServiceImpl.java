package com.academy.service.system;

import com.academy.config.DataSource;
import com.academy.config.DataSourceEnum;
import com.academy.entity.SysCode;
import com.academy.mapper.SysCodeMapper;
import com.academy.utils.DateUtil;
import com.academy.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 *
 * @description 通用服务类SeqCode 表数据服务层接口实现类
 * @author: shuZhiMing
 */

@Service("sysCodeService")
public class SysCodeServiceImpl implements SysCodeService {

	@Autowired
	private SysCodeMapper sysCodeMapper;
	/**
	 * @param type 编码的类型：例如warehouseCode
	 * @return outCode 返回的编码值
	 ***/
	@Override
	@DataSource(DataSourceEnum.POST)
	@Transactional
	public String getSysCode(String type) {
		Map<String, Object> con = new HashMap<>();
		con.put("type", type);
		List<SysCode> seqCodeRuleList = sysCodeMapper.selectByMap(con);
		if (seqCodeRuleList.size() > 0) {
			SysCode seqCodeRule = seqCodeRuleList.get(0);
			// 取当前时间并按编码规则中的格式转化
			String middleTimeValue = DateUtil.getStartardDatePattern(new Date(), seqCodeRule.getMiddle());
			// 得到后缀数值并补齐位数 (通过lpad 左填充方法)
			String endValue = StringUtil.lpad(String.valueOf(seqCodeRule.getCurrentValue()),
					String.valueOf(seqCodeRule.getMaxValue()).length(), "0");
			String outCode = seqCodeRule.getPrefix() + middleTimeValue + endValue;
			// 更新currentValue
			if (seqCodeRule.getCurrentValue() + seqCodeRule.getGrowthValue() >= seqCodeRule.getMaxValue()) {
				seqCodeRule.setCurrentValue(seqCodeRule.getSuffixValue());// 如果当前值加增长值大于最大值,则重置为后缀初始值
			} else {
				seqCodeRule.setCurrentValue(seqCodeRule.getCurrentValue() + seqCodeRule.getGrowthValue());
			}
			sysCodeMapper.updateById(seqCodeRule);
			return outCode;
		}
		return null;
	}

	/**
	 * @param type 编码的类型：例如warehouseCode
	 * @return outCode 返回的编码 适用于无限增长的编码 例如损益单业务中调整单
	 ***/
	@Override
	@DataSource(DataSourceEnum.POST)
	@Transactional
	public List<String> getSysCodeList(String type, Integer size) {
		Map<String, Object> con = new HashMap<>();
		con.put("type", type);
		List<SysCode> seqCodeRuleList = sysCodeMapper.selectByMap(con);
		List<String> seqCodeResult = new ArrayList<>();
		if (seqCodeRuleList.size() > 0) {
			SysCode seqCodeRule = seqCodeRuleList.get(0);
			// 取当前时间并按编码规则中的格式转化
			String middleTimeValue = DateUtil.getStartardDatePattern(new Date(), seqCodeRule.getMiddle());
			for (int i = 0; i < size; i++) {
				seqCodeRule.setCurrentValue(seqCodeRule.getCurrentValue() + seqCodeRule.getGrowthValue());
				// 得到后缀数值并补齐位数 (通过lpad 左填充方法)
				String endValue = StringUtil.lpad(String.valueOf(seqCodeRule.getCurrentValue()),
						String.valueOf(seqCodeRule.getMaxValue()).length(), "0");
				String outCode = seqCodeRule.getPrefix() + middleTimeValue + endValue;
				seqCodeResult.add(outCode);
			}
			seqCodeRule.setCurrentValue(0);
			sysCodeMapper.updateById(seqCodeRule);
			return seqCodeResult;
		}
		return null;
	}
}