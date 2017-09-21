package user.controller;

import org.python.antlr.ast.Str;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import user.model.User;
import user.service.RegisterValidateService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller(value = "/user")
public class UserController {

    @Autowired
    private RegisterValidateService registerValidateService;

    @RequestMapping(value = "/register")
    public Map<String,String> register(HttpServletRequest request, User user) throws Exception{
        String mod = request.getParameter("mod");
        Map<String, String> map = new HashMap();
        if("signUp".equals(mod)){
//            注册
            registerValidateService.processRegister(user);
            map.put("signUp","success");
        }else if("activate".equals(mod)){
//            激活
            String email = request.getParameter("email");
            String validateCode = request.getParameter("validateCode");

            try{
                registerValidateService.processActivate(email, validateCode);
                map.put("signUp","success");
            }catch (Exception e){
                map.put("signUp","failure");
            }
        }
        return map;
    }


}
