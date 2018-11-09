package com.blockeng.rabbit.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author maple
 * @date 2018/10/23 11:35
 * 配置
 **/
@Configuration
@Slf4j
public class RabbitConfirmConfig {
    /**
     * 重传次数，配置文件可配置
     */
    @Value("${spring.rabbit.support.confirm.retry:1}")
    private int retry = 1;

    /**
     * spring中实例化辅助类
     *
     * @param template
     * @return 辅助类
     */
    @Bean
    public RabbitTemplateSupport RabbitTemplateSupport(RabbitTemplate template) {
        //工厂
        ConnectionFactory factory = template.getConnectionFactory();
        //辅助类
        RabbitTemplateSupport support = new RabbitTemplateSupport();
        //装配template
        support.setTemplate(template);
        //校验工厂类型
        if (factory instanceof CachingConnectionFactory) {
            CachingConnectionFactory cachingConnectionFactory = (CachingConnectionFactory) factory;
            cachingConnectionFactory.setPublisherConfirms(true);//设置启用confirm
            /*
                1. mandatory标志位
                当mandatory标志位设置为true时，如果exchange根据自身类型和消息routeKey无法找到一个符合条件的queue，
                那么会调用basic.return方法将消息返还给生产者；当mandatory设为false时，出现上述情形broker会直接将消息扔掉。

                2. immediate标志位
                当immediate标志位设置为true时，如果exchange在将消息route到queue(s)时发现对应的queue上没有消费者，
                那么这条消息不会放入队列中。当与消息routeKey关联的所有queue(一个或多个)都没有消费者时，该消息会通过basic.return方法返还给生产者。

             */
            template.setMandatory(true);

            //设置回调，在回调中尝试重发
            template.setConfirmCallback((correlationData, b, s) -> {
                try {
                    if (correlationData == null) //防空
                        return;
                    MessageAndTimes messageAndTimes = support.getMessageMap().get(correlationData.getId()); //根据id获取缓存对象
                    if (!b) {//b表示发送成功与失败
                        if (messageAndTimes.getTimes() < retry) { //如果当前次数小于重传次数，重发
                            log.warn("Rabbit message {} send failed, may cause {}", messageAndTimes, s);
                            messageAndTimes.setTimes(messageAndTimes.getTimes() + 1);
                            template.convertAndSend(messageAndTimes.getTopic(), messageAndTimes.getRoutKey(), messageAndTimes.getMessage(), correlationData);
                        } else {//如果当前次数大于等于重传次数，放弃重发
                            log.warn("Rabbit message {} send failed, may cause {},stop retry", messageAndTimes, s);
                            support.getMessageMap().remove(correlationData.getId());
                        }
                    } else {
                        //不重发直接清除缓存
                        log.debug("Rabbit message {} sended", messageAndTimes);
                        support.getMessageMap().remove(correlationData.getId());
                    }
                } catch (Exception e) {
                    log.warn("Rabbit retry exception ", e);
                }
            });
        }
        return support;
    }

}
