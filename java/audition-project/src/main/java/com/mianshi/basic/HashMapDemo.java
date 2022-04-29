package com.mianshi.basic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：
 * HashMap 相关
 * @Author e-Feilong.Chen
 * @Date 2022/4/1 13:58
 */
public class HashMapDemo {
    //~fields
    //==================================================================================================================

    //~construct methods
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String[] args) {
        // Map<String, String> map = new HashMap<>();
        // // 使用Collections 将其变为线程安全集合
        // Collections.synchronizedMap(map);

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
    }
}
