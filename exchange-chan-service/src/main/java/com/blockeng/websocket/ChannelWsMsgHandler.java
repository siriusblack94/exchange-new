package com.blockeng.websocket;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.blockeng.websocket.vo.ResponseEntity;
import com.blockeng.framework.security.TokenProvider;
import com.blockeng.framework.security.UserDetails;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsSessionContext;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.util.Objects;
import java.util.Optional;

/**
 * @author qiang
 */
@Slf4j
@Component
public class ChannelWsMsgHandler implements IWsMsgHandler {

    public static ChannelWsMsgHandler INSTANCE = new ChannelWsMsgHandler();

    @Autowired
    public void setTokenProvider(TokenProvider tokenProvider) {
        ChannelWsMsgHandler.tokenProvider = tokenProvider;
    }

    private static TokenProvider tokenProvider;

    /**
     * 握手时走这个方法，业务可以在这里获取cookie，request参数等
     */
    @Override
    public HttpResponse handshake(HttpRequest request, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        String clientip = request.getClientIp();
        //log.info("收到来自{}的ws握手包\r\n{}", clientip, request.toString());
        return httpResponse;
    }

    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {

    }

    /**
     * 字节消息（binaryType = arraybuffer）过来后会走这个方法
     */
    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        return null;
    }

    /**
     * 当客户端发close flag时，会走这个方法
     */
    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        Tio.remove(channelContext, "receive close flag");
        return null;
    }

    /*
     * 字符消息（binaryType = blob）过来后会走这个方法
     */
    @Override
    public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) {
        WsSessionContext wsSessionContext = (WsSessionContext) channelContext.getAttribute();
        HttpRequest httpRequest = wsSessionContext.getHandshakeRequestPacket();//获取websocket握手包
        if (Objects.equals("ping", text)) {
            return "pong";
        }
        JSONObject payload = null;
        try {
            payload = JSONObject.parseObject(text);
        } catch (JSONException e) {

        }
        if (Optional.ofNullable(payload).isPresent()) {
            String sub = payload.getString("sub");
            String req = payload.getString("req");
            String cancel = payload.getString("cancel");
            String id = payload.getString("id");

            //如果用户已登录，同时绑定用户
            String authorization = payload.getString("authorization");
            if (!Strings.isNullOrEmpty(authorization) && authorization.startsWith("Bearer")) {
                String accessToken = authorization.substring("Bearer".length()).trim();
                UserDetails userDetails = (UserDetails) tokenProvider.getAuthentication(accessToken).getPrincipal();
                if (Optional.ofNullable(userDetails).isPresent()) {
                    Tio.bindUser(channelContext, userDetails.getId().toString());
                }
            }
            if (!Strings.isNullOrEmpty(sub)) {
                //绑定到群组，后面会有群发
                Tio.bindGroup(channelContext, sub);
                //返回值是要发送给客户端的内容，一般都是返回null
                return new ResponseEntity()
                        .setId(id)
                        .setSubbed(sub)
                        .setStatus("ok")
                        .build();
            } else if (!Strings.isNullOrEmpty(req)) {

            } else if (!Strings.isNullOrEmpty(cancel)) {
                //取消订阅通道
                Tio.unbindGroup(cancel, channelContext);
                return new ResponseEntity()
                        .setId(id)
                        .setCanceled(cancel)
                        .setStatus("ok")
                        .build();
            }
        }
        return null;
    }
}