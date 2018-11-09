package com.blockeng.wallet.ethereum.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.wallet.entity.AddressPool;
import com.blockeng.wallet.ethereum.service.CoinEthAddressPoolService;
import com.blockeng.wallet.help.ClientInfo;
import com.blockeng.wallet.mapper.AddressPoolMapper;
import com.blockeng.wallet.service.AddressPoolService;
import com.blockeng.wallet.service.impl.UserAddressServiceImpl;
import com.clg.wallet.bean.AddressBean;
import com.clg.wallet.bean.ClientBean;
import com.clg.wallet.enums.CoinType;
import com.clg.wallet.newclient.Client;
import com.clg.wallet.newclient.ClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class CoinEthAddressPoolServiceImpl extends ServiceImpl<AddressPoolMapper, AddressPool> implements CoinEthAddressPoolService {

    private static final Logger LOG = LoggerFactory.getLogger(UserAddressServiceImpl.class);


    @Autowired
    private ClientInfo clientInfo;

    @Autowired
    private AddressPoolService addressPoolService;


    @Value("${address.count}")
    public int maxAddressCount;

    public void createAddress() {
        ClientBean clientBean = clientInfo.getClientInfoFromType(CoinType.ETH);
        if (null != clientBean) {
            createAddress(clientBean);
        } else {
            LOG.error("coin数据库为空");
        }
    }

    private void createAddress(ClientBean clientBean) {
        int count = addressPoolService.selectAddressCount(0, clientBean.getCoinType());
        Client client = ClientFactory.getClient(clientBean);

        if (count <= maxAddressCount) {
            int addressCount = maxAddressCount;
            LOG.info("创建地址数量:" + addressCount);
            while (addressCount > 0) {
                AddressBean addressBean = client.getNewAddress().toObj(AddressBean.class);
                if (null != addressBean) {
                    if (addressPoolService.insertEthAddress(addressBean, clientBean.getCoinType(), clientBean.getId())) {
                        LOG.info("创建第:" + addressCount + "个,成功" + addressBean.toString());
                    } else {
                        LOG.info("创建第失败");
                    }
                } else {
                    LOG.info("创建第失败");
                }
                addressCount--;
            }
        }
    }

}
