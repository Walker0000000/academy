package com.academy.shiro.realm;


import com.academy.entity.SysUser;
import com.academy.mapper.SysPermissionMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户密码登录realm
 */
@Slf4j
public class UserPasswordRealm extends AuthorizingRealm {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    /**
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份
     *
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("---------------- 用户密码登录 ----------------------");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String userName = token.getUsername();
        // 从数据库获取对应用户名密码的用户
        Map<String, Object> contain = new HashMap<>();
        contain.put("loginName",userName);
        List<SysUser> sysUserList = sysPermissionMapper.getSysUserList(contain);
        if(sysUserList == null || sysUserList.isEmpty()){
            throw new UnknownAccountException();
        }
        SysUser sysUser = sysUserList.get(0);

        if (sysUser != null) {
            // 用户为禁用状态
            if ("N".equals(sysUser.getStatus())) {
                throw new DisabledAccountException();
            }
            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                    sysUser, //用户
                    sysUser.getPassword(), //密码
                    getName()  //realm name
            );
            this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
            return authenticationInfo;
        }
        throw new UnknownAccountException();
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("---------------- 执行 Shiro 权限获取 ---------------------");
        Object principal = principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        if (principal instanceof SysUser) {
            SysUser sysUser = (SysUser) principal;
            Map<String, Object> contain = new HashMap<>();
            contain.put("userCode",sysUser.getUserCode());
            Set<String> roles = sysPermissionMapper.findRoleNameByUserCode(contain);
            authorizationInfo.addRoles(roles);

            Set<String> permissions = sysPermissionMapper.findPermissionsByUserCode(contain);
            authorizationInfo.addStringPermissions(permissions);
        }
        log.info("---- 获取到以下权限 -----");
        log.info(authorizationInfo.getStringPermissions().toString());
        log.info("---------------- Shiro 权限获取成功 ----------------------");
        return authorizationInfo;
    }
}
