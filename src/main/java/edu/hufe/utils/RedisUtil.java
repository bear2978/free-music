package edu.hufe.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 86185
 * @time 2021.04.06
 * redisTemplate封装
 */
@Component
public class RedisUtil {

    /**
     * 是否开启redis缓存  true开启   false关闭
     */
    @Value("${spring.redis.open: #{false}}")
    private boolean open;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired(required = false)
    private ValueOperations<String,String> valueOperations;

    /**
     * 存储指定过期时间的值
     * @param key
     * @param value
     * @param expire
     */
    public void set(String key, Object value, long expire){
        valueOperations.set(key, toJson(value));
        if(expire != Const.REDIS_NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    /**
     * 存储默认过期时间的值
     * @param key
     * @param value
     */
    public void set(String key, Object value){
        set(key, value, Const.REDIS_DEFAULT_EXPIRE);
    }

    /**
     * 获取值
     * @param key
     * @param clazz
     * @param expire
     * @param <T>
     * @return
     */
    public <T> List<T> getList(String key, Class<T> clazz, long expire) {
        String value = null;
        if(open){
            value = valueOperations.get(key);
            if(expire != Const.REDIS_NOT_EXPIRE){
                redisTemplate.expire(key, expire, TimeUnit.SECONDS);
            }
        }
        return value == null ? null : fromJsonArray(value, clazz);
    }

    public <T> T get(String key, Class<T> clazz, long expire) {
        List<T> value = getList(key,clazz,expire);
        return value.size() == 0 ? null : value.get(0);
    }

    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, Const.REDIS_NOT_EXPIRE);
    }

    public <T> List<T> getList(String key, Class<T> clazz) {
        return getList(key, clazz, Const.REDIS_NOT_EXPIRE);
    }

    public String get(String key, long expire) {
        String value = valueOperations.get(key);
        if(expire != Const.REDIS_NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }

    public String get(String key) {
        return get(key, Const.REDIS_NOT_EXPIRE);
    }

    /**
     * 根据key删除缓存
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * Object转成JSON数据
     */
    private String toJson(Object object){
        if(object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String){
            return String.valueOf(object);
        }
        return JSON.toJSONString(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz){
        return JSON.parseObject(json, clazz);
    }

    /**
     * JSON数据，转成Object
     */
    private  <T> List<T> fromJsonArray(String json, Class<T> clazz){
        return JSON.parseArray(json, clazz);
    }
}
