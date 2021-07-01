package com.tuling.jvm.jvm2;

/**
 * @author e-Feilong.Chen
 * @version 1.0
 * @description TODO
 * @date 2021/7/1 18:27
 */
public class StackOverflowTest {
    static int count = 0;

    static void redo(){
        count ++;
        redo();
    }

    public static void main(String[] args) {
        try {
            redo();
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println(count);
        }
    }
}
