package com.mianshi.jvm;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-06-07 17:43
 * @Version 1.0
 */
public class Math {
    public static int initData = 666;
//    public static User user = new User();

    public int compute() {
        int a = 1;
        int b = 2;
        int c = a + b;
        return c;
    }

    public static void main(String[] args) {
        Math math = new Math();
        math.compute();
    }
}
