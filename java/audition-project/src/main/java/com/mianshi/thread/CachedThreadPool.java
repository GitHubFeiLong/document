package com.mianshi.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-06-07 19:40
 * @Version 1.0
 */
public class CachedThreadPool {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++){
            executorService.execute(new LiftOff());
        }
        executorService.shutdown();
    }
}
