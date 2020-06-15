package com.academy.config;

public enum DataSourceEnum {

    POST("post"),GET("get");

    private String value;

    DataSourceEnum(String value){this.value=value;}

    public String getValue() {
        return value;
    }

}
