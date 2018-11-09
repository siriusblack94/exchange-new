package com.blockeng.wallet.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.blockeng.wallet.entity.WalletCollectTask;

/**
 * <p>
 * 当钱包需要归集的时候,会吧数据插入到该表,现在一般是用在eth和eth这类型需要归集的币种 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface WalletCollectTaskMapper extends BaseMapper<WalletCollectTask> {

}
