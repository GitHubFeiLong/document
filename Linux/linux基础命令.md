centos中有用户标识符 uid，uid=0表示超级管理员

```
id 	
id-u	
id -u username
#切换成root权限
sudo -i
```

## 终端terminal

1.设备终端：键盘、鼠标、显示器

2.物理终端（/dev/console）:控制台 console

3.串行终端（/dev/ttyS#）ttyS

4.虚拟终端（tty:teletypewriters, /dev/tty#）:tty 可以有n个，Ctrl + Alt + F#

5.图形终端（/dev/tty7）:startx,xwindows,

​			CentOS6:Ctrl + Alt + F7

​			CentOS7:在哪个终端启动，即位于哪个虚拟终端

6.伪终端（pty:pseudo-tty, /dev/pts/#）:pty,SSH远程连接

7.查看当前的终端设置：tty

## 交互式

交互式接口：启动中断后，再终端设备附加一个交互式应用程序

GUI：Graphi User Interface

​			X protocol，window manager， desktop

​			Desktop：

​					GNOME（C,图形库gtk）

​					KDE（C++，图形库qt）

​					XFCE（轻量级桌面）

CLI：Command Line Interface、shell程序

## 什么是SHELL

+ shell是Linux系统的用户界面，提供了用户与内核进行交互操作的一种接口。它接收用户输入的命令，并把它送入内核去执行。

+ shell也被称为LINUX的命令解释器（command interpreter）

+ shell是一种高级程序设计语言

+ bash(主流) 

  ```
  echo $SHELL
  cat /etc/shells
  ```

  

  ![1574686539072](C:\Users\msi\AppData\Roaming\Typora\typora-user-images\1574686539072.png)

### bash shell

+ GNU Bourne-Again Shell(bash)是GNU计划中重要的工具软件之一，目前也是Linux标准的shell，与sh兼容
+ CentOS默认使用
+ 显示当前使用的shell
  + 命令：echo ${SHELL}

+ 显示当前系统使用的所有shell
  + 命令：cat /etc/shells

+ 主机名示例
  + 命令：hostname

### 命令提示符

1.命令提示符：prompt

[root@localhost ~]#

​	# 管理员

​	$ 普通用户

2.显示提示符格式

​	[root@localhost ~]#echo $PS1

3.修改提示符格式：略

文本编辑：nano

### 执行命令

1.输入命令后回车

​	提请shell程序找到键入命令所对应的可执行程序或代码，并由其分析后提交给内核分配资源将其运行起来

2.再shell中可执行的命令有两类

​	内部命令:由shell自带的，而且通过某命令形式提供

​		help内部命令列表

​		enable cmd 启用内部命令

​		enable -n cmd 禁用内部命令

​		enable -n 查看所有禁用的内部命令

​	外部命令：在文件系统路径下有对应的可执行程序文件

​		查看路径：which -a|--skip-alias; whereis

3.区别指定的命令是内部或外部命令

​	type COMMAND



**bc 计算器：**

​	**obase=2 :输入十进制，输出二进制**

​	**ibase=2:输入二进制，输出十进制**

**sz,rz：下载和上传**

**lshw:列出硬件**

**enable 查看所有内部外部命令**

**nano filename:编辑文件**

**sleep sencord：休眠**

**hostname:显示主机名**

**which command：列出命令目录**

**whereis command: 列出命令目录**

**whatis：查看命令是什么**

**type command**

**man [章节] keyword:keyword的帮助文档**

**id 或者 id -u : 查看用户**

**sudo -i：切换用户**

**pstree -p: 查看进程树**

**free -h:查看内存使用情况**

**clock -s:读取硬件时间，修改系统时间**

##  执行缓存表

Hash缓存表

1.系统初始hash表为空，当外部命令执行时，默认会从PATH路径下寻找该命令，找到后会将这条命令的路径记录到hash表中，当再次使用该命令时，shell解释器首先会查看hash表，存在将执行之，如果不存在，将回去PATH路径下寻找，利用hash缓存表可大大提高命令的调用速率

2.hash常见用法

​	hash	显示hash缓存

​	hash -l 显示hash缓存，可作为输入使用

​	hash -p path name 将命令全路径path起别名name

​	hash -t name 打印缓存中name的路径

​	hash -d name清除name缓存

​	hash -r 清除缓存

## 命令别名

1.显示当前shell进程所有可用的命令别名

​	**alias**

2.定义别名NAME，其相当于执行命令VALUE

​	**alias NAME='VALUE'**

3.在命令行中定义的别名，仅对当前shell进程有效

4.如果想用就有效，要定义在配置文件中

​	仅对当前用户：**~/.bashrc**

​	对所有用户有效：**/etc/bashrc**

5.删除别名：**unalias cdnet**

​	unalias [-a] name[name ...]

​	-a 取消所有别名

6.编辑配置给出的新配置不会立即生效

7.bash进程重新读取配置文件

​	source /path/to/config_file

​	. /path/to/config_file

8.如果别名同原命令同名，如果要执行原名令，可使用

​	\ALIASNAME

​	"ALIASNAME"

​	'ALIASNAME'

​	command ALIASNAME

​	/path/command

### 命令的执行（优先级）过程：

**别名>内部命令>hash表(外部)>$PATH（外部）**

### 命令格式

+ COMMAND [OPTIONS...] [ARGUMENTS...]

  选项：用于启用或关闭命令的某个或某些功能

  ​	短选项： -c 例如：-l, -h

  ​	长选项：--word 例如：--all, --human-readable

  参数：命令的作用对象，比如文件名，用户名等

+ 注意：
  + 多个选项以及多参数和命令之间使用空白字符隔开
  + 取消和结束命令执行：Ctrl+c，Ctrl+d
  + 多个命令可以用分号（;）分开
  + 一个命令可以用\分成多行

### 日期和时间

+ Linux的两种时钟：
  + 系统时钟：由Linux内核通过CPU的工作频率进行的
  + 硬件时钟：主板

+ 相关命令:

  + date 显示和设置系统时间

    date  +%s

    date -d @ 1509536033

  + hwclock， clock:显示硬件时钟

    clock -s, --hctosys 以硬件时钟为准，校正系统时钟

    -w，--systohc 以系统时钟为准，校正硬件时钟

+ 时区：/etc/localtime

+ 显示日历：cal -y

+ 设置时间为远程计算机时间：ntpdate 192.168.1.168

+ **timedatectl查看时间各种状态：**

+ timedatectl list-timezones: 列出所有时区

+ timedatectl set-local-rtc 1 将硬件时钟调整为与本地时钟一致, 0 为设置为 UTC 时间

+ timedatectl set-timezone Asia/Shanghai 设置系统时区为上海

### 获得帮助

+ 获取帮助的能力决定了技术的能力！
+ 多层次的帮助
  + whatis
    + 显示命令的简短描述
    + 使用数据库
    + 刚安装后不可立即使用
    + makewhatis | mandb 制作数据库
    + 使用示例：whatis cal 或者 man - f cal
  + command --help
  + man and info
  + /usr/share/doc/
  + Red Hat documentation
  + 其他网站搜索

### 命令帮助

type 查看命令是不是内部命令

+ 内部命令： help command 或 man bash

+ 外部命令：

  （1）COMMAND --help 或 COMMAND -h

  （2）使用手册（manual）：man COMMAND

  （3）信息页：info COMMAND

  （4）程序自身的帮助文档：README、INSTALL、ChangeLog

  （5）程序官方文档： 官方站点：Documentation

  （6）发行版的官方文档

  （7）Google

### man命令

1.提供命令帮助的文件

2.手册页存放在/usr/share/man

3.几乎每个命令都有man的“页面”

4.man页面分组为不同的“章节”

​	/usr/share/man 目录下的man1~man9

	* 1: 用户命令
	* 2：系统调用
	* 3：C库调用
	* 4：设备文件及特殊格式
	* 5：配置文件格式
	* 6：游戏
	* 7：杂项
	* 8：管理类的命令
	* 9：Linux内核API

5.统称为Linux手册

7.man命令的配置文件：/etc/man.config | main_db.config

​		MANPATH /PATH/TO/SOMEWHERE:指明man文件搜索位置

8.man -M /PATH/TO/SOMEWHERE COMMAND：到指定位置下搜索COMMAND命令的手册并显示

9.中文man需要安装包man-pages-zh-CN

10.man命令的操作方法：使用less命令实现

​	space，^v,^f,^F:向文件尾翻屏；

​	b,^b:向文件搜捕翻屏；

​	d,^d:向文件尾部翻半屏;

​	u,^u:向文件首部翻半屏;

​	ENTERN,^N,e,^E or j or ^j:向文件尾部翻一行;

​	y or ^Y or ^P or k or ^K:向文件首部翻一行;

​	q:退出；

​	#:跳转至第#行；

​	1G:回到文件首部；

​	G：翻至文件尾部；

### man帮助

+ man keyword passwd：keyword 章节1~9，默认查询是最小的章节

+ 查看man手册页：man [章节] keyword



+ 列出所有帮助：man -a keyword

+ 搜索man手册：man -k keyword 列出所有匹配的页面；使用whatis 数据库![1575381476129](C:\Users\msi\AppData\Roaming\Typora\typora-user-images\1575381476129.png)

  1代表就是man中的章节

+ 相当于whatis：man -f keyword

+ 打印man帮助文件的路径：man -w [章节] keyword （类似whereis keyword）

### man搜索

+ /KEYWORD：以KEYWORD指定的字符串为关键字，从当前位置向文件尾部搜索；不区分字符大小写； 键入n:下一个；键入N：上一个
+ ?KEYWORD：以KEYWORD指定的字符串为关键字，从当前位置向文件首部搜索；不区分字符大小写；n下一个；N上一个

### man帮助段落说明

+ NAME：名称及简要说明
+ SYNOPSIS 用法格式说明：
   + []	    可选内容
   + <>      必选内容
   + a|b    二选一
   + {}        分组
   + ...         同一内容可出现多次

+ DESCRIPTION 详细说明
+ OPTIONS 选项说明
+ EXAMPLES 示例
+ FILES 相关文件
+ AUTHOR 作者
+ COPYRIGHT 版本信息
+ REPORITNG BUGS bug信息
+ SEE ALSO 其他帮助参考

### info

+ man常用于命令参考，GNU工具info适合通用文档参考
+ 没有参数，列出所有的页面
+ info 页面的结构就像一个网站
+ 每一页分为“节点”
+ 链接结点之前 *，ENTER进入链接
+ info 【命令】

