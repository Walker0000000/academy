package com.academy.service.system;

import com.academy.common.SystemConstant;
import com.academy.entity.SysUser;
import com.academy.enums.ResultStatusCode;
import com.academy.shiro.common.LoginType;
import com.academy.shiro.common.UserToken;
import com.academy.utils.*;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service("sysPermissionService")
public class SysPermissionServiceImpl implements SysPermissionService {

    @Autowired
    private RedisUtil redisUtil;

    @Value("${resource.showImg}")
    private String showImg;

    @Override
    public JSONObject getLoginCode() {
        JSONObject captcha = new JSONObject();
        String code_base64 = null;
        String code_redis = null;
        ValidateCodeUtil.Validate v = ValidateCodeUtil.getRandomCode();     //直接调用静态方法，返回验证码对象
        if(v!=null){
            code_redis = v.getValue();
            code_base64 = v.getBase64Str();
        }
        //生成一个唯一值
        String cacheKey = SystemConstant.LOGIN_PIC_RANDOM + "_" + RandomNbr.getOlNbrByRandom();
        //唯一值作为key 图形验证码作为value 存入redis 并设置有效时间
        redisUtil.set(cacheKey, code_redis, 240);

        captcha.put("key",cacheKey);
        captcha.put("code","data:image/png;base64,"+code_base64);
        return captcha;
    }

    @Override
    public ResultUtil userNameAndPasswordLogin(LoginType loginType, JSONObject person) {

        String key = person.getString("key");
        if(StringUtil.isEmpty(key)){
            return ResultUtil.error("获取验证码异常");
        }
        String code = person.getString("code");
        if(StringUtil.isEmpty(code)){
            return ResultUtil.error("获取验证码异常");
        }
        String code_redis = redisUtil.getToString(key);
        if(!code.equals(code_redis)){
            return ResultUtil.error("获取验证码错误");
        }

        String userName = person.getString("userName");
        if(StringUtil.isEmpty(userName)){
            return ResultUtil.error("获取用户名失败");
        }
        String password = person.getString("password");
        if(StringUtil.isEmpty(password)){
            return ResultUtil.error("获取密码失败");
        }

        password = MD5Util.encrypt16(password);

        UserToken token = new UserToken(loginType, userName, password);

        JSONObject json = new JSONObject();
        json.put("url","/home/index");
        try {
            //登录不在该处处理，交由shiro处理
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            log.info("---------------- Shiro 凭证认证成功 ----------------------");
            return new ResultUtil(ResultStatusCode.OK, json);
        }catch (IncorrectCredentialsException | UnknownAccountException e){

            return new ResultUtil(ResultStatusCode.NOT_EXIST_USER_OR_ERROR_PWD);
        }catch (LockedAccountException e){
            return new ResultUtil(ResultStatusCode.USER_FROZEN);
        }catch (Exception e){
            return new ResultUtil(ResultStatusCode.SYSTEM_ERR);
        }
    }

    @Override
    public ResultUtil WxCodeLogin(net.sf.json.JSONObject params) {

        try {
            String code = params.getString("code");
            if(StringUtil.isEmpty(code)){
                ResultUtil.error("code 参数缺失");
            }
            UserToken token = new UserToken(LoginType.WECHAT_LOGIN, code);
            try {
                //登录不在该处处理，交由shiro处理 在 WechatLoginRealm 类处理
                Subject subject = SecurityUtils.getSubject();
                subject.login(token);
                log.info("---------------- Shiro 凭证认证成功 ----------------------");
                JSONObject data = new JSONObject();
                SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
                sysUser.setShowImg(showImg);
                data.put("userInfo",sysUser);
                data.put("Token",subject.getSession().getId());
                return new ResultUtil(ResultStatusCode.OK, data);
            }catch (IncorrectCredentialsException | UnknownAccountException e){
                return new ResultUtil(ResultStatusCode.NOT_EXIST_USER_OR_ERROR_PWD);
            }catch (LockedAccountException e){
                return new ResultUtil(ResultStatusCode.USER_FROZEN);
            }catch (Exception e){
                return new ResultUtil(ResultStatusCode.SYSTEM_ERR);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error("系统异常");
        }
    }

}
