package com.mianshi.jvm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-06-28 20:11
 * @Version 1.0
 */
public class MyClassLoaderTest {
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
            synchronized (getClassLoadingLock(name)){
                Class<?> c = findLoadedClass(name);
                if (c == null) {
                    long t1 = System.nanoTime();

                    if (!name.startsWith("com.mianshi")) {
                        c = this.getParent().loadClass(name);
                    } else {
                        c = findClass(name);
                    }

                    sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                    sun.misc.PerfCounter.getFindClasses().increment();
                    if (resolve) {
                        resolveClass(c);
                    }
                }
                return c;
            }
        }

        @Override
        protected Class<?> findClass(String name) {
            String str = name.replaceAll("\\.", "/");
            FileInputStream fis = null;
            byte[] data = null;
            try {
                fis = new FileInputStream(classPath + "/" + str + ".class");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                int len = fis.available();
                data = new byte[len];
                fis.read(data);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return defineClass(name, data, 0, data.length);
        }
    }

    public static void main(String[] args) throws Exception{
        //初始化自定义类加载器，会先初始化父类ClassLoader，其中会把自定义类加载器的父加载器设置为应用程序类加载器AppClassLoader
        MyClassLoader classLoader = new MyClassLoader("D:/test");
        //D盘创建 test/com/mianshi/jvm 几级目录，将User类的复制类User1.class丢入该目录
//        Class clazz = classLoader.loadClass("java.lang.String");
        Class clazz = classLoader.loadClass("com.mianshi.jvm.User1");
        Object obj = clazz.newInstance();
        Method method = clazz.getDeclaredMethod("sout", null);
        method.invoke(obj, null);
        System.out.println(clazz.getClassLoader().getClass().getName());
    }
}
