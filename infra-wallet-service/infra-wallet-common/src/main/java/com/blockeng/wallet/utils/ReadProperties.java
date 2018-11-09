package com.blockeng.wallet.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReadProperties {

    @Value("${task.isOpen.allJob}")
    public int allJob;//etc充值开关


    @Value("${task.isOpen.recharge}")
    public int recharge;//充值任务开关

    @Value("${task.isOpen.collect}")
    public int collect;//归集任务开关

    @Value("${task.isOpen.draw}")
    public int draw;//提币任务开关

    @Value("${task.isOpen.other}")
    public int other;//其他任务开关

    @Value("${task.isOpen.address}")
    public int address;//地址开关

    @Value("${ip.local.limit}")
    public int localLimit;//ip限制本地


    @Value("${task.ip.url}")
    public String ipUrl;//获取公网ip的url

    @Value("${plant.aes.key}")
    public String aesKey;//加密key

    @Value("${recharge.count}")
    public int maxCount;//加密key

}
