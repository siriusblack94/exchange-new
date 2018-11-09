package com.blockeng.service;

import com.blockeng.dto.RandCode;
import com.blockeng.dto.RandCodeVerifyDTO;

/**
 * @author qiang
 */
public interface RandCodeService {

    /**
     * 发送短信验证码
     *
     * @param randCode
     */
    void sendTo(RandCode randCode);

    /**
     * 验证短信验证码
     *
     * @return
     */
    boolean verify(RandCodeVerifyDTO randCodeVerifyDTO);
}