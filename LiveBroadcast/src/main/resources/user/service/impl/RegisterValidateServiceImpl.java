package user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import user.mapper.UserMapper;
import user.model.SendEmail;
import user.model.User;
import user.service.RegisterValidateService;
import util.MD5Util;

import java.util.Calendar;
import java.util.Date;

@Service
public class RegisterValidateServiceImpl implements RegisterValidateService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public void processRegister(User user) {
//       将用户的邮箱通过MD5变为激活码
        user.setValidateCode(MD5Util.encode2hex(user.getEmail()));
        userMapper.saveUser(user);
//       拼邮件
        StringBuffer sb = new StringBuffer("点击下面链接激活账号，48小时生效，否则重新注册账号，链接只能使用一次，请尽快激活！</br>");
        sb.append("<a href=\"http://www.cooodersan.xin/user/register?mod=activate&email=");
        sb.append(user.getEmail());
        sb.append("&validateCode=");
        sb.append(user.getValidateCode());
        sb.append("</a>");

//        发邮件
        SendEmail.send(user.getEmail(), sb.toString());
    }

    @Override
    public void processActivate(String email, String validateCode) throws Exception{
        User user = userMapper.findUser(email);

        if(user != null){
            if(user.getStatus() == 0){
                Date current = new Date();
//                计算出最后激活时间
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(user.getRegisterTime());
                calendar.add(Calendar.DATE, 2);
//                验证码是否过期
                if(current.before(calendar.getTime())){
                    if(validateCode.equals(user.getValidateCode())){
//                        状态变为激活
                        user.setStatus(1);
                        user.setEmail(email);
                        userMapper.updateUser(user);
                    }else{
                        throw new Exception("激活码不正确！");
                    }
                }else{
                    throw new Exception("激活码已过期！");
                }
            }else{
                throw new Exception("邮箱已激活，请登录");
            }
        }else{
            throw new Exception("该邮箱未注册！");
        }
    }
}
