package com.mianshi.thread;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-06-07 21:44
 * @Version 1.0
 */
public class Thread6 {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 20; i++) {
            executorService.execute(new RunnableDemo());
        }

        executorService.shutdown();
    }
}

class RunnableDemo implements Runnable {
    private final Random random = new Random();

    public void run() {

        int i = random.nextInt(10);

        try {
            TimeUnit.SECONDS.sleep(i);
            System.out.println(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
