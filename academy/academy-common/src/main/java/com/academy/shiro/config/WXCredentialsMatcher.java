package com.academy.shiro.config;

import com.academy.shiro.common.UserToken;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

public class WXCredentialsMatcher extends HashedCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo info) {
        UserToken token = (UserToken) authenticationToken;
        // 获得用户输入的密码:(可以采用加盐(salt)的方式去检验)
        String inPassword = token.getCode();
        // 获得数据库中的密码
        String dbPassword = (String) info.getCredentials();
        // 进行密码的比对
        return this.equals(inPassword, dbPassword);
    }

}
