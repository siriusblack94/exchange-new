package com.clg.wallet.wallet.Omni;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;


import com.blockeng.wallet.utils.Base64;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

public class CoinUtils {

    public static String RPC_USER = "";
    public static String RPC_PASSWORD = "";
    public static String RPC_ALLOWIP = "";
    public static String RPC_PORT = "";
    public static String WALLET_PWD = "";


    private static CoinUtils instance;
    private static void init(String RPC_USER,String RPC_PASSWORD,String RPC_ALLOWIP,String RPC_PORT,String WALLET_PWD) throws Throwable {
        if(null == instance){
            instance = new CoinUtils(RPC_USER,RPC_PASSWORD,RPC_ALLOWIP,RPC_PORT,WALLET_PWD);
        }
    }

    private JsonRpcHttpClient client;
    public CoinUtils(String RPC_USER,String RPC_PASSWORD,String RPC_ALLOWIP,String RPC_PORT,String WALLET_PWD) throws Throwable{
        this.RPC_USER = RPC_USER;
        this.RPC_PASSWORD = RPC_PASSWORD;
        this.RPC_ALLOWIP = RPC_ALLOWIP;
        this.RPC_PORT = RPC_PORT;
        this.WALLET_PWD = WALLET_PWD;
        // 身份认证
        String cred = Base64.encode(this.RPC_USER + ":" + this.RPC_PASSWORD);
        Map<String, String> headers = new HashMap<String, String>(1);
        headers.put("Authorization", "Basic " + cred);
        client = new JsonRpcHttpClient(new URL("http://"+this.RPC_ALLOWIP+":"+this.RPC_PORT), headers);
        System.out.println(client);
    }


    public static CoinUtils getInstance(String RPC_USER,String RPC_PASSWORD,String RPC_ALLOWIP,String RPC_PORT,String WALLET_PWD) throws Throwable {
        init(RPC_USER,RPC_PASSWORD,RPC_ALLOWIP,RPC_PORT,WALLET_PWD);
        return instance;
    }


    /**
     * 验证地址是否存在
     * @param address
     * @return
     * @throws Throwable
     */
    public String validateaddress(String address)throws Throwable{
        return  (String) client.invoke("validateaddress", new Object[] {address}, Object.class).toString();
    }


    /**
     * 如果钱包加密需要临时解锁钱包
     * @param password
     * @param time
     * @return
     * @throws Throwable
     */
    public String walletpassphase(String password,int time)throws Throwable{
        return  (String) client.invoke("walletpassphase", new Object[] {password,time}, Object.class).toString();
    }

    /**
     * 转账到制定的账户中
     * @param address
     * @param amount
     * @return
     * @throws Throwable
     */
    public String sendtoaddress(String address,double amount)throws Throwable{
        return  (String) client.invoke("sendtoaddress", new Object[] {address,amount}, Object.class).toString();
    }

    /**
     * 根据区块号获取区块内容
     * @param blockNumber
     * @return
     * @throws Throwable
     */
    public String getblockhash(int blockNumber )throws Throwable{
        return  (String) client.invoke("getblockhash", new Object[] {blockNumber}, Object.class).toString();
    }

    /**
     * 根据txid获取交易内容
     * @param txid
     * @return
     * @throws Throwable
     */
    public String getrawtransaction(String txid )throws Throwable{
        return  (String) client.invoke("getrawtransaction", new Object[] {txid,1}, Object.class).toString();
    }

    /**
     * 根据txid获取交易内容
     * @param txid
     * @return
     * @throws Throwable
     */
    public String gettransaction(String txid )throws Throwable{
        return  (String) client.invoke("gettransaction", new Object[] {txid}, Object.class).toString();
    }

    /**
     * 查询账户下的交易记录
     * @param account
     * @param count
     * @param offset
     * @return
     * @throws Throwable
     */
    public String listtransactions(String account, int count ,int offset )throws Throwable{
        return  (String) client.invoke("listtransactions", new Object[] {account,count,offset}, Object.class).toString();
    }

    /**
     * 获取地址下未花费的币量
     * @param account
     * @param count
     * @param offset
     * @return
     * @throws Throwable
     */
    public String listunspent( int minconf ,int maxconf ,String address)throws Throwable{
        String[] addresss= new String[]{address};
        return  (String) client.invoke("listunspent", new Object[] {minconf,maxconf,addresss}, Object.class).toString();
    }

    /**
     * 生成新的接收地址
     * @return
     * @throws Throwable
     */
    public String getNewaddress() throws Throwable{
        return  (String) client.invoke("getnewaddress", new Object[] {}, Object.class).toString();
    }

    /**
     * 获取钱包信息
     * @return
     * @throws Throwable
     */
    public String getInfo() throws Throwable{
        return  client.invoke("getinfo", new Object[] {}, Object.class).toString();
    }

     public static void main(String args[]){
         /**
          * ClientBean clientBean = new ClientBean();
          *         clientBean.setRpcPwd("Tz8IpQNHX4q7zFxOyeE9A8KRg98=");
          *         clientBean.setRpcUser("GCk+M3afa2hiVI++LChx7XgOeho=");
          *         clientBean.setRpcIp("47.75.146.167");
          *         clientBean.setRpcPort("14009");
          *         clientBean.setName("hc");
          *         clientBean.setCoinType("btc");
          */
         try{
             CoinUtils cu = CoinUtils.getInstance("GCk+M3afa2hiVI++LChx7XgOeho=","Tz8IpQNHX4q7zFxOyeE9A8KRg98=","47.75.146.167","14009","");
             System.out.println(cu.getInfo());
         }catch (Exception e){
             e.printStackTrace();
         } catch (Throwable throwable) {
             throwable.printStackTrace();
         }

     }
}
