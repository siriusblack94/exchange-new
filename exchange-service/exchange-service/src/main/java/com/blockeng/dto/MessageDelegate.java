package com.blockeng.dto;

import java.io.Serializable;
import java.util.Map;

/**
 * @author qiang
 */
public interface MessageDelegate {

    void handleMessage(String message);

    void handleMessage(Map message);

    void handleMessage(byte[] message);

    void handleMessage(Serializable message);

    void handleMessage(Serializable message, String channel);
}