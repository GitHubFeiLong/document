tty

pidof：查看进程号例如 pidof java

/proc : 该目录放的进程信息

tail -f file

/dev/null 用来删除东西

## 标准输入和输出

+ 程序：指令+数据
  + 读入数据：input
  + 输出数据：output

+ 打开的文件都有一个fd：file descriptor（文件描述符）
+ Linux给程序提供了三种I/O设备
  + 标准输入（STDIN）-0 默认接受来自键盘的输入
  + 标准输出（STDOUT）-1默认输出到终端窗口
  + 标准错误（STDERR）-2默认输出到终端窗口

+ I/O重定向：改变默认位置

### 把输出和错误重新定向到文件

+ STDOUT和STDERR可以被重定向到文件
  + 命令 操作符 文件名
  + 支持的操作符包括：
    + `>` 把STDOUT重定向到文件
    + 2> 把STDERR重定向到文件
    + &> 把所有输出重定向到文件

+ `>`文件内容会被覆盖
  + set -C 禁止将内容覆盖已有文件，但可以追加
  + `>| file` 强制覆盖
  + set +C 允许覆盖

+ `>>` 原有内容基础上，追加内容
+ 2> 覆盖重定向错误输出数据流
+ 2>> 追加重定向错误输出数据流
+ 标准输出和错误输出各自定向不同位置
  + COMMAND > /path/to?file.out 2 > /path/to/error.out

+ 合并标准输出和错误输出为同一个数据流进行重定向
  + &> 覆盖重定向
  + &>> 追加重定向
  + COMMAND > /path/to/file.out 2>&1 (顺序很重要)
  + COMMAND >> /path/to/file.out 2>&1

+ `()`: 合并多个程序的STDOUT
  + 例：`(cal 2007; cal 2008)` > all.txt

### tr命令

+ tr 转换和删除字符
+ tr [OPTION]... SET1 [SET2]
+ 选项：
  + -c -C --complement：取字符集的补集
  + -d --delete：删除所有属于第一字符集的字符
  + -s --squeeze-repeats：把连续重复的字符以单独一个字符表示
  + -t --truncate-set1：将第一个字符集对应字符转化为第二字符集对应的字符

+ [:alnum:]：字母和数字
+ [:alpha:] : 字母
+ [:cntrl:] ：控制（非打印）字符 
+ [:digit:] :数字
+ [:lower:] ：小写字母
+ [:print:]：可打印字符
+ [:punct:]：标点符号
+ [:space:] ：空白字符
+ [:upper:]：大写字母
+ [:xdigit:]：十六进制字符

### 文件中导入STDIN

+ 使用`<`来重定向标准输入
+ 某些命令能够接受从文件中导入的STDIN
  + tr 'a-z' 'A-Z' < /etc/issue
  + 该命令会把/etc/issue中的小写字符都转换成大写字符

+ tr -d abc < /etc/fstab
  + 删除fstab文件中的所有abc中任意字符

+ cat > file

  + hello cat

    你好 CAT

  + 按ctrl +d 离开，可以使用文件来代替键盘的输入

+ cat < file1 > file2
  + 将file1的内容复制到file2

+ cat < file1 >> file1
  + 循环追加。

### 把多行发送给STDIN

+ 使用 `<< 终止词` 命令从键盘把多行重定向给STDIN

  + 直到 终止词 位置的所有文本都发送给STDIN

  + 有时被称为就地文本（here documents）

  + mail -s "Please Call" admin@google.com << END

    `>`HI Wang

    `>`

    `>` Please give me实打实

    `>这个时发送的i西澳西`

    `>`乒乓球，带球，海域模式，事但撒旦

    `>END`

    当文本出现END就结束了，然后多行重定向