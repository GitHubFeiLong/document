package com.tuling.jvm.jvm1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author e-Feilong.Chen
 * @version 1.0
 * @description TODO
 * @date 2021/7/1 17:25
 */
public class MyClassLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // 初始化自定义类加载器，会先初始化父类ClassLoader，其中会把自定义类加载器的父加载器设置为应用程序类加载器AppClassLoader
        MyClassLoader myClassLoader = new MyClassLoader("D:/test");
        // D盘创建 test/com/tuling/jvm/jvm1 几级目录，将User.class丢入该目录
        Class clazz = myClassLoader.loadClass("com.tuling.jvm.jvm1.User1");
        Object obj = clazz.newInstance();
        Method method = clazz.getDeclaredMethod("sout", null);
        method.invoke(obj, null);

        MyClassLoader myClassLoader2 = new MyClassLoader("D:/test1");
        Class clazz2 = myClassLoader2.loadClass("com.tuling.jvm.jvm1.User1");
        Object obj2 = clazz2.newInstance();
        Method method2 = clazz2.getDeclaredMethod("sout", null);
        method2.invoke(obj2, null);
        // 类加载打印
        System.out.println(clazz.getClassLoader().getClass().getName());
    }
    static class MyClassLoader extends ClassLoader {
        private String classPath;
        public MyClassLoader(String classPath){
            this.classPath = classPath;
        }

        /**
         * 重写类加载方法，实现自己的加载逻辑，不委派给双亲加载
         * @param name
         * @param resolve
         * @return
         * @throws ClassNotFoundException
         */
        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            synchronized (getClassLoadingLock(name)) {
                // First, check if the class has already been loaded
                Class<?> c = findLoadedClass(name);
                if (c == null) {
                    long t0 = System.nanoTime();
                    if (!name.startsWith("com.tuling.jvm")) {
                        c = this.getParent().loadClass(name);
                    } else {
                        c = findClass(name);
                    }
                }
                if (resolve) {
                    resolveClass(c);
                }
                return c;
            }
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            try {
                byte[] data = loadByte(name);
                return defineClass(name, data, 0, data.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return super.findClass(name);
        }


        private byte[] loadByte(String name) throws IOException {
            name = name.replaceAll("\\.", "/");
            FileInputStream fis = new FileInputStream(classPath + "/" + name + ".class");
            int len = fis.available();
            byte[] data = new byte[len];
            fis.read(data);
            fis.close();
            return data;
        }
    }
}
