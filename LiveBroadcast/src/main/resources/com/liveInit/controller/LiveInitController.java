package com.liveInit.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.util.JedisUtil;

@Controller
@RequestMapping("/liveInit")
public class LiveInitController {

	@Autowired
	JedisUtil jedisUtil;
	
	@RequestMapping("/initC")
	@ResponseBody
	public Map<String,Object> initC(HttpServletRequest request) throws IOException{
	
		Map<String,Object> liveMap = new HashMap<String,Object>();
		jedisUtil.getResource();
		String LOLInfo = jedisUtil.getRedisStrValue("LOL_rooms");
		String DOTAInfo = jedisUtil.getRedisStrValue("DOTA2_rooms");
		String hearthstoneInfo = jedisUtil.getRedisStrValue("hearthstone_rooms");
		String kingGInfo = jedisUtil.getRedisStrValue("kingglory_rooms");
		liveMap.put("LOLInfo",LOLInfo);
		liveMap.put("DOTAInfo",DOTAInfo);
		liveMap.put("hearthstoneInfo",hearthstoneInfo);
		liveMap.put("kingGInfo",kingGInfo);
		return liveMap;
		
	}
}
