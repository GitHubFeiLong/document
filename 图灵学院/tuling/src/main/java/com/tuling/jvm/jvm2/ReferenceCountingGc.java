package com.tuling.jvm.jvm2;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-07-01 21:18
 * @Version 1.0
 */
public class ReferenceCountingGc {
    Object instance = null;

    public static void main(String[] args) {
        ReferenceCountingGc objA = new ReferenceCountingGc();
        ReferenceCountingGc objB = new ReferenceCountingGc();
        objA.instance = objB;
        objB.instance = objA;
        objA = null;
        objB = null;
    }
}
