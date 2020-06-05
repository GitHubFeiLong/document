# 文本处理工具awk

## awk工作原理和基本用法说明

awk: Aho, Weinberger, Kernighan,报告生成器，格式化文本输出，GNU/Linux发布的AWK目前由自由软件基会(FSF) 进行开发和维护，通常也称它为GNU AWK

有多种版本：

+ AWK：原先来源于AT & T实验室的AWK
+ NAWK：New awk, AT & T实验室的AWK的升级版
+ GAWK：即GNU AWK。所有的GNU/Linux发布版都自带GAWK，它与AWK和NAWK完全兼容

gawk：模式扫描和处理语言，可以实现下面功能

+ 文本处理
+ 输出格式化的文本报表
+ 执行算数运算
+ 执行字符串操作等等

格式：

```bash
awk [options] 'program' var=value file...
awk [options] -f programfile var=value file...
```

说明：

program 通常是被放在单引号中，并可以由三种部分组成

+ BEGIN语句块
+ 模式匹配的通用语句块
+ END语句块

常用选项：

+ -F "分隔符" 指明输入时用到的字段分隔符
+ -v var=value 变量赋值

**Program格式：**

```bash
pattern{action statements;...}
```

pattern:决定动作语句何时触发及触发事件，比如：BEGIN，END
action statements：对数据进行处理，放在{}内指明，常见：print,printf

## awk工作过程

![1591013094012](C:\Users\msi\AppData\Roaming\Typora\typora-user-images\1591013094012.png)

第一步：执行BEGIN{action;....}语句块中的语句
第二步：从文件或标准输入(stdin)读取一行，然后执行pattern{action;...}语句块，它逐行扫描文件，从第一行到最后一行重复这个过程，指导文件全部被读取完毕。
第三步：当读至输入流末尾时，执行END{action;...}语句块

**BEGIN语句块**在awk开始从输入流中读取行之前被执行，这是一个可选的语句块，比如变量初始化、打印输出表格的表头等语句通常可以写在BEGIN语句块中

**END语句块**在awk从输入流中读取完所有的行之后即被执行，比如打印所有行的分析结果，这类信息汇总都是在END语句块中完成，它也是一个可选语句块

**pattern语句块**中的通用命令是最重要的部分，也是可选的。如果没有提供pattern语句块，则默认执行{print}，即打印每一个读取到的行，awk读取的每一行都会执行该语句块。

#### 分隔符、域和基量

+ 由分隔符分隔的字段（列，域）标记$1,$2...$n称为域标识，$0为所有域，注意：和shell中变量$符号含义不同
+ 文件的每一行称为记录record
+ 如果省略action，则默认执行print $0的操作

#### 常用的action分类

+ output statements:print,printf
+ Expressions:算术，比较表达式
+ Compound statements：组合语句
+ Control statements, if, while等
+ input statements

#### awk控制语句

+ {statements;...} 组合语句
+ if(condition){statements;...}
+ if(condition){statements;...}else{statements;...}
+ while(condition){statements;...}
+ do{statements;...}while(condition)
+ for(expr1;expr2;expr3){statements;...}
+ break
+ continue
+ exit



## 动作print

格式:

```bash
print item1, item2, ...
```

说明：

+ 逗号分隔符
+ 输出item可以是字符串，也可是数值；当前记录的字段、变量或awk的表达式
+ 如省略item，相当于print $0

范例：

```bash
awk '{print "hello,awk"}'
awk -F: '{print}' /etc/passwd
awk -F: '{print $0}' /etc/passwd
awk -F: '{print "wang"}' /etc/passwd
awk -F: '{print $1}' /etc/passwd
awk -F: '{print $1,$2}' /etc/passwd
awk -F: '{print $1"\t"$2}' /etc/passwd
grep "^UUID" /etc/fstab | awk '{print $2,$4}'

[root@centos7: ~]#df | awk -F"[[:space:]]+|%" '{print $5}'
Use
84
0
1
2
0
18
1
1
0
```

### awk变量

awk中的变量分为：内置和自定义变量
常见的内置变量

FS：输入字段分隔符，默认为空白字符，相当于-F
范例：

```bash
awk -v FS=':' '{print $1,FS,$3}' /etc/passwd
awk -F: '{print $1,$3,$7}' /etc/passwd
```

OFS：输出字段分隔符,默认空白字符



https://ke.qq.com/webcourse/index.html#cid=445311&term_id=100532217&taid=3892855278324607&vid=5285890799486949453

47分钟