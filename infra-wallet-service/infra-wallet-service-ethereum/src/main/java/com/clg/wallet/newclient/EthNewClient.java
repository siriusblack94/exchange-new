package com.clg.wallet.newclient;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blockeng.wallet.config.Constant;
import com.clg.infra.util.GsonUtils;
import com.clg.infra.util.HttpRequestUtil;
import com.clg.infra.util.IntegerUtils;
import com.clg.wallet.bean.AddressBean;
import com.clg.wallet.bean.ClientBean;
import com.clg.wallet.bean.EThTransactionBean;
import com.clg.wallet.bean.EthResult;
import com.clg.wallet.bean.ResultDTO;
import com.clg.wallet.bean.TxData;
import com.clg.wallet.enums.ResultCode;
import com.clg.wallet.exception.EthSysnException;
import com.clg.wallet.help.EthToken;
import com.clg.wallet.help.Greeter;
import com.clg.wallet.help.Transfer;
import com.clg.wallet.help.WalletUtils;
import com.clg.wallet.utils.PassUtils;
import com.clg.wallet.utils.StringParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.NetPeerCount;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import org.web3j.tx.Contract;
import org.web3j.utils.Convert;
import org.web3j.utils.Files;
import org.web3j.utils.Convert.Unit;

public class EthNewClient extends NormalClient {
    private static final Logger LOG = LoggerFactory.getLogger(EthNewClient.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HashMap<String, EthToken> abiHash = new HashMap();
    private static Map<String, Integer> unitMap = new HashMap();
    private final Web3j web3j;
    public static String mKey = "R2MBYWJSW8PIESWSF78S57BNT9W8CWGJSX";
    private String ethUrl = "https://api.etherscan.io/api?module=proxy&action=eth_blockNumber&apikey=";
    private String etcUrl = "https://api.gastracker.io/v1/blocks/latest";

    public Web3j getWeb3j() {
        return this.web3j;
    }

    public EthNewClient(ClientBean clientBean) {
        super(clientBean);
        this.web3j = ClientInstance.getEthClient(this.mClientBean);
    }

    public ResultDTO getNewAddress() {
        String defaultPass = PassUtils.getPass();
        return this.getNewAddress(defaultPass);
    }

    public ResultDTO getNewAddress(String password) {
        return (new ResultDTO()).setResult(this.createAddress(password));
    }

    public static AddressBean createAddressAndSave(String password, String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }

            if (!file.exists()) {
                throw new FileNotFoundException("创建文件失败,请手工创建文件夹:" + path);
            } else {
                ECKeyPair ecKeyPair = Keys.createEcKeyPair();
                WalletFile walletFile = Wallet.createStandard(password, ecKeyPair);
                DateTimeFormatter format = DateTimeFormatter.ofPattern("'UTC--'yyyy-MM-dd'T'HH-mm-ss.nVV'--'");
                ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
                String fileName = now.format(format) + walletFile.getAddress() + ".json";
                File destination = new File(path, fileName);
                objectMapper.writeValue(destination, walletFile);
                String address = "0x" + walletFile.getAddress();
                return (new AddressBean()).setFileName(fileName).setKeystore(Files.readString(destination)).setPwd(password).setAddress(address);
            }
        } catch (Exception var10) {
            var10.printStackTrace();
            throw new EthSysnException("创建地址异常");
        }
    }

    public AddressBean createAddress(String password) {
        try {
            ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            WalletFile walletFile = Wallet.createStandard(password, ecKeyPair);
            String address = "0x" + walletFile.getAddress();
            String keystore = GsonUtils.toJson(walletFile);
            if (!StringUtils.isEmpty(address) && !StringUtils.isEmpty(keystore) && !StringUtils.isEmpty(password)) {
                AddressBean addressBean = (new AddressBean()).setKeystore(keystore).setPwd(password).setAddress(address);
                LOG.info(addressBean.toString());
                return addressBean;
            } else {
                throw new EthSysnException(StringUtils.isEmpty(address) ? "address不能为空" : (StringUtils.isEmpty(keystore) ? "keystore不能为空" : "password不能为空"));
            }
        } catch (Exception var7) {
            LOG.error("获取地址失败");
            throw new EthSysnException("获取地址失败");
        }
    }

    public ResultDTO getBalance() {
        throw new EthSysnException("请传入地址");
    }

    public ResultDTO getBalance(String address) {
        if (StringUtils.isEmpty(address)) {
            throw new EthSysnException("请传入地址");
        } else {
            BigDecimal balance = BigDecimal.ZERO;

            try {
                if (TextUtils.isEmpty(address)) {
                    throw new EthSysnException("查询余额地址不能为空");
                }

                balance = Convert.fromWei(new BigDecimal(((EthGetBalance)this.web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send()).getBalance()), Unit.ETHER);
            } catch (Exception var4) {
                LOG.error(var4.getMessage());
            }

            return (new ResultDTO()).setResult(balance);
        }
    }

    public ResultDTO getBalanceByAccount(String account) {
        return null;
    }

    public ResultDTO getTokenBalance(String assertId, String address) {
        if (TextUtils.isEmpty(address)) {
            throw new EthSysnException("查询token余额地址不能为空");
        } else {
            EthToken abi = (EthToken)abiHash.get(assertId);
            if (null == abi) {
                abi = this.getAbi(assertId);
                abiHash.put(assertId, abi);
            }

            BigDecimal balanceValue = null;

            try {
                balanceValue = new BigDecimal((BigInteger)abi.balanceOf(address).send());
                int uint = ((BigInteger)abi.decimals().send()).intValue();
                balanceValue = balanceValue.divide(BigDecimal.TEN.pow(uint));
            } catch (Exception var6) {
                var6.printStackTrace();
                throw new EthSysnException("查询异常");
            }

            return (new ResultDTO()).setResult(balanceValue);
        }
    }

    public EthResult sentEth(EThTransactionBean eThTransactionBean) throws Exception {
        if (!this.checkServer()) {
            throw new EthSysnException("服务器正在同步中,请不要进行,转账充值操作");
        } else {
            BigDecimal ethBalance = (BigDecimal)this.getBalance(eThTransactionBean.getFromAddress()).getResult();
            ethBalance = ethBalance.subtract(this.getEthFee());
            if (ethBalance.compareTo(new BigDecimal(0)) < 0) {
                return (new EthResult()).setCode(-3).setInfo("钱包账户余额为" + ethBalance.toString() + ",不能转币").setSuccess(false);
            } else if (!eThTransactionBean.isAllTransaction() && ethBalance.compareTo(eThTransactionBean.getBalance()) < 0) {
                return (new EthResult()).setInfo("打出失败,打出账户余额不足,当前账户余额+" + ethBalance.toString() + ",需要打出的币:" + eThTransactionBean.getBalance()).setCode(-3).setSuccess(false);
            } else {
                Credentials credentials = WalletUtils.loadCredentials(eThTransactionBean.getFromUserPass(), this.getKeystorePath(eThTransactionBean.getFromUserKeystore(), eThTransactionBean.getFromUserKeystorePath()));
                String txid = ((TransactionReceipt)Transfer.sendFunds(this.web3j, credentials, eThTransactionBean.getToAddress(), eThTransactionBean.isAllTransaction() ? ethBalance : eThTransactionBean.getBalance(), Unit.ETHER, eThTransactionBean.getAddPrice()).sendAsync().get()).getTransactionHash();
                if (StringUtils.isEmpty(txid)) {
                    throw new Exception("txid is empty!");
                } else {
                    return (new EthResult()).setTxid(txid).setSuccess(true).setInfo("打币成功");
                }
            }
        }
    }

    public boolean checkServer() {
        if (StringUtils.isEmpty(mKey)) {
            LOG.error("服务器正在同步中,请不要进行,转账充值操作");
            return false;
        } else {
            int explorerBlockNumber = this.getExplorerBlockNumber(mKey).toInteger();
            int blockCount = this.getBlockCount().toInteger();
            if (explorerBlockNumber - blockCount > 10) {
                LOG.error("服务器正在同步中,请不要进行,转账充值操作");
                return false;
            } else {
                return true;
            }
        }
    }

    public EthResult sentEthToken(EThTransactionBean eThTransactionBean) throws Exception {
        if (!this.checkServer()) {
            throw new EthSysnException("服务器正在同步中,请不要进行,转账充值操作");
        } else {
            EthToken abi = this.getAbi(eThTransactionBean.getContractAddress(), this.getKeystorePath(eThTransactionBean.getFromUserKeystore(), eThTransactionBean.getFromUserKeystorePath()), eThTransactionBean.getFromUserPass(), eThTransactionBean.getAddPrice());
            BigDecimal toTokenBalance = eThTransactionBean.getTokenBalance();
            BigDecimal fromTokenBalance = (BigDecimal)this.getTokenBalance(eThTransactionBean.getContractAddress(), eThTransactionBean.getFromAddress()).getResult();
            if (!eThTransactionBean.isAllTransaction() && fromTokenBalance.compareTo(toTokenBalance) < 0) {
                return (new EthResult()).setInfo("打出失败,打出账户余额不足,当前账户余额+" + fromTokenBalance.toString() + ",需要打出的币:" + eThTransactionBean.getTokenBalance()).setCode(-3).setSuccess(false);
            } else {
                BigDecimal ethBalance = (BigDecimal)this.getBalance(eThTransactionBean.getFromAddress()).getResult();
                BigDecimal tokenSpentPrice = this.getEthTokenFee();
                if (ethBalance.compareTo(tokenSpentPrice) < 0) {
                    return (new EthResult()).setCode(-2).setSuccess(false).setInfo("当前token无手续费,无法转账,请支付手续费");
                } else {
                    String txid = ((TransactionReceipt)abi.transfer(eThTransactionBean.getToAddress(), this.getRealBalance(abi, eThTransactionBean.isAllTransaction() ? fromTokenBalance : eThTransactionBean.getTokenBalance())).send()).getTransactionHash();
                    return StringUtils.isEmpty(txid) ? (new EthResult()).setTxid(txid).setSuccess(false).setInfo("打币失败") : (new EthResult()).setTxid(txid).setSuccess(true).setInfo("打币成功");
                }
            }
        }
    }

    public ResultDTO getListAddress() {
        Object accounts = new ArrayList();

        try {
            accounts = ((EthAccounts)this.web3j.ethAccounts().send()).getAccounts();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

        return (new ResultDTO()).setResult(accounts);
    }

    public ResultDTO getPeers() {
        return null;
    }

    public ResultDTO getBlockCount() {
        int count = 0;
        BigInteger latest = null;

        try {
            latest = ((EthBlockNumber)this.web3j.ethBlockNumber().send()).getBlockNumber();
            count = latest.intValue();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return (new ResultDTO()).setResult(count);
    }

    public ResultDTO getBlockHash(long blockNumber) {
        String hash = "";

        try {
            hash = ((EthBlock)this.web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(blockNumber), true).send()).getBlock().getHash();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return (new ResultDTO()).setResult(hash);
    }

    public ResultDTO getConnectionCount() {
        int count = 0;

        try {
            count = ((NetPeerCount)this.web3j.netPeerCount().send()).getQuantity().intValue();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

        return (new ResultDTO()).setResult(count);
    }

    public ResultDTO dumpPrivKey(String address) {
        return null;
    }

    public ResultDTO validateAddress(String address) {
        boolean validAddress = WalletUtils.isValidAddress(address);
        return (new ResultDTO()).setResult(validAddress);
    }

    public ResultDTO sendNormal(TxData tx) {
        return null;
    }

    public ResultDTO sendNormalToken(TxData tx) {
        return null;
    }

    public ResultDTO getTxBlockNumber(String txid) {
        int blockNumber = 0;

        try {
            BigInteger search = ((Transaction)((EthTransaction)this.web3j.ethGetTransactionByHash(txid).send()).getResult()).getBlockNumber();
            blockNumber = search.intValue();
        } catch (Exception var4) {
            LOG.error(var4.getMessage());
        }

        return (new ResultDTO()).setResult(blockNumber);
    }

    public ResultDTO getBlockByNumber(String txIdOrNumber) {
        return this.getBlockByNumber(txIdOrNumber, true);
    }

    public ResultDTO getBlockByNumber(String txIdOrNumber, boolean isFull) {
        Block block = null;

        try {
            if (IntegerUtils.isInt(txIdOrNumber)) {
                block = ((EthBlock)this.web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber((long)Integer.valueOf(txIdOrNumber)), isFull).send()).getBlock();
            } else {
                block = ((EthBlock)this.web3j.ethGetBlockByHash(txIdOrNumber, isFull).send()).getBlock();
            }
        } catch (IOException var5) {
            var5.printStackTrace();
            throw new EthSysnException("获取区块异常");
        }

        return (new ResultDTO()).setResult(block);
    }

    public ResultDTO getAddrFromAcount(String account) {
        return null;
    }

    public boolean validateAssertId(String assertId) {
        return WalletUtils.isValidAddress(assertId);
    }

    public ResultDTO getAccountState(String address) {
        return null;
    }

    public ResultDTO getTokenDecimals(String assetId) {
        if (unitMap.containsKey(assetId)) {
            return (new ResultDTO()).setResult(unitMap.get(assetId));
        } else {
            EthToken abi = this.getAbi(assetId);
            boolean var3 = false;

            try {
                int i = ((BigInteger)abi.decimals().send()).intValue();
                unitMap.put(assetId, i);
            } catch (Exception var5) {
                var5.printStackTrace();
            }

            return (new ResultDTO()).setResult(unitMap.get(assetId));
        }
    }

    public ResultDTO getBestBlockHash() {
        BigInteger latest = null;

        try {
            latest = ((EthBlockNumber)this.web3j.ethBlockNumber().send()).getBlockNumber();
            return this.getBlockHash(latest.longValue());
        } catch (IOException var3) {
            var3.printStackTrace();
            LOG.error(var3.getMessage());
            throw new EthSysnException("获取最高的区块hash异常");
        }
    }

    public ResultDTO getPendingTx() {
        return null;
    }

    public ResultDTO getInfo() {
        try {
            return (new ResultDTO()).setResult(((Web3ClientVersion)this.web3j.web3ClientVersion().send()).getWeb3ClientVersion());
        } catch (IOException var2) {
            var2.printStackTrace();
            throw new EthSysnException("客户端连接异常");
        }
    }

    public ResultDTO getTransactionConfirmed(String txid) {
        int count = 0;

        try {
            BigInteger latest = ((EthBlockNumber)this.web3j.ethBlockNumber().send()).getBlockNumber();
            BigInteger search = ((Transaction)((EthTransaction)this.web3j.ethGetTransactionByHash(txid).send()).getResult()).getBlockNumber();
            count = latest.subtract(search).intValue();
        } catch (Exception var5) {
            LOG.error(var5.getMessage());
        }

        return (new ResultDTO()).setResult(count);
    }

    public ResultDTO getTransactionFee(String txid) {
        try {
            TransactionReceipt transactionReceipt = (TransactionReceipt)((EthGetTransactionReceipt)this.web3j.ethGetTransactionReceipt(txid).send()).getTransactionReceipt().get();
            ResultDTO resultDTO = this.isFailed(txid);
            if (resultDTO.getStatusCode() == ResultCode.SUCCESS.getCode() && !resultDTO.toBoolean()) {
                Transaction transaction = (Transaction)((EthTransaction)this.web3j.ethGetTransactionByHash(txid).send()).getTransaction().get();
                BigInteger gasPrice = transaction.getGasPrice();
                BigInteger cumulativeGasUsed = transactionReceipt.getGasUsed();
                return (new ResultDTO()).setResult((new BigDecimal(gasPrice.multiply(cumulativeGasUsed))).divide(BigDecimal.TEN.pow(18)));
            } else {
                return resultDTO.toBoolean() ? (new ResultDTO()).setStatusCode(ResultCode.TX_FAILED.getCode()) : resultDTO;
            }
        } catch (IOException var7) {
            var7.printStackTrace();
            throw new RuntimeException(var7.getMessage());
        }
    }

    public ResultDTO getTransaction(String txid) {
        try {
            return (new ResultDTO()).setResult(((EthTransaction)this.web3j.ethGetTransactionByHash(txid).send()).getTransaction().get());
        } catch (IOException var3) {
            var3.printStackTrace();
            throw new EthSysnException("获取交易信息异常");
        }
    }

    public Integer getTransactionCount(String address, DefaultBlockParameterName type) {
        int transactionCount = -1;

        try {
            EthGetTransactionCount send = (EthGetTransactionCount)this.web3j.ethGetTransactionCount(address, type).send();
            if (null != send && !StringUtils.isEmpty(send.getResult())) {
                transactionCount = send.getTransactionCount().intValue();
            }
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return transactionCount;
    }

    public EthToken getAbi(String tokenAddress) {
        return this.getAbi(tokenAddress, (String)null, (String)null, 0L);
    }

    public EthToken getAbi(String tokenAddress, String key, String pass, Long priceNum) {
        if (StringUtils.isEmpty(tokenAddress)) {
            return null;
        } else {
            try {
                Credentials credentials = null;
                if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(pass)) {
                    credentials = WalletUtils.loadCredentials(pass, key);
                } else {
                    credentials = WalletUtils.loadCredentials("a50509a11524842229797", "{\"address\":\"9d6383c04ee6405ef3de27809cd3eb7d5b75101e\",\"id\":\"6a00ab41-c546-43b9-ab45-50d287fd8694\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"5121766623525cf23bd02f8b6d1d92f356fd2d91a9d5659ae9f4fde94b8ef4f4\",\"cipherparams\":{\"iv\":\"0d21b28a23ed34c2d5c49db1ce162130\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"3b1d97ccccb6ebb7e9ca697e665ab182cab9f32550f936b78b74b79803225f82\"},\"mac\":\"5208678e2a05bd19d133d19678c8402e180ae20e4db80e2d5f4eadb33279c941\"}}");
                }

                BigInteger gasPrice = this.getBigPrice();
                if (null != priceNum && priceNum > 0L) {
                    gasPrice = gasPrice.add(BigInteger.valueOf(priceNum));
                }

                EthToken abi = EthToken.load(tokenAddress, this.web3j, credentials, gasPrice, BigInteger.valueOf(120000L));
                return abi;
            } catch (Exception var8) {
                var8.printStackTrace();
                return null;
            }
        }
    }

    public BigInteger getGasPrice() {
        try {
            return ((EthGasPrice)this.web3j.ethGasPrice().send()).getGasPrice();
        } catch (IOException var2) {
            var2.printStackTrace();
            return BigInteger.valueOf(35000000000L);
        }
    }

    public ResultDTO isFailed(String txid) {
        try {
            boolean failed = true;
            TransactionReceipt transactionReceipt = (TransactionReceipt)((EthGetTransactionReceipt)this.web3j.ethGetTransactionReceipt(txid).send()).getTransactionReceipt().get();
            if (null != transactionReceipt && "0x1".equalsIgnoreCase(transactionReceipt.getStatus())) {
                if (StringUtils.isEmpty(transactionReceipt.getBlockNumber())) {
                    return (new ResultDTO()).setStatusCode(ResultCode.TX_BLOCKING.getCode());
                }

                if (StringUtils.isEmpty(transactionReceipt.getFrom())) {
                    failed = CollectionUtils.isEmpty(transactionReceipt.getLogs());
                } else {
                    failed = false;
                }
            }

            return (new ResultDTO()).setResult(failed);
        } catch (IOException var4) {
            var4.printStackTrace();
            throw new RuntimeException(var4.getMessage());
        }
    }

    public BigDecimal getEthFee() {
        try {
            BigInteger gasPrice = this.getBigPrice();
            return (new BigDecimal(gasPrice.multiply(BigInteger.valueOf(23000L)))).divide(BigDecimal.TEN.pow(18));
        } catch (Exception var2) {
            var2.printStackTrace();
            return new BigDecimal(0.003D);
        }
    }

    public BigDecimal getEthTokenFee() {
        BigInteger gasPrice = this.getBigPrice();
        BigDecimal spentEth = (new BigDecimal(gasPrice.multiply(BigInteger.valueOf(120000L)))).divide(BigDecimal.TEN.pow(18));
        return spentEth;
    }

    private BigInteger getBigPrice() {
        BigInteger gasPrice = this.getGasPrice();
        if (gasPrice.compareTo(Contract.GAS_PRICE) < 0) {
            gasPrice = Contract.GAS_PRICE;
        }

        return gasPrice.add(BigInteger.valueOf(5000000000L));
    }

    private BigInteger getRealBalance(EthToken abi, BigDecimal balance) throws Exception {
        int uint = ((BigInteger)abi.decimals().send()).intValue();
        return balance.multiply(BigDecimal.TEN.pow(uint)).toBigInteger();
    }

    public String getKeystorePath(String fromUserKeystore, String fromUserKeystorePath) {
        if (StringParser.isJson(fromUserKeystore)) {
            return fromUserKeystore;
        } else if (!StringUtils.isEmpty(fromUserKeystorePath)) {
            return fromUserKeystorePath + fromUserKeystore;
        } else {
            String type = this.mClientBean.getCoinType();
            type = type + "ereum";
            String mainnetKeyDirectory = WalletUtils.getMainnetKeyDirectory(type);
            return mainnetKeyDirectory + fromUserKeystore;
        }
    }

    public String getKeystorePath(String keystoreName) {
        return this.getKeystorePath(keystoreName, "");
    }

    public ResultDTO getExplorerBlockNumber(String key) {
        if ("etc".equalsIgnoreCase(this.mClientBean.getCoinType())) {
            JSONObject txData = (JSONObject)HttpRequestUtil.getJson(this.etcUrl, JSONObject.class);
            if (null != txData) {
                JSONArray items = txData.getJSONArray("items");
                if (null != items && items.size() > 0) {
                    JSONObject itemData = items.getJSONObject(0);
                    Integer blockCount = itemData.getInteger("height");
                    if (null != blockCount) {
                        return (new ResultDTO()).setResult(blockCount);
                    }
                }
            }
        } else if ("eth".equalsIgnoreCase(this.mClientBean.getCoinType())) {
            mKey = key;
            com.clg.wallet.bean.EthBlockNumber ethBlockNumber = (com.clg.wallet.bean.EthBlockNumber)HttpRequestUtil.getJson(this.ethUrl + key, com.clg.wallet.bean.EthBlockNumber.class);
            if (null != ethBlockNumber) {
                String result = ethBlockNumber.getResult();
                int blockCount = Integer.parseInt(result.substring(2), 16);
                return (new ResultDTO()).setResult(blockCount);
            }
        }

        return (new ResultDTO()).setResult(0).setStatusCode(ResultCode.EXPLORER_ERROR.getCode());
    }

    public ResultDTO sendContract(String keystore, String pass, BigInteger initialSupply, String tokenName, String tokenSymbol) {
        Credentials credentials = null;

        try {
            credentials = WalletUtils.loadCredentials(pass, keystore);
            System.out.println(credentials);
        } catch (Exception var10) {
            var10.printStackTrace();
            return (new ResultDTO()).setStatusCode(ResultCode.CREATE_CREDENTIALS_ERROR.getCode());
        }

        try {
            Greeter greeter = (Greeter)Greeter.deploy(this.web3j, credentials, this.getBigPrice(), Contract.GAS_LIMIT, initialSupply, tokenName, tokenSymbol).send();
            String contractAddress = greeter.getContractAddress();
            return (new ResultDTO()).setResult(contractAddress);
        } catch (Exception var9) {
            var9.printStackTrace();
            return (new ResultDTO()).setStatusCode(ResultCode.CREATE_CONTRACT_TIME_OUT.getCode());
        }
    }

//  转eth老地址
    public static void main1(String[] args) {
        ClientBean clientBean = (new ClientBean()).setRpcPort("9898").setRpcIp("47.75.146.167").setCoinType("ethToken");//eth
        EthNewClient client = (EthNewClient)ClientFactory.getClient(clientBean);
//        System.out.println(client.getBalance("0x562da9cf7abf8d8b380f3b7292da8b26b39ef93e"));
        System.out.println(client.getTokenBalance("0x331a244a58cfc1356d94c5fdf09f349cc87de195","0x562da9cf7abf8d8b380f3b7292da8b26b39ef93e"));

        BigDecimal ethBalance = client.getBalance("0x562da9cf7abf8d8b380f3b7292da8b26b39ef93e").toBigDecimal();
        BigDecimal mum = new BigDecimal("20.02");

        String formAddress = "0x562da9cf7abf8d8b380f3b7292da8b26b39ef93e";
        String keypwd = "56c526871fe41730460462e72ba7886c";
        String token = "0x58b6a8a3302369daec383334672404ee733ab239";
        String keyStore = "{\"address\":\"562da9cf7abf8d8b380f3b7292da8b26b39ef93e\",\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"bfd6bcfbe3ef2710464eadbd127a276eb625593f2d8fbd7e271ac7e9034bb622\",\"cipherparams\":{\"iv\":\"dc18488d79f79103dee942732882b07f\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"1ed6696cf72be9ebbaa10521d37ea125c8bb0a62c61a3d156387b635efb5999b\"},\"mac\":\"2c0bb1f444f82839ff1b228edd9b1ee48f6b701f64168b982ea24d0d373fee6a\"},\"id\":\"50346d5e-2a2d-4638-a0df-5bdea414247f\",\"version\":3}";
        String toAddress = "0x032aaF1509cDCFb963d9b9dfa3854EAf80eE5598";
        EThTransactionBean eThTransactionBean = new EThTransactionBean();
        eThTransactionBean
                .setFromAddress(formAddress)
                .setFromUserKeystore(keyStore)
                .setFromUserPass(keypwd)
                .setToAddress(toAddress)
                .setBalance(mum);
//                .setTokenBalance(mum)
//                .setContractAddress(token);

        EthResult ethResult = null;
//        BigDecimal tokenBalance = client.getTokenBalance(eThTransactionBean.getContractAddress(), eThTransactionBean.getFromAddress()).toBigDecimal();
        BigDecimal balance = client.getBalance(eThTransactionBean.getFromAddress()).toBigDecimal();
        try {
            if(balance.compareTo(mum)>=0){
                //ethResult = client.sentEthToken(eThTransactionBean);
                ethResult = client.sentEth(eThTransactionBean);
                System.out.println(ethResult.getTxid());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    //
    public static void main122(String[] args) {
        ClientBean clientBean = (new ClientBean()).setRpcPort("9898").setRpcIp("47.52.239.38").setCoinType("eth");//eth
        EthNewClient client = (EthNewClient)ClientFactory.getClient(clientBean);
//        System.out.println(client.getBalance("0x562da9cf7abf8d8b380f3b7292da8b26b39ef93e"));
        System.out.println(client.getBalance("0x562da9cf7abf8d8b380f3b7292da8b26b39ef93e"));
        BigDecimal mum = new BigDecimal("59.0797");
        String formAddress = "0x562da9cf7abf8d8b380f3b7292da8b26b39ef93e";
        String keypwd = "0x5ca9a71b1d01849c0a95490cc00559717fcf0d1d";
        String keyStore = "{\"address\":\"562da9cf7abf8d8b380f3b7292da8b26b39ef93e\",\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"bfd6bcfbe3ef2710464eadbd127a276eb625593f2d8fbd7e271ac7e9034bb622\",\"cipherparams\":{\"iv\":\"dc18488d79f79103dee942732882b07f\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"1ed6696cf72be9ebbaa10521d37ea125c8bb0a62c61a3d156387b635efb5999b\"},\"mac\":\"2c0bb1f444f82839ff1b228edd9b1ee48f6b701f64168b982ea24d0d373fee6a\"},\"id\":\"50346d5e-2a2d-4638-a0df-5bdea414247f\",\"version\":3}";
        String toAddress = "0x032aaF1509cDCFb963d9b9dfa3854EAf80eE5598";

        EThTransactionBean eThTransactionBean = new EThTransactionBean();
        eThTransactionBean
                .setFromAddress(formAddress)
                .setFromUserKeystore(keyStore)
                .setFromUserPass(keypwd)
                .setToAddress(toAddress)
                .setBalance(new BigDecimal("0"))
                .setTokenBalance(mum)
                .setContractAddress(null);

        EthResult ethResult = null;
        BigDecimal tokenBalance = client.getBalance(eThTransactionBean.getFromAddress()).toBigDecimal();
        try {
            if(tokenBalance.compareTo(mum)>=0){
                ethResult = client.sentEth(eThTransactionBean);
                //ethResult = client.sentEth(eThTransactionBean);
                System.out.println(ethResult.getTxid());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    //teken余额 0xd4de05944572d142fbf70f3f010891a35ac15188
    public static void mainyue(String[] args) {
        ClientBean clientBean = (new ClientBean()).setRpcPort("9898").setRpcIp("47.52.239.38").setCoinType("eth");//eth
        EthNewClient client = (EthNewClient)ClientFactory.getClient(clientBean);
        File file = new File("/Users/songhaichao/Documents/JKL/环境/Coinx/老环境备份/keystore2");
        String token = "0xd4de05944572d142fbf70f3f010891a35ac15188";
//        System.out.println(client.getTokenBalance(token,"0x562da9cf7abf8d8b380f3b7292da8b26b39ef93e"));
        BigDecimal balance = BigDecimal.ZERO;
        if(file.exists()&&file.isDirectory()){
            File fileNames[] = file.listFiles();
            for(File nodefile :fileNames){
                if(nodefile.getName().equals(".DS_Store")){
                    continue;
                }
               //String name = "UTC--2018-08-08T12-06-07.797148890Z--7b4ad8385c9aa4b6d8f0b9addfb310b57a8a4530";
               String name = nodefile.getName();
                System.out.println(name);
                name =name.substring(37,name.length());
                name = "0x"+name;
                System.out.println(name);
                balance = balance.add(new BigDecimal(client.getTokenBalance(token,name).getResult().toString()));
            }
            System.out.println(balance);
        }
        //System.out.println(client.getBalance("0x562da9cf7abf8d8b380f3b7292da8b26b39ef93e"));


    }
    //eth余额
    public static void mainsingereth(String[] args) {
        ClientBean clientBean = (new ClientBean()).setRpcPort("9898").setRpcIp("47.75.146.167").setCoinType("eth");//eth
        EthNewClient client = (EthNewClient)ClientFactory.getClient(clientBean);
        File file = new File("/Users/songhaichao/Documents/JKL/环境/Coinx/老环境备份/keystore");
//        System.out.println(client.getTokenBalance(token,"0x562da9cf7abf8d8b380f3b7292da8b26b39ef93e"));

        BigDecimal balance = BigDecimal.ZERO;
        if(file.exists()&&file.isDirectory()){
            File fileNames[] = file.listFiles();
            for(File nodefile :fileNames){
                if(nodefile.getName().equals(".DS_Store")){
                    continue;
                }
                //String name = "UTC--2018-08-08T12-06-07.797148890Z--7b4ad8385c9aa4b6d8f0b9addfb310b57a8a4530";
                String name = nodefile.getName();
                name =name.substring(37,name.length());
                name = "0x"+name;
                String fromaddress = "0xb27bad26e203cee5308d15ac7f7f911a67b3bafa";
                if(name.equalsIgnoreCase(fromaddress)){
                    System.out.println(name);
                    balance=new BigDecimal(client.getBalance(name).getResult().toString());

                    System.out.println(balance);

                    String toAddress = "0x032aaF1509cDCFb963d9b9dfa3854EAf80eE5598";
                    EThTransactionBean eThTransactionBean = new EThTransactionBean();
                    String keyStore = "";
                    String keyPassword = "YuCX3b";//"MiUTAo";//


                    try{
                        BufferedReader br = new BufferedReader(new FileReader(nodefile));//构造一个BufferedReader类来读取文件
                        String s = null;
                        while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                            if(s==""||s.length()==0) {
                                continue;
                            }else {
                                System.out.println("---"+s);
                                keyStore = s;
                                break;
                            }
                        }
                        br.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    BigDecimal mum = new BigDecimal("8.998812");
                    eThTransactionBean
                            .setFromAddress(fromaddress)
                            .setFromUserKeystore(keyStore)
                            .setFromUserPass(keyPassword)
                            .setToAddress(toAddress)
                            .setBalance(mum);

                    EthResult ethResult = null;

                    try {
                        if(balance.compareTo(mum)>=0){
                            //ethResult = client.sentEthToken(eThTransactionBean);
                            ethResult = client.sentEth(eThTransactionBean);
                            System.out.println(ethResult);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        //System.out.println(client.getBalance("0x562da9cf7abf8d8b380f3b7292da8b26b39ef93e"));


    }

    public static void main(String[] args) {
        ClientBean clientBean = (new ClientBean()).setRpcPort("9898").setRpcIp("47.91.227.3").setCoinType("eth");//eth
        EthNewClient client = (EthNewClient)ClientFactory.getClient(clientBean);
        File file = new File("D:/ccc.csv");
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行

                if(s==""||s.length()==0) {
                    continue;
                }else {
                    System.out.println(s);
                    BigDecimal balance = client.getTokenBalance("0xc5ec5bfe9275715f6de105c3c249f9ba7beafd91", s).toBigDecimal();
                    if (balance!=null&&balance.compareTo(BigDecimal.ZERO)>0)
                        write(s);
//            	 if(updateSql.indexOf("0E-20")>-1){
//
//				 }else{
//					 write(updateSql);
//				 }
                }
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public static void write(String text) {
        FileWriter fw = null;
        try {
            // 如果文件存在，则追加内容；如果文件不存在，则创建文件
            //File f = new File("C:\\Users\\EDZ\\Desktop\\bxx生产\\eth3.sql");
            File f = new File("D:/ddd.txt");
            fw = new FileWriter(f, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fw);
        pw.println(text);
        pw.flush();
        try {
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

