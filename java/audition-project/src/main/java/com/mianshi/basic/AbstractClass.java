package com.mianshi.basic;

/**
 * 类描述：
 * 抽象类可以有构造方法，但是不能直接实例化对象
 * @Author e-Feilong.Chen
 * @Date 2022/4/1 13:48
 */
public abstract class AbstractClass {
    //~fields
    //==================================================================================================================

    //~construct methods
    //==================================================================================================================
    public AbstractClass(){

    }
    //~methods
    //==================================================================================================================
    public static void main(String[] args) {
        AbstractClass abstractClass = new AbstractClass(){

        };

        System.out.println(abstractClass);
    }
}
