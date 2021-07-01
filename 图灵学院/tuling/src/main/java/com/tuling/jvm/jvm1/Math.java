package com.tuling.jvm.jvm1;

/**
 * @author e-Feilong.Chen
 * @version 1.0
 * @description TODO
 * @date 2021/6/29 9:21
 */
public class Math {
    public static final int initData = 666;
    public static User user = new User();

    public int compute() { // 一个方法对应一个栈帧内存区域
        int a = 1;
        int b = 2;
        int c = (a + b) * 10;
        return c;
    }

    public static void main(String[] args) {
        Math math = new Math();
        math.compute();
    }
}
