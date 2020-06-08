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



## Nginx root&alias

nginx指定文件路径有两种方式root和alias，这两者的用法区别，使用方法总结了下，方便大家在应用过程中，快速响应。root与alias主要区别在于nginx如何解释location后面的uri，这会使两者分别以不同的方式将请求映射到
服务器文件上。
**root:**

| 语法   | root path                  |
| :----- | -------------------------- |
| 默认值 | root html                  |
| 配置段 | http、server、location、if |

**alias:**

| 语法   | alias path |
| ------ | ---------- |
| 配置段 | location   |

实例1：

```bash
location ~^ /weblogs/ {
	root /data/weblogs/www.ttlsa.com;
	autoindex on;
	auth_basic "Restricted";
	auth_basic_user_file passwd/weblogs;
}
```

如果一个请求的URI是/weblogs/httplogs/www.ttlsa.com-access.log时，web服务器将会返回服务器上的
/data/weblogs/www.ttlsa.com/weblogs/httplogs/www.ttlsa.com-access.log的文件。root会根据完整的URI请求来映射，也就是/path/uri。因此，前面的请求映射为path/weblogs/httplogs/www.ttlsa.com-access.log。
实例2：

```bash
location ^~ /binapp/ {
	limit_conn limit 4;
	limit_rate 200k;
	internal;
	alias /data/statics/bin/apps/;
}
```

alias会把location后面配置的路径丢弃掉，把当前匹配到的目录指向到指定的目录。如果一个请求的URI是
/binapp/a.ttlsa.com/favicon时，web服务器将会返回服务器上的/data/statics/bin/apps/a.ttlsa.com/favicon.jgp的文件。

1. 使用alias时，目录名后面一定要加”/”。
2. alias可以指定任何名称。
3. alias在使用正则匹配时，必须捕捉要匹配的内容并在指定的内容处使用。
4. alias只能位于location块中。



## Nginx 变量

| 参数名称            | 注释                                                         |
| ------------------- | ------------------------------------------------------------ |
| $arg_PARAMETER      | HTTP 请求中某个参数的值，如/index.php?site=www.ttlsa.com，可以用$arg_site取得www.ttlsa.com这个值. |
| $args HTTP          | 请求中的完整参数。例如，在请求/index.php?width=400&height=200 中，$args表示字符串width=400&height=200. |
| $binary_remote_addr | 二进制格式的客户端地址。例如：\x0A\xE0B\x0E                  |
| $body_bytes_sent    | 表示在向客户端发送的http响应中，包体部分的字节数             |
| $content_length     | 表示客户端请求头部中的Content-Length 字段                    |
| $content_type       | 表示客户端请求头部中的Content-Type 字段                      |
| $cookie_COOKIE      | 表示在客户端请求头部中的cookie 字段                          |
| $document_root      | 表示当前请求所使用的root 配置项的值                          |
| $uri                | 表示当前请求的URI，不带任何参数                              |
| $document_uri       | 与$uri 含义相同                                              |
| $request_uri        | 表示客户端发来的原始请求URI，带完整的参数。$uri和$document_uri未必是用户的原始请求，在内部重定向后可能是重定向后的URI，而$request_uri 永远不会改变，始终是客户端的原始URI. |
| $host               | 表示客户端请求头部中的Host字段。如果Host字段不存在，则以实际处理的server（虚拟主机）名称代替。如果Host字段中带有端口，如IP:PORT，那么$host是去掉端口的，它的值为IP。$host是全小写的。这些特性与http_HEADER中的http_host不同，http_host只取出Host头部对应的值。 |
| $hostname           | 表示Nginx所在机器的名称，与gethostbyname调用返回的值相同     |
| $http_HEADER        | 表示当前HTTP请求中相应头部的值。HEADER名称全小写。例如，示请求中Host头部对应的值用$http_host表 |
| $sent_http_HEADER   | 表示返回客户端的HTTP响应中相应头部的值。HEADER名称全小写。例如，用$sent_http_content_type表示响应中Content-Type头部对应的值 |
| $is_args            | 表示请求中的URI是否带参数，如果带参数，$is_args值为?，如果不带参数，则是空字符串 |
| $limit_rate         | 表示当前连接的限速是多少，0表示无限速                        |
| $nginx_version      | 表示当前Nginx的版本号                                        |
| $query_string       | 请求URI中的参数，与$args相同，然而$query_string是只读的不会改变 |
| $remote_addr        | 表示客户端的地址                                             |
| $remote_port        | 表示客户端连接使用的端口                                     |
| $remote_user        | 表示使用Auth Basic Module时定义的用户名                      |
| $request_filename   | 表示用户请求中的URI经过root或alias转换后的文件路径           |
| $request_body       | 表示HTTP请求中的包体，该参数只在proxy_pass或fastcgi_pass中有意义 |
| $request_body_file  | 表示HTTP请求中的包体存储的临时文件名                         |
| $request_completion | 当请求已经全部完成时，其值为“ok”。若没有完成，就要返回客户端，则其值为空字符串；或者在断点续传等情况下使用HTTP range访问的并不是文件的最后一块，那么其值也是空字符串。 |
| $request_method     | 表示HTTP请求的方法名，如GET、PUT、POST等                     |
| $scheme             | 表示HTTP scheme，如在请求https://nginx.com/中表示https       |
| $server_addr        | 表示服务器地址                                               |
| $server_name        | 表示服务器名称                                               |
| $server_port        | 表示服务器端口                                               |
| $server_protocol    | 表示服务器向客户端发送响应的协议，如HTTP/1.1或HTTP/1.0       |



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



## Nginx 重写规则指南

### 一、rewrite模块介绍

nginx的重写模块是一个简单的正则表达式匹配与一个虚拟堆叠机结合。依赖于PCRE库，因此需要安装pcre。根据相关变量重定向和选择不同的配置，从一个location跳转到另一个location，不过这样的循环最多可以执行10次，超过后,nginx将返回500错误。同时，重写模块包含set指令，来创建新的变量并设其值，这在有些情景下非常有用的，如记录条件标识、传递参数到其他location、记录做了什么等等。

### 二、rewrite 模块指令

#### **break**

| 语法     | break                                            |
| -------- | ------------------------------------------------ |
| 默认值   | none                                             |
| 使用字段 | server, location, if                             |
| 作用     | 完成当前设置的重写规则，停止执行其他的重写规则。 |

#### **if**

| 语法                   | if (condition) { …}                                          |
| :--------------------- | ------------------------------------------------------------ |
| 默认值                 | none                                                         |
| 使用字段               | server, location                                             |
| 注意                   | 尽量考虑使用trp_files代替。                                  |
| 判断的条件可以有以下值 | 1. 一个变量的名称：空字符传”“或者一些“0”开始的字符串为false。<br/>2. 字符串比较：使用=或!=运算符<br/>3. 正则表达式匹配：使用~(区分大小写)和~\*(不区分大小写)，取反运算!~和!~*。<br/>4. 文件是否存在：使用 -f 和 !-f 操作符<br/>5. 目录是否存在：使用 -d 和 !-d 操作符<br/>7. 文件、目录、符号链接是否存在：使用 -e 和 !-e 操作符<br/>8. 文件是否可执行：使用 -x 和 !-x 操作符 |

#### return

| 语法     | return code          |
| -------- | -------------------- |
| 默认值   | none                 |
| 使用字段 | server, location, if |

停止处理并为客户端返回状态码。非标准的444状态码将关闭连接，不发送任何响应头。可以使用的状态码有：
204，400，402-406，408，410, 411, 413, 416与500-504。
如果状态码附带文字段落，该文本将被放置在响应主体。相反，如果状态码后面是一个URL，该URL将成为location头补值。没有状态码的URL将被视为一个302状态码。

#### rewrite

| 语法     | rewrite regex replacement flag                               |
| -------- | ------------------------------------------------------------ |
| 默认值   | none                                                         |
| 使用字段 | server, location, if                                         |
| 作用     | 按照相关的正则表达式与字符串修改URI，指令按照在配置文件中出现的顺序执行。可以在重写指令后面添加标记。 |

注意：如果替换的字符串以http://开头，请求将被重定向，并且不再执行多余的rewrite指令。
尾部的标记(flag)可以是以下的值：

- last –停止处理重写模块指令，之后搜索location与更改后的URI匹配。
- break –完成重写指令。
- redirect –返回302临时重定向，如果替换字段用http://开头则被使用。
- permanent –返回301永久重定向。



#### rewrite_log

| 语法     | rewrite_log on                                  |
| -------- | ----------------------------------------------- |
| 默认值： | rewrite_log off \| off                          |
| 使用字段 | server, location, if                            |
| 变量     | 无                                              |
| 作用     | 启用时将在error log中记录notice级别的重写日志。 |

#### set

| 语法     | set variable value           |
| -------- | ---------------------------- |
| 默认值   | none                         |
| 使用字段 | server, location, if         |
| 作用     | 为给定的变量设置一个特定值。 |

#### uninitialized_variable_warn

| 语法     | uninitialized_variable_warn on \|off |
| -------- | ------------------------------------ |
| 默认值   | uninitialized_variable_warn on       |
| 使用字段 | http, server, location, if           |
| 作用     | 控制是否记录未初始化变量的警告信息。 |

### 三、重写规则组成部分

1. 任何重写规则的第一部分都是一个正则表达式
   可以使用括号来捕获，后续可以根据位置来将其引用，位置变量值取决于捕获正则表达式中的顺序，$1引用第一个括号中的值，$2引用第二个括号中的值，以此类推。如：

   ```bash
   ^/images/([a-z]{2})/([a-z0-9]{5})/(.*)\.(png|jpg|gif)$
   ```

   $1是两个小写字母组成的字符串，
   $2是由小写字母和0到9的数字组成的5个字符的字符串，
   $3将是个文件名，
   $4是png、jpg、gif中的其中一个。

2. 重写规则的第二部分是URI
   请求被改写。该URI可能包含正则表达式中的捕获的位置参数或这个级别下的nginx任何配置变量。如：

   ```bash
   /data?file=$3.$4
   ```

   如果这个URI不匹配nginx配置的任何location，那么将给客户端返回301(永久重定向)或302(临时重定向)的状态码来表示重定向类型。该状态码可以通过第三个参数来明确指定。

3. 重写规则的第三部分
   第三部分也就是尾部的标记(flag)。last标记将导致重写后的URI搜索匹配nginx的其他location，最多可循环10次。如：

   ```bash
   rewrite '^/images/([a-z]{2})/([a-z0-9]{5})/(.*)\.(png|jpg|gif)$' /data?file=$3.$4 last;
   ```

   break指令可以当做自身指令。如：

   ```bash
   if ($bwhog) {
   	limit_rate 300k;
   	break;
   }
   ```

   另一个停止重写模块处理指令是return，来控制主HTTP模块处理请求。这意味着，nginx直接返回信息给客户端，与error_page结合为客户端呈现格式化的HTML页面或激活不同的模块来完成请求。如果状态码附带文字段落，该文本将被放置在响应主体。相反，如果状态码后面是一个URL，该URL将成为location头补值。没有状态码的URL将被视为一个302状态码。如：

   ```bash
   location = /image404.html {
   	return 404 "image not found\n";
   }
   ```

### 四、实例

```bash
http {
	# 定义image 日志格式
	log_format imagelog '[$time_local] ' $image_file ' ' $image_type ' ' $body_bytes_sent ' ' $status;
	# 开启重写日志
	rewrite_log on;
	server {
		root /home/www;
		location / {
			# 重写规则信息
			error_log logs/rewrite.log notice;
			# 注意这里要用‘’单引号引起来，避免{}
			rewrite '^/images/([a-z]{2})/([a-z0-9]{5})/(.*)\.(png|jpg|gif)$' /data?file=$3.$4;
			# 注意不能在上面这条规则后面加上“last”参数，否则下面的set 指令不会执行
			set $image_file $3;
			set $image_type $4;
		}
		location /data {
			# 指定针对图片的日志格式，来分析图片类型和大小
			access_log logs/images.log mian;
			root /data/images;
			# 应用前面定义的变量。判断首先文件在不在，不在再判断目录在不在，如果还不在就跳转到最后一个url 里
			try_files /$arg_file /image404.html;
		}
		location = /image404.html {
			# 图片不存在返回特定的信息
			return 404 "image not found\n";
		}
	}
}
```



### 五、创建新的重新规则

在接到要创建新的重写规则时，要弄清楚需求是什么样的，再决定怎么做。毕竟重写也是耗资源的、有效率之分的。
下面的这些问题有些帮助的：

1. 你的URL的模式是什么样的?
2. 是否有一个以上的方法来实现？
3. 是否需要捕获URL部分作为变量？
4. 重定向到另一个web上可以看到我的规则？
5. 是否要替换查询的字符串参数？

检查网站或应用程序布局，清楚URL模式。啰嗦一句：我一而再再而三的强调，运维不能与开发脱节，运维要参与到开发当中。如果有不止一种方法实现，创建一个永久重定向。同时，定义一个重写规范，来使网址清洁，还可以帮助网站更容易被找到。

**实例1. 要将home目录重定向到主页面上，目录结构如下：**

```bash
/
/home
/home/
/home/index
/home/index/
/index
/index.php
/index.php/

#重写规则如下：
rewrite ^/(home(/index)?|index(\.php)?)/?$ $scheme://$host/ permanent;

#指定$scheme和$host变量，因为要做一个永久重定向并希望nginx使用相同的参数来构造URL。
```

**实例2. 如果想分别记录各个部分的URL，可以使用正则表达式来捕获URI，然后，给变量分配指定位置变量，见上面的实例。**

**实例3. 当重写规则导致内部重定向或指示客户端调用该规则本身被定义的location时，必须采取特殊的动作来避免重写循环。如：在server配置段定义了一条规则带上last标志，在引用location时，必须使用break标志。**

```bash
server {
	rewrite ^(/images)/(.*)\.(png|jpg|gif)$ $1/$3/$2.$3 last;
	location /images/ {
		rewrite ^(/images)/(.*)\.(png|jpg|gif)$ $1/$3/$2.$3 break;
	}
}
```

**实例4. 作为重写规则的一部分，传递新的查询字符串参数是使用重写规则的目标之一。如：**

```bash
rewrite ^/images/(.*)_(\d+)x(\d+)\.(png|jpg|gif)$ /resizer/$1.$4?width=$2&height=$3? last;
#nginx重写规则说起来挺简单的，做起来就难，重点在于正则表达式，同时，还需要考虑到nginx执行顺序。
```



## nginx 逻辑运算

nginx的配置中不支持if条件的逻辑与&& 逻辑或|| 运算，而且不支持if的嵌套语法，否则会报下面的错误：
	nginx: [emerg] invalid condition。
我们可以用变量的方式来间接实现。
要实现的语句：

```bash
if ($arg_unitid = 42012 && $uri ~/thumb/){
	echo "www.ttlsa.com";
}
```

如果按照这样来配置，就会报nginx: [emerg] invalid condition错误。
可以这么来实现，如下所示：

```bash
set $flag 0;
if ($uri ~ ^/thumb/[0-9]+_160.jpg$){
	set $flag "${flag}1";
}
if ($arg_unitid = 42012){
	set $flag "${flag}1";
}
if ($flag = "011"){
	echo "www.ttlsa.com";
}
```



## 隐藏Nginx 版本号的安全性与方法

搭建好nginx或者apache，为了安全起见我们都会隐藏他们的版本号。
Nginx默认是显示版本号的，如：

```bash
[root@bkjz ~]# curl -I www.nginx.org
HTTP/1.1 301 Moved Permanently
Server: nginx/1.17.9
Date: Sun, 31 May 2020 08:13:09 GMT
Content-Type: text/html
Content-Length: 169
Connection: keep-alive
Keep-Alive: timeout=15
Location: http://nginx.org/
```

这样就给人家看到你的服务器nginx版本是1.17.9，前些时间暴出了一些Nginx版本漏洞，就是说有些版本有漏洞，而有些版本没有。这样暴露出来的版本号就容易变成攻击者可利用的信息。所以，从安全的角度来说，隐藏版本号会相对安全些！

那nginx版本号可以隐藏不？其实可以的，看下面的步骤：

1. 进入nginx配置文件的目录（此目录根据安装时决定），用vim编辑打开

   ```bash 
   vim nginx.conf
   ```

   在http {—}里加上server_tokens off; 如：

   ```bash
   http {
   ……省略
   sendfile on;
   tcp_nopush on;
   keepalive_timeout 60;
   tcp_nodelay on;
   server_tokens off;
   …….省略
   }
   ```

2. 编辑fastcgi.conf或fcgi.conf（这个配置文件名也可以自定义的，根据具体文件名修改）：

   ```bash
   找到：
   fastcgi_param SERVER_SOFTWARE nginx/$nginx_version;
   改为：
   fastcgi_param SERVER_SOFTWARE nginx;
   ```

3. 重新加载nginx配置:

   ```bash
   # /etc/init.d/nginx reload
   ```

   这样就完全对外隐藏了nginx版本号了，就是出现404、501等页面也不会显示nginx版本。
   下面测试一下：

   ```bash
   # curl -I 127.0.0.1
   HTTP/1.1 200 OK
   Server: nginx
   Date: Tue, 13 Jul 2010 14:26:56 GMT
   Content-Type: text/html; charset=UTF-8
   Connection: keep-alive
   Vary: Accept-Encoding
   ```

   

## CDN 调度器HAProxy、Nginx、Varnish

### CDN功能如下：

1. 将全网IP分为若干个IP段组，分组的依据通常是运营商或者地域，目的是让相同网络环境中的用户聚集到相同的组内；
2. 依据CDN服务器们的网络和容量，确定哪些CDN服务器适合服务哪些IP段组；
3. 根据以上两步得到的结论，让用户去最适合他的服务器得到服务。

说白了，就是根据用户不同的来源IP把用户请求重定向到不同的CDN服务器上去。那么，如何实现呢？
智能DNS是办法之一，稳定可靠且有效。但至少在两个环境下它不能完全满足我们：

1. 需要特别精细的调度时。由于大多数DNS Server不支持DNS扩展协议，所以拿不到用户的真实IP，只能根据Local DNS来调度。
2. 访问特别频繁时。由于每次调度都将触发一次DNS，如果请求变得密集，DNS请求本身带来的开销也会相应变大；
3. 需要根据服务器的带宽容量、连接数、负载情况、当机与否来调度时。由于DNS Server没有CDN节点服务器的信息，这种调度会变得困难。

这时候我们可以：
1、将用户先行引导到某一台或几台统一的服务器上去；
2、让它拿到用户的真实IP，计算出服务他的服务器；
3、通过HTTP302或其它方式把用户定位到最终服务器上。



部署在用户先访问到的那几台服务器上，负责定位IP然后重定向用户请求的那个软件，我们叫它“调度器”。

### HAProxy实现：

HAProxy不支持形如0.0.0.1-0.8.255.255 cn的IP段表示方法，只支持1.1.4.0/22 “CN”的IP段表示方法。
1、我们需要先把IP段转化成它认识的方式；
	a> 下载iprang.c或者iprang.c本地镜像；
	b> 编译gcc -s -O3 -o iprange iprange.c；
	c> 整理IP段列表geo.txt形如：

```bash
1 # head geo.txt
2 "1.0.0.0","1.0.0.255","AU"
3 "1.0.1.0","1.0.3.255","CN"
4 "1.0.4.0","1.0.7.255","AU"
5 "1.0.8.0","1.0.15.255","CN"
6 "1.0.16.0","1.0.31.255","JP"
7 "1.0.32.0","1.0.63.255","CN"
8 "1.0.64.0","1.0.127.255","JP"
9 "1.0.128.0","1.0.255.255","TH"
10 "1.1.0.0","1.1.0.255","CN"
11 "1.1.1.0","1.1.1.255","AU"
```

​	d> 输出HAProxy认识的IP段列表：

```bash
1# cut -d, -f1,2,5 geo.txt | ./iprange | head
2 1.0.0.0/24 "AU"
3 1.0.1.0/24 "CN"
4 1.0.2.0/23 "CN"
5 1.0.4.0/22 "AU"
6 1.0.8.0/21 "CN"
7 1.0.16.0/20 "JP"
8 1.0.32.0/19 "CN"
9 1.0.64.0/18 "JP"
10 1.0.128.0/17 "TH"
11 1.1.0.0/24 "CN"
12 1.1.1.0/24 "AU"
```

​	e> 便于管理的目的，将整合后的IP段归类到同一个文件中：

```bash
1 # cut -d, -f1,2,5 geo.txt | ./iprange | sed 's/"//g' | awk -F' ' '{ print $1 >> $2".subnets" }'
2 # ls *.subnets
3 A1.subnets AX.subnets BW.subnets CX.subnets FJ.subnets GR.subnets IR.subnets LA.subnets
ML.subnets NF.subnets PR.subnets SI.subnets TK.subnets VE.subnets
4 # cat AU.subnets
5 1.0.0.0/24
6 1.0.4.0/22
7 1.1.1.0/24
```

​	f> 把这些文件放到同一个文件夹下，我们以/etc/haproxy/conf/为例。

2. 正确配置HAProxy以这些IP段为规则正确调度；下面是一个haproxy.cfg的例子。配置好后重启Haproxy即可。

   ```bash
   1 global
   2 log 127.0.0.1 local2 debug
   3
   4 chroot /var/lib/haproxy
   5 pidfile /var/run/haproxy.pid
   6 maxconn 8000
   7 user haproxy
   8 group haproxy
   9 daemon
   10
   11 stats socket /var/lib/haproxy/stats
   12
   13 defaults
   14 mode http
   15 log global
   16 option httplog
   17 option dontlognull
   18 option http-server-close
   19 option forwardfor except 127.0.0.0/8
   20 option redispatch
   21 retries 3
   22 timeout http-request 10s
   23 timeout queue 1m
   24 timeout connect 10s
   25 timeout client 1m
   26 timeout server 1m
   27 timeout http-keep-alive 10s
   28 timeout check 10s
   29 maxconn 8000
   30
   31 frontend main *:5000
   32 acl geo_A1 src -f /etc/haproxy/conf/A1.subnets
   33 acl geo_AX src -f /etc/haproxy/conf/AX.subnets
   34 acl geo_BW src -f /etc/haproxy/conf/BW.subnets
   35 acl geo_CX src -f /etc/haproxy/conf/CX.subnets
   36 acl geo_FJ src -f /etc/haproxy/conf/FJ.subnets
   37
   38 ...
   39
   40 reqrep ^([^\ ]*)\ /(.*)\ HTTP \1\ /\2&ipfrom=A1\ HTTP if geo_A1
   41 reqrep ^([^\ ]*)\ /(.*)\ HTTP \1\ /\2&ipfrom=AX\ HTTP if geo_AX
   42 reqrep ^([^\ ]*)\ /(.*)\ HTTP \1\ /\2&ipfrom=BW\ HTTP if geo_BW
   43 reqrep ^([^\ ]*)\ /(.*)\ HTTP \1\ /\2&ipfrom=CX\ HTTP if geo_CX
   44 reqrep ^([^\ ]*)\ /(.*)\ HTTP \1\ /\2&ipfrom=FJ\ HTTP if geo_FJ
   45
   46 ...
   47
   48 default_backend static
   49
   50 backend static
   51 server static 127.0.0.1:6081 check
   ```

   

### Nginx 实现：

Nginx可以在核心模块HttpGeoModule（http://wiki.nginx.org/HttpGeoModule）的配合下实现调度：

```bash
1 http{
2
3 	...
4
5 	geo $useriprang {
6 		ranges;
7 		default a;
8 		0.0.0.1-0.8.255.255 a;
9 		0.9.0.0-0.255.255.255 a;
10 		1.0.0.0-1.0.0.255 a;
11 		1.0.1.0-1.0.1.255 b;
12 		1.0.2.0-1.0.3.255 b;
13 		1.0.4.0-1.0.7.255 a;
14 		...
15 		223.255.252.0-223.255.253.255 c;
16 		223.255.254.0-223.255.254.255 a;
17 		223.255.255.0-223.255.255.255 a;
18 	}
19
20 	upstream backend {
21 		server 127.0.0.1:81;
22 	}
23
24 	server {
25 		listen 80;
26 		client_max_body_size 10240m;
27
28 		location / {
29 			proxy_redirect off;
30 			proxy_pass http://backend$request_uri&useriprang=$useriprang;
31 			proxy_next_upstream http_502 http_504 error timeout invalid_header;
32 			proxy_cache cache_one;
33 			proxy_cache_key $host:$server_port$uri$is_args$args;
34 			expires 5s;
35 		}
36
37 	}
38
39 		...
40
41 }
```

Varnish 实现：
Varnish 则有两个插件可以实现调度：
https://github.com/cosimo/varnish-geoip （Last updated: 28/05/2013）
https://github.com/meetup/varnish-geoip-plugin （Last updated: 2010）
性能问题
如上所述，使用Haproxy、Nginx、Varnish 都能快速实现这个功能。
其中Nginx 和Varnish 使用了二分法在IP 表中定位用户IP，而Haproxy 是逐条过滤。
所以在IP 分得较细，IP 段组较多（归类后超过1000 组）时，Haproxy 会出现明显的性能衰减，其余两者没有这个问题。
其它
本文使用的软件版本如下：
HAProxy1.4.22，Nginx1.2.9，Varnish3.0.4。
HAProxy 和Varnish 都是目前的最新版本。



## Nginx tcp 代理

nginx tcp代理功能由nginx_tcp_proxy_module模块提供，同时监测后端主机状态。该模块包括的模块有：
ngx_tcp_module, ngx_tcp_core_module, ngx_tcp_upstream_module, ngx_tcp_proxy_module,
ngx_tcp_upstream_ip_hash_module。



### 安装

```bash
#下载tcp模块
wget https://github.com/yaoweibin/nginx_tcp_proxy_module/archive/master.zip
cd /usr/wkdir/nginx-1.6.3
patch -p1 < src/nginx_tcp_proxy_module/tcp.patch
cd nginx
./configure --add-module=/path/to/nginx_tcp_proxy_module
make
make install
```



### 配置

```bash
http {
	listen 80;
	location /status {
		check_status;
	}
}
tcp {
	upstream cluster_www_ttlsa_com {
	# simple round-robin
	server 127.0.0.1:1234;
	check interval=3000 rise=2 fall=5 		timeout=1000;
	#check interval=3000 rise=2 fall=5 	timeout=1000 type=ssl_hello;
	#check interval=3000 rise=2 fall=5 	timeout=1000 type=http;
	#check_http_send "GET / HTTP/1.0\r\n\r\n";
	#check_http_expect_alive http_2xx http_3xx;
}
	server {
		listen 8888;
		proxy_pass cluster_www_ttlsa_com;
	}
}
```

这会出现一个问题，就是tcp 连接会掉线。原因在于当服务端关闭连接的时候，客户端不可能立刻发觉连接已经被关闭，需要等到当Nginx 在执行check 规则时认为服务端链接关闭，此时nginx 会关闭与客户端的连接。

### 保持连接的配置

```bash
http {
	listen 80;
	location /status {
		check_status;
	}
}
tcp {
	timeout 1d;
	proxy_read_timeout 10d;
	proxy_send_timeout 10d;
	proxy_connect_timeout 30;
	upstream cluster_www_ttlsa_com {
		# simple round-robin
		server 127.0.0.1:1234;
		check interval=3000 rise=2 fall=5 timeout=1000;
		#check interval=3000 rise=2 fall=5 timeout=1000 type=ssl_hello;
		#check interval=3000 rise=2 fall=5 timeout=1000 type=http;
		#check_http_send "GET / HTTP/1.0\r\n\r\n";
		#check_http_expect_alive http_2xx http_3xx;
	}
	server {
		listen 8888;
		proxy_pass cluster_www_ttlsa_com;
		so_keepalive on;
		tcp_nodelay on;
	}
}
```

nginx_tcp_proxy_module 模块指令具体参见: http://yaoweibin.github.io/nginx_tcp_proxy_module/README.html



## nginx 正向代理

我们平时用的最多的最常见的是反向代理。反向代理想必都会配置的。那么nginx的正向代理是如何配置的呢？

```bash
server {
	listen 8090;
	location / {
		resolver 218.85.157.99 218.85.152.99;
		resolver_timeout 30s;
		proxy_pass http://$host$request_uri;
	}
	access_log /data/httplogs/proxy-$host-aceess.log;
}

```

就这么简单哈。
测试：
http://127.0.0.1:8090
resolver指令
语法: resolver address …[valid=time];
默认值: —
配置段: http, server, location
配置DNS服务器IP地址。可以指定多个，以轮询方式请求。
nginx会缓存解析的结果。默认情况下，缓存时间是名字解析响应中的TTL字段的值，可以通过valid参数更改。