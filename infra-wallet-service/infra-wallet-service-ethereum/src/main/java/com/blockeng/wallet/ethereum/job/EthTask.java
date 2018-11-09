package com.blockeng.wallet.ethereum.job;

import com.blockeng.wallet.entity.CoinConfig;
import com.blockeng.wallet.ethereum.service.CoinEthAddressPoolService;
import com.blockeng.wallet.ethereum.service.CoinEthCollectTaskService;
import com.blockeng.wallet.ethereum.service.CoinEthRechargeService;
import com.blockeng.wallet.ethereum.service.CoinEthWithdrawService;
import com.blockeng.wallet.help.ClientInfo;
import com.blockeng.wallet.service.CommitStatusService;
import com.clg.wallet.bean.ClientBean;
import com.clg.wallet.enums.CoinType;
import com.clg.wallet.newclient.ClientFactory;
import com.clg.wallet.newclient.EthNewClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class EthTask {

    @Autowired
    private ClientInfo clientInfo;

    @Autowired
    private CoinEthRechargeService coinEthRechargeService;

    @Autowired
    private CoinEthAddressPoolService coinEthAddressPoolService;

    @Autowired
    private CoinEthCollectTaskService coinEthCollectTaskService;

    @Autowired
    private CoinEthWithdrawService coinEthWithdrawService;

    @Autowired
    private CommitStatusService commitStatusService;

    /**
     * 增加eth充值业务
     *
     * @param taskRegistrar
     */
    public void addRecharge(ScheduledTaskRegistrar taskRegistrar) {
        List<CoinConfig> list = clientInfo.getCoinConfigFormType(CoinType.ETH);
        if (!CollectionUtils.isEmpty(list)) {
            CoinConfig ethCoin = list.get(0);
            if (null != ethCoin) {
                taskRegistrar.addTriggerTask(
                        () -> coinEthRechargeService.rechargeCoin(ethCoin), //1.添加任务内容(Runnable)
                        triggerContext -> {
                            return new CronTrigger(ethCoin.getTask()).nextExecutionTime(triggerContext);
                        }
                );
            }
        }
    }

    /**
     * 确定打款是否成功,并且计算打款的链上花费的手续费
     *
     * @param taskRegistrar
     */
    public void commitData(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                () -> commitStatusService.commitEth(), //1.添加任务内容(Runnable)
                triggerContext -> {
                    String cron = "0/15 * * * * ?";
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }

    /**
     * 创建地址
     *
     * @param taskRegistrar
     */
    public void createAddress(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                () -> coinEthAddressPoolService.createAddress(), //1.添加任务内容(Runnable)
                triggerContext -> {
                    String cron = "0/59 * * * * ?";
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }

    /**
     * 归账
     *
     * @param taskRegistrar
     */
    public void collection(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                () -> coinEthCollectTaskService.collectionTask(), //1.添加任务内容(Runnable)
                triggerContext -> {
                    String cron = "0/30 * * * * ?";
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }

    // /**
    //  * eth到账业务
    //  *
    //  * @param taskRegistrar
    //  */
    // public void commitData(ScheduledTaskRegistrar taskRegistrar) {
    //     taskRegistrar.addTriggerTask(
    //             () -> commitStatusService.commitEth(), //1.添加任务内容(Runnable)
    //             triggerContext -> {
    //                 String cron = "0/15 * * * * ?";
    //                 return new CronTrigger(cron).nextExecutionTime(triggerContext);
    //             }
    //     );
    // }
    //
    // /**
    //  * 确定打款是否成功,并且计算打款的链上花费的手续费
    //  *
    //  * @param taskRegistrar
    //  */
    // public void commitData(ScheduledTaskRegistrar taskRegistrar) {
    //     taskRegistrar.addTriggerTask(
    //             () -> commitStatusService.commitEth(), //1.添加任务内容(Runnable)
    //             triggerContext -> {
    //                 String cron = "0/15 * * * * ?";
    //                 return new CronTrigger(cron).nextExecutionTime(triggerContext);
    //             }
    //     );
    // }
    //

    /**
     * 提币
     *
     * @param taskRegistrar
     */
    public void withDraw(ScheduledTaskRegistrar taskRegistrar) {

        taskRegistrar.addTriggerTask(
                () -> {
                    try {
                        coinEthWithdrawService.transaction();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, //1.添加任务内容(Runnable)
                triggerContext -> {
                    String cron = "0/55 * * * * ?";
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }



    public static void main(String[] args) {

        AtomicInteger count=new AtomicInteger(0);

        ScheduledExecutorService service = Executors
                .newSingleThreadScheduledExecutor();
        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                count.incrementAndGet();
                System.out.println(count);
            }
        }, 1, 2, TimeUnit.SECONDS);
    }


//
//        ClientBean clientBean = (new ClientBean()).setRpcPort("9898").setRpcIp("47.91.227.3").setCoinType("eth");//eth
//        EthNewClient client = (EthNewClient)ClientFactory.getClient(clientBean);
//        File file = new File("D:/ggg.csv");
//        StringBuilder result = new StringBuilder();
//        try{
//            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
//            String s = null;
//            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
//
//                if(s==""||s.length()==0) {
//                    continue;
//                }else {
//
//                    BigDecimal balance = client.getTokenBalance("0xc5ec5bfe9275715f6de105c3c249f9ba7beafd91", s).toBigDecimal();
//                    if (balance!=null&&balance.compareTo(BigDecimal.ZERO)>0){
//                        System.out.println(s);
//                        write(s);
//
//                    }
//
////            	 if(updateSql.indexOf("0E-20")>-1){
////
////				 }else{
////					 write(updateSql);
////				 }
//                }
//            }
//            br.close();
//        }catch(Exception e){
//            e.printStackTrace();
//        }



    public static void write(String text) {
        FileWriter fw = null;
        try {
            // 如果文件存在，则追加内容；如果文件不存在，则创建文件
            //File f = new File("C:\\Users\\EDZ\\Desktop\\bxx生产\\eth3.sql");
            File f = new File("D:\\hhh.txt");
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
