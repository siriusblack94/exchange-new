package com.blockeng.wallet.service;

import com.baomidou.mybatisplus.service.IService;
import com.blockeng.wallet.dto.CoinAddressDTO;
import com.blockeng.wallet.dto.WalletResultDTO;
import com.clg.wallet.bean.AddressBean;
import com.blockeng.wallet.entity.AddressPool;

/**
 * <p>
 * 用户的地址池 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface AddressPoolService extends IService<AddressPool> {

    boolean insertEthAddress(AddressBean addressBean, String type, long id);

    int selectAddressCount(long coinId, String type);

    AddressPool selectAddress(Long id);


}
