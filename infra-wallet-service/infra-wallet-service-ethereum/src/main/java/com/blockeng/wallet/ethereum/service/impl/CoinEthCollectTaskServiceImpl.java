package com.blockeng.wallet.ethereum.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.wallet.entity.CoinConfig;
import com.blockeng.wallet.entity.UserAddress;
import com.blockeng.wallet.entity.WalletCollectTask;
import com.blockeng.wallet.ethereum.service.CoinEthCollectTaskService;
import com.blockeng.wallet.help.ClientInfo;
import com.blockeng.wallet.help.ETHClient;
import com.blockeng.wallet.mapper.WalletCollectTaskMapper;
import com.blockeng.wallet.service.UserAddressService;
import com.blockeng.wallet.service.WalletCollectTaskService;
import com.clg.wallet.bean.EthResult;
import com.clg.wallet.enums.CoinType;
import com.clg.wallet.enums.ResultCode;
import com.clg.wallet.help.WalletConstant;
import com.clg.wallet.newclient.Client;
import com.clg.wallet.newclient.ClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 当钱包需要归集的时候,会吧数据插入到该表,现在一般是用在eth和eth这类型需要归集的币种 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Transactional
public class CoinEthCollectTaskServiceImpl extends ServiceImpl<WalletCollectTaskMapper, WalletCollectTask> implements CoinEthCollectTaskService {

    private static final Logger LOG = LoggerFactory.getLogger(CoinEthCollectTaskServiceImpl.class);


    @Autowired
    private ETHClient ethClient;


    @Autowired
    private ClientInfo clientInfo;


    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private WalletCollectTaskService walletCollectTaskService;


    public void collectionTask() {
        List<WalletCollectTask> todoEthTask = walletCollectTaskService.getTodoTask(CoinType.ETH);
        List<WalletCollectTask> todoTokenTask = walletCollectTaskService.getTodoTask(CoinType.ETHTOKEN);
        collectionEth(todoEthTask);
        collectionEthToken(todoTokenTask);
    }

    /**
     * 归账ethToken
     *
     * @param todoTokenTask
     */
    private void collectionEthToken(List<WalletCollectTask> todoTokenTask) {

        if (!CollectionUtils.isEmpty(todoTokenTask)) { //归账ethToken
            LOG.info("开始归账,总数是" + todoTokenTask.size());

            for (WalletCollectTask task : todoTokenTask) {
                LOG.info("开始归账:" + task.getFromAddress() + "  名称:" + task.getCoinName());
                UserAddress userEthBean = userAddressService.getByCoinIdAndUserId(task.getUserId(), task.getCoinId());
                if (StringUtils.isEmpty(userEthBean.getKeystore()) && StringUtils.isEmpty(userEthBean.getPwd())) {
                    walletCollectTaskService.updateTaskStatus(task.getId(), "", "归账失败,不存在当前地址的keystore和pwd,address:" + userEthBean.getAddress(), 1);
                    return;
                }
                if (null != userEthBean)
                    try {
                        CoinConfig coin = clientInfo.getCoinConfigFormId(task.getCoinId());
                        if (null == coin) {
                            LOG.error("不存在该币种,name:" + task.getCoinName() + " id:" + task.getId());
                            return;
                        }
                        //获取地址及密钥
                        EthResult ethResult = ethClient.sendEthTokenToCenter(userEthBean, task.getToAddress(), coin);
                        if (ethResult.isSuccess()) {
                            walletCollectTaskService.updateTaskStatus(task.getId(), ethResult.getTxid(), ethResult.getInfo(), 1);
                            LOG.info(LOG.getName() + "归账任务成功");
                        } else {

                            if (WalletConstant.NOT_ENOUGH_MONEY == ethResult.getCode()) { //归账的时候,如果当前账户没有余额,就不需要归集,直接改成1
                                walletCollectTaskService.updateTaskStatus(task.getId(), ethResult.getTxid(), ethResult.getInfo(), 1);
                            } else if (WalletConstant.NOT_ENOUGH_FEE == ethResult.getCode()) { // 打币是成功的,但是没有手续费没有手续费,需要打出手续费
                                LOG.info(LOG.getName() + "打出手续费完成:txid=" + ethResult.getTxid() + " info:" + ethResult.getInfo());
                            }
                        }

                    } catch (Exception e) {
                        LOG.info(LOG.getName() + "归账任务失败,error:" + e.toString());
                    }
            }
        }
    }

    /**
     * 归账eth
     *
     * @param todoEthTask
     */
    private void collectionEth(List<WalletCollectTask> todoEthTask) {

        if (!CollectionUtils.isEmpty(todoEthTask)) { //归账eth

            for (WalletCollectTask task : todoEthTask) {
                LOG.info("开始归账:" + task.getFromAddress() + "  名称:" + task.getCoinName());
                UserAddress userEthBean = userAddressService.getByCoinIdAndUserId(task.getUserId(), task.getCoinId());
                if (StringUtils.isEmpty(userEthBean.getKeystore()) && StringUtils.isEmpty(userEthBean.getPwd())) {
                    walletCollectTaskService.updateTaskStatus(task.getId(), "", "归账失败,不存在当前地址的keystore和pwd,address:" + userEthBean.getAddress(), 1);
                    return;
                }
                if (null != userEthBean)
                    try {
                        EthResult ethResult = ethClient.sendEthToCenter(clientInfo.getClientInfoFromType(CoinType.ETH), userEthBean, task);
                        if (ethResult.isSuccess()) {
                            walletCollectTaskService.updateTaskStatus(task.getId(), ethResult.getTxid(), ethResult.getInfo(), 1);
                        } else {
                            if (WalletConstant.NOT_ENOUGH_MONEY == ethResult.getCode()) {
                                walletCollectTaskService.updateTaskStatus(task.getId(), ethResult.getTxid(), ethResult.getInfo(), 1);
                            }
                        }
                    } catch (Exception e) {
                        LOG.info(LOG.getName() + "归账任务失败,error:" + e.toString());
                    }
            }
        }

    }
}
