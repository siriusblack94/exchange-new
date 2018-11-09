package com.blockeng.config;

import com.blockeng.dto.DefaultMessageDelegate;
import com.blockeng.dto.MessageDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author qiang
 * @TODO 线上日志， 消息订阅推送
 */
@Configuration
public class RedisConfig {

    /*@Autowired
    private FundAccountChangeListener fundAccountChangeListener;*/

    @Autowired
    private DefaultMessageDelegate messageDelegate;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Object.class));
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer(Object.class));
        return redisTemplate;
    }

    /**
     * 创建连接工厂
     *
     * @param redisConnectionFactory
     * @param messageListenerAdapter
     * @return
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory, MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
/*         redisMessageListenerContainer.addMessageListener(fundAccountChangeListener, new ChannelTopic(Constant.ACCOUNT_CHANGE_CHANNEL));
         redisMessageListenerContainer.addMessageListener(messageListenerAdapter, new ChannelTopic(Constant.USER_POSITIONS_CHANNEL));*/
        return redisMessageListenerContainer;
    }

    /**
     * 绑定消息监听者和接收监听的方法
     *
     * @param messageDelegate
     * @return
     */
    @Bean
    public MessageListenerAdapter messageListenerAdapter(MessageDelegate messageDelegate) {
        return new MessageListenerAdapter(messageDelegate);
    }
}