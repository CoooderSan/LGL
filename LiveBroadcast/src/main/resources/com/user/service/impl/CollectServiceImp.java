package com.user.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.user.service.CollectService;
import com.util.JedisUtil;

@Service("CollectService")
public class CollectServiceImp implements CollectService{
	
	/**
	 * logger 日志记录器
	 */
	private static final Logger logger = Logger.getLogger(JedisUtil.class);
	
	@Autowired
    JedisUtil jedisUtil;

	public Integer updateUserCollect(String updateFlag,String userName,String collectInfo){
		
		Integer result = 0;
		Boolean updateResult = false;
		Map<String,String> userOldCollectInfoMap = new HashMap<String, String>();
		jedisUtil.getResource();
		String userOldCollectInfo = jedisUtil.getRedisStrValue(userName+"_collectInfo");
		if(userOldCollectInfo != null && !("null").equals(userOldCollectInfo)){
			userOldCollectInfoMap = (Map)JSON.parseObject(userOldCollectInfo);
		}
		//如果为新增收藏
		if(updateFlag.equals("1")){
			if(userOldCollectInfoMap.get(collectInfo) == null){
				userOldCollectInfoMap.put(collectInfo, collectInfo);
				updateResult = jedisUtil.setRedisStrValue(userName+"_collectInfo", toString(userOldCollectInfoMap));
			}
		}
		//如果为取消收藏
		else if(updateFlag.equals("0")){
			if(userOldCollectInfoMap.get(collectInfo) != null){
				userOldCollectInfoMap.remove(collectInfo);
				if(userOldCollectInfoMap.size() == 0){
					updateResult = jedisUtil.delRedisStrValue(userName+"_collectInfo");
				}else{
					updateResult = jedisUtil.setRedisStrValue(userName+"_collectInfo", toString(userOldCollectInfoMap));
				}
			}
		}
		if(updateResult)result = 1;
		return result;
	};
	
	private String toString(Map<String,String> userOldCollectInfoMap){
		StringBuffer newMapString = new StringBuffer();
		int count = 0;
		try {
			newMapString.append("{");
			for(String key : userOldCollectInfoMap.keySet()){
				count++;
				newMapString.append("'"+key+"':");
				if(count == userOldCollectInfoMap.size()){
					newMapString.append("'"+userOldCollectInfoMap.get(key)+"'");
				}else{
					newMapString.append("'"+userOldCollectInfoMap.get(key)+"',");
				}
			}
			newMapString.append("}");
			newMapString.toString();
		} catch (Exception e) {
			logger.error("收藏信息Map转String出错", e);
		}
		return newMapString.toString();
	}
}
