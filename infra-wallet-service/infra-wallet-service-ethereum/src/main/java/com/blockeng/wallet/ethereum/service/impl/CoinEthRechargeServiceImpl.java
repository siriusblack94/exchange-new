package com.blockeng.wallet.ethereum.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.wallet.config.Constant;
import com.blockeng.wallet.dto.CoinRechargeMessageDTO;
import com.blockeng.wallet.entity.*;
import com.blockeng.wallet.enums.MessageChannel;
import com.blockeng.wallet.ethereum.service.CoinEthRechargeService;
import com.blockeng.wallet.help.ClientInfo;
import com.blockeng.wallet.mapper.CoinRechargeMapper;
import com.blockeng.wallet.service.*;
import com.blockeng.wallet.utils.ReadProperties;
import com.blockeng.wallet.utils.GsonUtils;
import com.clg.wallet.enums.CoinType;
import com.clg.wallet.newclient.Client;
import com.clg.wallet.newclient.ClientFactory;
import com.clg.wallet.newclient.EthNewClient;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 用户充值,当前用户充值成功之后添加数据到这个表,充值一般无手续费.当status为1的时候表示充值成功 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Transactional
public class CoinEthRechargeServiceImpl extends ServiceImpl<CoinRechargeMapper, CoinRecharge> implements CoinEthRechargeService {

    private static final Logger LOG = LoggerFactory.getLogger(CoinEthRechargeServiceImpl.class);

    @Autowired
    private ClientInfo clientInfo;

    @Autowired
    private CoinConfigService coinConfigService;

    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private CoinRechargeService coinRechargeService;

    @Autowired
    private WalletCollectTaskService walletCollectTaskService;

    @Autowired
    private AdminAddressService adminAddressService;

    @Autowired
    private ReadProperties readProperties;

    public CoinConfig ethCoinBean;

    private Client client;


    public void rechargeCoin(CoinConfig ethCoin) {
        client = ClientFactory.getClient(clientInfo.getClientInfoFromType(CoinType.ETH));
        ethCoinBean = ethCoin;
        LOG.info(ethCoin.getName() + "开始查询区块----" + CoinType.ETHTOKEN);
        Map<String, CoinConfig> contractInfo = clientInfo.getTokenList(CoinType.ETHTOKEN);
        LOG.info("ercToken有几个" + contractInfo.size());
        if (contractInfo != null) {
            Set<String> keys = contractInfo.keySet();   //此行可省略，直接将map.keySet()写在for-each循环的条件中
//            for (String key : keys) {
//                LOG.info("key值：" + key + " value值：" + contractInfo.get(key));
//                LOG.info("初始化检测的币种----" + contractInfo.get(key));
//            }
        }
        if (null != contractInfo && ethCoinBean != null) {
            try {
                int latest = client.getBlockCount().toInteger() - ethCoin.getMinConfirm();//获取当前区块高度,减去12
                int currentCount = Integer.valueOf(coinConfigService.lastBlock(ethCoinBean.getId()));
                if (latest > currentCount) {
                    int count = latest - currentCount;
                    if (count > readProperties.maxCount) {//每次最多处理100笔
                        latest = currentCount + readProperties.maxCount;
                    }
                    recharge(contractInfo, ethCoinBean, currentCount, latest);
                } else {
                    LOG.info(ethCoin.getName() + "暂时无新的区块高度");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            LOG.info(ethCoin.getName() + "查询区块结束");
        } else {
            LOG.info("初始化失败");
        }
    }

    private void recharge(Map<String, CoinConfig> coinMap, CoinConfig ethCoinBean, int currentBlockNumber, int latest) {
        CoinConfig nowCoin;
        for (int i = currentBlockNumber + 1; i < latest; ++i) {
            LOG.info(ethCoinBean.getName() + "区块开始,高度为:" + i);
            List<EthBlock.TransactionResult> transactions = ((EthBlock.Block) client.getBlockByNumber("" + i).getResult()).getTransactions();
            LOG.info(ethCoinBean.getCoinType() + "----" + ethCoinBean.getId());
            if (CollectionUtils.isEmpty(transactions)) {//如果区块高度为null,或者个数为0,继续看下一个
                continue;
            }
            for (EthBlock.TransactionResult item : transactions) {
                EthBlock.TransactionObject transaction = (EthBlock.TransactionObject) item;
                String input = transaction.getInput();

                if (!TextUtils.isEmpty(transaction.getTo())) {
                    String to = transaction.getTo().toLowerCase();
                    String contractAddress = "";

                    if (!CollectionUtils.isEmpty(coinMap) && coinMap.containsKey(to)) {//解析合约
                        nowCoin = coinMap.get(to);
                        contractAddress = nowCoin.getContractAddress();
                        if (!TextUtils.isEmpty(input) && input.length() >= 138) {//136表示合约的最低长度
                            transaction.setTo("0x" + input.substring(34, 74));
                        } else {
                            continue;
                        }
                    } else if ("0x".equals(input) || "0x0".equals(input)) {
                        nowCoin = ethCoinBean;
                    } else {
                        continue;
                    }
                    UserAddress userEthResult = userAddressService.getByCoinIdAndAddr(transaction.getTo().toLowerCase(), nowCoin.getId());//查到到当前用户
                    if (null != userEthResult) {
                        BigDecimal money;
                        if (!StringUtils.isEmpty(contractAddress)) {
                            String moneyStr = input.substring(74, 138);//金额长度64
                            int unit = client.getTokenDecimals(contractAddress).toInteger();
                            money = new BigDecimal(new BigInteger(moneyStr, 16)).divide(BigDecimal.TEN.pow(unit));
                            LOG.info("精度：【" + unit + "】---moneyStr：【" + moneyStr + "】" + "----money：【" + money + "】");
                        } else {
                            money = Convert.fromWei(new BigDecimal(transaction.getValue()), Convert.Unit.ETHER);
                        }
                        LOG.info(nowCoin.getName() + "查询到一条记录,交易id为:" + transaction.getHash());
                        LOG.info(nowCoin.getName() + "查询到111111记录,交易id为:");
                        LOG.info("------1---" + transaction.getHash());
                        LOG.info("------2---" + transaction.getTo());

                        LOG.info("----3-----" + userEthResult.getUserId());
                        LOG.info("------4-----" + money);
                        LOG.info("------5-----" + Constant.INSERT_SUCCESS);
                        LOG.info("------6-----" + nowCoin);
                        LOG.info("------7-----" + transaction.getGasPrice());
                        LOG.info("------8-----" + transaction.getGas());

                        try {
                            ((EthNewClient) client).isFailed(transaction.getHash());
                        } catch (Exception e) {
                            e.printStackTrace();
                            LOG.info(nowCoin.getName() + "充值异常,交易id为:" + transaction.getHash());
                        }
                        if (i < 6117751) {
                            continue;
                        }
                        if (((EthNewClient) client).isFailed(transaction.getHash()).toBoolean()) {
                            LOG.info("异常处理");
                            LOG.info(ethCoinBean.getName() + "充值失败,交易id为:" + transaction.getHash());
                        } else {
                            LOG.info("正常处理");
                            AdminAddress adminAddress = adminAddressService.queryAdminAccount(CoinType.ETH, Constant.ADMIN_ADDRESS_TYPE_FEE);//3表示手续费
                            if (null == adminAddress || !transaction.getFrom().equalsIgnoreCase(adminAddress.getAddress())) {
                                boolean isSuccess = coinRechargeService.addRecord(transaction.getHash(), transaction.getTo(), userEthResult.getUserId(), money, Constant.INSERT_SUCCESS, nowCoin);
                                if (isSuccess) {
                                    addBackTask(nowCoin, money, transaction.getTo(), userEthResult);//增加归账任务
                                }
                                LOG.info(ethCoinBean.getName() + (isSuccess ? "增加充值任务成功:" : "插入充值失败") + transaction.getHash());
                            } else {
                                LOG.info(ethCoinBean.getName() + "归账手续费无需到账");
                            }
                        }
                    }
                }
            }
            currentBlockNumber = i;
        }
        coinConfigService.updateCoinLastblock("" + currentBlockNumber, ethCoinBean.getId());
    }

    private void addBackTask(CoinConfig config, BigDecimal money, String fromAddress, UserAddress userEthResult) {
        AdminAddress payAdminAddress = adminAddressService.queryAdminAccount(config.getCoinType(), Constant.ADMIN_ADDRESS_TYPE_PAY);//2打款
        AdminAddress backAdminAddress = adminAddressService.queryAdminAccount(config.getCoinType(), Constant.ADMIN_ADDRESS_TYPE_COLLECT);//3手续费

        if (null == backAdminAddress || null == backAdminAddress || config.getCoinType().toLowerCase().endsWith("token")) {
            String type = config.getCoinType().replace("Token", "");
            if (StringUtils.isEmpty(type)) {
                LOG.info(config.getCoinType() + "的归账钱包为空");
            } else {
                payAdminAddress = adminAddressService.queryAdminAccount(type, Constant.ADMIN_ADDRESS_TYPE_PAY);//2打款
                backAdminAddress = adminAddressService.queryAdminAccount(type, Constant.ADMIN_ADDRESS_TYPE_COLLECT);//3手续费
            }

        }
        String toAddress = null;
        if (null != payAdminAddress) {
            toAddress = payAdminAddress.getAddress();
            BigDecimal payBalance = BigDecimal.ZERO;
            if (config.getCoinType().toLowerCase().endsWith("token")) {
                payBalance = client.getTokenBalance(config.getContractAddress(), toAddress).toBigDecimal();
            } else {
                payBalance = client.getBalance(toAddress).toBigDecimal();//打款账户余额
            }
            BigDecimal creditLimit = config.getCreditLimit();
            if (null != creditLimit && creditLimit.compareTo(BigDecimal.ZERO) > 0 && payBalance.compareTo(creditLimit) > 0) {//如果这样需要更改归账地址
                if (null != backAdminAddress && !StringUtils.isEmpty(backAdminAddress.getAddress())) {
                    String backAddress = backAdminAddress.getAddress();
                    toAddress = StringUtils.isEmpty(backAddress) ? toAddress : backAddress;
                }
            }
        }
        walletCollectTaskService.addCollectTask(money, userEthResult.getUserId(), config.getId(), fromAddress, toAddress);
    }
}
