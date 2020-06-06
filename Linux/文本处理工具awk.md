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

#### FS：输入字段分隔符，默认为空白字符，相当于-F

范例：

```bash
awk -v FS=':' '{print $1,FS,$3}' /etc/passwd
awk -F: '{print $1,$3,$7}' /etc/passwd
S=:;awk -v FS=$S '{print $1,FS,$3}' /etc/passwd
```

#### OFS：输出字段分隔符,默认空白字符

范例：

```bash
awk -v FS=':' -v OFS=':' '{print $1,$3,$7}' /etc/passwd
```

#### RS:输入记录分隔符，指定输入时的换行符

范例：

```bash
awk -v RS=' ' '{print }' /etc/passwd

[root@centos7: ~]#cat awk.txt 
a,b,c;d,e
f;h,i,j
k;l;m;
n,o,p
[root@centos7: ~]#awk -F"," '{print $2}' awk.txt 
b
i

o
# 默认输入是回车换行为分割记录（使用RS变量指定分号切割记录）
[root@centos7: ~]#awk -F"," -v RS=";"  '{print $2}' awk.txt 
b
e
f
i


o
```

#### ORS:输出记录分隔符，输出时用指定符号代替换行符

范例：

```bash
awk -v RS=' ' -v ORS='###' '{print $0}' /etc/passwd

[root@centos7: ~]#awk -F"," -v RS=";" -v ORS='##'  '{print $2}' awk.txt 
b##e
f##i######o##
```

#### NF:字段数量

范例:

```bash
#引用变量时，变量前不需加$
awk -F: '{print NF}' /etc/fstab 
awk -F: '{print $(NF-1)}' /etc/passwd
```

范例：发现连接数超过3个以上的IP拒绝访问

```bash
cat deny_dos.sh
while true;do
	ss -nt | grep "^ESTAB" | awk -F"[[:space:]]+|:" '{print $(NF-2)}' |sort | uniq -c | while read count ip; do
	if [ ${count} -gt 3 ]; then
		iptables -A INPUT -s ${ip} -j REJECT
	fi
  done
  sleep 1000;
done  
```



#### NR:记录号

范例:

```bash
#统计所有行行号
[root@centos7: ~]#awk '{print NR}' /etc/fstab;
1
2
3
4
5
6
7
8
9
10
11
12
#统计最后一行的行号
awk 'END {print NR}' /etc/fstab
12
```



#### FNR:各文件分别计数，记录号

范例:

```bash
#每个文件独立的记录行号
[root@centos7: ~]#awk -F: '{print FNR}' /etc/passwd /etc/fstab 
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
1
2
3
4
5
6
7
8
9
10
11
12
```



#### FILENAME:当前文件名

范例：

```bash
awk '{print FILENAME}' /etc/fstab
```



#### ARGC:命令行参数的个数

范例：

```bash
[root@centos7: ~]#awk 'BEGIN {print ARGC}' /etc/fstab /etc/inittab
3
[root@centos7: ~]#awk 'BEGIN {print ARGV[0],ARGV[1],ARGV[2]}' /etc/fstab /etc/inittab
awk /etc/fstab /etc/inittab
```

#### ARGV：数组，保存的是命令行所给定的各参数

范例：

```bash
[root@centos7: ~]#awk 'BEGIN {print ARGV[0]}' /etc/fstab /etc/inittab
awk
[root@centos7: ~]#awk 'BEGIN {print ARGV[1]}' /etc/fstab /etc/inittab
/etc/fstab
```



#### 自定义变量（区分字符大小写）

+ -v var=value
+ 在program中直接定义

范例：

```bash
#程序外定义变量
[root@centos7: ~]#awk -v name="cfl" '{print name}' /etc/issue
cfl
cfl
cfl
#程序内定义
[root@centos7: ~]#awk 'BEGIN {name="cfl";print name}' /etc/issue
cfl

#将程序(单引号中的内容)定义在文件中，然后命令调用文件
[root@centos7: ~]#cat f1.awk 
BEGIN {name="cfl";print name}
[root@centos7: ~]#awk -f f1.awk /etc/issue
cfl
```



### 动作printf

printf可以实现格式化输出

格式：

```bash
printf "FORMAT", item1, item2,...
```

说明：

+ 必须指定FORMAT
+ 不会自动换行，需要显式地给出换行控制符，\n
+ FORMAT中需要分别为后面每个item指定格式符

格式符：与item一一对应

+ %c：显示字符的ASCII码
+ %d,%l：显示十进制整数
+ %e,%E:显示科学计数法数值
+ %f：显示为浮点数
+ %g,%G：以科学计数法或浮点形式显示数值
+ %s：显示字符串
+ %u：无符号整数
+ %%：显示%自身

修饰符

```bash
#[.#] 第一个数字控制显示的宽度；第二个#表示小数点后的精度，如%3.1f
- 左对齐（默认右对齐）如%-15s
+ 显示数值的正负符号  如%+d
```

范例：

```bash
awk -F '{printf "%s",$1}' /etc/passwd
awk -F '{printf "%s\n",$1}' /etc/passwd
awk -F:  '{printf "%s===%d\n",$1,$3}' /etc/passwd
#左对齐
awk -F:  '{printf "%-20s===%20d\n",$1,$3}' /etc/passwd

awk -F:  'BEGIN{print "--------------------------------"} {printf "%-20s|%10d\n",$1,$3}' /etc/passwd

```



### 操作符

算数操作符

```bash
x+y,x-y,x*y,x/y,x^y,x%y
-x:转换为负数
+x：将字串转换为数值
```

字符串操作符：没有符号的操作符，字符串连接

赋值操作符：

```bash
=,+=,-=,*=,/=,%=,^=,++,--
```

范例:

```bash
awk 'BEGIN{i=0;print ++i, i}'
awk 'BEGIN{i=0;print i++, i}'
```

比较操作符：

```bash
==,!=,>,>=,<,<=
```

模式匹配符：

```bash
~ 左边是否和右边匹配，包含关系
!~ 是否不匹配
```

范例：

```bash
[root@centos7: ~]#awk -F: '$0 ~ /root/{print $1}' /etc/passwd
root
operator

[root@centos7: ~]#awk '$0 ~"^root"' /etc/passwd
root:x:0:0:root:/root:/bin/bash

awk '%0 !~ /root/' /etc/passwd
#省略{}表示{print $0}
[root@centos7: ~]#awk -F: '$3==0' /etc/passwd
root:x:0:0:root:/root:/bin/bash
```

逻辑操作符：

```bash
与&&，或||，非!
```

范例：

```bash
awk -F: '$3>=0 && $3 <=1000 {print $1}' /etc/passwd
[root@centos7: ~]#awk -F: '$3==0 || $3>=1000{print $1}' /etc/passwd
root
nfsnobody
centos7
mage

awk -F: '!($3==0){print $1}' /etc/passwd
awk -F: '!($3>=500){print $3}' /etc/passwd
```

条件表达式（三目表达式）

```bash
selector?if-true-expression:if-false-expression
```

范例：

```bash

[root@centos7: ~]#awk -F: '{$3>=1000?usertype="Common User":usertype="sysUser"; printf "%-20s:%12s\n",$1,usertype}' /etc/passwd

```



### 模式PATTERN

PATTERN：根据pattern条件，过滤匹配的行，再做处理

1. 如果未指定：空模式，匹配每一行

   范例：

   ```bash
   awk -F: '{print $1}' /etc/passwd
   ```

2. /regular expression/：仅处理能够模式匹配到的行，需要用/ / 括起来

   范例：

   ```bash
   awk '/^UUID/{print $1}' /etc/fstab
   awk '!/^UUID/{print $1}' /etc/fstab
   ```

3. relational expression:关系表达式，结果为“真”才会被处理

   真：结果为非0值，非空字符串

   假：结果为空字符串或0值
   范例：

   ```bash
   awk -F: 'i=1;j=1{print i,j}' /etc/passwd
   awk '!0' /etc/passwd
   awk '!1' /etc/passwd
   awk -F: '$3>=1000{print $1,$3}' /etc/passwd
   awk -F: '$3<1000{print $1,$3}' /etc/passwd
   awk -F: '$NF=="/bin/bash"{print $1,$NF}' /etc/passwd
   awk -F: '$NF ~ /bash$/{print $1,$NF}' /etc/passwd
   ```

4. line range:行范围

   /pat1/，/pat2/ 不支持直接给出数字格式

   范例：

   ```bash
   awk -F: '/^root\>/,/^nobody\>/{print $1}' /etc/passwd
   awk -F: 'NR>=10&&NR<=20{print NR,$1}' /etc/passwd
   ```

5. BEGIN/END模式

   BEGIN{}：仅再开始处理文件中的文本之前执行一次

   END{}：仅在文本处理完成之后执行一次

   范例：

   ```bash
   awk -F: 'BEGIN{print "USER USERID"}{print $1":"$3}END{print "END FILE"}' /etc/passwd
   awk -F: '{print "USER USERID";print $1":"$3}END{print "END FILE"}' /etc/passwd
   seq 10 | awk 'i=0'
   seq 10 | awk 'i=1'
   seq 10 | awk 'i=!i'
   seq 10 | awk '{i=!i;print i}'
   seq 10 | awk '!(i=!i)'
   seq 10 | awk -v i=1 'i=!i'
   ```

   ### 条件判断if-else

   语法：

   ```bash
   if(condition){statement;...}{else statement}
   if(condition1){statement1}sles if(condition2){statement2}else{seatement3}
   ```

   使用场景



