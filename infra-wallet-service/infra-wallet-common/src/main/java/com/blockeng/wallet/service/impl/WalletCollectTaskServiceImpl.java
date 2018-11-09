package com.blockeng.wallet.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.wallet.config.Constant;
import com.blockeng.wallet.entity.AdminAddress;
import com.blockeng.wallet.entity.CoinConfig;
import com.blockeng.wallet.entity.WalletCollectTask;
import com.blockeng.wallet.help.ClientInfo;
import com.blockeng.wallet.mapper.WalletCollectTaskMapper;
import com.blockeng.wallet.service.AdminAddressService;
import com.blockeng.wallet.service.WalletCollectTaskService;
import com.clg.wallet.bean.ClientBean;
import com.clg.wallet.enums.CoinType;
import com.clg.wallet.newclient.ClientFactory;
import com.clg.wallet.newclient.EthNewClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

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
public class WalletCollectTaskServiceImpl extends ServiceImpl<WalletCollectTaskMapper, WalletCollectTask> implements WalletCollectTaskService {

    private static final Logger LOG = LoggerFactory.getLogger(WalletCollectTaskServiceImpl.class);

    @Autowired
    private AdminAddressService adminAddressService;

    @Autowired
    private ClientInfo clientInfo;

    public boolean updateTaskStatus(Long id, String txid, String mark, int status) {
        return super.updateById(new WalletCollectTask().setId(id).setTxid(txid).setMark(mark).setStatus(status));
    }

    public boolean updateTaskStatus(Long id, String txid, String mark, int status, BigDecimal chainFee) {
        return super.updateById(new WalletCollectTask().setId(id).setTxid(txid).setMark(mark).setStatus(status).setChainFee(chainFee));
    }

    public List<WalletCollectTask> getTodoTask(String type) {
        EntityWrapper<WalletCollectTask> ew = new EntityWrapper<>();
        ew.eq("status", 0);
        ew.eq("coin_type", type);
        return super.selectList(ew);
    }

    @Override
    public List<WalletCollectTask> queryNoFeeList(Long id, String type) {
        EntityWrapper<WalletCollectTask> ew = new EntityWrapper<>();
        if (null != id && id > 0) {
            ew.eq("coin_id", id);
        }
        if (!StringUtils.isEmpty(type)) {
            ew.eq("coin_type", type);
        }
        ew.andNew().isNull("chain_fee").or("chain_fee", "");
        ew.andNew().isNotNull("txid");
        return super.selectList(ew);
    }


    public boolean addCollectTask(BigDecimal amount, Long userId, Long coinId, String fromAddress, String toAddress) {
        return addCollectTask(amount, userId, coinId, fromAddress, toAddress, 0);
    }

    public boolean addCollectTask(BigDecimal amount, Long userId, Long coinId, String fromAddress, String toAddress, int status) {
        CoinConfig coin = clientInfo.getCoinConfigFormId(coinId);
        if (CoinType.ETH.equalsIgnoreCase(coin.getCoinType())) {//eth归账需要扣除手续费
            EthNewClient client = (EthNewClient) clientInfo.getClientFromId(coinId);
            amount = amount.subtract(client.getEthFee());
            if (BigDecimal.ZERO.compareTo(amount) >= 0) {
                LOG.info("归账金额不足,无需归账,userid:" + userId);
                return true;//无需归集
            }
        }
        WalletCollectTask walletCollectTask = new WalletCollectTask();
        walletCollectTask.
                setAmount(amount).
                setUserId(userId).
                setCoinName(coin.getName()).
                setCoinId(coinId).
                setCoinType(coin.getCoinType()).
                setFromAddress(fromAddress).
                setToAddress(toAddress).
                setStatus(status);
        return super.insert(walletCollectTask);
    }
}
