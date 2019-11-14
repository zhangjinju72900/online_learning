package com.tedu.base.util;

import com.tedu.base.common.redis.KeyNotFindException;
import com.tedu.base.common.redis.RedisService;
import com.tedu.base.util.AESUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.List;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2019-08-22
 */
@Component("aliVideoService")
public class AliVideoService {


    @Resource
    RedisUtil redisUtil;

    @Resource
    AESUtil aesUtil;

    public String getToken(String vid) {
        String token = redisUtil.get(vid);
        Long expire = Instant.now().getEpochSecond() + 7200;

        String result = "";
        try {
            if (token != null && !token.equals("")) {
                Long expireTime = Long.parseLong(redisUtil.get(vid + "TokenExpire"));
                if (Instant.now().getEpochSecond() - expireTime >= 0) {
                    //Token过期
                    result = aesUtil.encrypt("aliToken", vid);
                    token = result;
                    redisUtil.update(vid, result);
                    redisUtil.update(vid + "TokenExpire", expire.toString());
                }
            } else {
                result = aesUtil.encrypt("aliToken", vid);
                token = result;
                redisUtil.add(vid, result);
                redisUtil.add(vid + "TokenExpire", expire.toString());
            }
        } catch (Exception e) {

        }


        return token;
    } 
    public String getToken(String key,String value) {
        String token = redisUtil.get(key);
        if(token!=null&&!"".equals(token)) {
        	redisUtil.delete(key);
        	return token;
        }else if(!"".equals(value)) {
        	Long expire = 1L;

            String result = "";
            try {
                    result = aesUtil.encrypt(key, value);
                    token = result;
                    redisUtil.add(result, value);
                    redisUtil.add(result + "TokenExpire", expire.toString());
            } catch (Exception e) {

            }
        }


        return token;
    } 
    public void removeToken(String key) {

        try {
            	redisUtil.delete(key);;
        } catch (Exception e) {

        }


    }
}
