package com.tuling.jvm.jvm2;

/**
 * 添加运行JVM参数： -XX:+PrintGCDetails
 * @author e-Feilong.Chen
 * @version 1.0
 * @description TODO
 * @date 2021/7/1 19:02
 */
public class GCTest {
    public static void main(String[] args) {
        byte[] allocation1, allocation2;
        allocation1 = new byte[60000*1024];
        allocation2 = new byte[60000*1024];
    }
}
