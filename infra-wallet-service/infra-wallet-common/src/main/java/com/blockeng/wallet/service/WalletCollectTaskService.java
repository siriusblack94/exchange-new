package com.blockeng.wallet.service;

import com.baomidou.mybatisplus.service.IService;
import com.blockeng.wallet.config.Constant;
import com.blockeng.wallet.entity.AdminAddress;
import com.blockeng.wallet.entity.CoinRecharge;
import com.blockeng.wallet.entity.WalletCollectTask;
import com.clg.wallet.bean.ClientBean;
import com.clg.wallet.newclient.ClientFactory;
import com.clg.wallet.newclient.EthNewClient;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 当钱包需要归集的时候,会吧数据插入到该表,现在一般是用在eth和eth这类型需要归集的币种 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface WalletCollectTaskService extends IService<WalletCollectTask> {


    boolean updateTaskStatus(Long id, String txid, String mark, int status);

    boolean updateTaskStatus(Long id, String txid, String mark, int status, BigDecimal chainFee);


    List<WalletCollectTask> getTodoTask(String type);


    List<WalletCollectTask> queryNoFeeList(Long id, String type);


    boolean addCollectTask(BigDecimal amount, Long userId, Long coinId, String fromAddress, String toAddress);

    boolean addCollectTask(BigDecimal amount, Long userId, Long coinId, String fromAddress, String toAddress, int status);

}
