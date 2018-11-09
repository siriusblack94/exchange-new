package com.blockeng.service.impl;

import com.blockeng.service.LockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Description: redis 内存锁
 * @Author: Chen Long
 * @Date: Created in 2018/5/26 下午3:02
 * @Modified by: Chen Long
 */
@Service
public class LockServiceImpl implements LockService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获取内存锁
     *
     * @param redisKey redis key
     * @param value    值
     * @param waitLock 是否等待锁
     * @return
     */
    @Override
    public boolean getLock(String redisKey, String value, boolean waitLock) {
        long mills = System.currentTimeMillis() + 2000L;
        String key = new StringBuffer(redisKey).append(value).toString();
        try {
            while (!redisTemplate.opsForValue().setIfAbsent(key, value)) {
                if (!waitLock || System.currentTimeMillis() >= mills) {
                    return false;
                }
                Thread.sleep(0L, 100);
            }
            redisTemplate.expire(key, 30000, TimeUnit.MILLISECONDS);
            return true;
        } catch (Exception var13) {
            return false;
        }
    }

    /**
     * 释放内存锁
     *
     * @param redisKey redis key
     * @param value    值
     * @return
     */
    @Override
    public boolean unlock(String redisKey, String value) {
        String key = new StringBuffer(redisKey).append(value).toString();
        return redisTemplate.delete(key);
    }
}
