package com.mianshi.basic;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/4/18 22:00
 */
public class InstanceOfTest {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String[] args) {
        // Object obj = null;
        // System.out.println(obj instanceof Object);
        //
        // int i = 10;
        // long l = 10;
        // double d = 10.0;
        // System.out.println(i == l);
        // System.out.println(i == d);
        // System.out.println(l == d);

        System.out.println("Integer.MAX_VALUE = " + Integer.MAX_VALUE);
        System.out.println("result = " + getInt());
    }

    public static int getInt() {
        int i = 1;
        try {
            i *= 10;
            System.out.println(10/0);
            // throw new RuntimeException("1");
            System.out.println(" try i = " + i);
            return i;
        } catch(Exception e) {
            i *= 5;
            System.out.println("catch i= " + i);
            return i;
        } finally {
            i *= 3;
            System.out.println("finally i = " + i);
            // return i;
        }

    }

}