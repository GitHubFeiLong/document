package com.mianshi.thread;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-06-07 19:16
 * @Version 1.0
 */
public class LiftOff implements Runnable {

    protected int countDown = 10;
    private static int taskCount = 0;
    private final int id = taskCount++;
    public LiftOff(){

    }
    public LiftOff(int countDown){
        this.countDown = countDown;
    }

    public String status () {
        return "#" + id + "(" + (countDown > 0 ? countDown : "Liftoff!") + "),";
    }
    public void run() {
        while(countDown -- > 0){
            System.out.print(status());
            // 当前线程从执行状态（运行状态）变为可执行态（就绪状态）。
            // Thread.yield();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            try {
//                TimeUnit.MILLISECONDS.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        System.out.println("");
    }

    public static void main(String[] args) {
//        LiftOff liftOff = new LiftOff(10);
//        new Thread(liftOff).start();
        for (int i = 0; i < 5; i++) {
            new Thread(new LiftOff()).start();
        }
    }
}
