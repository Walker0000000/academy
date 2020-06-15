package com.academy.shiro.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShiroUtil {

    public static Map<String, String> getFilterChainDefinitionMap(){
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 公共请求
        filterChainDefinitionMap.put("/common/**", "anon");
        //放行Swagger2页面，需要放行这些
        filterChainDefinitionMap.put("/swagger-ui.html","anon");
        filterChainDefinitionMap.put("/swagger/**","anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/webjars/springfox-swagger-ui/css/typography.css", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**","anon");
        filterChainDefinitionMap.put("/v2/**","anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        //
        // 静态资源
        filterChainDefinitionMap.put("/images/**", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/page/**", "anon");
        filterChainDefinitionMap.put("/layui/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/fileResource/**", "anon");

        // druid数据库
        filterChainDefinitionMap.put("/druid/**", "anon");

        //微信登陆
        filterChainDefinitionMap.put("/wx/wxLogin/**", "anon");
        filterChainDefinitionMap.put("/wx/wxHome/**", "anon");
        filterChainDefinitionMap.put("/wx/goods/**", "anon");
        //后台登录方法
        filterChainDefinitionMap.put("/login/*", "anon"); // 表示可以匿名访问
        //其余接口一律拦截
        filterChainDefinitionMap.put("/**", "user");
        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截
        filterChainDefinitionMap.put("/**", "kickOut,anon");
        return filterChainDefinitionMap;
    }

}
