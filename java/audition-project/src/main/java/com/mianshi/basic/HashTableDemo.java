package com.mianshi.basic;

import java.util.Hashtable;
import java.util.Map;

/**
 * 类描述：
 * HashTable 添加元素，key与value均不能为null
 * value = null: put时判断为null，直接抛出NPE
 * key = null: put时，使用key做hash时，直接NPE
 * @Author e-Feilong.Chen
 * @Date 2022/4/26 17:27
 */
public class HashTableDemo {
    //~fields
    //==================================================================================================================

    //~construct methods
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String[] args) {
        Map<String, Long> map = new Hashtable<>();

        map.put("key", 1L);
        map.put(null, 1L);
        map.put("key", null);


    }
}
