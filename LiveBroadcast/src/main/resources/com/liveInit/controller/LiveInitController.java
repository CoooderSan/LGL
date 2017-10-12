package com.liveInit.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.alibaba.fastjson.JSON;
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
	public Map<String,Object> initC(HttpServletRequest request, HttpSession session) throws IOException{
			Map<String,Object> liveMap = new HashMap<String,Object>();
			String userName =(String)session.getAttribute("userName");
			if(userName != null && !("").equals(userName) && !("null").equals(userName)){
				String userNameCollectInfo = jedisUtil.getRedisStrValue(userName+"_collectInfo");
				if(userNameCollectInfo != null && !("").equals(userNameCollectInfo) && !("null").equals(userNameCollectInfo)){
					Map<String,String> userNameCollectInfoMap = (Map)JSON.parseObject(userNameCollectInfo);
					List<String> userNameCollectInfoList = new ArrayList<String>();
					for(String key : userNameCollectInfoMap.values()){
						userNameCollectInfoList.add(key);
					}
					liveMap.put("userNameCollectInfo",userNameCollectInfoList);
				}
			}
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
