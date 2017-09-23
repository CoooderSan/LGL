package com.user.model;

import java.util.Date;

public class User {
//    private Long id;
    private String name; //用户名
    private String password; //密码
    private String email; //邮箱
    private int status; //激活状态
    private String validateCode; //激活码
    private Date registerTime; //注册时间

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    @Override
    public String toString() {
        return "{" +
                "name:'" + name + '\'' +
                ", password:'" + password + '\'' +
                ", email:'" + email + '\'' +
                ", status:" + status +
                ", validateCode:'" + validateCode + '\'' +
                ", registerTime:" + registerTime +
                '}';
    }
}
