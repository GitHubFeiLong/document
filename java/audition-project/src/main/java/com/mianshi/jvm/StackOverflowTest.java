package com.mianshi.jvm;

import net.bytebuddy.implementation.bytecode.Throw;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-06-29 21:49
 * @Version 1.0
 */
// JVM设置 ‐Xss128k(默认1M)
public class StackOverflowTest {
    static int count = 0;

    static void redo(){
        count ++;
        redo();
    }

    public static void main(String[] args) {
        try {
            redo();
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println(count);
        }
    }
}
