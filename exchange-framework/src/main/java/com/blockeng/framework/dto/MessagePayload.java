package com.blockeng.framework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


/**
 * @author qiang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessagePayload {

    private String userId;

    @NonNull
    private String channel;

    @NonNull
    private String body;
}