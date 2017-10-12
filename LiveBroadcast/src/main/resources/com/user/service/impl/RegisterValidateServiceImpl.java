package com.user.service.impl;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import com.user.mapper.UserMapper;
import com.user.model.SendEmail;
import com.user.model.User;
import com.user.service.RegisterValidateService;
import com.util.JedisUtil;
import com.util.MD5Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("registerValidateService")
public class RegisterValidateServiceImpl implements RegisterValidateService{

    /*@Autowired
    private UserMapper userMapper;*/

    @Autowired
    private JedisUtil jedisUtil;

    public void processRegister(User user) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//       将用户的邮箱通过MD5变为激活码
        user.setValidateCode(MD5Util.encode2hex(user.getEmail()));
        user.setRegisterTime(sdf.format(new Date()));
//        userMapper.saveUser(user);
//        存入redis
        jedisUtil.setRedisStrValue(user.getName(),user.toString());
//       拼邮件
        StringBuffer sb = new StringBuffer("点击下面链接激活账号，48小时生效，否则重新注册账号，链接只能使用一次，请尽快激活！</br>");
//        sb.append("<a href=\"http://www.cooodersan.xin/user/register?mod=activate&email=");
        sb.append("<a href=\"http://localhost:8080/LGL/user/register?mod=activate&email=");
        sb.append(user.getEmail());
        sb.append("&validateCode=");
        sb.append(user.getValidateCode());
        sb.append("&name=");
        sb.append(user.getName());
        sb.append("\">激活我的LiveGamesList</a>");

//        发邮件
        SendEmail.send(user.getEmail(), sb.toString());
    }

    public void processActivate(User user) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        User user = userMapper.findUser(email);
        User userObj = new User();
//        查找出redis中的user对象
        String obj = jedisUtil.getRedisStrValue(user.getName());
        if(obj != null && !"".equals(obj)){
            JSONObject object = JSONObject.fromObject(obj);
            userObj = (User)JSONObject.toBean(object, User.class);
        }

        if(userObj != null){
            if(userObj.getStatus() == 0){
                Date current = new Date();
//                计算出最后激活时间
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(sdf.parse(userObj.getRegisterTime()));
                calendar.add(Calendar.DATE, 2);
//                验证码是否过期
                if(current.before(calendar.getTime())){
                    if(userObj.getValidateCode().equals(user.getValidateCode())){
//                        状态变为激活
                        userObj.setStatus(1);
                        userObj.setEmail(user.getEmail());
                        jedisUtil.setRedisStrValue(userObj.getName(), userObj.toString());
//                        userMapper.updateUser(user);
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

    public Map<String, String> signIn(User user) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("retCode","0");
//        从redis中获取user
        String userObj = jedisUtil.getRedisStrValue(user.getName());
        if(userObj != null && !"".equals(userObj)){
            Map<String,String> userMap = (Map) JSON.parseObject(userObj);
//            如果密码一致返回登录成功
            if(user.getPassword().equals(userMap.get("password"))){
                map.put("retCode","1");
            }
        }
        return map;
    }
}
