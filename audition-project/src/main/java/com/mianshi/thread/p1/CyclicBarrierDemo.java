package com.mianshi.thread.p1;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-06-14 10:12
 * @Version 1.0
 */
public class CyclicBarrierDemo {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10, new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " 所有人都到达会议室，开始本周会议.....");
            }
        });
        System.out.println("等待所有人到达会议室。。。");
        for (int i = 0; i < 10; i++) {
            new Thread(new CyDemo1(cyclicBarrier), "员工" + i).start();
        }
    }
}
class CyDemo1 implements Runnable{
    private CyclicBarrier cyclicBarrier;

    public CyDemo1(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "到达会议室");
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "开始汇报本周工作");

    }
}

