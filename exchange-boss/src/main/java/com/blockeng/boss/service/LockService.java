package com.blockeng.boss.service;

/**
 * @Description: redis 内存锁
 * @Author: Chen Long
 * @Date: Created in 2018/5/26 下午3:02
 * @Modified by: Chen Long
 */
public interface LockService {

    /**
     * 获取内存锁
     *
     * @param redisKey redis key
     * @param value    值
     * @param waitLock 是否等待
     * @return
     */
    boolean getLock(String redisKey, String value, boolean waitLock);

    /**
     * 释放锁
     *
     * @param redisKey redis key
     * @param value    值
     * @return
     */
    boolean unlock(String redisKey, String value);
}
