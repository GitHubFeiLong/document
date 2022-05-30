package com.mianshi.thinking.in.java.basic.local;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/5/24 22:36
 */
public class C {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String[] args) {
        new C3();
    }

}
class C1 {
    public C1() {
        System.out.println("c1");
    }
}
class C2 {
    public C2() {
        System.out.println("c2");
    }
}

class C3 extends C1{
    private C2 c2= new C2();

}