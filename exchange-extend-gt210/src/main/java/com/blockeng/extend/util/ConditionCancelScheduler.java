package com.blockeng.extend.util;

import lombok.Data;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class ConditionCancelScheduler {
    private static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) throws Exception{

        timedTask((missionParm) -> {
            System.out.println("work");
            return false;
        }, 1,(callbackParm) -> {
            System.out.println("end");
            return false;
        },1,new Timer( 1, 1, TimeUnit.SECONDS));
    }

    public static void timedTask(Function<Object,Boolean> mission,Object missionParm,
                                 Function<Object,Boolean> doneCallback,Object callbackParm,
                                 Timer timer) throws Exception {
        final String jobID = "work";
        final AtomicInteger count = new AtomicInteger(0);
        final Map<String, Future> futures = new HashMap<>();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Future future = scheduler.scheduleAtFixedRate (new Runnable() {
            @Override
            public void run() {
                System.out.println(count.getAndIncrement());
                boolean flag= mission.apply(missionParm);
                if (count.get() > 10||flag) {
                    Future future = futures.get(jobID);
                    if (future != null) future.cancel(true);
                    countDownLatch.countDown();
                }
            }
        }, timer.getInitialDelay(), timer.getDelay(), timer.getTimeUnit());

        futures.put(jobID, future);
        countDownLatch.await();
        scheduler.shutdown();
        if (count.get() > 10)
        doneCallback.apply(callbackParm);
    }
}
@Data
class Timer{
    public Timer() {
    }
    private long initialDelay =1;
    private long delay=1;
    private TimeUnit timeUnit=TimeUnit.SECONDS;

    public Timer(long initialDelay, long delay, TimeUnit timeUnit) {
        this.initialDelay = initialDelay;
        this.delay = delay;
        this.timeUnit = timeUnit;
    }
}