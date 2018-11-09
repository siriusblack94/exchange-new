package com.blockeng.wallet.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.wallet.entity.ClientBeanMapper;
import com.blockeng.wallet.entity.CoinConfig;
import com.blockeng.wallet.entity.CoinServer;
import com.blockeng.wallet.help.ClientInfo;
import com.blockeng.wallet.mapper.CoinServerMapper;
import com.blockeng.wallet.service.CoinServerService;
import com.clg.wallet.bean.ResultDTO;
import com.clg.wallet.enums.CoinType;
import com.clg.wallet.enums.ResultCode;
import com.clg.wallet.newclient.Client;
import com.clg.wallet.newclient.ClientFactory;
import com.clg.wallet.newclient.EthNewClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 当用户发起提币的时候,吧数据插入到该表 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Transactional
public class CoinServerServiceImpl extends ServiceImpl<CoinServerMapper, CoinServer> implements CoinServerService {

    private static final Logger LOG = LoggerFactory.getLogger(CoinServerServiceImpl.class);


    @Autowired
    private ClientInfo clientInfo;


    @Value("${eth.web.apikey}")
    private String ethApikey;


    @Override
    public void checkServer(String type) {
        HashMap<String, List<CoinConfig>> typeCoinMap = clientInfo.getTypeCoinMap();
        if (!CollectionUtils.isEmpty(typeCoinMap) && !StringUtils.isEmpty(type)) {
            List<CoinConfig> coinList = typeCoinMap.get(type);
            checkItem(coinList);
        }
    }


    private void checkItem(List<CoinConfig> coinConfigs) {
        if (CollectionUtils.isEmpty(coinConfigs)) {
            LOG.info("没有查询到需要服务的类");
            return;
        }
        for (CoinConfig coin : coinConfigs) {//检查每个是否正常运行
            Client client = ClientFactory.getClient(ClientBeanMapper.INSTANCE.form(coin));
            if (null == client) {
                LOG.error("获取钱包客户端失败");
                continue;
            }
            ResultDTO result = client.getInfo();
            CoinServer newCoinServer = null;
            String mark = "服务器正常";
            if (null != result && result.getStatusCode() == ResultCode.SUCCESS.getCode()) {//服务器正常
                Integer blockNumber = client.getBlockCount().toInteger();
                int realBlockHeight = 0;
                if (CoinType.ETH.equalsIgnoreCase(coin.getCoinType())) {
                    realBlockHeight = ((EthNewClient) client).getExplorerBlockNumber(ethApikey).toInteger();
                }
                if (realBlockHeight - blockNumber > 10) {
                    mark = "钱包服务器严重落后于真实高度,请重启钱包服务,或者等待钱包同步完成";
                }
                newCoinServer = new CoinServer().setRpcIp(clientInfo.getCoinNetIp(coin.getId())).setRpcPort(coin.getRpcPort()).
                        setRealNumber(realBlockHeight).
                        setWalletNumber(blockNumber).setRunning(0).setMark(mark);
            } else { //服务器连接异常
                newCoinServer = new CoinServer().setRpcIp(clientInfo.getCoinNetIp(coin.getId())).setRpcPort(coin.getRpcPort()).
                        setRealNumber(0).
                        setWalletNumber(0).setRunning(1).setMark("钱包服务器异常,请检查服务是否开启");
            }
            CoinServer coinServer = selectCount(coin.getName());
            newCoinServer.setCoinName(coin.getName());
            if (null != coinServer) {
                newCoinServer.setId(coinServer.getId());
                super.updateById(newCoinServer);
            } else {
                super.insert(newCoinServer);
            }
        }
    }

    @Override
    public CoinServer selectCount(String name) {
        EntityWrapper<CoinServer> ew = new EntityWrapper<>();
        ew.eq("coin_name", name);
        return super.selectOne(ew);
    }
}
