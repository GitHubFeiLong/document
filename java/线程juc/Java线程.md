# 线程

## 实现多线程的方式

### 1. extends Thread

```java
class Demo1 extends Thread {
    @Override
    public void run(){
        
    }
    public static void main(String[] args){
        new Thread(new Demo1()).start();
    }
}
```



### 2. implements Runnable

```java
class Demo1 implements Runnable{

    @Override
    public void run() {
        System.out.println("Demo1");
    }
     public static void main(String[] args) {
        new Thread(new Demo1()).start();
    }
}
```



### 3. implements Callable

有返回值。

```java
class Demo1 implements Callable<String> {

    @Override
    public String call() throws Exception {
        System.out.println("call");
        return "call11";
    }
    public static void main(String[] args) {
        FutureTask futureTask = new FutureTask(new Demo3());
        new Thread(futureTask).start();
        System.out.println(futureTask.get());
    }
}
```



## JUC

### 1. CountDownLatch 闭锁

CountDownLatch对象有三个常用的方法（共6个）。
CountDownLatch 又叫闭锁，可以让一个线程等待其他一组线程都执行结束之后再继续执行，如果在主方法中使用，就会将主线程阻塞，等待指定个数的线程都执行结束之后，主线程在恢复执行。

注意：CountDownLatch的值减为0时，不可恢复，所以有叫闭锁

**方法：**

1. 构成方法`CountDownLatch(int count)`，用于指定多少个线程。
2. `void countDown()`，执行完一个线程，count减一。
3. `void await()`,阻塞当前作用域的线程，当count等于0时，继续执行。

```java
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
        System.out.println("线程一");
        countDownLatch.countDown();
    }
}
class CDemo2 implements Runnable {

    private CountDownLatch countDownLatch;

    public CDemo2(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }
    @Override
    public void run() {
        System.out.println("线程二");
        countDownLatch.countDown();
    }
}
```

### 2. CyclicBarrier 循环栅栏

**CyclicBarrier** 有叫循环栅栏，它的作用是等待一组线程都执行到某个状态后再继续执行；看起来好像和**CountDownLatch**差不多。

**方法：**

1. 构造方法：` CyclicBarrier(int parties, Runnable barrierAction)` 第一个参数，表示等待多少个线程，第二个参数，在最后一个运行的线程执行任务。
2. `int await()` 等待阻塞，当有`parties` 线程都启动后，所有线程再执行await()后面的代码。
3. 

```java
public class CyclicBarrierDemo {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10, new Runnable() {
            @Override
            public void run() {
                System.out.println("所有人都到达会议室，开始本周会议.....");
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
```



### 3. Exchanger 线程交换器

Exchange主要用于交换两个线程之间的数据，当两个线程配对之后，将两个线程的数据交换，然后再一起执行，如果只有一个线程调用Exchange方法，这个线程将阻塞，直到有另一个线程和他配对。

```java
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
```

