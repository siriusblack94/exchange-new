package com.clg.wallet.newclient;

import com.clg.wallet.bean.ClientBean;
import com.clg.wallet.exception.AchainException;
import com.clg.wallet.utils.TaskStringUtils;
import com.clg.wallet.wallet.Omni.JSONRPCClient;
import com.clg.wallet.wallet.bitcoin.Bitcoin;
import com.clg.wallet.wallet.bitcoin.BitcoinJSONRPCClient;
import com.clg.wallet.wallet.bitcoin.OmniCoreJSONRPCClient;
import com.clg.wallet.wallet.bitcoin.Bitcoin.Info;
import com.clg.wallet.wallet.bitcoin.OmniCore.OmniInfo;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.http.HttpService;

public class ClientInstance {
    private static final Logger LOG = LoggerFactory.getLogger(ClientInstance.class);
    private static HashMap<String, Object> clientMap = new HashMap();

    public ClientInstance() {
    }

    private static Object getClient(String rpcIp, String rpcPort) {
        return clientMap.get(rpcIp + rpcPort);
    }

    private static void setClient(String rpcIp, String rpcPort, Object obj) {
        clientMap.put(rpcIp + rpcPort, obj);
    }

    public static Web3j getEthClient(ClientBean coin) {
        if (null != coin) {
            String rpcIP = coin.getRpcIp();
            String rpcPort = coin.getRpcPort();
            Web3j web3j = (Web3j)getClient(rpcIP, rpcPort);
            if (web3j != null) {
                return web3j;
            } else {
                StringBuffer rpcUrl = new StringBuffer("http://");
                rpcUrl.append(rpcIP).append(":").append(rpcPort).append("/");
                 web3j = new JsonRpc2_0Web3j(new HttpService(rpcUrl.toString()));
                setClient(rpcIP, rpcPort, web3j);
                return web3j;
            }
        } else {
            LOG.info("eth钱包初始参数不能为空");
            throw new IllegalArgumentException("RPC no setting");
        }
    }

    public static JsonRpcHttpClient getJsonRpcClient(final ClientBean coin) {
        try {
            String rpcIP = coin.getRpcIp();
            String rpcPort = coin.getRpcPort();
            JsonRpcHttpClient client = (JsonRpcHttpClient)getClient(rpcIP, rpcPort);
            if (null != client) {
                return client;
            } else {
                if (!StringUtils.isEmpty(coin.getRpcUser()) && !StringUtils.isEmpty(coin.getRpcPwd())) {
                    Authenticator.setDefault(new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(coin.getRpcUser(), coin.getRpcPwd().toCharArray());
                        }
                    });
                }

                StringBuffer urlValue = new StringBuffer(200);
                urlValue.append("http://");
                urlValue.append(rpcIP);
                urlValue.append(":");
                urlValue.append(rpcPort);
                if (!StringUtils.isEmpty(coin.getContext())) {
                    urlValue.append("/").append(coin.getContext());
                }

                URL url = new URL(urlValue.toString());
                JsonRpcHttpClient jsonRpcHttpClient = new JsonRpcHttpClient(url);
                setClient(coin.getRpcIp(), coin.getRpcPort(), jsonRpcHttpClient);
                return jsonRpcHttpClient;
            }
        } catch (MalformedURLException var7) {
            var7.printStackTrace();
            throw new AchainException(var7.getMessage());
        }
    }

    public static JSONRPCClient getNewOmni(ClientBean coin) throws Exception {
        if (null != coin) {
            JSONRPCClient bitcoin = (JSONRPCClient)getClient(coin.getRpcIp(), coin.getRpcPort());
            if (null != bitcoin) {
                return bitcoin;
            } else if (!TaskStringUtils.isAllEmpty(new String[]{coin.getRpcUser(), coin.getRpcPwd(), coin.getRpcIp(), coin.getRpcPort()})) {
                String rpcUrl = "http://" + coin.getRpcUser() + ":" + coin.getRpcPwd() + "@" + coin.getRpcIp() + ":" + coin.getRpcPort();

                try {
                    return new JSONRPCClient(new URL(rpcUrl));
                } catch (MalformedURLException var5) {
                    throw new Exception(var5);
                }
            } else {
                LOG.info("钱包初始化参数错误：" + coin.getName());
                throw new IllegalArgumentException("RPC no setting");
            }
        } else {
            LOG.info("btc钱包初始参数不能为空");
            throw new IllegalArgumentException("RPC no setting");
        }
    }

    public static JSONRPCClient getOutClient(ClientBean coin) throws Exception {
        if (null != coin) {
            JSONRPCClient bitcoin = (JSONRPCClient)getClient(coin.getRpcIpOut(),coin.getRpcPortOut());
            if (null != bitcoin) {
                return bitcoin;
            } else if (!TaskStringUtils.isAllEmpty(new String[]{coin.getRpcUserOut(), coin.getRpcPwdOut(), coin.getRpcIpOut(), coin.getRpcPortOut()})) {
                String rpcUrl = "http://" + coin.getRpcUserOut() + ":" + coin.getRpcPwdOut() + "@" + coin.getRpcIpOut() + ":" + coin.getRpcPortOut();
                try {
                    return new JSONRPCClient(new URL(rpcUrl));
                } catch (MalformedURLException var5) {
                    throw new Exception(var5);
                }
            } else {
                LOG.info("钱包地址服务器初始化参数错误：" + coin.getName());
                throw new IllegalArgumentException("RPC no setting");
            }
        } else {
            LOG.info("钱包地址服务器初始化参数不能为空");
            throw new IllegalArgumentException("RPC no setting");
        }
    }

    public static Bitcoin getBitCoinClient(ClientBean coin) throws Exception {
        if (null != coin) {
            Bitcoin bitcoin = (Bitcoin)getClient(coin.getRpcIp(), coin.getRpcPort());
            if (null != bitcoin) {
                return bitcoin;
            } else if (!TaskStringUtils.isAllEmpty(new String[]{coin.getRpcUser(), coin.getRpcPwd(), coin.getRpcIp(), coin.getRpcPort()})) {
                String rpcUrl = "http://" + coin.getRpcUser() + ":" + coin.getRpcPwd() + "@" + coin.getRpcIp() + ":" + coin.getRpcPort();

                BitcoinJSONRPCClient client;
                try {
                    client = new BitcoinJSONRPCClient(new URL(rpcUrl));
                } catch (MalformedURLException var7) {
                    throw new Exception(var7);
                }

                Info info;
                try {
                    info = client.getNetworkinfo();
                } catch (Exception var6) {
                    info = client.getInfo();
                }

                if (null != info && StringUtils.isEmpty(info.errors())) {
                    LOG.info("钱包初始化结束：" + coin.getName());
                    setClient(coin.getRpcIp(), coin.getRpcPort(), client);
                    return client;
                } else {
                    LOG.info("钱包初始化异常：" + coin.getName());
                    throw new NullPointerException("Get CoinClient Error");
                }
            } else {
                LOG.info("钱包初始化参数错误：" + coin.getName());
                throw new IllegalArgumentException("RPC no setting");
            }
        } else {
            LOG.info("btc钱包初始参数不能为空");
            throw new IllegalArgumentException("RPC no setting");
        }
    }

    public static OmniCoreJSONRPCClient getUsdtClient(ClientBean coin) throws Exception {
        if (null != coin) {
            OmniCoreJSONRPCClient bitcoin = (OmniCoreJSONRPCClient)getClient(coin.getRpcIp(), coin.getRpcPort());
            if (null != bitcoin) {
                return bitcoin;
            } else if (!TaskStringUtils.isAllEmpty(new String[]{coin.getRpcUser(), coin.getRpcPwd(), coin.getRpcIp(), coin.getRpcPort()})) {
                String rpcUrl = "http://" + coin.getRpcUser() + ":" + coin.getRpcPwd() + "@" + coin.getRpcIp() + ":" + coin.getRpcPort();

                OmniCoreJSONRPCClient client;
                try {
                    client = new OmniCoreJSONRPCClient(new URL(rpcUrl));
                } catch (MalformedURLException var5) {
                    throw new Exception(var5);
                }

                OmniInfo info = client.omni_getinfo();
                if (null != info) {
                    LOG.info("钱包初始化结束：" + coin.getName());
                    setClient(coin.getRpcIp(), coin.getRpcPort(), client);
                    return client;
                } else {
                    LOG.info("钱包初始化异常：" + coin.getName());
                    throw new NullPointerException("Get CoinClient Error");
                }
            } else {
                LOG.info("钱包初始化参数错误：" + coin.getName());
                throw new IllegalArgumentException("RPC no setting");
            }
        } else {
            LOG.info("usdt钱包初始参数不能为空");
            throw new IllegalArgumentException("RPC no setting");
        }
    }
}
