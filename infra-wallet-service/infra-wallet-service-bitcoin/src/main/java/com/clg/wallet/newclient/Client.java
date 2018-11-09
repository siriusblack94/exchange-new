package com.clg.wallet.newclient;

import com.clg.wallet.bean.ResultDTO;
import com.clg.wallet.bean.TxData;

public interface Client {
    String getName();

    ResultDTO getNewAddress();

    ResultDTO getNewAddress(String var1);

    ResultDTO getBalance();

    ResultDTO getBalance(String var1);

    ResultDTO getBalanceByAccount(String var1);

    ResultDTO getTokenBalance(String var1, String var2);

    ResultDTO getListAddress();

    ResultDTO getPeers();

    ResultDTO getBlockCount();

    ResultDTO getBlockHash(long var1);

    ResultDTO getConnectionCount();

    ResultDTO dumpPrivKey(String var1);

    ResultDTO validateAddress(String var1);

    ResultDTO sendNormalIn(TxData var1);

    ResultDTO sendNormalOut(TxData var1);

    ResultDTO sendNormalTokenIn(TxData var1);

    ResultDTO sendNormalTokenOut(TxData var1);

    ResultDTO getTxBlockNumber(String var1);

    ResultDTO getBlockByNumber(String var1);

    ResultDTO getAddrFromAcount(String var1);

    boolean validateAssertId(String var1);

    ResultDTO getAccountState(String var1);

    ResultDTO getTokenDecimals(String var1);

    ResultDTO getBestBlockHash();

    ResultDTO getPendingTx();

    ResultDTO getInfo();

    ResultDTO getTransactionConfirmedIn(String var1);

    ResultDTO getTransactionFeeIn(String var1);

    ResultDTO getTransactionIn(String var1);

    ResultDTO getTransactionConfirmedOut(String var1);

    ResultDTO getTransactionFeeOut(String var1);

    ResultDTO getTransactionOut(String var1);

    ResultDTO changePassword(String var1, String var2);

    ResultDTO getExplorerBlockNumber(String var1);
}