package user.service;

import user.model.User;

public interface RegisterValidateService {

//    处理注册
    void processRegister(User user);

//    处理激活
    void processActivate(String email, String validateCode) throws Exception;
}
