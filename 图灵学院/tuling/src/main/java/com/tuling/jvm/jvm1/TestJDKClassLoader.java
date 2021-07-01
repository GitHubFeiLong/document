package com.tuling.jvm.jvm1;

import sun.misc.Launcher;

import java.net.URL;

/**
 * @author e-Feilong.Chen
 * @version 1.0
 * @description TODO
 * @date 2021/7/1 17:03
 */
public class TestJDKClassLoader {
    public static void main(String[] args) {
        // 查看类加载器
        System.out.println(String.class.getClassLoader());
        System.out.println(com.sun.crypto.provider.DESKeyFactory.class.getClassLoader());
        System.out.println(TestJDKClassLoader.class.getClassLoader());

        System.out.println();
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        ClassLoader extClassLoader = systemClassLoader.getParent();;
        ClassLoader bootstrapClassLoader = extClassLoader.getParent();
        System.out.println("systemClassLoader = " + systemClassLoader);
        System.out.println("extClassLoader = " + extClassLoader);
        System.out.println("bootstrapClassLoader = " + bootstrapClassLoader);

        System.out.println();
        System.out.println("bootstrapLoader 加载以下文件：");
        URL[] urLs = Launcher.getBootstrapClassPath().getURLs();
        for (int i = 0; i < urLs.length; i++) {
            System.out.println("urLs[i] = " + urLs[i]);
        }

        System.out.println();
        System.out.println("extClassLoader加载以下文件：");
        System.out.println(System.getProperty("java.ext.dirs"));

        System.out.println();
        System.out.println("appClassLoader加载以下文件：");
        System.out.println(System.getProperty("java.class.path"));
    }
}
