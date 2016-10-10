package com.kollice.book.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by kollice on 2016/4/3.
 */
@Component
@ConfigurationProperties(prefix = "author",locations = {"classpath:conf/author.properties"}) // 读取properties文件中的配置信息
public class AuthorSettings {
    private String name;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
