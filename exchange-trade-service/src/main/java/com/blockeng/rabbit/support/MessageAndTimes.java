package com.blockeng.rabbit.support;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author maple
 * @date 2018/10/23 11:55
 * 缓存消息实体类
 **/
@Data
@AllArgsConstructor
public class MessageAndTimes {

    /**
     * 重传次数
     */
    private int times;
    /**
     * 消息体
     */
    private Object message;
    /**
     * 题目
     */
    private String topic = "";
    /**
     * 路由
     */
    private String routKey;

}
