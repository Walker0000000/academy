package com.academy.shiro.common;

public enum LoginType {

    /**
     * 用户密码登录
     */
    USER_PASSWORD("user_password_realm"),
    /**
     * 第三方登录(微信登录)
     */
    WECHAT_LOGIN("wechat_login_realm");

    private String type;

    private LoginType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return this.type.toString();
    }
}
