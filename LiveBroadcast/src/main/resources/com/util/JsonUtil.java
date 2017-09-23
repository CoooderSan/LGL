package com.util;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 将传入类型转换为json格式字符串
 * @author Yll
 * 
 */
public class JsonUtil {
	
	/**
	 * 将map 装换为json格式字符串
	 * @author Yll
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String convertMapToJsonString(Map map){
		return JSONObject.fromObject(map).toString();
	}
	
	/**
	 * 将List<T> 装换为String
	 * @author Yll
	 * @return
	 */
	public static <T> String convertMapToJsonString(List<T> vt){
		return JSONArray.fromObject(vt).toString();
	}
	
	/**
	 * 将<T> 装换为String
	 * @author Yll
	 * @return
	 */
	public static <T> String convertObjectToJsonString(T t){
		return JSONObject.fromObject(t).toString();
	}
	/**
	 * json字符串转为对象返回
	 * @param json
	 * @param t
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T jsonToBean(String json, Class<T> t) throws InstantiationException, IllegalAccessException{
		return (T)JSONObject.toBean(JSONObject.fromObject(json), t.newInstance().getClass());
	}
	
}