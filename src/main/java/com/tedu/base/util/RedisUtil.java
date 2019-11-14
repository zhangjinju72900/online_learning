package com.tedu.base.util;

import java.util.List;  
import java.util.Map;  
import java.util.Set;  
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.tedu.base.common.redis.KeyNotFindException;
import com.tedu.base.engine.util.FormLogger;  
  
  
/** 
 *  
 * @author QLQ
 * 基于spring和redis的redisTemplate工具类 
 * 针对所有的hash 都是以h开头的方法 
 * 针对所有的Set 都是以s开头的方法                    不含通用方法 
 * 针对所有的List 都是以l开头的方法 
 */  
@Component//交给Spring管理(在需要缓存的地方自动注入即可使用)
public class RedisUtil {  
  
    @Autowired//(自动注入redisTemplet)
    private RedisTemplate<String, Object> redisTemplate;  
      
    //=============================common============================  
    /**
	   * 设置redisTemplate
	   * @param redisTemplate the redisTemplate to set
	   */ 
	  public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) { 
	    this.redisTemplate = redisTemplate; 
	  } 
	     
	  /**
	   * 获取 RedisSerializer
	   * <br>------------------------------<br>
	   */ 
	  protected RedisSerializer<String> getRedisSerializer() { 
	    return redisTemplate.getStringSerializer(); 
	  }
	 
	/**
	 * 添加
	 * @throws Exception 
	 */
	public boolean add(String keyStr, String value) throws Exception  {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(keyStr);
				byte[] name = serializer.serialize(value);
				return connection.setNX(key, name);
			}
		});
		if (!result) {
			throw new Exception("添加失败，可能原因是key已经存在, key = " + keyStr);
		}
		return result;
	}

	/**
	 * 删除
	 */
	public void delete(List<String> keys) {
		redisTemplate.delete(keys);
	}

	/**
	 * 删除
	 */
	public void delete(String keys) {
		redisTemplate.delete(keys);
	}

	/**
	 * 修改对象
	 * @throws Exception 
	 */
	public boolean update(String keyStr, String value) throws Exception   {
		if (get(keyStr) == null) {
			throw new Exception("数据行不存在, key = " + keyStr);
		}
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(keyStr);
				byte[] name = serializer.serialize(value);
				connection.set(key, name);
				return true;
			}
		});
		return result;
	}

	public String get(final String keyId) {
		String result = redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(keyId);
				byte[] value = connection.get(key);
				if (value == null) {
					return null;
				}
				return serializer.deserialize(value);
			}
		});
		return result;
	} 
      
}