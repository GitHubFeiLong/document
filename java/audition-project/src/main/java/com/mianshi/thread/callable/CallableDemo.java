package com.mianshi.thread.callable;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-06-07 21:33
 * @Version 1.0
 */
public class CallableDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        ArrayList<Future> result = new ArrayList<Future>();

        for (int i = 0; i < 10; i++) {
            result.add(executorService.submit(new TaskWithResult(i)));
        }
        System.out.println();
        for (Future f: result) {
            System.out.println("f.isDone() = " + f.isDone());
            System.out.println("f.get() = " + f.get());
            System.out.println("f.isDone() = " + f.isDone());
        }

        executorService.shutdown();
    }
}
