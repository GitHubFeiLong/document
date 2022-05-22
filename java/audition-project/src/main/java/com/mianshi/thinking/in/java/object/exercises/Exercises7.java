package com.mianshi.thinking.in.java.object.exercises;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/5/22 12:19
 */
public class Exercises7 {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    public static void main(String[] args) {
        Incrementable.increment();
        System.out.println("Incrementable.i = " + Incrementable.i);
    }
    
    static class Incrementable {
        static int i;
        static void increment() {
            Incrementable.i++;
        }
    }

}