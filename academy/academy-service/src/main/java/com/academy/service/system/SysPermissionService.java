package com.academy.service.system;

import com.academy.shiro.common.LoginType;
import com.academy.utils.ResultUtil;
import com.alibaba.fastjson.JSONObject;

public interface SysPermissionService {

    JSONObject getLoginCode();

    ResultUtil userNameAndPasswordLogin(LoginType loginType, JSONObject person);

    ResultUtil WxCodeLogin(net.sf.json.JSONObject params);
}
