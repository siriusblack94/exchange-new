package com.blockeng.service;

import com.blockeng.vo.SendForm;

/**
 * <p>
 * 短信信息 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface SmsService {

    /**
     * 发送短信
     *
     * @param form
     */
    void sendTo(SendForm form);
}