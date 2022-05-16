package com.mianshi.thread.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 类描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2022/5/5 14:06
 */
public class CallableDemo1 implements Callable<String> {
    @Override
    public String call() throws Exception {
        Thread.sleep(10000);
        return "demo";
    }
    //~fields
    //==================================================================================================================

    //~construct methods
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask futureTask = new FutureTask(new CallableDemo1());
        Thread thread = new Thread(futureTask);
        thread.start();
        Object o = futureTask.get();
        System.out.println("o = " + o);

    }
}
