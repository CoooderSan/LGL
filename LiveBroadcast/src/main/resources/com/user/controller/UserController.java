package com.user.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.user.model.User;
import com.user.service.CollectService;
import com.user.service.RegisterValidateService;
import com.util.JedisUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

	/**
	 * logger 日志记录器
	 */
	private static final Logger logger = Logger.getLogger(JedisUtil.class);
	
    @Autowired
    private RegisterValidateService registerValidateService;
    
    @Autowired
    private CollectService collectService;

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
    
    @RequestMapping(value="/collect")
    @ResponseBody
    public Map<String,String> collect(HttpServletRequest request, HttpSession session){
    	
    	Map<String,String> resultMap = new HashMap<String, String>();
    	//判断新增收藏和取消收藏
    	String updateFlag = request.getParameter("updateFlag");
    	if(updateFlag == null ||("null").equals(updateFlag) || updateFlag.isEmpty()){
    		logger.debug("收藏失败,updateFlag异常");
    		resultMap.put("result", "0");
    		resultMap.put("message", "收藏失败");
    		return resultMap;
    	}
    	//用户名信息
    	String userName = (String) session.getAttribute("userName");
    	if(userName == null ||("null").equals(userName) || userName.isEmpty()){
    		logger.debug("收藏失败,userName异常");
    		resultMap.put("result", "0");
    		resultMap.put("message", "收藏失败");
    		return resultMap;
			}
			//收藏信息
			String collectInfo = request.getParameter("collectInfo");
			if(collectInfo == null ||("null").equals(collectInfo) || collectInfo.isEmpty()){
				logger.debug("收藏失败,collectInfo异常");
				resultMap.put("result", "0");
				resultMap.put("message", "收藏失败");
				return resultMap;
			}
			try {
				Integer result = collectService.updateUserCollect(updateFlag, userName, collectInfo);
				if(result == 1){
					resultMap.put("result", "1");
					resultMap.put("message", "收藏成功");
				}else{
					logger.debug("收藏失败");
    			resultMap.put("result", "0");
        		resultMap.put("message", "收藏失败");
    		}
		} catch (Exception e) {
			logger.error("收藏出错", e);
			resultMap.put("result", "0");
    		resultMap.put("message", "收藏出错");
		}
    	return resultMap;
    }


}
