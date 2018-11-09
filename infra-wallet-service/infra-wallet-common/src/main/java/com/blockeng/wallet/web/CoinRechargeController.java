package com.blockeng.wallet.web;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户充值,当前用户充值成功之后添加数据到这个表,充值一般无手续费.当status为1的时候表示充值成功 前端控制器
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@RestController
@RequestMapping("/coinRecharge")
public class CoinRechargeController {

}

