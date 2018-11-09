package com.blockeng.websocket;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.tio.core.cluster.DefaultMessageListener;
import org.tio.core.cluster.TioClusterConfig;
import org.tio.server.ServerGroupContext;
import org.tio.websocket.server.WsServerStarter;

/**
 * @author tanyaowu
 * 2017年6月28日 下午5:34:04
 */
@Component
public class ChannelWebsocketStarter implements CommandLineRunner {

    private WsServerStarter wsServerStarter;

    private static ServerGroupContext serverGroupContext;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * @return the serverGroupContext
     */
    public static ServerGroupContext getServerGroupContext() {
        return serverGroupContext;
    }

    public WsServerStarter getWsServerStarter() {
        return wsServerStarter;
    }

    @Override
    public void run(String... args) throws Exception {
        wsServerStarter = new WsServerStarter(ChannelServerConfig.SERVER_PORT, ChannelWsMsgHandler.INSTANCE);

        serverGroupContext = wsServerStarter.getServerGroupContext();
        serverGroupContext.setName(ChannelServerConfig.PROTOCOL_NAME);
        serverGroupContext.setServerAioListener(ChannelServerAioListener.INSTANCE);
        //实例化t-io集群配置
        TioClusterConfig tioClusterConfig = TioClusterConfig.newInstance("WS_", redissonClient);
        //开启群组集群-默认不集群
        tioClusterConfig.setCluster4group(true);
        //设置消息的监听
        tioClusterConfig.addMessageListener(new DefaultMessageListener(serverGroupContext));
        //配置t-io集群
        serverGroupContext.setTioClusterConfig(tioClusterConfig);
        //设置ip统计时间段
        //serverGroupContext.ipStats.addDurations(ChannelServerConfig.IpStatDuration.IPSTAT_DURATIONS);
        //设置ip监控
        //serverGroupContext.setIpStatListener(ShowcaseIpStatListener.me);
        //设置心跳超时时间
        serverGroupContext.setHeartbeatTimeout(ChannelServerConfig.HEARTBEAT_TIMEOUT);
        wsServerStarter.start();
    }
}