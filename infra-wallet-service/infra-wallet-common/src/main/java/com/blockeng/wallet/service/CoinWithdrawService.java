package com.blockeng.wallet.service;

import com.baomidou.mybatisplus.service.IService;
import com.blockeng.wallet.entity.CoinWithdraw;

import java.util.List;

/**
 * <p>
 * 当用户发起提币的时候,吧数据插入到该表 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface CoinWithdrawService extends IService<CoinWithdraw> {


    boolean updateTx(CoinWithdraw item);

    List<CoinWithdraw> queryOutList(String type);

    List<CoinWithdraw> queryNoFeeList(Long id, String type);

    void updateDrawInfo(CoinWithdraw coinWithdraw);

    void withDraw(String message, String type);
}
