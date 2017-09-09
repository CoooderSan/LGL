package liveInit.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sun.mail.iap.Response;

import util.JedisUtil;

@Controller
@RequestMapping("/liveInit")
public class LiveInitController {

	@Autowired
	JedisUtil jedisUtil;
	
	@RequestMapping("/initC")
	@ResponseBody
	public void initC(HttpServletRequest request,HttpServletResponse response) throws IOException{
	
		jedisUtil.getResource();
		String a = jedisUtil.getRedisStrValue("douyu_lol_rooms");
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		PrintWriter out = response.getWriter();
		out.print(a);
		
	}
}
