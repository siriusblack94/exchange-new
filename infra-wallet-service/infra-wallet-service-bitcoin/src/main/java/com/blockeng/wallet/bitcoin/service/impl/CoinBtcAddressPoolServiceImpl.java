package com.blockeng.wallet.bitcoin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.wallet.bitcoin.service.CoinBtcAddressPoolService;
import com.blockeng.wallet.entity.AddressPool;
import com.blockeng.wallet.entity.CoinConfig;
import com.blockeng.wallet.help.ClientInfo;
import com.blockeng.wallet.mapper.AddressPoolMapper;
import com.blockeng.wallet.service.AddressPoolService;
import com.blockeng.wallet.service.CoinConfigService;
import com.clg.wallet.bean.AddressBean;
import com.clg.wallet.enums.CoinType;
import com.clg.wallet.newclient.Client;
import com.clg.wallet.newclient.ClientFactory;
import com.clg.wallet.wallet.Omni.OmniNewClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

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
public class CoinBtcAddressPoolServiceImpl extends ServiceImpl<AddressPoolMapper, AddressPool> implements CoinBtcAddressPoolService {

    @Autowired
    private CoinConfigService coinConfigService;


    private static final Logger LOG = LoggerFactory.getLogger(CoinBtcAddressPoolServiceImpl.class);

    @Autowired
    private ClientInfo clientInfo;

    @Autowired
    private AddressPoolService addressPoolService;

    @Value("${address.count}")
    public int maxAddressCount;

    public void createAddress() {
        List<CoinConfig> list = coinConfigService.selectCoinFromType(CoinType.BTC);
        if (!CollectionUtils.isEmpty(list)) {
            for (CoinConfig item : list) { //循环遍历每个币种
                LOG.info("name:"+item.getName()+"--type:"+item.getCoinType()+"--autoaddress:"+item.getAutoAddress());
                if(item.getAutoAddress()==1){
                    createAddress(item.getId(), item);
                }
            }
        } else {
            LOG.error("coin数据库为空");
        }
    }

    private void createAddress(long coinId, CoinConfig item) {
        LOG.info("创建地址:" + item.getName());
        String type = item.getCoinType();
        int count = addressPoolService.selectAddressCount(coinId, type);
        Client client = ClientFactory.getClient(clientInfo.getClientInfoFromId(coinId));

        if (count <= maxAddressCount) {
            int addressCount = maxAddressCount;
            LOG.info("创建地址数量:" + addressCount);
            while (addressCount > 0) {
                AddressBean addressBean = null;
                String address = client.getNewAddress().getResult().toString();
                if (!StringUtils.isEmpty(address)) {
                    addressBean = new AddressBean().setAddress(address);
                }
                if (null != addressBean) {
                    if (addressPoolService.insertEthAddress(addressBean, item.getCoinType(), item.getId())) {
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
