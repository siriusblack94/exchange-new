package com.blockeng.wallet.service;

import com.baomidou.mybatisplus.service.IService;
import com.blockeng.wallet.entity.UserAddress;

public interface FeeService {

    /**
     * 获取每个币种的手续费,归账和打款的手续费
     *
     * @param id
     * @param type
     */
    void queryChainFee(Long id, String type);

}
