package com.mianshi.thread.p1;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-06-14 9:34
 * @Version 1.0
 */
public class Thread1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new Thread(new Demo1()).start();
        new Thread(new Demo2()).start();
        FutureTask futureTask = new FutureTask(new Demo3());
        new Thread(futureTask).start();
        System.out.println(futureTask.get() + "1");
//        while (true) {
//            if (futureTask.isDone()) {
//                System.out.println(futureTask.get() + "1");
//                break;
//            }
//        }
        System.out.println("end");
    }
}
class Demo1 implements Runnable{

    @Override
    public void run() {
        System.out.println("Demo1");
    }
}

class Demo2 extends Thread {
    @Override
    public void run() {
        System.out.println("Demo2");
    }
}

class Demo3 implements Callable<String> {

    @Override
    public String call() throws Exception {
        System.out.println("call");
        return "call";
    }
}
