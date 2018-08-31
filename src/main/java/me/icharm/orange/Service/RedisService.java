package me.icharm.orange.Service;


import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Simple package for redis operations
 *
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/31 18:50
 */
@Service
public class RedisService {

    @Autowired
    StringRedisTemplate redis;

    /**
     * Set string value
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        redis.opsForValue().set(key, value);
    }

    /**
     * Set string value with exprie second count
     *
     * @param key    key
     * @param value  value
     * @param exprie second
     */
    public void set(String key, String value, Long exprie) {
        redis.opsForValue().set(key, value, exprie, TimeUnit.SECONDS);
    }

    public void set(String key, Object value) {
        set(key, JSON.toJSONString(value));
    }

    /**
     * Set string value by key with exprie time
     *
     * @param key key
     * @param value value
     * @param exprie exprie time, Default unit is second.
     */
    public void set(String key, Object value, Long exprie) {
        set(key, JSON.toJSONString(value), exprie);
    }

    /**
     * Set string value by key with offest
     *
     * @param key key
     * @param value value
     * @param offest offest
     */
    public void setWithOffest(String key, Object value, Long offest) {
        redis.opsForValue().set(key, JSON.toJSONString(value), offest);
    }

    /**
     * Get value by key
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return redis.opsForValue().get(key);
    }

    /**
     * Get object by key and class type
     *
     * @param key   key
     * @param clazz T class
     * @param <T>   generic type
     * @return
     */
    public <T> T get(String key, Class<T> clazz) {
        String json = get(key);
        return JSON.parseObject(json, clazz);
    }

    /**
     * Delete by key
     *
     * @param key key
     */
    public void delete(String key) {
        redis.delete(key);
    }

    /**
     * Is key existed
     *
     * @param key key
     * @return true existed, false not.
     */
    public boolean hasKey(String key) {
        return redis.hasKey(key);
    }

    /**
     * Set exprie time by key
     *
     * @param key key
     * @param time seconds or min or ...
     * @param unit Specify the unit of the previous parameter
     * @return
     */
    public boolean exprie(String key, Long time, TimeUnit unit){
        return redis.expire(key, time, unit);
    }
}
