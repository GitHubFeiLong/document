package com.mianshi.jvm;

import java.util.ArrayList;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-06-05 19:48
 * @Version 1.0
 */
public class HeapTest {

    byte[] a = new byte[1024 * 100];

    public static void main(String[] args) throws InterruptedException {
        ArrayList<HeapTest> heapTests = new ArrayList<HeapTest>();
        while (true) {
            heapTests.add(new HeapTest());
            Thread.sleep(10);
        }
    }
}
