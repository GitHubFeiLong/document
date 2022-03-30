package com.mianshi.jvm;

import sun.misc.Launcher;

import java.net.URL;
import java.util.Arrays;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-06-13 17:37
 * @Version 1.0
 */
public class Demo2 {
    public static void main(String[] args) {
//        System.out.println(String.class.getClassLoader());
//        System.out.println(com.sun.crypto.provider.DESKeyFactory.class.getClassLoader());
//        System.out.println(Demo2.class.getClassLoader().getClass().getName());

//        ClassLoader appClassLoader = ClassLoader.getSystemClassLoader();
//        ClassLoader extClassLoader = appClassLoader.getParent();
//        ClassLoader bootStrapLoader = extClassLoader.getParent();
//        System.out.println(appClassLoader);
//        System.out.println(extClassLoader);
//        System.out.println(bootStrapLoader);

//        URL[] urLs = Launcher.getBootstrapClassPath().getURLs();
//        System.out.println(Arrays.toString(urLs));

        System.out.println(System.getProperty("java.ext.dirs"));

        System.out.println(System.getProperty("java.class.path"));
    }
}
