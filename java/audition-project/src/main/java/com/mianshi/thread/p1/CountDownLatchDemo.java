package com.mianshi.thread.p1;

import java.util.concurrent.CountDownLatch;

/**
 * 类描述：
 * CountDownLatch  可以让主线程等待所有都执行完了才执行
 * @Author msi
 * @Date 2021-06-14 9:56
 * @Version 1.0
 */
public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("开始");
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(new CDemo1(countDownLatch)).start();
        new Thread(new CDemo2(countDownLatch)).start();
        countDownLatch.await();
        System.out.println("结束");
    }
}
class CDemo1 implements Runnable {
    private CountDownLatch countDownLatch;

    public CDemo1(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        System.out.println("线程一等待执行");
        countDownLatch.countDown();
        System.out.println("线程一执行完成");
    }
}
class CDemo2 implements Runnable {

    private CountDownLatch countDownLatch;

    public CDemo2(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }
    @Override
    public void run() {
        System.out.println("线程二等待执行");
        countDownLatch.countDown();
        System.out.println("线程二执行完成");
    }
}
