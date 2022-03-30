package com.mianshi.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-06-07 18:32
 * @Version 1.0
 */
public class Thread1 implements Runnable{

    public Thread1() {
        System.out.println("Thread1构造方法");
    }
    public void stop() {
        System.out.println("线程终止了");
    }

    public void run() {
        System.out.println("run 方法调用1");
        Thread.yield();
        System.out.println("run 方法调用2");
        Thread.yield();
        System.out.println("run 方法调用3");
        Thread.yield();
        stop();
    }

    public static void main(String[] args) {
//        new Thread(new Thread1()).start();
//        for (int i = 0; i < 5; i++) {
//            new Thread(new Thread1()).start();
//        }

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            executorService.execute(new Thread1());
        }

        executorService.shutdown();
    }
}
