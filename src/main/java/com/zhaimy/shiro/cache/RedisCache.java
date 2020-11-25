package com.zhaimy.shiro.cache;

import com.zhaimy.utils.ApplicationContextUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Collection;
import java.util.Set;

/**
 * 自定义redis缓存的实现
 */
public class RedisCache<k,v> implements Cache<k,v> {
    private String cacheName;

    public RedisCache(String cacheName) {
        this.cacheName = cacheName;
    }

    public RedisCache() {
    }

    @Override
    public v get(k k) throws CacheException {

        return (v)getredisTemplate().opsForHash().get(this.cacheName,k.toString());

    }

    @Override
    public v put(k k, v v) throws CacheException {

        //redisTemplate.opsForValue().set(k.toString(),v);
        getredisTemplate().opsForHash().put(this.cacheName,k.toString(),v);

        return null;
    }

    @Override
    public v remove(k k) throws CacheException {

        Long delete = getredisTemplate().opsForHash().delete(this.cacheName, k.toString());

        return (v) delete;
    }

    @Override
    public void clear() throws CacheException {
        getredisTemplate().delete(this.cacheName);

    }

    @Override
    public int size() {
        return getredisTemplate().opsForHash().size(this.cacheName).intValue();
    }

    @Override
    public Set<k> keys() {
        return getredisTemplate().opsForHash().keys(this.cacheName);
    }

    @Override
    public Collection<v> values() {

        return getredisTemplate().opsForHash().values(this.cacheName);
    }


    private  RedisTemplate getredisTemplate() {

        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");

        //序列化map中两个K为字符串 map<k,map<k,v>>
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        return redisTemplate;

    }
}
