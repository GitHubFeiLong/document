# CentOS安装RabbitMQ



## 官网查看RabbitMQ和对应的Erlang版本

1. 进入 [RabbitMQ 官网](www.rabbitmq.com) ，点击 顶上的 Get Started

![1590987138633](..\typora-user-images\1590987138633.png)

2. 点击Download + Installation

![1590987165098](..\typora-user-images\1590987165098.png)



3. 点击左侧的[Erlang Versions](https://www.rabbitmq.com/which-erlang.html) 查看对应版本

![1590987199947](..\typora-user-images\1590987199947.png)



## 下载Erlang 和 RabbitMQ的压缩包文件

### 下载RabbitMQ



#### uninx [直通车链接](https://www.rabbitmq.com/install-generic-unix.html)

![1590987490502](..\typora-user-images\1590987490502.png)





根据第一步，知道了自己要下载erlang的版本后，进入 [erlang官网](www.erlang.org) ， 点击页面顶部的 **DOWNLOADS**
![1591000236508](..\typora-user-images\1591000236508.png)



在右侧选择需要下载的**版本**，然后在左侧下载**Source File**（源文件）下载。

![1591000353957](..\typora-user-images\1591000353957.png)



或者在 http://erlang.org/download/ 页面下载。

往下拉，找到otp_src_xx.xx.tar.gz 格式的文件（二进制源代码），注意别下错文件

![1590988340356](..\typora-user-images\1590988340356.png)



## 解压进行安装

### 先安装必备文件

```bash
#安装其他必备软件
[root@localhost: local]#yum -y install make gcc gcc-c++ kernel-devel m4 ncurses-devel openssl-devel ncurses-devel
```



### 1.安装Erlang语言

```bash
#解压
[root@localhost: local]#tar xvf otp_src_22.0.tar.gz
#进入解压的目录后，生成MakeFile文件（编译前）
[root@localhost: otp_src_22.0]#./configure 
#编译 && 安装
[root@localhost: otp_src_22.0]#make -j2 && make install
#时间有点长，然后验证，输入 halt(). 退出
[root@localhost: otp_src_22.0]#bin/erl
Erlang/OTP 22 [erts-10.4] [source] [64-bit] [smp:1:1] [ds:1:1:10] [async-threads:1] [hipe]

Eshell V10.4  (abort with ^G)
1> halt().
# 设置环境变量
[root@localhost: otp_src_22.0]#echo 'export PATH=$PATH:/usr/local/otp_src_22.0/bin' >> /etc/profile
[root@localhost: otp_src_22.0]#source /etc/profile
#和上面一样进行验证
[root@localhost: otp_src_22.0]#erl

```

### 2.安装RabbitMQ

```bash
#解压
[root@localhost: local]#tar xvf rabbitmq-server-generic-unix-3.7.15.tar.xz
#随后移动至/usr/local/下 改名rabbitmq：
[root@localhost: local]#cp -r rabbitmq_server-3.7.15/ /usr/local/rabbitmq

#添加到环境变量去
[root@localhost: local]#echo 'export PATH=$PATH:/usr/local/rabbitmq/sbin' >> /etc/profile
[root@localhost: local]#source /etc/profile
```

#### 启动MQ管理方式

```bash
#启动后台管理
rabbitmq-plugins enable rabbitmq_management  
#后台运行rabbitmq
rabbitmq-server -detached   
```

#### 如果启动MQ报错

[启动rabbitmq，提示ERROR: node with name "rabbit" already running on "localhost"](https://www.cnblogs.com/Sisiflying/p/6386988.html)

```bash
➜  ~ rabbitmq-server
ERROR: node with name "rabbit" already running on "localhost"
➜  ~ ps aux|grep epmd
wangyizhe         949   0.0  0.0  2461372    256   ??  S    二04下午   0:00.99 /usr/local/Cellar/erlang/19.0.2/lib/erlang/erts-8.0.2/bin/epmd -daemon
wangyizhe       14871   0.0  0.0  2424600    432 s001  R+    4:12下午   0:00.00 grep --color=auto --exclude-dir=.bzr --exclude-dir=CVS --exclude-dir=.git --exclude-dir=.hg --exclude-dir=.svn epmd
➜  ~ ps aux|grep erl
wangyizhe        4519   0.7  0.4  4134296  29636 s000  S    二05下午  12:43.67 /usr/local/Cellar/erlang/19.0.2/lib/erlang/erts-8.0.2/bin/beam.smp -W w -A 64 -P 1048576 -t 5000000 -stbt db -K true -B i -- -root /usr/local/Cellar/erlang/19.0.2/lib/erlang -progname erl -- -home /Users/wangyizhe -- -pa /usr/local/Cellar/rabbitmq/3.6.4/ebin -noshell -noinput -s rabbit boot -sname rabbit@localhost -boot /usr/local/opt/erlang/lib/erlang/bin/start_clean -kernel inet_default_connect_options [{nodelay,true}] -rabbit tcp_listeners [{"127.0.0.1",5672}] -sasl errlog_type error -sasl sasl_error_logger false -rabbit error_logger {file,"/usr/local/var/log/rabbitmq/rabbit@localhost.log"} -rabbit sasl_error_logger {file,"/usr/local/var/log/rabbitmq/rabbit@localhost-sasl.log"} -rabbit enabled_plugins_file "/usr/local/etc/rabbitmq/enabled_plugins" -rabbit plugins_dir "/usr/local/Cellar/rabbitmq/3.6.4/plugins" -rabbit plugins_expand_dir "/usr/local/var/lib/rabbitmq/mnesia/rabbit@localhost-plugins-expand" -os_mon start_cpu_sup false -os_mon start_disksup false -os_mon start_memsup false -mnesia dir "/usr/local/var/lib/rabbitmq/mnesia/rabbit@localhost" -kernel inet_dist_listen_min 25672 -kernel inet_dist_listen_max 25672
wangyizhe        4532   0.0  0.0  2434824    464   ??  Ss   二05下午   0:12.77 erl_child_setup 256
wangyizhe         949   0.0  0.0  2461372    256   ??  S    二04下午   0:00.99 /usr/local/Cellar/erlang/19.0.2/lib/erlang/erts-8.0.2/bin/epmd -daemon
wangyizhe       14897   0.0  0.0  2424600    472 s001  R+    4:12下午   0:00.00 grep --color=auto --exclude-dir=.bzr --exclude-dir=CVS --exclude-dir=.git --exclude-dir=.hg --exclude-dir=.svn erl
➜  ~ kill -9 4519
➜  ~ rabbitmq-server
 
              RabbitMQ 3.6.4. Copyright (C) 2007-2016 Pivotal Software, Inc.
  ##  ##      Licensed under the MPL.  See http://www.rabbitmq.com/
  ##  ##
  ##########  Logs: /usr/local/var/log/rabbitmq/rabbit@localhost.log
  ######  ##        /usr/local/var/log/rabbitmq/rabbit@localhost-sasl.log
  ##########
              Starting broker...
 completed with 10 plugins.
```



### 添加用户和权限

默认网页guest用户是不允许访问的，需要增加一个用户修改一下权限，代码如下：
 添加用户:`rabbitmqctl add_user admin admin`

添加权限:`rabbitmqctl set_permissions -p "/" admin ".*" ".*" ".*"`

修改用户角色:`rabbitmqctl set_user_tags admin administrator`

## RabbitMQ的简单指令

```bash
启动服务：rabbitmq-server -detached [ /usr/local/rabbitmq/sbin/rabbitmq-server  -detached ]
重启服务：rabbitmq-server restart
关闭服务：rabbitmqctl stop
查看状态：rabbitmqctl status
列出角色：rabbitmqctl list_users
开启某个插件：rabbitmq-pluginsenable xxx
关闭某个插件：rabbitmq-pluginsdisablexxx
注意：重启服务器后生效。
```

