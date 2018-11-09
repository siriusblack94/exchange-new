package com.blockeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.entity.UserFavoriteMarket;
import com.blockeng.framework.enums.MarketType;

import java.util.List;

/**
 * @Description: 用户收藏交易对
 * @Author: Chen Long
 * @Date: Created in 2018/5/20 上午11:51
 * @Modified by: Chen Long
 */
public interface UserFavoriteMarketService extends IService<UserFavoriteMarket> {

    /**
     * 查询用户收藏交易对
     *
     * @param userId     用户ID
     * @param marketType 交易对类型
     * @return
     */
    List<UserFavoriteMarket> queryUserFavoriteMarket(Long userId, MarketType marketType);
}
