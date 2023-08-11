package com.mju.domain;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Long id;
    private String password;
    private String phone;
    private String email;
    private String salt;
    private Date createTime;
    private Date updateTime;
    private UserInfo userInfo;
}
