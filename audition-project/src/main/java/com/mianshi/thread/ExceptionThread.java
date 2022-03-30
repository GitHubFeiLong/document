package com.mianshi.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-06-08 15:11
 * @Version 1.0
 */
public class ExceptionThread {
    public static void main(String[] args) {
        try {
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.execute(new Exception("demo"));
            executorService.shutdown();
        } catch (RuntimeException e) {
            System.out.println("捕获线程run方法 异常");
            return;
        }
    }
}
class Exception extends Thread {

    public Exception(String name) {
        super(name);
    }

    @Override
    public void run() {
        throw new RuntimeException("自定义异常");
    }
}
