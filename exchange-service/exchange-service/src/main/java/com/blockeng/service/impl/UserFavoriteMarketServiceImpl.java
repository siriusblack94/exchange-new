package com.blockeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.entity.UserFavoriteMarket;
import com.blockeng.framework.enums.MarketType;
import com.blockeng.mapper.UserFavoriteMarketMapper;
import com.blockeng.service.UserFavoriteMarketService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户收藏交易对 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
public class UserFavoriteMarketServiceImpl extends ServiceImpl<UserFavoriteMarketMapper, UserFavoriteMarket> implements UserFavoriteMarketService {

    /**
     * 查询用户收藏交易对
     *
     * @param userId     用户ID
     * @param marketType 交易对类型
     * @return
     */
    @Override
    public List<UserFavoriteMarket> queryUserFavoriteMarket(Long userId, MarketType marketType) {
        QueryWrapper<UserFavoriteMarket> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("type", marketType.getCode());
        return baseMapper.selectList(wrapper);
    }
}
