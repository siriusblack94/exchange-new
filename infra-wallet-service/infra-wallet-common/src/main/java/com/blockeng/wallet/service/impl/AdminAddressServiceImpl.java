package com.blockeng.wallet.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.wallet.entity.AdminAddress;
import com.blockeng.wallet.help.ClientInfo;
import com.blockeng.wallet.mapper.AdminAddressMapper;
import com.blockeng.wallet.service.AdminAddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 平台归账手续费等账户 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Transactional
public class AdminAddressServiceImpl extends ServiceImpl<AdminAddressMapper, AdminAddress> implements AdminAddressService {

    private ClientInfo clientInfo;

    @Override
    public AdminAddress queryAdminAccount(String type, int status) {
        return baseMapper.selectOne(new AdminAddress().setCoinType(type).setStatus(status));
    }

    @Override
    public AdminAddress queryAdminAccount(Long coinId, int status) {
        return baseMapper.selectOne(new AdminAddress().setCoinId(coinId).setStatus(status));
    }


}
