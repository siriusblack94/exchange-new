package com.blockeng.service.impl;

import com.blockeng.dto.WithdrawAddressDTO;
import com.blockeng.entity.Coin;
import com.blockeng.entity.UserWallet;
import com.blockeng.framework.enums.BaseStatus;
import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.mapper.UserWalletMapper;
import com.blockeng.service.CoinService;
import com.blockeng.service.UserWalletService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.user.dto.UserDTO;
import com.blockeng.user.feign.UserServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 用户钱包表 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Slf4j
public class UserWalletServiceImpl extends ServiceImpl<UserWalletMapper, UserWallet> implements UserWalletService {

    @Autowired
    private CoinService coinService;

    @Autowired
    private UserServiceClient userServiceClient;

    /**
     * 添加提币地址
     *
     * @param withdrawAddress
     * @param userId          用户ID
     * @return
     */
    @Override
    public void addAddress(WithdrawAddressDTO withdrawAddress, Long userId) {
        UserDTO user = userServiceClient.selectById(userId);
        if (!StringUtils.isNotEmpty(user.getPaypassword()) || !new BCryptPasswordEncoder().matches(withdrawAddress.getPayPassword(), user.getPaypassword())) {
            log.error("资金交易密码错误");
            throw new GlobalDefaultException(50020);
        }
        UserWallet userWallet = new UserWallet();
        Coin coin = coinService.queryById(withdrawAddress.getCoinId());
        userWallet.setAddr(withdrawAddress.getAddress())
                .setCoinId(withdrawAddress.getCoinId())
                .setCoinName(coin.getName())
                .setUserId(userId)
                .setName(withdrawAddress.getName())
                .setSort(0)
                .setStatus(BaseStatus.EFFECTIVE.getCode())
                .setLastUpdateTime(new Date())
                .setCreated(new Date());
        baseMapper.insert(userWallet);
    }
}
