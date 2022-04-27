package com.mianshi.jvm;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-06-13 17:32
 * @Version 1.0
 */
public class Demo1 {
    public static void main(String[] args) {
        new A ();
        System.out.println("-====-");
        B b = null;
    }
}
class A {
    static {
        System.out.println("static A"); // 1
    }
    public A () {
        System.out.println("Constructor A"); //2
    }
}

class B {
    static {
        System.out.println("static B");
    }
    public B () {
        System.out.println("Constructor B");
    }
}
