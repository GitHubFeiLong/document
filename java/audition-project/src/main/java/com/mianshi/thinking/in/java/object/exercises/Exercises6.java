package com.mianshi.thinking.in.java.object.exercises;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/5/22 11:49
 */
public class Exercises6 {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String[] args) {
        Exercises6 exercises6 = new Exercises6();
        System.out.println("storage = " + exercises6.storage("hh哈哈"));
    }
    int storage(String s) {
        return s.length() * 2;
    }

}