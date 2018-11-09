package com.blockeng.service;

import com.blockeng.entity.CashRecharge;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.framework.http.Response;
import com.blockeng.vo.CashRechargeForm;

/**
 * <p>
 * 充值表 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface CashRechargeService extends IService<CashRecharge> {

    /**
     * 法币充值
     *
     * @param cashRecharge 法币充值请求参数
     * @param userId       当前登录用户
     * @return
     */
    Response c2cBuy(CashRechargeForm cashRecharge, long userId);
}
