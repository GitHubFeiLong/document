package com.mianshi.thread.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 类描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2022/4/7 14:55
 */
public class CallableTest1 {
    //~fields
    //==================================================================================================================

    //~construct methods
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                for (int i = 0; i < 10; i++) {
                    System.out.println("线程正在执行");
                    Thread.sleep(1000);
                }
                return 100;
            }
        };
        FutureTask futureTask = new FutureTask(callable);
        Thread thread = new Thread(futureTask, "hh");
        System.out.println("开始执行");
        thread.start();
        // System.out.println(futureTask.get());
        System.out.println("结束执行");
    }
}
