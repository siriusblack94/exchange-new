package com.clg.wallet.newclient;

import com.clg.wallet.bean.ClientBean;
import com.clg.wallet.wallet.Omni.OmniNewClient;
import java.util.HashMap;

public class ClientFactory {
    static HashMap<String, Client> hashMap = new HashMap();

    public ClientFactory() {
    }

    public static Client getClient(ClientBean clientBean) {
        String coinType = clientBean.getCoinType();
        if (null != hashMap.get(coinType)) {
            return (Client)hashMap.get(coinType);
        } else {
            Client client = null;
            if (!coinType.equalsIgnoreCase("eth") && !coinType.equalsIgnoreCase("ethToken") && !coinType.equalsIgnoreCase("etc")) {
                if (!coinType.equalsIgnoreCase("act") && !coinType.equalsIgnoreCase("actToken")) {
                    if (!coinType.equalsIgnoreCase("neo") && !coinType.equalsIgnoreCase("neoToken")) {
                        if (coinType.equalsIgnoreCase("btc")) {
                            client = new OmniNewClient(clientBean);
                        } else if (coinType.equalsIgnoreCase("xrp")) {
                            client = new XrpNewClient(clientBean);
                        } else if (coinType.equalsIgnoreCase("wcg")) {
                            client = new WcgNewClient(clientBean);
                        } else {
                            if (!coinType.equalsIgnoreCase("btcToken")) {
                                throw new RuntimeException("暂时未开放改币种");
                            }

                            client = new OmniNewClient(clientBean);
                        }
                    } else {
                        client = new NeoNewClient(clientBean);
                    }
                } else {
                    client = new ActNewClient(clientBean);
                }
            } else {
                client = new EthNewClient(clientBean);
            }

            return (Client)client;
        }
    }
}