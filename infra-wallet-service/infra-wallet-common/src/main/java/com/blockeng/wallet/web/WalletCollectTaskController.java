package com.blockeng.wallet.web;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 当钱包需要归集的时候,会吧数据插入到该表,现在一般是用在eth和eth这类型需要归集的币种 前端控制器
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@RestController
@RequestMapping("/walletCollectTask")
public class WalletCollectTaskController {


}

