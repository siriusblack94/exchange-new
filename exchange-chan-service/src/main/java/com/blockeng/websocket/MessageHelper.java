package com.blockeng.websocket;

import com.blockeng.websocket.vo.ResponseEntity;
import org.tio.core.Tio;
import org.tio.server.ServerGroupContext;

import java.util.Optional;

/**
 * @author qiang
 */
public class MessageHelper {

    public static void broadcast(String destination, Object message) {
        ServerGroupContext serverGroupContext = ChannelWebsocketStarter.getServerGroupContext();
        if (Optional.ofNullable(serverGroupContext).isPresent()) {

            Tio.sendToGroup(serverGroupContext, destination, new ResponseEntity()
                    .setId(serverGroupContext.getId())
                    .setSubbed(destination)
                    .put("data", message)
                    .build());
        }
    }
}
