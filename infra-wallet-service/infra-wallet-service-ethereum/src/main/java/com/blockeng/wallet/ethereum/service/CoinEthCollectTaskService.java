package com.blockeng.wallet.ethereum.service;


import com.baomidou.mybatisplus.service.IService;
import com.blockeng.wallet.entity.WalletCollectTask;

/**
 * <p>
 * 当钱包需要归集的时候,会吧数据插入到该表,现在一般是用在eth和eth这类型需要归集的币种 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface CoinEthCollectTaskService extends IService<WalletCollectTask> {

    void collectionTask();
}
