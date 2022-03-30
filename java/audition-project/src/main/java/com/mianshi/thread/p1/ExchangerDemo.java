package com.mianshi.thread.p1;

import java.util.concurrent.Exchanger;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-06-14 13:16
 * @Version 1.0
 */
public class ExchangerDemo {
    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();
        new Thread(new Boy(exchanger)).start();
        new Thread(new Girl(exchanger)).start();
    }
}

class Boy implements Runnable {
    private Exchanger exchanger;

    public Boy(Exchanger exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        String str = "boy";
        try {
            str = (String) exchanger.exchange(str);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("boy is " + str);
    }
}

class Girl implements Runnable {

    private Exchanger exchanger;

    public Girl(Exchanger exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        String str1 = "Girl";
        try {
            str1 = (String)exchanger.exchange(str1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Girl is " + str1);
    }
}
