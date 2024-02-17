package com.sjf.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description: Redis 工具类
 * @Author: SJF
 * @Date: 2024/1/14 16:14
 */
@Component
@SuppressWarnings("all")
public final class RedisUtil {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 为指定的 key 重新设置缓存过期时间
     * @param key: 需要设置的 key
     * @param time: 重新设置的过期时间，单位为 s
     * @return: 此次操作的结果
     */
    public Boolean setExpireTime(String key, long time){
        if(time <= 0){
            return false;
        }else {
            try {
                redisTemplate.expire(key,time, TimeUnit.MINUTES);
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * 根据 key 获取缓存过期时间
     * @param key: 获取过期时间的 key
     * @return: 该 key 的缓存过期时间
     */
    public Long getExpireTime(String key){
        return redisTemplate.getExpire(key);
    }

    /**
     * 判断 key 是否存在
     * @param key: 被判断的 key 值
     * @return: 判断的结果
     */
    public Boolean hasKey(String key){
        try {
            redisTemplate.hasKey(key);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * String 类型缓存新增
     * @param key: 新增的键
     * @param value: 新增的值
     * @param time: 设置的过期时间，传入负值代表永不过期
     * @return: 操作的结果
     */

    public Boolean set(String key, Object value, Long time) {
        try {
            if(time > 0) {
                redisTemplate.opsForValue().set(key, value, time,TimeUnit.MINUTES);
            }else {
                redisTemplate.opsForValue().set(key,value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * String 类型缓存获取
     * @param key: 需要获取的 key
     * @return: 获取到的值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 递增操作
     * @param key:   键
     * @param delta: 要增加几(大于0)
     */
    public Long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public void del(String key){
        if (hasKey(key)){
            redisTemplate.delete(key);
        }
    }


    /**
     * 递减操作
     * @param key: 键
     * @param delta: 要减少几(小于0)
     */
    public Long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }


    // ================================ Map =================================

    /**
     * HashGet
     * @param key:  键 不能为null
     * @param item: 项 不能为null
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取 hashKey 对应的所有键值
     * @param key: 键
     * @return: 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     * @param key: 键
     * @param map: 对应多个键值
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * HashSet 并设置时间
     * @param key:  键
     * @param map:  对应多个键值
     * @param time: 时间(秒)
     * @return: true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                setExpireTime(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key:   键
     * @param item:  项
     * @param value: 值
     * @return: true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key:   键
     * @param item:  项
     * @param value: 值
     * @param time:  时间(秒) 注意:如果已存在的 hash 表有时间,这里将会替换原有的时间
     * @return true: 成功 false 失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                setExpireTime(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 删除 hash 表中的值
     * @param key:  键 不能为 null
     * @param item: 项 可以使多个 不能为 null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }


    /**
     * 判断 hash 表中是否有该项的值
     * @param key:  键 不能为 null
     * @param item: 项 不能为 null
     * @return true: 存在 false 不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }


    /**
     * hash 递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key:  键
     * @param item: 项
     * @param by:   要增加几(大于0)
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }


    /**
     * hash 递减
     * @param key:  键
     * @param item: 项
     * @param by:   要减少记(小于0)
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    // ================================ List =================================
    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0)
                setExpireTime(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
