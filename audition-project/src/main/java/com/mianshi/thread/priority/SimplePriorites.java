package com.mianshi.thread.priority;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 类描述：
 * 线程优先级
 * @Author msi
 * @Date 2021-06-08 14:08
 * @Version 1.0
 */
public class SimplePriorites implements Runnable{

    private int countDown = 5;
    private volatile double d;
    private int priority;

    public SimplePriorites(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return Thread.currentThread() + " : " + countDown;
    }

    @Override
    public void run() {
        Thread.currentThread().setPriority(this.priority);
        while (true) {
            for (int i = 0; i < 100000; i++) {
                d += (Math.PI + Math.E) / (double)i;
                if (i % 1000 == 0) {
                    Thread.yield();
                }
                System.out.println(this);
                if (--countDown == 0) {
                    return;
                }
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            executorService.execute(new SimplePriorites(Thread.MIN_PRIORITY));
        }
        executorService.execute(new SimplePriorites(Thread.MAX_PRIORITY));

        executorService.shutdown();
    }
}
