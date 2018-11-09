package com.blockeng.admin.service;

import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.entity.Coin;
import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.entity.CoinConfig;

import java.util.List;

/**
 * <p>
 * 币种配置信息 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
public interface CoinService extends IService<Coin> {
    /**
     * 新增币种的时候，异步初始化，account表的数
     *
     * @param coinId 币种id
     */
    void ansyUpdateAccounts(Long coinId);

    /**
     * 增加币种
     *
     * @param coin
     */
    ResultMap insertCoin(Coin coin);

    /**
     * 修改币种
     *
     * @param coin
     * @return
     */
    ResultMap updateCoin(Coin coin);

    boolean updateConfig(Coin newCoin, CoinConfig coinConfig);

    /**
     * 查询所有有效币种
     * */
    List<Coin> getAllValidCoin();
}
