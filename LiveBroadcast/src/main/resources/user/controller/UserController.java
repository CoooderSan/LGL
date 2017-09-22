package user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import user.model.User;
import user.service.RegisterValidateService;
import util.JedisUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private RegisterValidateService registerValidateService;

    @Autowired
    JedisUtil jedisUtil;

    @RequestMapping(value = "/register")
    public Map<String,String> register(HttpServletRequest request, User user) throws Exception{
        String mod = request.getParameter("mod");
        Map<String, String> map = new HashMap();
        if("signUp".equals(mod)){
//            注册
            registerValidateService.processRegister(user);
            map.put("retCode","1");
        }else if("activate".equals(mod)){
//            激活
            String email = request.getParameter("email");
            String validateCode = request.getParameter("validateCode");

            try{
                registerValidateService.processActivate(user);
                map.put("retCode","1");
            }catch (Exception e){
                map.put("retCode","0");
            }
        }
        return map;
    }

    @RequestMapping(value = "/signIn")
    @ResponseBody
    public Map<String,String> signIn(HttpServletRequest request, HttpSession session, User user) throws Exception{
        Map<String, String> map = registerValidateService.signIn(user);
        if("1".equals(map.get("retCode"))){
//            如果登录成功，将用户放入session中
            session.setAttribute("userName",user.getName());
        }
        return map;
    }


}
