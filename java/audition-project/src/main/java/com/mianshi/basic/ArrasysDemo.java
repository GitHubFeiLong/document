package com.mianshi.basic;

import java.util.Arrays;
import java.util.List;

/**
 * 类描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2022/4/1 10:59
 */
public class ArrasysDemo {
    //~fields
    //==================================================================================================================

    //~construct methods
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String[] args) {
        testAsList();
    }

    /**
     * 测试，Arrays.asList 方法返回的集合添加元素报错
     */
    public static void testAsList(){
        String[] arrays = {"张三", "李四", "王麻子"};
        // 注意Arrays.asList 返回的是Arrays的内部类ArrayList
        List<String> list = Arrays.asList(arrays);
        System.out.println("list = " + list);
        // 报错 => 因为Arrays的内部类ArrayList未重写AbstractList的add()方法
        list.add("李六");
    }
}
