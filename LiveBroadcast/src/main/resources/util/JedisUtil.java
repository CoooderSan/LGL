package util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Redis缓存操作的工具类
* @Title: JedisUtil.java 
* @Package com.suyin.redis.util 
* @Description:
* @author yyy   
* @date 2015年7月23日 下午5:36:15 
* @version V1.0
 */
public class JedisUtil {
	
	/**
	 * logger 日志记录器
	 */
	private static final Logger logger = Logger.getLogger(JedisUtil.class);
	
	/**
	 * jedisPool redis连接池
	 */
	@Resource(name = "jedisPool")
	public JedisPool jedisPool;
	
	/**
	 * 方法描述:从连接池获取jedis对象
	 * @return 返回Jedis的有效对象
	 */
	public Jedis getResource(){
		logger.debug("[JedisUtil:getResource]: get jedis Resource from Pool...");
		Jedis jedis = null;//声明jedis对象
		int cycleTimes = 0;//出现异常已经循环获取的次数
		try{
			jedis = jedisPool.getResource();//从pool中获取jedis对象
		}catch (JedisConnectionException ex) {
			try {
				//获取占用异常,捕获异常,等待0.5秒后继续执行获取
				logger.debug("[JedisUtil:getResource]:redis pool is full,Program will sleep 0.5s to wait an idle.message:\n"+ex.getMessage());
				
				while(cycleTimes < 3){
					cycleTimes ++ ;//获取次数 +1;
		            Thread.sleep(500);//等待0.5s
		            
		        	logger.debug("[JedisUtil:getResource]:waiting to get an idle...");
		        	try{
		        		jedis = jedisPool.getResource();//重新获取jedis对象
		        	}catch(JedisConnectionException ex1){
			        	logger.debug("[JedisUtil:getResource]:get an idle failed.Program will try again.");
		        		//出现获取异常,继续执行
		        		continue;
		        	}
		        	
		        	//获取到对象后跳出循环
		        	if(jedis != null){
			        	//输出获取成功的消息
			        	logger.debug("[JedisUtil:getResource]:get an idle success.");
		        		break;
		        	}
		        	else{ //如果获取出对象为null,则继续循环等待获取.
			        	logger.debug("[JedisUtil:getResource]:get an idle is null.Program will try again.");
		        		continue;
		        	}
				}
            }
			//处理线程截断异常
            catch (InterruptedException e) {
            	logger.error("[JedisUtil:getResource]:get jedis object failed.message:\n"+e.getMessage());
            }
		}
		//获取对象如果不为空则返回
		if(jedis != null){
			logger.debug("[JedisUtil:getResource]: get jedis Resource from Pool success.");
		}else{//当循环获取超过三次直接抛出异常 返回null
        	logger.error("[JedisUtil:getResource]:get jedis object failed.if redis server is runing,please check the configration and Network connection.");
        	//throw new SPTException("server can not get jedis Resource!");
		}
		return jedis;
	}
	
	/**
	 * 方法描述:使用完毕后将jedis对象归还连接池
	 * @param Jedis 从pool中获取的jedis对象
	 */
	public void returnResource(Jedis jedis){
		try{
			if(jedis != null)
				this.jedisPool.returnResource(jedis);//归还对象至pool
			logger.debug("[JedisUtil:returnResource]: return jedis Resource to Pool  ...");
		}catch(JedisConnectionException ex){
			//归还失败,强制销毁该链接
			this.jedisPool.returnBrokenResource(jedis);
		}
	}
	
	/**
	 * 设定一个key的活动时间（s）
	 */
	public Long expire(String key,int seconds) {
		Jedis jedis = null;//声明一个链接对象
		Long value = null;//
		
		try{
			jedis = this.getResource();//获取jedis资源
			
			
			if(jedis != null) 
				value = jedis.expire(key,seconds);
			
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}
	/**
	 * hash操作
	 * 向名称为key的hash中添加元素field<—>value
	 */
	public Long hset(String key, String field, String values) {
		Jedis jedis = null;//声明一个链接对象
		Long value = null;//
		
		try{
			jedis = this.getResource();//获取jedis资源
			
			if(jedis != null) 
				value = jedis.hset(key,field,values);
			
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}
	/**
	 * hash操作
	 * 返回名称为key的hash中field对应的value
	 */
	public String hget(String key, String field) {
		Jedis jedis = null;//声明一个链接对象
		String value = null;//
		
		try{
			jedis = this.getResource();//获取jedis资源
			
			
			if(jedis != null) 
				value = jedis.hget(key,field);
			
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}
	/**
	 * hash操作
	 * 将名称为key的hash中field的value增加integer
	 */
	public Long hincrBy(String key, String field, int integer) {
		Jedis jedis = null;//声明一个链接对象
		Long value = null;//
		
		try{
			jedis = this.getResource();//获取jedis资源
			
			if(jedis != null) 
				value = jedis.hincrBy(key,field,integer);
			
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * hash操作
	 * 名称为key的hash中是否存在键为field的域
	 * @author zyh
	 */
	public boolean hexists(String key, String field) {
		Jedis jedis = null;//声明一个链接对象
		boolean value = false;//
		try{
			jedis = this.getResource();//获取jedis资源
			if(jedis != null) 
				value = jedis.hexists(key,field);
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}
	/**
	 * hash操作
	 * 删除名称为key的hash中键为field的域
	 * @author zyh
	 */
	public Long hdel(String key, String... fields) {
		Jedis jedis = null;//声明一个链接对象
		Long value = null;//
		
		try{
			jedis = this.getResource();//获取jedis资源
			
			
			if(jedis != null) 
				value = jedis.hdel(key,fields);
			
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}
	/**
	 * hash操作
	 * 返回名称为key的hash中元素个数
	 * @author zyh
	 */
	public Long hlen(String key) {
		Jedis jedis = null;//声明一个链接对象
		Long value = null;//
		try{
			jedis = this.getResource();//获取jedis资源
			if(jedis != null) 
				value = jedis.hlen(key);
			
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * hash操作
	 * 返回名称为key的hash中所有键
	 * @author zyh
	 */
	public Set<String> hkeys(String key) {
		Jedis jedis = null;//声明一个链接对象
		Set<String> value = null;//
		
		try{
			jedis = this.getResource();//获取jedis资源
			
			if(jedis != null) 
				value = jedis.hkeys(key);
			
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * hash操作
	 * 返回名称为key的hash中所有键对应的value
	 * @author zyh
	 */
	public List<String> hvals(String key) {
		Jedis jedis = null;//声明一个链接对象
		List<String> value = null;//
		
		try{
			jedis = this.getResource();//获取jedis资源
			if(jedis != null) 
				value = jedis.hvals(key);
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * hash操作
	 * 返回名称为key的hash中所有的键（field）及其对应的value
	 * @author zyh
	 */
	public Map<String,String> hgetall(String key) {
		Jedis jedis = null;//声明一个链接对象
		Map<String,String> value = null;//
		
		try{
			jedis = this.getResource();//获取jedis资源
			if(jedis != null) 
				value = jedis.hgetAll(key);
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}
	
	
	
	
	/**
	 * list操作
	 * 在名称为key的list尾添加一个值为values的元素
	 * @author zyh
	 */
	public Long rpush(String key,String... values) {
		Jedis jedis = null;//声明一个链接对象
		Long value = null;//
		
		try{
			jedis = this.getResource();//获取jedis资源
			
			
			if(jedis != null) 
				value = jedis.rpush(key,values);
			
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}
	/**
	 * list操作
	 * 在名称为key的list头添加一个值为values的 元素
	 * @author zyh
	 */
	public Long lpush(String key,String... values) {
		Jedis jedis = null;//声明一个链接对象
		Long value = null;//
		
		try{
			jedis = this.getResource();//获取jedis资源
			
			if(jedis != null) 
				value = jedis.lpush(key,values);
			
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}
	/**
	 * list操作
	 * 返回名称为key的list的长度
	 * @author zyh
	 */
	public Long llen(String key) {
		Jedis jedis = null;//声明一个链接对象
		Long value = null;//
		
		try{
			jedis = this.getResource();//获取jedis资源
			
			if(jedis != null) 
				value = jedis.llen(key);
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * list操作
	 * 返回名称为key的list中index位置的元素
	 * @author zyh
	 */
	public String lindex(String key,int index) {
		Jedis jedis = null;//声明一个链接对象
		String value = null;//
		
		try{
			jedis = this.getResource();//获取jedis资源
			
			if(jedis != null) 
				value = jedis.lindex(key,index);
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}
	

	/**
	 * list操作
	 * 给名称为key的list中index位置的元素赋值为val
	 * @author zyh
	 */
	public String lset(String key,int index,String val) {
		Jedis jedis = null;//声明一个链接对象
		String value = null;//
		try{
			jedis = this.getResource();//获取jedis资源
			if(jedis != null) 
				value = jedis.lset(key,index,val);
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * list操作
	 * 返回名称为key的list中start至end之间的元素（下标从0开始，下同）
	 * @author zyh
	 */
	public List<String> lrange(String key,long start,long end) {
		Jedis jedis = null;//声明一个链接对象
		List<String> value = null;//
		
		try{
			jedis = this.getResource();//获取jedis资源
			
			if(jedis != null) 
				value = jedis.lrange(key,start,end);
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}
	/**
	 * list操作
	 * 截取名称为key的list，保留start至end之间的元素（下标从0开始)
	 * @author zyh
	 */
	public String ltrim(String key,long start,long end) {
		Jedis jedis = null;//声明一个链接对象
		String value = null;//
		
		try{
			jedis = this.getResource();//获取jedis资源
			
			if(jedis != null) 
				value = jedis.ltrim(key,start,end);
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * list操作
	 * 返回并删除名称为key的list中的首元素
	 * @author zyh
	 */
	public String lpop(String key) {
		Jedis jedis = null;//声明一个链接对象
		String value = null;
		
		try{
			jedis = this.getResource();//获取jedis资源
			
			if(jedis != null) 
				value = jedis.lpop(key);
			
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}
	/**
	 * list操作
	 * 返回并删除名称为key的list中的尾元素
	 * @author zyh
	 */
	public String rpop(String key) {
		Jedis jedis = null;//声明一个链接对象
		String value = null;
		
		try{
			jedis = this.getResource();//获取jedis资源
			
			if(jedis != null) 
				value = jedis.rpop(key);
			
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}
	
	
	/**
	 * 确认一个key是否存在
	 * @author zyh
	 */
	public boolean exists(String key) {
		Jedis jedis = null;//声明一个链接对象
		boolean value = false;
		
		try{
			jedis = this.getResource();//获取jedis资源
			
			if(jedis != null) 
				value = jedis.exists(key);
			
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}
	/**
	 * set操作
	 * 测试member是否是名称为key的set的元素
	 * @author zyh
	 */
	public boolean sismember(String key, String member) {
		Jedis jedis = null;//声明一个链接对象
		boolean value = false;//
		
		try{
			jedis = this.getResource();//获取jedis资源
			
			if(jedis != null) 
				value = jedis.sismember(key,member);
			
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}
	/**
	 * String操作
	 * 名称为key的string增1操作
	 * @author zyh
	 */
	public Long incr(String key) {
		Jedis jedis = null;//声明一个链接对象
		Long value = null;//
		
		try{
			jedis = this.getResource();//获取jedis资源
			
			if(jedis != null) 
				value = jedis.incr(key);
			
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}
	
	

	/**
	 * string操作
	 * 名称为key的string减1操作
	 * @author zyh
	 */
	public Long decr(String key) {
		Jedis jedis = null;//声明一个链接对象
		Long value = null;//
		
		try{
			jedis = this.getResource();//获取jedis资源
			
			if(jedis != null) 
				value = jedis.decr(key);
			
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 方法描述:根据关键字从redis服务器中获取对应的value
	 * @param String key 键值
	 * @return 存储在redis中的value
	 */
	public String getRedisStrValue(String key) {
		
		Jedis jedis = null;//声明一个链接对象
		String value = null;//获取的键值所对应的值
		
		try{
			jedis = this.getResource();//获取jedis资源
			
			//资源不为空,则获取对应的value
			if(jedis != null) 
				value = jedis.get(key);
			
		}finally{
			if(jedis != null)
				this.returnResource(jedis);
		}
		return value;
	}


	/**
	 * 方法描述:往redis中注入缓存对象
	 * @param String key 对象的键值
	 * @param String value 键值所对应的值
	 * @return 返回成功与否,成功返回true 失败返回false
	 */
	public boolean setRedisStrValue(String key,String value) {
		
		Jedis jedis = null;//声明一个链接对象
		boolean flag = true;//返回标记,默认成功
		
		try{
			jedis = this.getResource();//获取资源
			
			//资源不为空则执行注入操作 否则返回注入失败
			if(jedis != null) 
				jedis.set(key,value);
			else
				flag = false;
		}finally{
			//归还资源
			if(jedis != null)
				this.returnResource(jedis);
		}
		return flag;
	}

	/**
	 * 方法描述:往redis中注入缓存对象
	 * @param String key 对象的键值
	 * @param String value 键值所对应的值
	 * @param int seconds 键值存储时间,如果为负数,则不设存储上限时间 单位:秒
	 * @return 返回成功与否,成功返回true 失败返回false
	 */
	public boolean setRedisStrValue(String key,String value,int seconds) {
		
		boolean flag = true;//返回标记,默认成功
		
		//如果设置时间为负数,则无上限时间
		if(seconds <= 0){
			this.setRedisStrValue(key, value);
			return flag;
		}
		
		Jedis jedis = null;//声明一个链接对象
		
		try{
			jedis = this.getResource();//获取资源
			
			//资源不为空则执行注入操作 否则返回注入失败
			if(jedis != null) {
				//判断是否已经存在,如果已经存在则删除
				if(jedis.exists(key)){
					jedis.del(key);
				}
				//该方法内容为,如果含有相同的key值,则不覆盖.
				jedis.setex(key,seconds,value);
			}
			else
				flag = false;
		}finally{
			//归还资源
			if(jedis != null)
				this.returnResource(jedis);
		}
		return flag;
	}
	
	/**
	 * 方法描述:删除redis中的缓存
	 * @param 缓存的key值
	 * @return 返回是否成功,成功:true 失败:false
	 */
	public boolean delRedisStrValue(String... keys) {
		
		Jedis jedis = null;//声明一个链接对象
		boolean flag = true;//返回标记,默认成功
		try{
			jedis = this.getResource();//获取资源
			
			//资源不为空则执行删除操作 否则返回注入失败
			if(jedis != null)
				jedis.del(keys);
			else
				flag = false;
		}finally{
			//归还资源
			if(jedis != null)
				this.returnResource(jedis);
		}
		return flag;
	}	
	
	/**
	 * 方法描述:查询对应的缓存keys
	 */
	public Set<String> getKeys(String keyPrefix) {
		
		logger.info("RedisUtil.getKeys param: "+keyPrefix);
		
		Jedis jedis = null;//jedis对象
		Set<String> keys = null;//keys列表
		
		try{
			 //获取连接
			 jedis = this.getResource();
			 //根据前台传过来的规则获取缓存key列表
			 if(null != keyPrefix && !"".equals(keyPrefix)){
				 keys = jedis.keys(keyPrefix);
			 }
		}catch (Exception e) {
			logger.error("[JedisUtil.getKeys]:failed. throw e:" + e.getMessage());
		}finally{
			//使用完毕后将jedis对象归还连接池
			if(jedis != null)
				jedisPool.returnResource(jedis);
		}
		
		return keys;
	}

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	
	
	/**
	 * 设置redis的watch，监视变量
	 * @param watchKey  最大值的key、
	 * @param value  watch的值
	 * @return 1成功  0失败
	 */
	public int initWatch(String watchKey,String value){
		
		int result = 0;
		try{
			Jedis jedis = this.getResource();
			jedis.set(watchKey,value);
			 
			// 将产品id当做watch的key，监控这个长度
			String numKey = watchKey+"_Num";
            //判断是否有这个key
            if(!jedis.exists(numKey)){
           	 jedis.set(numKey,"0");
            }
            
            jedis.watch(numKey);// watchkeys
			this.returnResource(jedis);
			result = 1;
		}catch(Exception e){
			logger.error("[JedisUtil:initWatch]: 设置redis的watch，监视变量 failed.message:\n"+e.getMessage());
			e.printStackTrace();
		} 
		
		return result;
		
	}
	/**
	 * 添加元素，watch监控
	 * @param succKey  添加的key
	 * @param value   添加的值
	 * @param watchKey  最大值的key
	 * @return 1成功  0失败
	 */
	public int addByWatch(String succKey,String value,String watchKey){
		
		int result = 0;
		try{
			Jedis jedis = this.getResource();
			//得到该产品总共限额
			 String watchValueStr = jedis.get(watchKey);
            int watchValue = Integer.valueOf(watchValueStr);
            
          
            String numKey = watchKey+"_Num";
            //得到watch里面，增加的条数
            String numWatchStr = jedis.get(numKey);
            int numWatch = Integer.valueOf(numWatchStr);
			
            if(numWatch<watchValue){
           	
           	 Transaction tx = jedis.multi();// 开启事务
           	  
           	 tx.incr(numKey);
           	  
           	 List<Object> list = tx.exec();// 提交事务，如果此时watchkeys被改动了，则返回null
           	  
           	 if(list!=null){
           		 
           		jedis.sadd(succKey, value);//添加到set集合，
           		 
           		result = 1;
           	 }else{
           		result = 0;
           	 }
           	 
            }else{
            	result = 0;
            }
			
			this.returnResource(jedis);
			 
		}catch(Exception e){
			logger.error("[JedisUtil:addByWatch]: 添加元素，watch监控 failed.message:\n"+e.getMessage());
			e.printStackTrace();
		} 
		
		
		return result;
	}
	
	/**
	 * 验证值是否存在，存在返回结果并删除
	 * @param succKey
	 * @param value
	 * @return 1成功  0失败
	 */
	public int findValue(String succKey,String value){
		int result = 0;
		try{
			Jedis jedis = this.getResource();
			Set<String> midSet = jedis.smembers(succKey);
			
			boolean flag = false;
			for (String str : midSet) {
				if(str.equals(value)){
					flag = true;
					jedis.srem(succKey, value);
					break;
				}
			}
            if(flag){
            	result = 1;
            }
            
			this.returnResource(jedis);
			 
		}catch(Exception e){
			logger.error("[JedisUtil:findValue]:验证值是否存在，存在返回结果并删除 failed.message:\n"+e.getMessage());
			e.printStackTrace();
		} 
		
		return result;
	}
	 
}
