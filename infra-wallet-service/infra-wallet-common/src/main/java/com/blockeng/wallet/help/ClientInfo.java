package com.blockeng.wallet.help;

import com.blockeng.wallet.config.Constant;
import com.clg.wallet.bean.ClientBean;
import com.clg.wallet.bean.EThTransactionBean;
import com.blockeng.wallet.entity.AdminAddress;
import com.blockeng.wallet.entity.ClientBeanMapper;
import com.blockeng.wallet.entity.CoinConfig;
import com.blockeng.wallet.entity.CoinWithdraw;
import com.blockeng.wallet.service.AdminAddressService;
import com.blockeng.wallet.service.CoinConfigService;
import com.blockeng.wallet.utils.DESUtil;
import com.clg.wallet.newclient.Client;
import com.clg.wallet.newclient.ClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.sound.midi.SysexMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Slf4j
public class ClientInfo {

    private static final Logger LOG = LoggerFactory.getLogger(ClientInfo.class);


    HashMap<String, List<CoinConfig>> typeCoinMap = new HashMap<>();


    HashMap<Long, CoinConfig> idCoinMap = new HashMap<>();


    Map<String, Map<String, CoinConfig>> typeContractAddrMap = new HashMap<>();

    Map<String, Map<Long, CoinConfig>> typeContractIdMap = new HashMap<>();

    Map<Long, String> netIpMap = new HashMap<>();


    @Autowired
    private AdminAddressService adminAddressService;

    @Autowired
    private CoinConfigService coinConfigService;


    private List<CoinConfig> allCoinList;//所有的币种集合list


    private long oldTime; //之前的时间

    private long INTERVAL_TIME = 1000 * 60;

    @Autowired
    private DESUtil desUtil;

    @Autowired
    private IpUtils ipUtils;


    /**
     * @param id 币种id
     * @return 返回当前币种的公网ip
     */
    public String getCoinNetIp(Long id) {
        return netIpMap.get(id);
    }

    /**
     * @param type 获取对应币种的id或者type
     * @return 返回实体
     */
    public List<CoinConfig> getCoinConfigFormType(String type) {
        return getCoin(0L, type);
    }

    /**
     * @return 返回所有币种实体
     */
    public HashMap<String, List<CoinConfig>> getTypeCoinMap() {
        initCoinData();
        return typeCoinMap;
    }

    /**
     * @param coinId 获取对应币种的id
     * @return 返回币种配置实体实体
     */
    public CoinConfig getCoinConfigFormId(Long coinId) {
        List<CoinConfig> coin = getCoin(coinId, null);
        return coin.get(0);
    }

    private List<CoinConfig> getCoin(Long coinId, String type) {
        initCoinData();
        if (coinId > 0) {
            List<CoinConfig> coinList = new ArrayList<>();
            coinList.add(idCoinMap.get(coinId));
            return coinList;
        } else {
            return typeCoinMap.get(type.toLowerCase());
        }
    }

    public synchronized void initCoinData(boolean sysn) {
        long nowTime = System.currentTimeMillis();
        if (!sysn && !CollectionUtils.isEmpty(allCoinList) && (nowTime - oldTime) <= INTERVAL_TIME) {
            return;
        }
        //System.out.println(sysn);
        oldTime = nowTime;
        List<CoinConfig> nowCoinList = coinConfigService.selectAllCoin();


        if (CollectionUtils.isEmpty(nowCoinList)) {
            LOG.error("币种信息为空");
            return;
        }
        if (compareList(nowCoinList, allCoinList)) {
            return;
        }

        //重新赋值并清空数据
        allCoinList = nowCoinList;
        typeContractAddrMap.clear();
        netIpMap.clear();
        idCoinMap.clear();
        typeCoinMap.clear();

        String ip = ipUtils.getNetIp();
        LOG.info("公网ip:" + ip);

        for (CoinConfig item : allCoinList) {

            String rpcIp = item.getRpcIp();
            String rpcPwd = item.getRpcPwd();
            String rpcPwdOut =item.getRpcPwdOut();

            if (!ipUtils.isIP(rpcIp)) {
                LOG.error("ip网络格式错误");
                continue;
            }

            if (StringUtils.isEmpty(item.getTask())) {
                LOG.error("task任务不能为空或者格式错误");
                continue;
            }

            String type = item.getCoinType();

            if (StringUtils.isEmpty(type)) {
                LOG.error("币种类型错误");
                continue;
            }

            netIpMap.put(item.getId(), item.getRpcIp());

            if (!StringUtils.isEmpty(ip) && ip.equalsIgnoreCase(item.getRpcIp())) {//说明当前公网ip和当前服务器相同
                LOG.info("更改网络ip");
                item.setRpcIp("127.0.0.1");
            }

            if (!StringUtils.isEmpty(rpcPwd)) {  //rpc的密码
                item.setRpcPwd(desUtil.decrypt(rpcPwd));
            }

            if (!StringUtils.isEmpty(rpcPwdOut)) {  //rpc的密码
                item.setRpcPwdOut(desUtil.decrypt(rpcPwdOut));
            }

            if (!StringUtils.isEmpty(item.getWalletPass())) {//钱包密码
                item.setWalletPass(desUtil.decrypt(item.getWalletPass()));
            }
            if (!StringUtils.isEmpty(item.getWalletPassOut())) {//钱包密码
                item.setWalletPassOut(desUtil.decrypt(item.getWalletPassOut()));
            }
            //LOG.info("coin："+item.getRpcPwdOut()+"--"+item.getRpcIpOut()+"--"+item.getRpcPortOut()+"--"+item.getRpcUserOut());
            idCoinMap.put(item.getId(), item);


            type = type.toLowerCase();
            if (!typeCoinMap.containsKey(type)) {
                typeCoinMap.put(type, new ArrayList<>());
            }
            typeCoinMap.get(type).add(item);
        }
    }

    public synchronized void initCoinData() {
        initCoinData(false);
    }

    public ClientBean getClientInfoFromType(String type) {
        List<CoinConfig> coinList = getCoin(0L, type);
        if (!CollectionUtils.isEmpty(coinList)) {
            return ClientBeanMapper.INSTANCE.form(coinList.get(0));
        } else {
            return null;
        }
    }

    public ClientBean getClientInfoFromId(Long coinId) {
        List<CoinConfig> coinList = getCoin(coinId, null);
        if (!CollectionUtils.isEmpty(coinList)) {
            return ClientBeanMapper.INSTANCE.form(coinList.get(0));
        } else {
            return null;
        }
    }


    private boolean compareList(List<CoinConfig> nowList, List<CoinConfig> oldList) {

        if (nowList == oldList) {
            return true;
        }
        if (CollectionUtils.isEmpty(nowList)) {
            return false;
        }
        if (CollectionUtils.isEmpty(oldList)) {
            return false;
        }
        if (nowList.size() != oldList.size()) {
            return false;
        }

        for (int i = 0; i < nowList.size(); i++) {
            CoinConfig nowCoinConfig = nowList.get(i);
            CoinConfig oldCoinConfig = oldList.get(i);

            if (!nowCoinConfig.toString().equalsIgnoreCase(oldCoinConfig.toString())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取合约对应的token信息的充值数据
     *
     * @param type 类型
     * @return
     */
    public Map<String, CoinConfig> getTokenList(String type) {
        type = type.toLowerCase();
        Map<String, CoinConfig> contractCoinMap = typeContractAddrMap.get(type);
        if (CollectionUtils.isEmpty(contractCoinMap)) {
            contractCoinMap = new HashMap<>();
            List<CoinConfig> configList = getCoinConfigFormType(type);
            if (!CollectionUtils.isEmpty(configList)) {
                for (CoinConfig coin : configList) {
                    CoinConfig coinConfig = contractCoinMap.get(coin.getContractAddress());
                    if (null == coinConfig && !StringUtils.isEmpty(coin.getContractAddress())) {
                        String contractAddress = coin.getContractAddress();
                        if (!coin.getContractAddress().startsWith("0x")) {
                            contractAddress = "0x" + contractAddress;
                        }
                        contractAddress = contractAddress.toLowerCase();
                        contractCoinMap.put(contractAddress, coin);
                    }
                }
                typeContractAddrMap.put(type, contractCoinMap);
            }
        }
        return contractCoinMap;
    }

    /**
     * 获取合约对应的token信息的充值数据
     *
     * @param type 类型
     * @return
     */
    public Map<Long, CoinConfig> getTokenIdList(String type) {
        type = type.toLowerCase();
        Map<Long, CoinConfig> contractCoinMap = typeContractIdMap.get(type);
        if (CollectionUtils.isEmpty(contractCoinMap)) {
            contractCoinMap = new HashMap<>();
            List<CoinConfig> configList = getCoinConfigFormType(type);
            if (!CollectionUtils.isEmpty(configList)) {
                for (CoinConfig coin : configList) {
                    CoinConfig coinConfig = contractCoinMap.get(coin.getContractAddress());
                    if (null == coinConfig && coin.getId() > 0) {
                        contractCoinMap.put(coin.getId(), coin);
                    }
                }
                typeContractIdMap.put(type, contractCoinMap);
            }
        }
        return contractCoinMap;
    }

    @SuppressWarnings("all")
    public EThTransactionBean getEthTransactionBean(CoinConfig coin, CoinWithdraw item) {
        String type = coin.getCoinType().toLowerCase();
        log.info("币的类型-1-" + type);
        type = (type.replace("token", ""));
        log.info("币的类型-2-" + type);
        AdminAddress payAdminAddress = adminAddressService.queryAdminAccount(type, Constant.ADMIN_ADDRESS_TYPE_PAY);//获取打款账户信息
        AdminAddress feeAdminAddress = adminAddressService.queryAdminAccount(type, Constant.ADMIN_ADDRESS_TYPE_FEE);//获取手续费账户信息

        EThTransactionBean eThTransactionBean = new EThTransactionBean();
        eThTransactionBean
                .setFromAddress(payAdminAddress.getAddress())
                .setFromUserKeystore(desUtil.decrypt(payAdminAddress.getKeystore()))
                .setFromUserPass(desUtil.decrypt(payAdminAddress.getPwd()))
                .setToAddress(item.getAddress().trim())
                .setBalance(item.getMum())
                .setTokenBalance(item.getMum())
                .setContractAddress(coin.getContractAddress());
        if (null != feeAdminAddress) {
            eThTransactionBean.setFeeAddress(feeAdminAddress.getAddress())
                    .setFeeUserKeystorePath(desUtil.decrypt(feeAdminAddress.getKeystore()))
                    .setFeeUserPass(desUtil.decrypt(feeAdminAddress.getPwd()));
        }
        return eThTransactionBean;
    }

    public Client getClientFromType(String type) {
        type = type.toLowerCase();
        if (StringUtils.isEmpty(type)) {
            throw new RuntimeException("获取客户端的" + type + "不能为空");
        }
        ClientBean clientType = getClientInfoFromType(type);
        if (null == clientType) {
            throw new RuntimeException("获取客户端的信息不能为空");
        }
        Client client = ClientFactory.getClient(clientType);
        if (null == client) {
            throw new RuntimeException("获取客户端的实体不能为空");
        }
        return client;
    }

    public Client getClientFromId(Long coinId) {
        if (null == coinId || coinId <= 0) {
            throw new RuntimeException("获取客户端的失败不能,coinid不能为零或者为空");
        }
        ClientBean clientType = getClientInfoFromId(coinId);
        if (null == clientType) {
            throw new RuntimeException("获取客户端的信息不能为空");
        }
        Client client = ClientFactory.getClient(clientType);
        if (null == client) {
            throw new RuntimeException("获取客户端的实体不能为空");
        }
        return client;
    }
}
