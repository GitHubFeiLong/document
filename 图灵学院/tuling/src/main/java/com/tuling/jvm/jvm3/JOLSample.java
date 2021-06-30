package com.tuling.jvm.jvm3;

import org.openjdk.jol.info.ClassLayout;

/**
 * 类描述：
 * 计算对象大小
 * @Author msi
 * @Date 2021-06-30 19:40
 * @Version 1.0
 */
public class JOLSample {
    public static void main(String[] args) {
        ClassLayout layout = ClassLayout.parseInstance(new Object());
        System.out.println("layout.toPrintable() = " + layout.toPrintable());
        ClassLayout layout1 = ClassLayout.parseInstance(new int[]{});
        System.out.println("layout1.toPrintable() = " + layout1.toPrintable());
        ClassLayout layout2 = ClassLayout.parseInstance(new A());
        System.out.println("layout2.toPrintable() = " + layout2.toPrintable());
    }

    // -XX:+UseCompressedOops 默认开启的压缩所有指针
    // ‐XX:+UseCompressedClassPointers 默认开启的压缩对象头里的类型指针Klass Pointer
    // Oops : Ordinary Object Pointers
    public static class A {
        //8B mark word
        //4B Klass Pointer 如果关闭压缩‐XX:‐UseCompressedClassPointers或‐XX:‐UseCompressedOops，则占用8B
        int id; //4B
        String name; //4B 如果关闭压缩‐XX:‐UseCompressedOops，则占用8B
        byte b; //1B
        Object o; //4B 如果关闭压缩‐XX:‐UseCompressedOops，则占用8B
    }
}
