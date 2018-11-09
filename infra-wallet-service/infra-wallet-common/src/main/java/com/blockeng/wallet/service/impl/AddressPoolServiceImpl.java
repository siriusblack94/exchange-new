package com.blockeng.wallet.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.clg.wallet.bean.AddressBean;
import com.blockeng.wallet.entity.AddressPool;
import com.blockeng.wallet.mapper.AddressPoolMapper;
import com.blockeng.wallet.service.AddressPoolService;
import com.blockeng.wallet.utils.DESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Random;

/**
 * <p>
 * 用户的地址池 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Transactional
public class AddressPoolServiceImpl extends ServiceImpl<AddressPoolMapper, AddressPool> implements AddressPoolService {


    private static final Logger LOG = LoggerFactory.getLogger(UserAddressServiceImpl.class);

    @Autowired
    private DESUtil desUtil;


    public int selectAddressCount(long coinId, String type) {
        EntityWrapper<AddressPool> ew = new EntityWrapper<>();
        if (coinId > 0) {
            ew.eq("coin_id", coinId);
        }
        if (!StringUtils.isEmpty(type)) {
            ew.eq("coin_type", type);
        }
        return super.selectCount(ew);
    }

    @Override
    public AddressPool selectAddress(Long id) {
        EntityWrapper<AddressPool> ew = new EntityWrapper<>();
        ew.eq("coin_id", id);
        return super.selectOne(ew);
    }


    public boolean insertEthAddress(AddressBean addressBean, String type, long id) {
        AddressPool addressPool = new AddressPool();
        addressPool.setCoinType(type)
                .setCoinId(id)
                .setAddress(addressBean.getAddress());

        if (!StringUtils.isEmpty(addressBean.getPwd())) {
            addressPool.setPwd(desUtil.encrypt(addressBean.getPwd()));
        }
        if (!StringUtils.isEmpty(addressBean.getKeystore())) {
            addressPool.setKeystore(desUtil.encrypt(addressBean.getKeystore()));
        }
        return super.insert(addressPool);
    }

}
