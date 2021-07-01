package com.tuling.jvm.jvm1;

/**
 * @author e-Feilong.Chen
 * @version 1.0
 * @description TODO
 * @date 2021/7/1 16:58
 */
public class TestDynamicLoad {
    public static void main(String[] args) {
        new A ();
        System.out.println("-====-");
        // B类不会被加载，只有在new B()时才加载
        B b = null;
    }
}
class A {
    static {
        System.out.println("static A");
    }
    public A () {
        System.out.println("Constructor A");
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
