package com.blockeng.wallet.service;

import com.baomidou.mybatisplus.service.IService;
import com.blockeng.wallet.dto.WalletResultDTO;
import com.blockeng.wallet.entity.CoinConfig;
import com.blockeng.wallet.exception.CoinException;

import java.util.List;

/**
 * <p>
 * 币种配置信息 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface CoinConfigService extends IService<CoinConfig> {

    String lastBlock(Long id);

    List<CoinConfig> selectCoinFromType(String type);

    //    @Cached(expire = 1, cacheType = CacheType.LOCAL)
    List<CoinConfig> selectAllCoin();

    CoinConfig selectCoinFromId(Long coinId);

    boolean updateCoinLastblock(String currentBlockNumber, Long id);

    WalletResultDTO updateCoinPass(String newPass, String oldPass, CoinConfig coinConfig) throws CoinException;
}

