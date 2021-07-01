package com.tuling.jvm.jvm2;

/**
 * @author e-Feilong.Chen
 * @version 1.0
 * @description TODO
 * @date 2021/7/1 18:46
 */
public class EscapeAnalysis {
    public User test1(){
        User user = new User();
        return user;
    }
    public void test2(){
        User user = new User();
    }
}
