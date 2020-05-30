# Nginx 练习

## Nginx 简单使用

1. 绑定hosts

   ```bash
   [root@centos7: ~]#vim /etc/hosts
   #在文件后天添加下面，然后保存退出（前面IP地址是本机IP）
   192.168.2.100 www.heytool.com
   192.168.2.100 bbs.heytool.com
   ```

2. 创建测试站点

   ```bash
   [root@centos7: ~]# mkdir –p /www/html/www.heytool.com
   [root@centos7: ~]# mkdir –p /www/html/bbs.heytool.com
   
   [root@centos7: ~]# echo "www.heytool.com" > /www/html/www.heytool.com
   [root@centos7: ~]# echo "bbs.heytool.com" > /www/html/bbs.heytool.com
   ```

3. 修改Nginx的配置文件

   ```bash
   [root@centos7: conf]#vim nginx.conf
   server {
           listen       80;
           server_name  www.heytool.com;
           root /www/html/www.heytool.com;
           index index.html index.htm;
           location / {
                   root /www/html/www.heytool.com;
           }
   
   server{
           listen 80;
           server_name bbs.heytool.com;
           root /www/html/www.heytool.com;
           index index.html index.htm;
           location / {
                   root /www/html/bbs.heytool.com;
           }
           error_page 500 502 503 504 /50x.html;
           location = /580x.html{
                   root /www/html/bbs.heytool.com;
           }
       }
   ```

   

4. 启动Nginx

   ```bash
   # 检查配置文件是否写错
   [root@centos7: nginx-1.18.0]# sbin/nginx -t
   nginx: the configuration file /usr/local/nginx-1.18.0/conf/nginx.conf syntax is ok
   nginx: configuration file /usr/local/nginx-1.18.0/conf/nginx.conf test is successful
   # 启动nginx
   [root@centos7: nginx-1.18.0]# sbin/nginx
   
   #其他命令 
   nginx -s reload #重启
   nginx -s stop#停止
   ```

5. 测试(本机)
   

   5.1 浏览器测试
   输入www.heytool.com

   ![1590813608043](..\typora-user-images\1590813608043.png)
   输入bbs.heytool.com

   ![1590813691552](..\typora-user-images\1590813691552.png)
   5.2 终端测试

   ```bash
   [root@centos7: conf]#curl www.heytool.com
   this is a.ttlsa.com!
   [root@centos7: conf]#curl bbs.heytool.com
   this is a.ttlsa.com!
   ```

   

## Nginx配置虚拟主机

**准备站点**

​	我们站点统一放到/data/site下，每个站点根目录名称都和域名相同,具体如下。

- 新建a.ttlsa.com的站点根目录

```bash
[root@centos7: conf]#mkdir -p /data/site/a.ttlsa.com
```

- 新建a 站的首页index.html

```bash
[root@centos7: conf]#echo "this is a.ttlsa.com!" > /data/site/a.ttlsa.com/index.html
```

- 新建b.ttlsa.com 站点根目录

```bash
[root@centos7: conf]#mkdir -p /data/site/b.ttlsa.com
```

- 新建b 站首页index.html,内容如this is b.ttlsa.com！

```bash
[root@centos7: conf]#echo "this is b.ttlsa.com!" > /data/site/b.ttlsa.com/index.html
```

- 新建日志文件目录

```bash
[root@centos7: conf]#mkdir -p /data/logs/nginx
```

​	我们统一将日志存放到/data/logs下,这边是存放nginx日志,所以nginx日志保持在当前的nginx目录下.日志统
一存放相对来说比较规范（如果你不习惯,你可以按自己的方式来做）



**配置nginx 虚拟主机**

1. 增加nginx 主配置文件nginx.conf,先配置nginx日志格式，在nginx.conf找到如下内容，并且将#注释标志去掉

   ```bash
    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';
   ```

2. 配置nginx的主配置文件

   ```bash
   [root@centos7: conf]#vim nginx.conf
    server {
           listen       80;
           server_name  a.ttlsa.com;
           access_log  /data/logs/nginx/a.ttlsa.com-access.log main;
           root /data/site/a.ttlsa.com;
   
           location / {
           }
   }
   server{
           server_name b.ttlsa.com;
           listen 80;
           root /data/site/b.ttlsa.com;
           access_log /data/logs/nginx/b.ttlsa.com-access.log main;
   
           location / {
   
           }
       }
   ```

3. 配置讲解
   server{}：配置虚拟主机必须有这个段。
   server_name：虚拟主机的域名，可以写多个域名，类似于别名，比如说你可以配置成server_name b.ttlsa.com c.ttlsa.com d.ttlsa.com，这样的话，访问任何一个域名，内容都是一样的。
   listen 80：监听ip和端口，这边仅仅只有端口，表示当前服务器所有ip的80端口，如果只想监听127.0.0.1
   的80，写法如下：
   listen 127.0.0.1:80
   root /data/site/b.ttlsa.com：站点根目录，你网站文件存放的地方。注：站点目录和域名尽量一样，养成一个好习惯
   access_log /data/logs/nginx/b.ttlsa.com-access.log main：访问日志
   location /{} 默认uri

4. 添加host

   ```bash
   #ip记得换成自己的ip
   [root@centos7: conf]#echo -e '192.168.2.100 a.ttlsa.com\n192.168.2.100 b.ttlsa.com' >> /etc/hosts
   ```

5. 重启Nginx

   ```bash
   [root@centos7: sbin]#nginx -t
   nginx: the configuration file /usr/local/nginx-1.18.0/conf/nginx.conf syntax is ok
   nginx: configuration file /usr/local/nginx-1.18.0/conf/nginx.conf test is successful
   [root@centos7: sbin]#nginx -s reload
   ```

   

6. 使用curl测试

   ```bash
   [root@centos7: sbin]# curl http://a.ttlsa.com
   this is a.ttlsa.com! //a站点内容
   [root@centos7: sbin]# curl http://b.ttlsa.com
   this is b.ttlsa.com! //b站点内容
   ```

   

## Nginx location配置

今天讲下location的用法，部分内容是直接从网络上摘取的，这边做了一个整理，为了便于理解和学习，我这边做了一些例子。

语法规则：location [=|~|~*|^~] /uri/ { …}

| 规则 | 解释                                                         | 优先级 |
| :--: | :----------------------------------------------------------- | ------ |
|  =   | 对URI做精确匹配                                              | ⭐⭐⭐⭐⭐  |
|  ^~  | 对URI的最左边部分做匹配检查，不区分字符大小写                | ⭐⭐⭐⭐   |
|  ~   | 对URI的做正则表达式模式匹配，区分字符大小写                  | ⭐⭐⭐    |
|  ~*  | 对URI的做正则表达式模式匹配，不区分字符大小写<br />(和上面的唯一区别就是大小写) | ⭐⭐⭐    |
|  !~  | 区分大小写不匹配                                             |        |
| !~*  | 不区分大小写不匹配的正则                                     |        |
|  /   | 通用匹配，任何请求都会匹配到，默认匹配.                      | ⭐      |

下面讲讲这些语法的一些规则和优先级
多个location配置的情况下匹配顺序为（参考资料而来，还未实际验证，试试就知道了，不必拘泥，仅供参
考）
示例：

```bash
# 匹配规则
location / {
echo "/"; //需要安装echo模块才行,这边大家可以改成各自的规则
}
location = / {
echo "=/";
}
location = /nginx {
echo "=/nginx";
}
location ~ \.(gif|jpg|png|js|css)$ {
echo "small-gif/jpg/png";
}
location ~* \.png$ {
echo "all-png";
}
location ^~ /static/ {
echo "static";
}

#以下是各种的访问情况
访问http://a.ttlsa.com/ .因为/是完全匹配的
如下：
# curl http://a.ttlsa.com/
=/

访问http://a.ttlsa.com/nginx,因为完全匹配了”=/nginx”
# curl http://a.ttlsa.com/nginx
=/nginx

访问http://a.ttlsa.com/nginx,从第一个开始尝试匹配,最后匹配到了~* \.png$ .
# curl http://a.ttlsa.com/xxx/1111.PNG （注意,这是大写）
all-png
访问http://a.ttlsa.com/static/1111.png,虽然static放在最后面,但是因为有^的缘故,他是最匹配的.
# curl http://a.ttlsa.com/static/1111.png
Static

好了，最后给出我们先上环境的静态文件的匹配规则
location ~* .*\.(js|css)?$ {
    expires 7d; //7天过期,后续讲解
    access_log off; //不保存日志
}
location ~* .*\.(png|jpg|gif|jpeg|bmp|ico)?$ {
    expires 7d;
    access_log off;
}
location ~* .*\.(zip|rar|exe|msi|iso|gho|mp3|rmvb|mp4|wma|wmv|rm)?$ {
	deny all; //禁止这些文件下载，大家可以根据自己的环境来配置
}

```





## Nginx的日志配置

日志对于统计排错来说非常有利的。本文总结了nginx日志相关的配置如access_log、log_format、
open_log_file_cache、log_not_found、log_subrequest、rewrite_log、error_log。
nginx有一个非常灵活的日志记录模式。每个级别的配置可以有各自独立的访问日志。日志格式通过log_format 命令来定义。ngx_http_log_module是用来定义请求日志格式的。

### 1.access_log 指令

用access_log指令日志文件存放路径
注意：
在定义日志目录中要注意的是，nginx进程设置的用户和组必须有对该路径创建文件的权限，
假设nginx的usr指令设置的用户名 和用户组都是www，而logs 目录的用户名和组是root，那么日志文件将无法被创建。

语法: 

```bash
access_log path [format [buffer=size [flush=time]]];
access_log path format gzip[=level] [buffer=size] [flush=time];
access_log syslog:server=address[,parameter=value] [format];
access_log off;
```

默认值:
		 access_log logs/access.log combined;
配置段: 
		http, server, location, if in location, limit_except

注释：

```bash
gzip压缩等级。
buffer设置内存缓存区大小。
flush保存在缓存区中的最长时间。
不记录日志：access_log off;
使用默认combined格式记录日志：access_log logs/access.log 或access_log logs/access.log combined;
```



### 2.log_format 指令

**语法:** log_format name string …;
**默认值:** log_format combined “…”;
**配置段:** http
name表示格式名称，string表示定义的格式。log_format有一个默认的无需设置的combined日志格式，相当于apache的combined日志格式，如下所示：

```bash
log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                  '$status $body_bytes_sent "$http_referer" '
                  '"$http_user_agent" "$http_x_forwarded_for"';
```

如果nginx位于负载均衡器，squid，nginx反向代理之后，web服务器无法直接获取到客户端真实的IP地址了。
$remote_addr获取反向代理的IP地址。反向代理服务器在转发请求的http头信息中，可以增加X-Forwarded-For信息，用来记录客户端IP地址和客户端请求的服务器地址。

如下所示：

```bash
log_format porxy '$http_x_forwarded_for - $remote_user [$time_local] '
				' "$request" $status $body_bytes_sent '
				' "$http_referer" "$http_user_agent" ';
```

日志格式允许包含的变量注释如下：

```bash
$remote_addr, $http_x_forwarded_for 记录客户端IP地址
$remote_user 记录客户端用户名称
$request 记录请求的URL和HTTP协议
$status 记录请求状态
$body_bytes_sent 发送给客户端的字节数，不包括响应头的大小；该变量与Apache模块mod_log_config里的
“%B”参数兼容。
$bytes_sent 发送给客户端的总字节数。
$connection 连接的序列号。
$connection_requests 当前通过一个连接获得的请求数量。
$msec 日志写入时间。单位为秒，精度是毫秒。
$pipe 如果请求是通过HTTP流水线(pipelined)发送，pipe值为“p”，否则为“.”。
$http_referer 记录从哪个页面链接访问过来的
$http_user_agent 记录客户端浏览器相关信息
$request_length 请求的长度（包括请求行，请求头和请求正文）。
$request_time 请求处理时间，单位为秒，精度毫秒；从读入客户端的第一个字节开始，直到把最后一个字符发送
给客户端后进行日志写入为止。
$time_iso8601 ISO8601标准格式下的本地时间。
$time_local 通用日志格式下的本地时间。
```

实例

```bash
http {
	log_format main '$remote_addr - $remote_user [$time_local] "$request" '
					'"$status" $body_bytes_sent "$http_referer" '
					'"$http_user_agent" "$http_x_forwarded_for" '
					'"$gzip_ratio" $request_time $bytes_sent $request_length';
	log_format srcache_log '$remote_addr - $remote_user [$time_local] "$request" '
							'"$status" $body_bytes_sent $request_time $bytes_sent $request_length '
						'[$upstream_response_time] [$srcache_fetch_status][$srcache_store_status] [$srcache_expire]';

	open_log_file_cache max=1000 inactive=60s;
	server {
		server_name ~^(www\.)?(.+)$;
		access_log logs/$2-access.log main;
		error_log logs/$2-error.log;
		location /srcache {
			access_log logs/access-srcache.log srcache_log;
		}
	}
}
```

### 3.open_log_file_cache 指令

| 语法   | open_log_file_cache max=N [inactive=time] [min_uses=N] [valid=time];<br/>open_log_file_cache off; |
| ------ | ------------------------------------------------------------ |
| 默认值 | open_log_file_cache off;                                     |
| 配置段 | http, server, location                                       |

对于每一条日志记录，都将是先打开文件，再写入日志，然后关闭。
可以使用open_log_file_cache来设置日志 (文件缓存默认是off)
格式如下：

| 参数     | 值                                                           |
| -------- | ------------------------------------------------------------ |
| max      | 设置缓存中的最大文件描述符数量，如果缓存被占满，采用LRU算法将描述符关闭。 |
| inactive | 设置存活时间，默认是10s                                      |
| min_uses | 设置在inactive时间段内，日志文件最少使用多少次后，该日志文件描述符记入缓存中，默认是1次 |
| valid    | 设置检查频率，默认60s                                        |
| off      | 禁用缓存                                                     |

实例：

```bash
open_log_file_cache max=1000 inactive=20s valid=1m min_uses=2;
```



### 4.log_not_found 指令

| 语法   | log_not_found on                            |
| ------ | ------------------------------------------- |
| 默认值 | log_not_found on                            |
| 配置段 | http, server, location                      |
| 作用   | 是否在error_log中记录不存在的错误，默认是。 |



### 5.log_subrequest 指令

| 语法   | log_subrequest on                                    |
| ------ | ---------------------------------------------------- |
| 默认值 | log_subrequest off;                                  |
| 配置段 | http, server, location                               |
| 作用   | 是否在access_log中记录子请求的访问日志，默认不记录。 |



### 6.rewrite_log 指令

由ngx_http_rewrite_module模块提供的。用来记录重写日志的。对于调试重写规则建议开启。

| 语法   | rewrite_log on                                  |
| ------ | ----------------------------------------------- |
| 默认值 | ewrite_log off;                                 |
| 配置段 | http, server, location, if                      |
| 作用   | 启用时将在error log中记录notice级别的重写日志。 |



### 7.error_log 指令

语法: error_log file | stderr | syslog:server=address[,parameter=value] [debug | info | notice |

| 默认值: | error_log logs/error.log error; |
| ------- | ------------------------------- |
| 配置段  | main, http, server, location    |
| 作用    | 配置错误日志                    |



## Nginx 日志切割

nginx日志默认情况下都写入到一个文件中，文件会变的越来越大，非常不方便查看分析。
以日期来作为日志的切割是比较好的，通常我们是以每日来做统计的，下面来说说nginx日志切割。

### 1.脚本+定时执行

```
#step1：加脚本 cut_nginx_log.sh，主进程把USR1信号发给worker，worker接到这个信号后，会重新打开日志文件

#!/bin/bash

logs_path="/usr/local/nginx-1.6.0/logs"

log_name="access.log"

pid_path="/usr/local/nginx-1.6.0/logs/nginx.pid"

mv ${logs_path}/${log_name} ${logs_path}/"access-"$(date --date="LAST DAY" "+%Y-%m-%d").log

kill -USR1 `cat ${pid_path}`

#find ${logs_path} -name "*.log" -mtime +30 -delete

#step2：crontab 定时跑脚本,每天0点0分执行脚本

crontab -e

0 0 * * * /usr/local/nginx-1.6.0/logs/cut_nginx_log.sh
```

### 2.使用logrotate

vim /etc/logrotate.d/nginx

```bash
/usr/local/nginx/logs/www.willko.cn.log /usr/local/nginx/logs/nginx_error.log {

notifempty

daily

sharedscripts;

postrotate

/bin/kill -USR1 `/bin/cat /usr/local/nginx/nginx.pid`

endscript

}
```

多个日志以空格分开，

notifempty 如果日志为空则不做轮询

daily 每天执行一次

postrotate 日志轮询后执行的脚本

这样，每天都会自动轮询，生成nginx.log.1-n