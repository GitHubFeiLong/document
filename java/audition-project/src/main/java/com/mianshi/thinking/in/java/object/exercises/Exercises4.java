package com.mianshi.thinking.in.java.object.exercises;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/5/22 11:38
 */
public class Exercises4 {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String[] args) {
        DataOnly dataOnly = new DataOnly();
        System.out.println(dataOnly.i);
        System.out.println(dataOnly.d);
        System.out.println(dataOnly.b);
    }

    static class DataOnly {
        int i;
        double d;
        boolean b;
    }
}

