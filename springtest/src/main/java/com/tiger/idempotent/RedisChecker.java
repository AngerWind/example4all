/**
 * @Title: RedisLock.java
 * @Copyright: © 2018 我来贷
 * @Company: 深圳卫盈智信科技有限公司
 */

package com.tiger.idempotent;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

/**
 * Redis校验
 *
 * @author sweet.qin
 * @date 2018-12-12 17:00:43
 * @version v1.0
 */

public class RedisChecker {
	
    @SuppressWarnings("rawtypes")
	private static DefaultRedisScript<List> script;
    
    private static String luaScript ="qosScript.lua";
    	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;        
	
	/**
	 * 
	 * 脚本初始化
	 *
	 */
	@SuppressWarnings("rawtypes")
	@PostConstruct
	public void ScriptInit() {
			script = new DefaultRedisScript<List>();
			script.setResultType(List.class);
			script.setScriptSource(new ResourceScriptSource(new ClassPathResource(luaScript)));
	}

	@SuppressWarnings("rawtypes")
	public List check(String key,int expired,int times) throws Exception {

		//key
		List<String> keys = new ArrayList<String>();
		keys.add(key);
		//参数
		List<String> args = new ArrayList<String>();
		args.add(String.valueOf(expired));
		args.add(String.valueOf(times));		
		args.add(String.valueOf(System.currentTimeMillis()));		

        //spring自带的执行脚本方法中，集群模式直接抛出不支持执行脚本异常，此处拿到原redis的connection执行脚本
		List result = stringRedisTemplate.execute(new RedisCallback<List>() {
			@Override
			public List doInRedis(RedisConnection connection) throws DataAccessException {
				Object nativeConnection = connection.getNativeConnection();
				// 集群模式和单点模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
				// 集群
				if (nativeConnection instanceof JedisCluster) {
					return (List) ((JedisCluster) nativeConnection).eval(script.getScriptAsString(), keys, args);
				}
				// 单点
				else if (nativeConnection instanceof Jedis) {
					return (List) ((Jedis) nativeConnection).eval(script.getScriptAsString(), keys, args);
				}
				return null;
			}
		});
		
		return result;
	}	


}
