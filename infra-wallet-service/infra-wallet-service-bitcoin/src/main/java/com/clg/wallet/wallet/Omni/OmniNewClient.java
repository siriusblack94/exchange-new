package com.clg.wallet.wallet.Omni;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blockeng.wallet.config.Constant;
import com.blockeng.wallet.help.ClientInfo;
import com.clg.infra.util.HttpRequestUtil;
import com.clg.wallet.bean.ClientBean;
import com.clg.wallet.bean.ResultDTO;
import com.clg.wallet.bean.TxData;
import com.clg.wallet.enums.ResultCode;
import com.clg.wallet.help.JsonRpcClient;
import com.clg.wallet.newclient.NormalClient;
import com.clg.wallet.utils.BigDecimalUtils;
import com.clg.wallet.wallet.Omni.bean.BtcBlock;
import com.clg.wallet.wallet.Omni.bean.BtcTransaction;
import com.clg.wallet.wallet.Omni.bean.BtcTxIn;
import com.clg.wallet.wallet.Omni.bean.BtcTxItem;
import com.clg.wallet.wallet.Omni.bean.BtcTxOut;
import com.clg.wallet.wallet.Omni.bean.OmniBalance;
import com.clg.wallet.wallet.Omni.bean.OmniItemTransaction;
import com.clg.wallet.wallet.Omni.bean.OmniTxItem;
import com.clg.wallet.wallet.Omni.bean.ValidateAddress;
import com.googlecode.jsonrpc4j.JsonRpcClientException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jdk.nashorn.api.scripting.JSObject;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
@Slf4j
public class OmniNewClient extends NormalClient {
    private static JsonRpcClient jsonRpcClient = JsonRpcClient.getInstance();
    private static final Logger LOG = LoggerFactory.getLogger(OmniNewClient.class);
    private static BigDecimal fee = new BigDecimal(1.0E-4D);
    private static BigDecimal dust = new BigDecimal(6.0E-6D);
    private String btcUrl = "https://blockchain.info/latestblock";
    private String dogeUrl = "https://dogechain.info/chain/Dogecoin/q/getblockcount";

    public OmniNewClient(ClientBean clientBean) {
        super(clientBean);
    }

    public ResultDTO getNewAddress() {
        return this.getNewAddress("");
    }

    public ResultDTO getNewAddress(String tag) {
        String address = (String)jsonRpcClient.newInvoke(this.mClientBean, "getnewaddress", String.class, new Object[]{tag});
        return (new ResultDTO()).setResult(address);
    }

    public ResultDTO getBalance() {
        return this.getBalance((String)null);
    }

    public ResultDTO getBalance(String address) {
        BigDecimal balance;
        if (StringUtils.isEmpty(address)) {
            balance = (BigDecimal)jsonRpcClient.newInvoke(this.mClientBean, "getbalance", BigDecimal.class, new Object[0]);
        } else {
            balance = (BigDecimal)jsonRpcClient.newInvoke(this.mClientBean, "getreceivedbyaddress", BigDecimal.class, new Object[]{address});
        }

        return (new ResultDTO()).setResult(balance);
    }
    public ResultDTO getBalanceOut() {
        return this.getBalanceOut((String)null);
    }

    public ResultDTO getBalanceOut(String address) {
        BigDecimal balance;
        if (StringUtils.isEmpty(address)) {
            balance = (BigDecimal)jsonRpcClient.outInvoke(this.mClientBean, "getbalance", BigDecimal.class, new Object[0]);
        } else {
            balance = (BigDecimal)jsonRpcClient.outInvoke(this.mClientBean, "getreceivedbyaddress", BigDecimal.class, new Object[]{address});
        }

        return (new ResultDTO()).setResult(balance);
    }
    public ResultDTO getTransactionFeeIn(String txid) {
        BigDecimal fee = BigDecimal.ZERO;

        BigDecimal inBalance = BigDecimal.ZERO;

        BigDecimal outBalance = BigDecimal.ZERO;

        BtcTransaction btcTransaction = (BtcTransaction)this.getTransactionIn(txid).toObj(BtcTransaction.class);
        List<BtcTxIn> vin = btcTransaction.getVin();
        Integer vout;
        BtcTransaction vinTransaction;
        if (!CollectionUtils.isEmpty(vin)) {
            for(Iterator var7 = vin.iterator(); var7.hasNext(); inBalance = inBalance.add(((BtcTxOut)vinTransaction.getVout().get(vout)).getValue())) {
                BtcTxIn item = (BtcTxIn)var7.next();
                vout = item.getVout();
                String vinTxid = item.getTxid();
                vinTransaction = (BtcTransaction)this.getTransactionIn(vinTxid).toObj(BtcTransaction.class);
            }
        }

        List<BtcTxOut> btcvout = btcTransaction.getVout();
        BigDecimal value;
        if (!CollectionUtils.isEmpty(btcvout)) {
            for(Iterator var14 = btcvout.iterator(); var14.hasNext(); outBalance = outBalance.add(value)) {
                BtcTxOut item = (BtcTxOut)var14.next();
                value = item.getValue();
            }
        }
        fee = inBalance.subtract(outBalance);
        if(vin==null&&btcvout==null){
            fee =  btcTransaction.getFee()==null ? BigDecimal.ZERO : btcTransaction.getFee();
        }
        return (new ResultDTO()).setResult(fee.compareTo(BigDecimal.ZERO) > 0 ? fee : BigDecimal.ZERO);
    }


    public ResultDTO getTransactionFeeOut(String txid) {
        BigDecimal fee = BigDecimal.ZERO;

        BigDecimal inBalance = BigDecimal.ZERO;

        BigDecimal outBalance = BigDecimal.ZERO;

        BtcTransaction btcTransaction = (BtcTransaction)this.getTransactionOut(txid).toObj(BtcTransaction.class);
        List<BtcTxIn> vin = btcTransaction.getVin();
        Integer vout;
        BtcTransaction vinTransaction;
        if (!CollectionUtils.isEmpty(vin)) {
            for(Iterator var7 = vin.iterator(); var7.hasNext(); inBalance = inBalance.add(((BtcTxOut)vinTransaction.getVout().get(vout)).getValue())) {
                BtcTxIn item = (BtcTxIn)var7.next();
                vout = item.getVout();
                String vinTxid = item.getTxid();
                vinTransaction = (BtcTransaction)this.getTransactionOut(vinTxid).toObj(BtcTransaction.class);
            }
        }

        List<BtcTxOut> btcvout = btcTransaction.getVout();
        BigDecimal value;
        if (!CollectionUtils.isEmpty(btcvout)) {
        for(Iterator var14 = btcvout.iterator(); var14.hasNext(); outBalance = outBalance.add(value)) {
            BtcTxOut item = (BtcTxOut)var14.next();
            value = item.getValue();
        }
        }
        fee = inBalance.subtract(outBalance);
        if(vin==null&&btcvout==null){
            fee =  btcTransaction.getFee()==null ? BigDecimal.ZERO : btcTransaction.getFee();
        }
        return (new ResultDTO()).setResult(fee.compareTo(BigDecimal.ZERO) > 0 ? fee :BigDecimal.ZERO );
    }

    public ResultDTO getBlockCount() {
        int blockCount = (Integer)jsonRpcClient.newInvoke(this.mClientBean, "getblockcount", Integer.class, new Object[0]);
        return (new ResultDTO()).setResult(blockCount);
    }

    public ResultDTO getTokenBalance(String assertId, String address) {
        OmniBalance balance = (OmniBalance)jsonRpcClient.newInvoke(this.mClientBean, "omni_getbalance", OmniBalance.class, new Object[]{address, Integer.valueOf(assertId)});
        return (new ResultDTO()).setResult(balance.getBalance());
    }

    public ResultDTO getWalletTokenBalance(String assertId) {
        BigDecimal balance = BigDecimal.ZERO;
        List<OmniItemTransaction> omniItemTransactions = this.omniListTransactions();
        if (!CollectionUtils.isEmpty(omniItemTransactions)) {
            Iterator var4 = omniItemTransactions.iterator();

            while(var4.hasNext()) {
                OmniItemTransaction item = (OmniItemTransaction)var4.next();
                if (Integer.valueOf(assertId) == item.propertyid) {
                    balance = balance.add(item.amount);
                }
            }
        }

        return (new ResultDTO()).setResult(balance);
    }

    public ResultDTO validateAddress(String address) {
        ValidateAddress validateaddress = (ValidateAddress)jsonRpcClient.newInvoke(this.mClientBean, "validateaddress", ValidateAddress.class, new Object[]{address});
        boolean isRight;
        if (null != validateaddress && !StringUtils.isEmpty(validateaddress.getAddress())) {
            isRight = false;
        } else {
            isRight = true;
        }

        return (new ResultDTO()).setResult(isRight);
    }

    public ResultDTO listSinceBlock(String blockHash) {
        JSONObject jsonObject = (JSONObject)jsonRpcClient.newInvoke(this.mClientBean, "listsinceblock", JSONObject.class, new Object[]{blockHash});
        List<OmniTxItem> omniTxItems = JSONObject.parseArray(JSONObject.toJSONString(jsonObject.getJSONArray("transactions")), OmniTxItem.class);
        return (new ResultDTO()).setResult(omniTxItems);
    }

    public ResultDTO listUnspent(int confirm) {
        JSONArray jsonObject = (JSONArray)jsonRpcClient.newInvoke(this.mClientBean, "listunspent", JSONArray.class, new Object[]{confirm});
        List<BtcTxItem> omniTxItems = JSONObject.parseArray(JSONObject.toJSONString(jsonObject), BtcTxItem.class);
        return (new ResultDTO()).setResult(omniTxItems);
    }

    private boolean unlockWalletIn(String pass) {
        try {
            jsonRpcClient.newInvoke(this.mClientBean, "walletpassphrase", JSONObject.class, new Object[]{pass, 10000L});
            return true;
        } catch (JsonRpcClientException var3) {
            if (var3.toString().contains("The wallet passphrase entered was incorrect")) {
                return false;
            } else {
                throw var3;
            }
        }
    }
    private boolean unlockWalletOut(String pass) {
        try {
            jsonRpcClient.outInvoke(this.mClientBean, "walletpassphrase", JSONObject.class, new Object[]{pass, 10000L});
            return true;
        } catch (JsonRpcClientException var3) {
            if (var3.toString().contains("The wallet passphrase entered was incorrect")) {
                return false;
            } else {
                throw var3;
            }
        }
    }

    private BtcTxItem getFeeBtcTx() {
        BtcTxItem feeItem = null;
        ResultDTO walletTokenBalance = this.listUnspent(6);
        List<BtcTxItem> omniTxItems = (List)walletTokenBalance.getResult();
        Iterator var4 = omniTxItems.iterator();

        while(var4.hasNext()) {
            BtcTxItem item = (BtcTxItem)var4.next();
            if (item.amount.compareTo(fee.add(dust)) > 0) {
                feeItem = item;
                break;
            }
        }

        return feeItem;
    }

    public ResultDTO sendNormalTokenIn(TxData tx) {
        BtcTxItem feeBtcTx = this.getFeeBtcTx();
        if (null == feeBtcTx) {
            return (new ResultDTO()).setStatusCode(ResultCode.FEE_NOTENOUGH_ERROR.getCode());
        } else {
            String opReturn = (String)jsonRpcClient.newInvoke(this.mClientBean, "omni_createpayload_simplesend", String.class, new Object[]{Integer.valueOf(tx.getAssertId()), BigDecimalUtils.formatBigDecimal(tx.getBalance(), 8).toString()});
            JSONArray txRaws = new JSONArray();
            JSONObject txItem = new JSONObject();
            txRaws.add(txItem);
            txItem.put("txid", feeBtcTx.txid);
            txItem.put("vout", feeBtcTx.vout);
            JSONObject sendObj = new JSONObject();
            String raw = (String)jsonRpcClient.newInvoke(this.mClientBean, "createrawtransaction", String.class, new Object[]{txRaws, sendObj});
            raw = (String)jsonRpcClient.newInvoke(this.mClientBean, "omni_createrawtx_opreturn", String.class, new Object[]{raw, opReturn});
            raw = (String)jsonRpcClient.newInvoke(this.mClientBean, "omni_createrawtx_reference", String.class, new Object[]{raw, tx.getToAddress()});
            txItem.put("scriptPubKey", feeBtcTx.getScriptPubKey());
            txItem.put("value", feeBtcTx.getAmount());
            raw = (String)jsonRpcClient.newInvoke(this.mClientBean, "omni_createrawtx_change", String.class, new Object[]{raw, txRaws, tx.getToAddress(), 1.0E-4D});
            return !this.unlockWalletIn(tx.getPass()) ? (new ResultDTO()).setStatusCode(ResultCode.PASS_ERROR.getCode()) : (new ResultDTO()).setResult(this.signTx(raw));
        }
    }

    public String signTx(String raw) {
        JSONObject signInfo = (JSONObject)jsonRpcClient.newInvoke(this.mClientBean, "signrawtransaction", JSONObject.class, new Object[]{raw});
        raw = signInfo.getString("hex");
        Boolean complete = signInfo.getBoolean("complete");
        if (complete) {
            return (String)jsonRpcClient.newInvoke(this.mClientBean, "sendrawtransaction", String.class, new Object[0]);
        } else {
            throw new RuntimeException("签名失败,暂时不支持多重签名");
        }
    }

    public ResultDTO getBlockByNumber(String txIdOrNumber) {
        JSONObject jsonObject = null;
        if ("hc".equalsIgnoreCase(this.mClientBean.getName())) {
            jsonRpcClient.newInvoke(this.mClientBean, "getblockbynumber", JSONObject.class, new Object[]{Integer.valueOf(txIdOrNumber)});
        } else {
            String hash = (String)jsonRpcClient.newInvoke(this.mClientBean, "getblockhash", String.class, new Object[]{Integer.valueOf(txIdOrNumber)});
            jsonObject = (JSONObject)jsonRpcClient.newInvoke(this.mClientBean, "getblock", JSONObject.class, new Object[]{hash});
        }

        BtcBlock btcBlock = (BtcBlock)JSONObject.toJavaObject(jsonObject, BtcBlock.class);
        return (new ResultDTO()).setResult(btcBlock);
    }

    public ResultDTO sendNormalIn(TxData tx) {
        if (!StringUtils.isEmpty(tx.getPass()) && !this.unlockWalletIn(tx.getPass())) {
            return (new ResultDTO()).setStatusCode(ResultCode.PASS_ERROR.getCode());
        } else {
            try{
                String sender = (String)jsonRpcClient.newInvoke(this.mClientBean, "sendtoaddress", String.class, new Object[]{tx.getToAddress(),tx.getBalance().doubleValue()});
                if (!StringUtils.isEmpty(tx.getPass())) {
                    jsonRpcClient.newInvoke(this.mClientBean, "walletlock", JSObject.class, new Object[0]);
                }
                return (new ResultDTO()).setResult(sender);
            }catch(Exception e){
                if (!StringUtils.isEmpty(tx.getPass())) {
                    jsonRpcClient.newInvoke(this.mClientBean, "walletlock", JSObject.class, new Object[0]);
                }
                return (new ResultDTO()).setResult(e.getMessage());
            }
        }
    }

    public ResultDTO sendNormalOut(TxData tx) {
        if (!StringUtils.isEmpty(tx.getPass()) && !this.unlockWalletOut(tx.getPass())) {
            return (new ResultDTO()).setStatusCode(ResultCode.PASS_ERROR.getCode());
        } else {
            try{
                String sender = (String)jsonRpcClient.outInvoke(this.mClientBean, "sendtoaddress", String.class, new Object[]{tx.getToAddress(),tx.getBalance().doubleValue()});
                if (!StringUtils.isEmpty(tx.getPass())) {
                    jsonRpcClient.outInvoke(this.mClientBean, "walletlock", JSObject.class, new Object[0]);
                }
                return (new ResultDTO()).setResult(sender);
            }catch(Exception e){
                if (!StringUtils.isEmpty(tx.getPass())) {
                    jsonRpcClient.outInvoke(this.mClientBean, "walletlock", JSObject.class, new Object[0]);
                }
                return (new ResultDTO()).setResult(e.getMessage());
            }
        }
    }


    public List<OmniItemTransaction> omniListTransactions() {
        JSONArray omniListTransactions = (JSONArray)jsonRpcClient.newInvoke(this.mClientBean, "omni_listtransactions", JSONArray.class, new Object[0]);
        List<OmniItemTransaction> omniItemTransactions = JSONObject.parseArray(JSONObject.toJSONString(omniListTransactions), OmniItemTransaction.class);
        return omniItemTransactions;
    }

    public ResultDTO getInfo() {
        String method = "hc".equalsIgnoreCase(this.mClientBean.getName()) ? "getinfo" : "getnetworkinfo";
        JSONObject info = (JSONObject)jsonRpcClient.newInvoke(this.mClientBean, method, JSONObject.class, new Object[0]);
        return (new ResultDTO()).setResult(info);
    }

    public ResultDTO getTransactionIn(String txid) {
        BtcTransaction btcTransaction = null;
        try {
             JSONObject jsonObject = (JSONObject)jsonRpcClient.newInvoke(this.mClientBean, "getrawtransaction", JSONObject.class, new Object[]{txid, 1});
           // JSONObject jsonObject = (JSONObject)jsonRpcClient.newInvoke(this.mClientBean, "gettransaction", JSONObject.class, new Object[]{txid});

            btcTransaction = (BtcTransaction)JSONObject.toJavaObject(jsonObject, BtcTransaction.class);
        } catch (Exception var4) {
            if (var4.toString().contains("Invalid or non-wallet transaction id")) {
                LOG.info("更当前钱包无关");
            }
        }
        return (new ResultDTO()).setResult(btcTransaction);
    }

    public ResultDTO getTransactionOut(String txid) {
        BtcTransaction btcTransaction = null;
        try {
            JSONObject jsonObject = (JSONObject)jsonRpcClient.outInvoke(this.mClientBean, "getrawtransaction", JSONObject.class, new Object[]{txid, 1});

            btcTransaction = (BtcTransaction)JSONObject.toJavaObject(jsonObject, BtcTransaction.class);
        } catch (Exception var4) {
            if (var4.toString().contains("Invalid or non-wallet transaction id")) {
                LOG.info("更当前钱包无关");
            }
        }
        return (new ResultDTO()).setResult(btcTransaction);
    }

    public ResultDTO getExplorerBlockNumber(String key) {
        int blockNumber = 0;
        String url;
        if ("btc".equalsIgnoreCase(this.mClientBean.getName())) {
            url = this.btcUrl + (StringUtils.isEmpty(key) ? "" : key);
            JSONObject lastBlockInfo = (JSONObject)HttpRequestUtil.getJson(url, JSONObject.class);
            if (null != lastBlockInfo) {
                blockNumber = lastBlockInfo.getInteger("height");
            }
        } else if ("goge".equalsIgnoreCase(this.mClientBean.getName())) {
            url = this.dogeUrl + (StringUtils.isEmpty(key) ? "" : key);
            blockNumber = (Integer)HttpRequestUtil.getJson(url, Integer.class);
        }

        return (new ResultDTO()).setResult(blockNumber);
    }

    public ResultDTO getTransactionConfirmedIn(String txid) {
        BtcTransaction transaction = (BtcTransaction)this.getTransactionIn(txid).toObj(BtcTransaction.class);
        return (new ResultDTO()).setResult(transaction.getConfirmations());
    }

    public ResultDTO getTransactionConfirmedOut(String txid) {
        BtcTransaction transaction = (BtcTransaction)this.getTransactionOut(txid).toObj(BtcTransaction.class);
        return (new ResultDTO()).setResult(transaction.getConfirmations());
    }

    public ResultDTO dumpPrivKey(String address) {
        String dumpPriKey = (String)jsonRpcClient.newInvoke(this.mClientBean, "dumpprivkey", String.class, new Object[]{address});
        return (new ResultDTO()).setResult(dumpPriKey);
    }
//    public ResultDTO getAddressDumpPrivKey(String address) {
//        String dumpPriKey = (String)jsonRpcClient.addressInvoke(this.mClientBean, "dumpprivkey", String.class, new Object[]{address});
//        return (new ResultDTO()).setResult(dumpPriKey);
//    }
   ///SGC入
    public static void main0(String[] args) {
        ClientBean clientBean = new ClientBean();
        clientBean.setRpcPwd("GskRSZ5lvtcqFoYCLXjpDb0PI6amdn");
        clientBean.setRpcUser("coinxSGCin");
        clientBean.setRpcIp("47.91.209.103");
        clientBean.setRpcPort("21523");
        clientBean.setWalletPass("Thq7En9e4vjk0kv0PHQ7mAVemWXXS5");
        clientBean.setName("btc");
        OmniNewClient omniNewClient = new OmniNewClient(clientBean);
        ResultDTO result = omniNewClient.getBlockCount();
        System.out.println(result);
        //ResultDTO address = omniNewClient.getBalance("SPnraFXBmsHq9mMhg3m2ecyXC2TpZVWg5T");

        //SkaicfjMkzfqC1Hgb9ZgmfQ4HxnCScTLPK
        //SWmJuoRxBeG9wNvJxqnV1D1rw9WNBcESeK
        //SXhZW3tigMotsgVgAHv2bwiNi1P8cGg7Xn  VGX5C3fjQaCkM5MFiT6fvDKzsqRPPTCfhvUUQBLzhvMPHPCgKLfv
        //ResultDTO address =  omniNewClient.dumpPrivKey("SPzwAZa1qkq1bDbdCcB3ZsHG6i2jPhM7Dc");//.getNewAddress();


    }
    //SGC出
    public static void main1(String[] args) {
        ClientBean clientBean = new ClientBean();
        clientBean.setRpcPwd("N2o9ZqJMb45sut7OCKRy6DXjUkLSm0");
        clientBean.setRpcUser("coinxSGCout");
        clientBean.setRpcIp("47.91.211.146");
        clientBean.setRpcPort("21523");
        clientBean.setName("btc");

        clientBean.setRpcPwdOut("N2o9ZqJMb45sut7OCKRy6DXjUkLSm0");
        clientBean.setRpcUserOut("coinxSGCout");
        clientBean.setRpcIpOut("47.91.211.146");
        clientBean.setRpcPortOut("21523");

        OmniNewClient omniNewClient = new OmniNewClient(clientBean);
        //ResultDTO result = omniNewClient.getBlockCount();
        ResultDTO result =  omniNewClient.sendNormalOut(new TxData().
                setToAddress("Sj8fXuUfmfCzcAmMsFJaptwnfvDQPWSKoQ").
                setPass("Thq7En9e4vjk0kv0PHQ7mAVemWXXS5").
                setBalance(BigDecimalUtils.formatBigDecimal(BigDecimal.valueOf(1.0))));


                //omniNewClient.validateAddress("Sj8fXuUfmfCzcAmMsFJaptwnfvDQPWSKoQ");

               // omniNewClient.getTransactionFeeIn("53a104573612b6fcdbba3cfcdb917f50e7a4f63b002aae10a7c8d279396331cd");
        System.out.println(result);
        //ResultDTO address = omniNewClient.getBalance("Sa4mmZvDWqFwBaChNcqxJnWYGVP5aQWmXH");//提币帐户
       // ResultDTO address =  omniNewClient.dumpPrivKey("SbnGNnJMku9QLMRTr98fYmKp1CLXvTDjgK");//.getNewAddress();
       // ResultDTO address =  omniNewClient.dumpPrivKey("SbnGNnJMku9QLMRTr98fYmKp1CLXvTDjgK");//.getNewAddress();
        //ResultDTO address =  omniNewClient.getBalance();//.getNewAddress();
        //ResultDTO address =  omniNewClient.getBalance("SbnGNnJMku9QLMRTr98fYmKp1CLXvTDjgK");//.getNewAddress();
        //ResultDTO o = omniNewClient.validateAddress("SRmNvech17UENNBBLsUDHhbfertVSfmyUC");
       // System.out.println(address);



        //SbnGNnJMku9QLMRTr98fYmKp1CLXvTDjgK

        //

//        ResultDTO info = omniNewClient.getInfo();
//        System.out.println(info);

//        String txid = omniNewClient.sendNormalOut(new TxData().
//                setToAddress("ShMN7jYaNieAiriRJMxM19VomiJ3b9BK6B").
//                setPass("Thq7En9e4vjk0kv0PHQ7mAVemWXXS5").
//                setBalance(BigDecimalUtils.formatBigDecimal(new BigDecimal(10)))).getResult().toString();
    }
    //倒腾btc
    public static void main(String[] args) {
        ClientBean clientBean = new ClientBean();
//        clientBean.setRpcPwd("LKYd36wVIragFxfvuU8pjnq12y");
//        clientBean.setRpcUser("coinxBitin");
//        clientBean.setRpcIp("47.91.209.102");
//        clientBean.setRpcPort("8332");
//        clientBean.setName("btc");
//
//        clientBean.setRpcPwdOut("0R6lqMPALnvF2T5x9euObUscmH");
//        clientBean.setRpcUserOut("coinxBitout");
//        clientBean.setRpcIpOut("47.52.233.141");
//        clientBean.setRpcPortOut("8332");
         ClientInfo clientInfo;
        clientBean.setRpcPwd("C916C0396DB2FD1D04979118D2FB857ECE2618FB692BB3F26260F9ED97234F9B");
        clientBean.setRpcUser("coinflyBitin");
        clientBean.setRpcIp("47.91.227.3");
        clientBean.setRpcPort("8332");
        clientBean.setName("btc");
        clientBean.setCoinType("btc");

        clientBean.setRpcPwdOut("3DE0AE132BFA56B938E4009D3730C041F98F231AA5431BD5593F383249B51FC6");
        clientBean.setRpcUserOut("coinflyBitout");
        clientBean.setRpcIpOut("47.91.227.3");
        clientBean.setRpcPortOut("8332");

        OmniNewClient omniNewClient = new OmniNewClient(clientBean);
        try{
            BigDecimal bomniNewClientalance = omniNewClient.getBalance().toBigDecimal();
            System.out.println(bomniNewClientalance);
        }catch (Exception e){
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }


//        BtcBlock btcBlock = omniNewClient.getBlockByNumber("541250").toObj(BtcBlock.class);
//        System.out.println(btcBlock.getTx().size());
//
//        for (int i = 1; i < btcBlock.getTx().size(); i++) {
//            LOG.error("查询到一条用户数据,txid是:" + btcBlock.getTx().get(i));
//            BtcTransaction btcTransaction = omniNewClient.getTransactionIn(btcBlock.getTx().get(i)).toObj(BtcTransaction.class);
//        BtcTransaction btcTransaction = omniNewClient.getTransactionOut("8dfc5191f46caef4f60022d6e676843e198ae2d9a1f62873c62bffe4c4537fea").toObj(BtcTransaction.class);
//            LOG.error("查询到一条用户数据,txid是:" + btcTransaction);
//            if (null != btcTransaction && !CollectionUtils.isEmpty(btcTransaction.getVout())) {
//                List<BtcTxOut> vout = btcTransaction.getVout();
//                for (BtcTxOut item : vout) {
//                    String address = "3ArSC8JXLJpVc8PVkgiTAwuXyfs8LJAmSy";
//                    List<String> addressesList = item.getScriptPubKey().getAddresses();
//                    for(String add:addressesList){
//                        if(address.equalsIgnoreCase(add)){
//                            System.out.println(btcTransaction.getTxid());
//                            System.out.println(item.getValue());
//                        }
//                    }
//                }
//            }
//        }




//        BigDecimal bomniNewClientalance = omniNewClient.getBalanceOut().toBigDecimal();
//        System.out.println(balance);
//        BigDecimal mum = new BigDecimal("0.3308295");//0.33130759
//        BigDecimal balance = omniNewClient.getBalance().toBigDecimal();
//        if (balance.compareTo(mum) < 0) {
//            System.out.println("余额不足");
//            return;
//        }
//        String toAddress = "3KtVQFTXgSRe48gc9Et1tJ9gSZ8sWg8big";
//        String txid = omniNewClient.sendNormalIn(new TxData().
//                setToAddress(toAddress).
//                setPass("").
//                setBalance(mum)).getResult().toString();

//        if (!StringUtils.isEmpty("8dfc5191f46caef4f60022d6e676843e198ae2d9a1f62873c62bffe4c4537fea")) {
//            BigDecimal fee = omniNewClient.getTransactionFeeIn("8dfc5191f46caef4f60022d6e676843e198ae2d9a1f62873c62bffe4c4537fea").toBigDecimal();
//            System.out.println("打币成功：txid-"+"f1568179a37e1a97ee6b72dbef8c375805cd3ddce0250e1bc839b8d7738c42aa");
//            System.out.println("打币成功：手续费-"+fee);
//        } else {
//            System.out.println("打币失败,未知异常");
//        }
    }
    //倒腾LTC
    public static void mainltc(String[] args) {
        ClientBean clientBean = new ClientBean();
        clientBean.setRpcPwd("Il6F8AC2oKsSy0Dz3mxRgVGc71");
        clientBean.setRpcUser("coinxLitein");
        clientBean.setRpcIp("47.75.146.167");
        clientBean.setRpcPort("9332");
        clientBean.setName("LTC");
        clientBean.setCoinType("btc");

        clientBean.setRpcPwdOut("v0p5HieCIgZsTkOjLmKSFVw8Q1");
        clientBean.setRpcUserOut("coinxLiteout");
        clientBean.setRpcIpOut("47.52.239.38");
        clientBean.setRpcPortOut("9332");

        OmniNewClient omniNewClient = new OmniNewClient(clientBean);
        ResultDTO result = omniNewClient.getBlockCount();
        System.out.println(result);
//        BigDecimal mum = new BigDecimal("0.3308295");//0.33130759
//        BigDecimal balance = omniNewClient.getBalance().toBigDecimal();
//        if (balance.compareTo(mum) < 0) {
//            System.out.println("余额不足");
//            return;
//        }
//        String toAddress = "3KtVQFTXgSRe48gc9Et1tJ9gSZ8sWg8big";
//        String txid = omniNewClient.sendNormalIn(new TxData().
//                setToAddress(toAddress).
//                setPass("").
//                setBalance(mum)).getResult().toString();

//        if (!StringUtils.isEmpty("f1568179a37e1a97ee6b72dbef8c375805cd3ddce0250e1bc839b8d7738c42aa")) {
//            BigDecimal fee = omniNewClient.getTransactionFeeIn("f1568179a37e1a97ee6b72dbef8c375805cd3ddce0250e1bc839b8d7738c42aa").toBigDecimal();
//            System.out.println("打币成功：txid-"+"f1568179a37e1a97ee6b72dbef8c375805cd3ddce0250e1bc839b8d7738c42aa");
//            System.out.println("打币成功：手续费-"+fee);
//        } else {
//            System.out.println("打币失败,未知异常");
//        }
    }
    //倒腾QTUM
    public static void main5(String[] args) {
        ClientBean clientBean = new ClientBean();
        clientBean.setRpcPwd("eoz16Kl23YiXEbISn0stFWhcyG");
        clientBean.setRpcUser("coinxQtumin");
        clientBean.setRpcIp("47.75.146.167");
        clientBean.setRpcPort("3889");
        clientBean.setName("btc");
        clientBean.setCoinType("btc");

        clientBean.setRpcPwdOut("ujWBtpdETCKHPa4MJNibOnZ9kR");
        clientBean.setRpcUserOut("coinxQtumout");
        clientBean.setRpcIpOut("47.52.239.38");
        clientBean.setRpcPortOut("3889");

        OmniNewClient omniNewClient = new OmniNewClient(clientBean);
        BigDecimal balance = omniNewClient.getBalanceOut().toBigDecimal();
        System.out.println(balance);
//        BigDecimal mum = new BigDecimal("0.3308295");//0.33130759
//        BigDecimal balance = omniNewClient.getBalance().toBigDecimal();
//        if (balance.compareTo(mum) < 0) {
//            System.out.println("余额不足");
//            return;
//        }
//        String toAddress = "3KtVQFTXgSRe48gc9Et1tJ9gSZ8sWg8big";
//        String txid = omniNewClient.sendNormalIn(new TxData().
//                setToAddress(toAddress).
//                setPass("").
//                setBalance(mum)).getResult().toString();

//        if (!StringUtils.isEmpty("f1568179a37e1a97ee6b72dbef8c375805cd3ddce0250e1bc839b8d7738c42aa")) {
//            BigDecimal fee = omniNewClient.getTransactionFeeIn("f1568179a37e1a97ee6b72dbef8c375805cd3ddce0250e1bc839b8d7738c42aa").toBigDecimal();
//            System.out.println("打币成功：txid-"+"f1568179a37e1a97ee6b72dbef8c375805cd3ddce0250e1bc839b8d7738c42aa");
//            System.out.println("打币成功：手续费-"+fee);
//        } else {
//            System.out.println("打币失败,未知异常");
//        }
    }
    //倒腾DASH
    public static void main6(String[] args) {
        ClientBean clientBean = new ClientBean();
        clientBean.setRpcPwd("R46mfihnwbljW3o9UOtHqJMBTV");
        clientBean.setRpcUser("coinxDashin");
        clientBean.setRpcIp("47.75.146.167");
        clientBean.setRpcPort("9998");
        clientBean.setName("btc");
        clientBean.setCoinType("btc");

        clientBean.setRpcPwdOut("nIYDbXgfJAtMzCrT3j2Ew6xq9V");
        clientBean.setRpcUserOut("coinxDashout");
        clientBean.setRpcIpOut("47.52.239.38");
        clientBean.setRpcPortOut("9998");

        OmniNewClient omniNewClient = new OmniNewClient(clientBean);
        BigDecimal balance = omniNewClient.getBalance().toBigDecimal();
        System.out.println(balance);
//        BigDecimal mum = new BigDecimal("0.3308295");//0.33130759
//        BigDecimal balance = omniNewClient.getBalance().toBigDecimal();
//        if (balance.compareTo(mum) < 0) {
//            System.out.println("余额不足");
//            return;
//        }
//        String toAddress = "3KtVQFTXgSRe48gc9Et1tJ9gSZ8sWg8big";
//        String txid = omniNewClient.sendNormalIn(new TxData().
//                setToAddress(toAddress).
//                setPass("").
//                setBalance(mum)).getResult().toString();

//        if (!StringUtils.isEmpty("f1568179a37e1a97ee6b72dbef8c375805cd3ddce0250e1bc839b8d7738c42aa")) {
//            BigDecimal fee = omniNewClient.getTransactionFeeIn("f1568179a37e1a97ee6b72dbef8c375805cd3ddce0250e1bc839b8d7738c42aa").toBigDecimal();
//            System.out.println("打币成功：txid-"+"f1568179a37e1a97ee6b72dbef8c375805cd3ddce0250e1bc839b8d7738c42aa");
//            System.out.println("打币成功：手续费-"+fee);
//        } else {
//            System.out.println("打币失败,未知异常");
//        }
    }
    //倒腾DOGE
    public static void main10(String[] args) {
        ClientBean clientBean = new ClientBean();
        clientBean.setRpcPwd("UzsmfPqkgvZt8yL1IVMeFXhb25");
        clientBean.setRpcUser("coinxDogin");
        clientBean.setRpcIp("47.75.146.167");
        clientBean.setRpcPort("22555");
        clientBean.setName("btc");
        clientBean.setCoinType("btc");

        clientBean.setRpcPwdOut("PRb8wOKi5zlCa7WrhX3yxYtj2N");
        clientBean.setRpcUserOut("coinxDogout");
        clientBean.setRpcIpOut("447.52.239.38");
        clientBean.setRpcPortOut("22555");

        OmniNewClient omniNewClient = new OmniNewClient(clientBean);
        ResultDTO resultDTO = omniNewClient.getBlockCount();
        System.out.println(resultDTO);
//        BigDecimal mum = new BigDecimal("205196.408002");//0.33130759
//        //BigDecimal balance = omniNewClient.getBalance().toBigDecimal();
//        if (balance.compareTo(mum) < 0) {
//            System.out.println("余额不足");
//            return;
//        }
//        String toAddress = "DKHCkj7sbM1KRQhSmGsbKU1aHAvXfsoCAG";
//        String txid = omniNewClient.sendNormalOut(new TxData().
//                setToAddress(toAddress).
//                setPass("").
//                setBalance(mum)).getResult().toString();
//
//        if (!StringUtils.isEmpty(txid)) {
//            BigDecimal fee = omniNewClient.getTransactionFeeIn(txid).toBigDecimal();
//            System.out.println("打币成功：txid-"+txid);
//            System.out.println("打币成功：手续费-"+fee);
//        } else {
//            System.out.println("打币失败,未知异常");
//        }
    }
    //测试usdt
    public static void mainUSDT(String[] args) {
        ClientBean clientBean = new ClientBean();
        clientBean.setRpcPwd("LKYd36wVIragFxfvuU8pjnq12y");
        clientBean.setRpcUser("coinxUSDTin");
        clientBean.setRpcIp("47.75.146.167");
        clientBean.setRpcPort("8382");
        clientBean.setName("usdt");
        clientBean.setCoinType("usdt");

        clientBean.setRpcPwdOut("LKYd36wVIragFxfvuU8pjnq12y");
        clientBean.setRpcUserOut("coinxUSDTout");
        clientBean.setRpcIpOut("47.52.239.38");
        clientBean.setRpcPortOut("8382");

        OmniNewClient omniNewClient = new OmniNewClient(clientBean);
        //BigDecimal balance = omniNewClient.getBalance().toBigDecimal();
        //System.out.println(balance);

        ResultDTO result= omniNewClient.getBalanceOut();
        //18QAN2CsXcKoKyS6vr3sQjEnZpLZUZYiN5
        //result = omniNewClient.dumpPrivKey("18QAN2CsXcKoKyS6vr3sQjEnZpLZUZYiN5");
        //L4e4RS2DwnemPcUHa2ho9zazXVcPkkBFRjys8Y2EdrWqdUfDC2z7
        System.out.println(result.getResult());


    }
    //测试HC
    public static void mainhc(String[] args) {
        ClientBean clientBean = new ClientBean();
        clientBean.setRpcPwd("Tz8IpQNHX4q7zFxOyeE9A8KRg98=");
        clientBean.setRpcUser("GCk+M3afa2hiVI++LChx7XgOeho=");
        clientBean.setRpcIp("47.75.146.167");
        clientBean.setRpcPort("14009");
        clientBean.setName("hc");
        clientBean.setCoinType("btc");

        clientBean.setRpcPwdOut("LKYd36wVIragFxfvuU8pjnq12y");
        clientBean.setRpcUserOut("coinxUSDTout");
        clientBean.setRpcIpOut("47.52.239.38");
        clientBean.setRpcPortOut("8382");

        OmniNewClient omniNewClient = new OmniNewClient(clientBean);
        //BigDecimal balance = omniNewClient.getBalance().toBigDecimal();
        //System.out.println(balance);

        ResultDTO result = omniNewClient.getInfo();
        System.out.println(result.getResult());


    }




}