package com.mianshi.basic;

/**
 * 类描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2022/4/1 13:18
 */
public class Test1 {
    //~fields
    //==================================================================================================================

    //~construct methods
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String[] args) {
        int j = 0;
        for (int i = 0; i < 100; i++) {
            j = j++;
        }
        System.out.println("j = " + j);
    }
}
