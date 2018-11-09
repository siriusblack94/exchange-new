package com.clg.wallet.help;

import com.clg.infra.util.GsonUtils;
import com.clg.wallet.bean.ClientBean;
import com.clg.wallet.exception.AchainException;
import com.clg.wallet.newclient.ClientInstance;
import com.clg.wallet.wallet.Omni.JSONRPCClient;
import com.googlecode.jsonrpc4j.JsonRpcClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class JsonRpcClient {
    private static com.clg.wallet.help.JsonRpcClient client = null;

    private JsonRpcClient() {
    }

    public static com.clg.wallet.help.JsonRpcClient getInstance() {
        if (null == client) {
            Class var0 = com.clg.wallet.help.JsonRpcClient.class;
            synchronized(com.clg.wallet.help.JsonRpcClient.class) {
                if (null == client) {
                    client = new com.clg.wallet.help.JsonRpcClient();
                }
            }
        }

        return client;
    }

    public <T> T invoke(ClientBean clientBean, String method, Object params, Class<T> clazz) {
        try {
            T result = ClientInstance.getJsonRpcClient(clientBean).invoke(method, params, clazz);
            return result;
        } catch (JsonRpcClientException var6) {
            throw var6;
        } catch (Throwable var7) {
            var7.printStackTrace();
            throw new AchainException(var7.getMessage());
        }
    }

    public <T> T newInvoke(ClientBean clientBean, String method, Class<T> clazz, Object... params) {
        try {
            JSONRPCClient jsonRpcClient = ClientInstance.getNewOmni(clientBean);

            Object query;
            if (StringUtils.isEmpty(params)) {
                query = jsonRpcClient.query(method, new Object[0]);
            } else {
                query = jsonRpcClient.query(method, params);
            }

            T result = GsonUtils.convertObj(GsonUtils.toJson(query), clazz);
            return result;
        } catch (JsonRpcClientException var8) {
            throw var8;
        } catch (Throwable var9) {
            System.out.println("errorrorreweweweweew:" + clientBean.toString());
            var9.printStackTrace();
            throw new AchainException(var9.getMessage());
        }
    }

    public <T> T outInvoke(ClientBean clientBean, String method, Class<T> clazz, Object... params) {
        try {
            JSONRPCClient jsonRpcClient = ClientInstance.getOutClient(clientBean);
            Object query;
            if (StringUtils.isEmpty(params)) {
                query = jsonRpcClient.query(method, new Object[0]);
            } else {
                query = jsonRpcClient.query(method, params);
            }

            T result = GsonUtils.convertObj(GsonUtils.toJson(query), clazz);
            return result;
        } catch (JsonRpcClientException var8) {
            throw var8;
        } catch (Throwable var9) {
            System.out.println("errorrorreweweweweew:" + clientBean.toString());
            var9.printStackTrace();
            throw new AchainException(var9.getMessage());
        }
    }
}
