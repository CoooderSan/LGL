package com.user.service;

import com.user.model.User;

import java.util.Map;

public interface RegisterValidateService {

//    处理注册
    void processRegister(User user);

//    处理激活
    void processActivate(User user) throws Exception;

//    登录
    Map<String, String> signIn(User user);
}
