package com.blockeng.wallet.service.impl;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.wallet.dto.CoinAddressDTO;
import com.blockeng.wallet.dto.WalletResultCode;
import com.blockeng.wallet.dto.WalletResultDTO;
import com.blockeng.wallet.entity.AddressPool;
import com.blockeng.wallet.entity.AdminAddress;
import com.blockeng.wallet.entity.CoinConfig;
import com.blockeng.wallet.entity.UserAddress;
import com.blockeng.wallet.help.ClientInfo;
import com.blockeng.wallet.mapper.UserAddressMapper;
import com.blockeng.wallet.service.AddressPoolService;
import com.blockeng.wallet.service.AdminAddressService;
import com.blockeng.wallet.service.CoinConfigService;
import com.blockeng.wallet.service.UserAddressService;
import com.blockeng.wallet.utils.AddressCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;


/**
 * <p>
 * 用户钱包地址信息 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Transactional
@Slf4j
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

    @Autowired
    private CoinConfigService coinConfigService;

    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private AdminAddressService adminAddressService;

    @Autowired
    private ClientInfo clientInfo;

    @Autowired
    private AddressPoolService addressPoolService;

    @Override
    public UserAddress getByCoinIdAndAddr(String to, Long coinId) {
        UserAddress userAddress = new UserAddress().setAddress(to);
        if (null != coinId && coinId > 0) {
            userAddress.setCoinId(coinId);
        }
        return baseMapper.selectOne(userAddress);
    }

    @Override
    public UserAddress getByCoinIdAndUserId(Long userId, Long coinId) {
        UserAddress userAddress = new UserAddress();
        if (null != coinId && coinId > 0) {
            userAddress.setCoinId(coinId);
        }
        if (null != userId && userId > 0) {
            userAddress.setUserId(userId);
        }
        return baseMapper.selectOne(userAddress);
    }

    @Override
    public int selectCount(String to, long coidId) {
        EntityWrapper<UserAddress> ew = new EntityWrapper<>();
        ew.eq("address", to);
        if (coidId > 0) {
            ew.eq("coin_id", coidId);
        }
        return baseMapper.selectCount(ew);
    }


    @Override
    public synchronized String getAddress(CoinAddressDTO getAddressDTO) {
        Long userId = getAddressDTO.getUserId();
        log.info("进来了1   userId:[" + userId + "]--coinid:" + getAddressDTO.getCoinId());
        CoinConfig coin = coinConfigService.selectById(getAddressDTO.getCoinId());
        log.info("进来了2  coin:[" + coin + "]");
        if (null == coin) {
            return WalletResultDTO.errorResult(WalletResultCode.NOT_EXIST_COIN_ID.getCode(), WalletResultCode.NOT_EXIST_COIN_ID.getMessage()).toJson();
        }
        EntityWrapper<UserAddress> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("coin_id", coin.getId());
        log.info("进来了3   userId:[" + userId + "]" + "-----coinid:[" + coin.getId() + "]");
        UserAddress userAddress = userAddressService.selectOne(wrapper);
        log.info("进来了4   userAddress:[" + userAddress + "]");
        if (userAddress != null) {
            // 已经存在地址
            return WalletResultDTO.successResult(userAddress.getAddress()).toJson();
        }
        AdminAddress adminAddress;
        EntityWrapper<AdminAddress> addressWrapper = new EntityWrapper<>();
        addressWrapper.eq("coin_id", coin.getId()).eq("status", 4);
        List<AdminAddress> adminAddressList = adminAddressService.selectList(addressWrapper);
        if (!CollectionUtils.isEmpty(adminAddressList)) {
            if (adminAddressList.size() == 1) {
                adminAddress = adminAddressList.get(0);
            } else {
                int index = new Random().nextInt(adminAddressList.size());
                adminAddress = adminAddressList.get(index);
            }
            String address = adminAddress.getAddress() + "|" + AddressCodeUtil.idToCode(getAddressDTO.getUserId());
            UserAddress newAddress = new UserAddress(userId, coin.getId(), address, null, null);
            baseMapper.insert(newAddress);
            return WalletResultDTO.successResult(address).toJson();
        }
        log.info("进来了5  getCoinType:[" + coin.getCoinType() + "]");
        if (coin.getCoinType().contains("Token")) {
            log.info("进来了6  getCoinType:[" + coin.getCoinType().replace("Token", "") + "]");
            // 当前为token币种
            CoinConfig mainCoin = clientInfo.getCoinConfigFormType(coin.getCoinType().replace("Token", "")).get(0);
            log.info("进来了7  mainCoin:[" + mainCoin + "]");
            log.info("进来了8  user_id:[" + userId + "]-----coin_id:[" + mainCoin.getId() + "]");
            EntityWrapper<UserAddress> wrapperMain = new EntityWrapper<>();
            wrapperMain.eq("user_id", userId)
                    .eq("coin_id", mainCoin.getId());
            userAddress = super.selectOne(wrapperMain);
            log.info(" erc20  userAddress:[" + userAddress + "]");
            if (userAddress != null) {
                // 如果已经获取过主链钱包地址，则token直接使用主链钱包地址
                UserAddress newAddress = new UserAddress(userId, coin.getId(),
                        userAddress.getAddress(), userAddress.getKeystore(), userAddress.getPwd());
                baseMapper.insert(newAddress);
                return WalletResultDTO.successResult(newAddress.getAddress()).toJson();
            }
            // 未获取主链钱包地址，则通过主链币种ID获取钱包地址
            AddressPool addressPool = addressPoolService.selectAddress(mainCoin.getId());
            if (addressPool != null) {
                // 添加用户当前token币种的主链币种钱包地址
                userAddress = new UserAddress(userId, mainCoin.getId(), addressPool.getAddress(), addressPool.getKeystore(), addressPool.getPwd());
                baseMapper.insert(userAddress);
                // 添加用户当前token币种钱包地址，与主链地址保持一致
                userAddress = new UserAddress(userId, coin.getId(), addressPool.getAddress(), addressPool.getKeystore(), addressPool.getPwd());
                baseMapper.insert(userAddress);
                // 从地址池中删除当前获取到的钱包地址
                addressPoolService.deleteById(addressPool.getId());
                return WalletResultDTO.successResult(addressPool.getAddress()).toJson();
            }
            return WalletResultDTO.errorResult(WalletResultCode.NOT_FIND_WALLET_ADDRESS.getCode(), WalletResultCode.NOT_FIND_WALLET_ADDRESS.getMessage()).toJson();
        }


        // 主链币种
        AddressPool addressPool = addressPoolService.selectAddress(coin.getId());
        if (addressPool != null) {
            // 添加用户当前token币种钱包地址，与主链地址保持一致
            userAddress = new UserAddress(userId, coin.getId(), addressPool.getAddress(), addressPool.getKeystore(), addressPool.getPwd());
            baseMapper.insert(userAddress);
            addressPoolService.deleteById(addressPool.getId());
            return WalletResultDTO.successResult(addressPool.getAddress()).toJson();
        }
        return WalletResultDTO.errorResult(WalletResultCode.NOT_FIND_WALLET_ADDRESS.getCode(), WalletResultCode.NOT_FIND_WALLET_ADDRESS.getMessage()).toJson();
    }


    /**
     * @param coinId
     * @return
     */
    private AddressPool getAddressPool(Long coinId) {
        AddressPool addressPool = addressPoolService.selectAddress(coinId);
        addressPoolService.deleteById(addressPool.getId());
        return addressPool;
    }
}
