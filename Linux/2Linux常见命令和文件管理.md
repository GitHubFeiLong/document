## 额外学习到的命令

查看系统字符编码

echo $LANG

查看ASCII表

man ascii

创建文件：touch file

# 常见命令

## 简单命令

+ 关机：halt，poweroff
+ 重启：reboot
  + -f：强制，不调用shutdown
  + -p：切断电源

+ 关机或重启：shutdown
  + shutdown [OPTION].. [TIME][MESSAGE] [MESSAGE]
  + -r：reboot 重启
  + -h：halt 关机
  + -c：cancel 取消
  + TIME：无指定，默认相当于+1（CentOS7）
    + now：立刻，相当于+0
    + +m：相对时间表示法，几分钟之后；
    + hh:mm：绝对时间表示，指明具体时间

+ 用户登录信息查看命令
  + whoami：显示当前登录有效用户
  + who：系统当前所有的登录会话
  + w：系统当前所有的登录会话及所做的操作

+ nano 文本编辑器

+ screen命令(安装**rpm -ivh** /run/media/centos7/CentOS\ 7\ x86_64/**Packages/screen-4.1.0-0.25.20120314git3c2946.el7.x86_64.rpm**  )：

  + 查看screen有哪些会话
    + screen -r
+ 创建新screen会话
    + screen -S [SESSION]
  + 加入screen会话
  + screen -x [SESSION]
  + 退出并关闭screen会话
    + exit
+ 剥离当前screen会话
    + Ctrl +a,d
  + 显示所有以经打开的screen会话
  + screen -ls
  + 恢复某screen会话
    + screen -r [SESSION]

+ echo命令

  + 功能：显示字符
  + 语法：echo [-neE] [字符串]

  + 说明：echo会将输入的字符串送往标准输出。输出的字符串以空白字符隔开，并在最后加上换行号
  + 选项：
    + -E（默认）不支持\解释功能
    + -n 不自动换行
    + -e 启用 \ 字符的解释功能

  + 显示变量：

    + echo "$VAR_NAME"  变量会替换，弱引用

    + echo '$VAR_NAME'    变量不会替换，强引用

+ 命令行扩展：$() 或 ``

  + 把一个命令的输出打印给另一个命令的参数

    **echo "This system's name is $(hostname)"**

    This system's name is 192.168.30.128

    **echo "i am \`whoami`"**

    i am root

+ 括号扩展：{}
  + 打印重复字符串的简化形式
    + echo file{1,3,5}，结果为： file1,file2,file3
    + rm -f file{1,3,5}
    + echo {1..10}
    + echo {a..z}
    + echo {000..20..2}

+ '' , "" , \`` 区别： 
  + \``:变量，命令都能识别
  + ''：变量，命令都不能识别
  + "" ：只能识别变量，不能识别命令

## 命令行历史

+ 保存你输入的命令历史。可以用它来重复执行命令
+ 登录shell时，会读取命令历史文件中记录下的命令 ~/.bash_history
+ 登录进shell后新执行的命令只会记录在缓存中；这些命令会用户退出时“追加”至命令历史文件中
+ 重复前一个命令，有四种方法
  + 重复前一个命令使用上方向键，并回车执行
  + 按!!并回车执行
  + 输入!-1并回车执行
  + 按Ctrl+p并回车执行

+ !:0 执行前一条命令（去除参数）
+ Ctrl + n 显示当前历史中的下一条命令，但不执行
+ Ctrl + j 执行当前命令
+ !n 执行history命令输出对应序号n的命令
+ !-n 执行history历史中倒数第n个命令
+ !string 重复前一个以“string”开头的命令
+ !?string 重复前一个包含“string”的命令
+ !string:p 仅打印命令历史，而不执行
+ !$:p  打印输出!$ (上一条命令的最后一个参数) 的内容
+ !*:p  打印输出 !* (上一条命令的所有参数)的内容
+ ^string 删除上一条命令中的第一个string
+ ^string1^string2 将上一条命令中的第一个个string1替换为string2
+ !:gs/string1/string2 将上一条命令中所有的string1都替换为string2

+ 使用up(向上)和down（向下）键来浏览从前输入的命令
+ ctrl -r来在命令历史中搜索命令
  + （reverse-i-search）`':

+ Ctrl+g：从历史搜索模式退出
+ 要重新调用前一个命令中最后一个参数
  + !$ 表示
  + Esc，.(点击Esc键松开，然后点击 . 键)
  + Alt + . (按住Alt键的同时点击.键)

### 命令history

+ history [-c] [-d offset] [n]
+ history -anrw [filename]
+ history -ps arg [arg...]
  + -c：清空（内存）命令历史
  + -d offset：删除历史中指定的第offset个命令
  + n：显示最近的n条记录
  + -a：追加本次会话新执行的命令历史列表至历史文件
  + -r：读历史文件附加到历史列表
  + -w：保存历史列表到指定的历史文件
  + -n：地历史文件中未读过的行到历史列表
  + -p：展开历史参数成多行，但不存在历史列表中
  + -s：展开历史参数成一行，附加在历史列表后

### 命令历史相关环境变量

+ HISTSIZE：命令历史记录的条数
+ HISTFILE：指定历史文件，默认为~/.bash_history
+ HISTFILESIZE:命令历史文件记录历史的条数
+ HISTTIMEFORMAT= “%F %T” 显示时间
+ HISTIGNORE="str1:str2*..." 忽略str1命令，str2开头的历史
+ 控制命令历史的记录方式：
  + 环境变量：HISTCONTROL
    + ignoredups 默认，忽略重复的命令，连续且相同为“重复”
    + ignorespace 忽略所有以空白开头的命令
    + ignoreboth 相当于ignoredups，ignorespace的组合
    + erasedups 删除重复命令

+ export 变量名=“值”
+ 存放在 /etc/profile 或 ~/.bash_profile

### 快捷键

+ Ctrl + l 清屏幕，相当于clear命令
+ Ctrl + o 执行当前命令，并重新显示本命令
+ Ctrl + s 阻止屏幕输出，锁定
+ Ctrl + q 允许屏幕输出，解锁
+ Ctrl + c 终止命令
+ Ctrl + z 挂起命令
+ Ctrl + a 光标移到命令行首，相当于Home
+ Ctrl + e 光标移到命令行尾，相当于End
+ Ctrl + f 光标向右移动一个字符
+ Ctrl + b 光标向左移动一个字符
+ Alt + f 光标向右移动一个单词尾
+ Alt + b 光标向左移动一个单词首
+ Ctrl + xx 光标在命令行首和光标之间
+ Ctrl + u 从光标处删除至命令行首
+ Ctrl + k 从光标处删除至命令行尾
+ Alt + r 删除当前整行
+ Ctrl + w 从光标处向左删除至单词尾
+ Alt + d 从光标处向右删除至单词尾
+ Ctrl + d 删除光标处的一个字符
+ Ctrl + h删除光标前的一个字符
+ Ctrl + y 将删除的字符粘贴至光标后
+ Alt + c 从光标处开始向右更改为首字母大写的单词
+ Alt + u 从光标处开始，将右边一个单词更改为大写
+ Alt + l 从光标处开始，将右边一个单词更改为小写
+ Ctrl + t 交换光标处和之前的字符位置
+ Alt + t 交换光标处和之前的单词位置
+ Alt + N 提示输入指定字符后，重复显示该字符N次
+ 注意：Alt 组合快捷键经常和其他软件冲突