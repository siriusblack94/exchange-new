package com.blockeng.wallet.ethereum.web;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.blockeng.wallet.entity.CoinConfig;
import com.blockeng.wallet.entity.UserAddress;
import com.blockeng.wallet.service.CoinConfigService;
import com.blockeng.wallet.service.UserAddressService;
import com.clg.wallet.bean.ClientBean;
import com.clg.wallet.newclient.ClientFactory;
import com.clg.wallet.newclient.EthNewClient;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: sirius
 * @Date: 2018/9/27 16:34
 * @Description:
 */
@RestController
@RequestMapping("/info")
@Slf4j
@Api(value = "钱包查询", description = "钱包查询")
public class WalletController {

    @Autowired
    private  CoinConfigService  coinConfigService;

    @Autowired
    private UserAddressService userAddressService;

    @GetMapping(value = "/balance/{id}")
    public Object getBalance(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id))  return "error";
        EntityWrapper<CoinConfig> ew = new EntityWrapper<>();
        ew.eq("status", 1);
        ew.eq("id", id);
        CoinConfig coinConfig = coinConfigService.selectOne(ew);
        if (coinConfig==null)  return "error";
        EntityWrapper<UserAddress> ew1 = new EntityWrapper<>();
        ew1.eq("coin_id", id);
        List<UserAddress> userAddresses = userAddressService.selectList(ew1);
        if (userAddresses==null||userAddresses.size()==0)  return "error";
        ClientBean clientBean = new ClientBean();
        clientBean.setRpcPwd(coinConfig.getRpcPwd());
        clientBean.setRpcUser(coinConfig.getRpcUser());
        clientBean.setRpcIp(coinConfig.getRpcIp());
        clientBean.setRpcPort(coinConfig.getRpcPort());
        clientBean.setName(coinConfig.getName());
        clientBean.setCoinType(coinConfig.getCoinType());

        clientBean.setRpcPwdOut(coinConfig.getRpcPwdOut());
        clientBean.setRpcUserOut(coinConfig.getRpcUserOut());
        clientBean.setRpcIpOut(coinConfig.getRpcIpOut());
        clientBean.setRpcPortOut(coinConfig.getRpcPortOut());

        EthNewClient client = (EthNewClient)ClientFactory.getClient(clientBean);

        BigDecimal balanceTotal = BigDecimal.ZERO;
        int count = 0;
        Map<String,Object> map = new HashMap<>();

        if ("ETH".equals(coinConfig.getName())) {
            for (UserAddress userAddress : userAddresses) {
                BigDecimal balance = client.getBalance(userAddress.getAddress()).toBigDecimal();
                if (balance.compareTo(BigDecimal.ZERO)>0) count++;
                balanceTotal= balanceTotal.add(balance);
            }
        }else {
            for (UserAddress userAddress : userAddresses) {
                BigDecimal balance = client.getTokenBalance(coinConfig.getContractAddress(), userAddress.getAddress()).toBigDecimal();
                if (balance.compareTo(BigDecimal.ZERO)>0) count++;
                balanceTotal= balanceTotal.add(balance);
            }
        }

        log.info("--balanceTotal---"+balanceTotal);
        log.info("--count---"+count);
        map.put("balanceTotal",balanceTotal);
        map.put("count",count);
        map.put("name",coinConfig.getName());
        return JSON.toJSONString(map);
    }


    public static void main(String[] args) {
        ClientBean clientBean = (new ClientBean()).setRpcPort("9898").setRpcIp("47.91.227.3").setCoinType("eth");//eth
        EthNewClient client = (EthNewClient)ClientFactory.getClient(clientBean);
        File file = new File("D:/aaa.csv");
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
                File f = new File("D:/bbb.txt");
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
