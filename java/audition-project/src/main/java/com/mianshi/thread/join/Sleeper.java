package com.mianshi.thread.join;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-06-08 14:53
 * @Version 1.0
 */
public class Sleeper extends Thread {

    private int duration;


    public Sleeper(String name, int sleepTime) {
        super(name);
        this.duration = sleepTime;
        start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            System.out.println(getName() + " was interrupted.isInterrupted() " + isInterrupted());
            return;
        }
        System.out.println(getName() + "has awakened");
    }
}

class Joiner extends Thread {
    private Sleeper sleeper;

    public Joiner(String name, Sleeper sleeper) {
        super(name);
        this.sleeper = sleeper;
        start();
    }

    @Override
    public void run() {
        try {
            sleeper.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        System.out.println(getName() + "join completed");
    }
}

class joining {
    public static void main(String[] args) {
        Sleeper sleeper = new Sleeper("sleepy", 1500);
        Sleeper grumpy = new Sleeper("grumpy", 1500);

        Joiner dopey = new Joiner("Dopey", sleeper), doc = new Joiner("Doc", grumpy);

        grumpy.interrupt();

    }
}
