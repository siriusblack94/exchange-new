package com.clg.wallet.newclient;

import com.clg.wallet.bean.ClientBean;
import com.clg.wallet.bean.ResultDTO;
import com.clg.wallet.bean.TxData;

public class NormalClient implements Client {
    protected ClientBean mClientBean;

    public NormalClient(ClientBean clientBean) {
        this.mClientBean = clientBean;
    }

    public String getName() {
        return this.mClientBean.getName();
    }

    public ResultDTO getNewAddress() {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getNewAddress(String tag) {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getBalance() {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getBalance(String address) {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getBalanceByAccount(String account) {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getTokenBalance(String assertId, String address) {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getListAddress() {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getPeers() {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getBlockCount() {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getBlockHash(long blockNumber) {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getConnectionCount() {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO dumpPrivKey(String address) {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO validateAddress(String address) {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO sendNormalIn(TxData tx) {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO sendNormalTokenIn(TxData tx) {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO sendNormalOut(TxData tx) {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO sendNormalTokenOut(TxData tx) {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getTxBlockNumber(String txid) {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getBlockByNumber(String txIdOrNumber) {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getAddrFromAcount(String account) {
        throw new RuntimeException("当前类未实现该方法");
    }

    public boolean validateAssertId(String assertId) {
        return false;
    }

    public ResultDTO getAccountState(String address) {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getTokenDecimals(String assetId) {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getBestBlockHash() {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getPendingTx() {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getInfo() {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getTransactionConfirmedIn(String txid) {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getTransactionFeeIn(String txid) {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getTransactionIn(String txid) {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getTransactionConfirmedOut(String txid) {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getTransactionFeeOut(String txid) {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getTransactionOut(String txid) {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO changePassword(String oldPass, String newPass) {
        throw new RuntimeException("当前类未实现该方法");
    }

    public ResultDTO getExplorerBlockNumber(String key) {
        return (new ResultDTO()).setResult(0);
    }
}