package com.mianshi.thinking.in.java.object.exercises;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/5/22 12:22
 */
public class Exercises8 {

    //~fields
    //==================================================================================================================

    static int i;

    //~methods
    //==================================================================================================================
    public static void main(String[] args) {
        Exercises8.i = 10;
        Exercises8 exercises81 = new Exercises8();
        Exercises8 exercises82 = new Exercises8();
        Exercises8 exercises83 = new Exercises8();
        System.out.println("exercises81 = " + exercises81.get());
        System.out.println("exercises82 = " + exercises82.get());
        System.out.println("exercises83 = " + exercises83.get());
    }

    public int get () {
        return Exercises8.i;
    }

}