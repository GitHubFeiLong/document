# JVM（Java Virtual Machine）



## 栈帧：

 	1. 局部变量表：存放局部变量
 	2. 操作数栈：存储临时的操作数
 	3. 动态链接：指向运行时常量池的方法引用	
 	4. 方法出口：存储调用方法的位置

## 方法区（元空间）

字节码被类装载器装载到方法区中 常量、静态变量、类元信息（存放类）

## 本地方法栈

存放本地方中的局部变量

## 堆

堆(init:600M)：年轻代(200M)、老年代(400M)
年轻代：Eden(8/10) --> From(1/10) --> To(1/10)
Survivor区：From(1/10) <--> To(1/10)
老年代(2/3)
new出来的对象放在Eden区
Eden区使用minor GC，清理Eden区内存，清理From区，Eden区存活下来的对象挪移到From区
From区存活下来的对象挪移到To区。
当一个对象经过15次minor GC后，对象就会被放到老年代
当老年代存满了就会触发 full GC（非常耗时STW Stop To World）。



## 调优工具

### jvisualvm

jdk自带的调优工具

### Arthas

http://arthas.gitee.io/

简介：`Arthas` 是Alibaba开源的Java诊断工具，深受开发者喜爱。

