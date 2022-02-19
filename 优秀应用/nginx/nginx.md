# Nginx学习

**I/O模型**
**Nginx介绍**
**Nginx安装**
**Nginx各种模块**

## httpd MPM 

​	httpd MPM:
​	1.prefork:进程模型，两级结构，主进程master负责生成子进程，每个子进程负责响应一个请求。
​	2.worker：线程模型，三级结构，主进程master负责生成子进程，每个子进程负责生成多个线程，
​	每个线程响应一个请求。
​	3.event：线程模型，三级结构，主进程master负责生成子进程，每个子进程响应多个请求。

## I/O介绍

###  I/O：

​	1.网络IO：本质是socket读取
​	2.磁盘IO：
 每次IO，都要经由两个阶段：
​	1.第一步：将数据从磁盘文件先加载至内核内存空间（缓冲区），等待数据准备完成，时间较长。
​	2.第二步：将数据从内核缓冲区复制到用户空间的进程的内存中，时间较短
 同步/异步：关注的是消息通信机制
​	1.同步：synchronous，调用者等待被调用者返回消息，才能继续执行。
​	2.异步：asynchronous，被调用者通过状态、通知或回调机制主动通知调用者被调用者的运行状态
 阻塞/非阻塞：关注调用者在等待结果返回之前所处的状态
​	1.阻塞：blocking，指IO操作需要彻底完成后才返回到用户空间，调用结果返回之前，调用者被挂起。
​	2.非阻塞：nonblocking，指IO操作被调用后立即返回给用户一个状态值，无需等到IO操作彻底完成，最终的调用结果返回之前，调用者不会挂起

###  I/O模型：

​	**阻塞型、非阻塞型、复用型、信号驱动型、异步**
​	

	1、同步阻塞I/O模型：
		用户进程阻塞于recvfrom的调用 ->  应用程序（recvfrom）  
		-- 系统调用--> 内核（无数据报准备好）
		-- 等待数据 --> 数据报准备好复制数据报  
		-- 将数据从内核复制到用户空间 --> 复制完成  
		--- 返回成功指示 --> 处理数据报 --> 用户进程
	2、同步非阻塞I/O模型：
		用户线程发起io请求立即返回。但并未读取到任何数据，用户线程需要不断发起io请求，直到数据到达后，才真正读取到数据，继续执行。即“轮询”机制
		整个io请求的过程中，虽然用户线程每次发起io请求后可以立即返回，但是为了等到数据，然需要不断地轮询、重复请求，消耗了大量的cpu资源。
		
		是比较浪费cpu的方式，一般很少直接使用这种模型，而是在其他io模型中使用非阻塞io这一特性
	
		进程反复调用recvfrom等待返回成功指示（轮询） ->  
		
		应用进程					内核
					
					系统调用
					--------->		
		recvfrom	EWOULDBLOCK 	无数据报准备好
					<---------
					
					系统调用
					--------->		
		recvfrom	EWOULDBLOCK 	无数据报准备好
					<---------
					
					系统调用
					--------->		
		recvfrom	EWOULDBLOCK 	数据报准备好
					<---------
										|	
										|
										|
				返回成功指示	<--------
	  处理数据报					拷贝完成
	
	3.IO多路复用模型：
		a.多个连接共用一个等待机制，本模型会阻塞进程，但是进程是阻塞在select或者poll这两个系统调用上，而不是阻塞在真正的io操作上
		b.用户首先将需要进行io操作添加到select中，继续执行其他工作（异步），同时等待select系统调用返回。当数据到达时，io被激活，select函数返回。用户线程正式发起read请求，读取数据并继续执行。
		c.从流程上来看，使用select函数进行io请求和同步阻塞模型没有太大的区别，甚至还多了添加监视io，以及调用select函数的额外操作，效率更差。并阻塞了两次，但是第一次阻塞在select上时，select可以监控多个io上是否已有io操作准备就绪，即可达到在同一个线程内同时处理多个io请求的目的。而不像阻塞io那种，一次只能监控一个io
		d.虽然上述方式允许单线程内处理多个io请求，但是每个io请求的过程还是阻塞的（在select函数上阻塞），平均时间甚至比同步阻塞io模型还要长。如果用户线程只是注册自己需要的io请求，然后去做自己的事情，等到数据来到时在进行处理，则可以提高cpu的利用率
		e.io多路复用是最常使用的io模型，但是其异步程度还不够彻底，因它使用了会阻塞线程的select系统调用。因此io多路复用只能称为异步阻塞io模型，而非真正的异步io。
		
		IO多路复用是指内核一旦发现进程指定的一个或者多个io条件准备读取，就通知该进程
		IO多路复用适用如下场合：
			当客户端处理多个描述符时（一般是交互式输入和网络套接口），必须使用i/O复用。
			当一个客户端同时处理多个套接字时，此情况可能的，但很少出现。
			当一个ICP服务器既要处理监听套接字，又要处理已连接套接字，一般也要用到I/O复用。
			当一个服务器既要处理TCP，又要chuliUDP，一般要使用I/O复用。
			当一个服务器要处理多个服务或多个协议，一般要使用I/o复用。
	
	4.信号驱动IO模型		
		a.信号驱动IO：signal-driven I/O
		b.用户进程可以通过sigaction系统调用注册一个信号处理程序，然后主程序可以继续向下执行，当有IO操作准备就绪时，由内核通知触发一个SIGIO信号处理程序执行，然后将用户进程所需要的数据从内核空间拷贝到用户空间
		c.此模型的优势在于等待数据报到达期间进程不被阻塞。用户主程序可以继续执行，只要等待来自信号处理函数的通知。
		d.该模型并不常用。
	
	5.异步IO模型	

​	

### I/O模型的具体实现

 主要实现方式有以下几种：
	1.Select(跨平台)：Linux实现对应，I/O复用模型，BSD4.2最早实现
	2.Poll（改进版的Select 差不多一样）：Linux实现，对应I/O复用模型，System V unix最早实现
	3.Epoll：Linux实现，对应I/O复用模型，具有信号驱动I/O模型的某些特性
	4.Kqueue：FreeBSD实现，对应I/O复用模型，具有信号驱动I/O模型某些特性
	5./dev/poll: SUN的Solaris实现，对应I/O复用模型，具有信号驱动I/O模型的某些特性
	6.Iocp： Windows实现，对应第5种（异步I/O）模型
	

	select/poll/epoll


​	
	select:
		操作方式：遍历
		底层实现：数组
		IO效率：每次调用都进行线性变量，时间复杂度O(n)
		最大连接数：1024（x86）或2048（x64）
		fd拷贝：每次调用select，都需要把fd集合从用户态拷贝到内核态
		
	poll	
		操作方式：遍历
		底层实现：链表
		IO效率：每次调用都进行线性变量，时间复杂度O(n)
		最大连接数：无上限
		fd拷贝：每次调用pollt，都需要把fd集合从用户态拷贝到内核态
	epoll	
		操作方式：回调
		底层实现：哈希表
		IO效率：事件通知方式，每当fd就绪系统注册的回调函数就会被调用，将就绪fd放到redllist里面，时间复杂度O(1)
		最大连接数：无上限
		fd拷贝：调用epoll_ct时拷贝进内核并保存，之后每次epoll_wait不拷贝

 poll:本质上和select没有区别，它将用户传入的数组拷贝到内核空间，然后查询每个fd对应的设备状态，
	其没有最大连接数的限制，原因是它是基于链表来存储的。
	大量的fd数组被整体复制于用户态和内核地址空间之间，而不管这样的复制是不是有意义。
	poll特点是“水平触发”，如果报告了fd后，没有被处理，那么下次poll时会再次报告该fd
	边缘触发：只通知一次。
		
 epoll:在Linux 2.6内核中提出的select和poll的增强版本
	1.支持水平触发LT和边缘触发ET，最大的特点在于边缘触发，他只告诉进程哪些fd刚刚变为就需态，并且只会通知一次。
	2.使用“事件”的就绪通知方式，通过epoll_ctl注册fd，一旦gaifd就绪，内核就会采用类似callback的回调机制来激活fd，epoll_wait便可以收到通知。
 优点：
	1.没有最大并发连接的限制：能打开的FD的上限远大于1024（1G的内存能监听约10万个端口）
	2.效率提升：非轮询的方式，不会随着FD数目的增加而效率下降；只有活跃可用的FD才会调用callback函数，即epoll最大的有点就在于它只管理“活跃”的连接，而跟连接总数无关。
	3.内存拷贝，利用mmap（Memory Mapping）加速与内核空间的消息传递；即epoll使用mmap减少复制开销。

## Nginx介绍：

​	Nginx:engine X,2002年，开源，商业版。
​	NGINX是免费，开源，高性能的HTTP和反向代理服务器，邮件代理服务器，通用TCP/UDP代理服务器
​	解决C10k（1万连接 10K Connections）
​	官网：http://nginx.org
​	二次开发板：Tengine,OpenResty(章亦春)
​	

	特性：
		a.模块化设计，较好的扩展性
		b.高可靠性
		c.支持热部署：不停机更新配置文件，升级版本，更换日志文件
		d.低内存消耗：10000个kepp-alive连接模式下的非活动连接，仅需2.5M内存
		e.event-driven,aio,mmap,sendFile
	基本功能：
		a.静态资源的web服务器
		b.http协议反向代理服务器
		c.pop3/imap4协议反向代理服务器
		d.FastCGI（LNMP），uWSGI(python)等协议
		e.模块化（非DSO），如ZIP，SSL模块
	nginx的程序架构：
		web服务相关的功能：
			1.虚拟主机（server）
			2.支持keep-alive和管道连接
			3.访问日志（支持基于日志缓冲提高性能）
			4.url rewirte 地址重写
			5.路径别名 alias
			6.基于IP及用户的访问限制
			7.支持速率限制及并发数限制
			8.重新配置和在线升级而无须中断客户的工作进程
			9.Memacached（缓存） 的GET接口
		master/worker结构：
			一个master进程：
				负载加载和分析配置文件、管理worker进程、平滑升级
			一个或多个worker进程：
				处理并响应用户请求。
			缓存相关的进程：
				cache loader：载入缓存对象
				cache manager：管理缓存对象	
		nginx模块：
			nginx高度模块化，但其模块早期不支持DSO机制；1.9.11版本支持动态装载和卸载
			模块分类：
				核心模块：
					core module
				标准模块：
					HTTP模块：ngx_http_*
						HTTP Core modules 默认功能
						HTTP Optional modules 需编译时指定
					Mail模块（邮件）： ngx_mail_*
					Stream 模块（TCP）：ngx——stream_*
				第三方模块
				
		nginx的功用
			1.静态的web资源服务器	
				html，图片，js，css，tt等静态资源
			2.结合FastCGI/uWSGI/SCGI等协议反向代理动态资源请求
			3.http/https协议的反向代理
			4.imap4/pop3协议的反向代理
			5.tcp/udp协议的请求转发（反向代理）
		
		安装：stable
		常用命令：
			验证配置是否正确: nginx -t
	 
			查看Nginx的版本号：nginx -V
	 
			启动Nginx：start nginx
	 
			快速停止或关闭Nginx：nginx -s stop
	 
			正常停止或关闭Nginx：nginx -s quit
	 
			配置文件修改重装载命令：nginx -s reload


​		
		nginx配置
			配置文件的组成部分：
				主配置文件：nginx.conf
				子配置文件：include conf.d/*.conf
				fastcgi,uwsgi,scgi等协议相关的配置文件
				mime.types：支持mime类型
			主配置文件的配置命令：
				directive value [value2 ...]
			注意：
				1)指令必须以分号结尾
				2)支持使用配置变量
					内建变量：由Nginx模块引入，可直接引用。
					自定义变量：由用户使用set命令定义
								set variable_name value
					引用变量：$variable_name
		nginx配置文件
			主配置文件结构：四部
				1)main block：主配置段，即全局配置段，对http，mail都有效。
					event{
						...
					} 时间驱动相关的配置
				2)http{
					....
				} http/https协议相关配置段
				
				3)mail{
					...
				} mail协议(邮件协议)相关配置段
				
				4)stream{
					...
				} stream服务器相关配置段
		
		http协议相关的配置结构:
		http{
			#各server的公共配置
			...
			...
			
			server{#每个server用于定义一个虚拟主机
				...
			}
			server{
				...
				server_name 虚拟主机名
				root	主目录;
				alias	路径别名;
				location[OPERATOR]URL{
					#指定URL的特性
					...
					if CONDITION{
					
					}
				}
			}
		}
		
		Main 全局配置段常见的配置指令分类：
			1）正常运行必备的配置
				1.user
					Synax:	user user[group];
					Default:	user nobody nobody;
					Contet:	main
					指定worker进程的运行身份，如组不指定，默认和用户名同名
				2.pid/PATH/TO/PID_FILE
					指定存储nginx主进程PID的文件路径
				3.include file | mask
					指明包含进来的其他配置文件片段
				4.load_module file
					模块加载配置文件：/usr/share/nginx/modules/*.conf
					指明要加载的动态模块路径：/usr/lib64/nginx/modules
			2）优化性能相关的配置
				1.worker_processes number | auto
					worker进程的数量：通常应该为当前主机的cpu的物理核心数
				2.worker_cpu_affinity cpumask...
					worker_cpu_affinity auto [cpumask]提高缓存命中率
					CPU MASK ： 
						00000001：0号CPU
						00000010：1号CPU
						10000000：8号CPU
					worker_cpu_affinity 0001 0010 0100 1000;
					worker_cpu_affinity 0101 1010;	
			3）用于调试及定位问题相关的配置
				指定worker进程的nice值，设定worker进程优先级：[-20,20]
			4）事件驱动相关的配置
				worker进程所能够打开的文件数量上限，如65535
			帮助文档：http://nginx.org/en/docs/
			
			事件驱动相关的配置：
				events {
					...
				}
				1.worker_connections number;
					每个worker进程所能够打开的最大并发连接数数量，如10240。
					总最大并发数：worker_processes * worker_connections
				2.use method
					指明并发连接请求的处理方法，默认自动选择最优方法
					use epoll；
				3.accept_mutex on|off 互斥
					处理新的连接请求的方法；on指由各个worker轮流处理新请求，Off指每个新请求的到达都会通知（唤醒）所有的worker进程，但只有一个进程可获得连接，造成”惊群“，影响性能。
		
			调试和定位问题：
				1）daemon on | off
					是否已守护进程方式运行nginx，默认是守护进程方式。
				2）master_process on|off
					是否以master/worker模型运行nginx；默认位no，off将不启动worker。
				3）error_log file[level]
					错误日志文件及其级别；出于调试需要，可设定为debug；但debug仅在编译时使用了”--with-debug“选项时才有效。
					方式：file/path/logfile;
					stderr:发送到标准错误
					syslog：server-address[,parameter=values]:发送到syslog memory：size内存
					level:debug|info|notice|warn|error|crit|alter|emerg
		
		http协议的相关配置(重要)：
			http{
				...
				server{
					...
					server_name
					root
					location[OPERATOR]/uri/{
						...
					}
				}
				server{
					...
				}
			}
		
			ngx_http_core_module
			与套接字相关的配置：
				1）server{...}
					配置一个虚拟主机
					server{
						#listen 80 default_server; 默认访问这个
						listen address[:PORT]|PORT;
						server_name SERVER_NAME;
						root /PATH/TO/DOCUMENT_ROOT;
					}
					# 可以将虚拟主机创建到一个*.conf文件中，然后使用include引进。	
					#include /etc/nginx/config/*.conf;
				2）listen PORT|address[:port]|unix:/PATH/TO/SOCKET_FILE
					listen address[:port][default_server][ssl][http2|spdy][backlog=number][rcvbuf=size][sndbuf=size]
						default_server	设定位默认虚拟主机
						ssl				限制仅能通过ssl连接提供服务
						backlog=number	超过并发连接数后，新请求进入后援队列的长度
						rcvbuf=size		接收缓冲区大小
						sndbuf=size		发送缓冲区大小
					注意：
						a.基于port；
							listen PORT;	指令监听在不同的端口
						b.基于ip的虚拟主机
							listen IP:PORT;	IP地址不同
						c.基于hostname
							server_name fqdn;	指令指向不同的主机名
				3）server_name name...;
					1.虚拟主机的主机名称后可跟多个由空白字符分隔的字符串
					2.支持*通配任意长度的任意字符
						server_name *.magedu.com www.bai*.com
					3.支持~起始的字符做正则表达式模式匹配，因为性能原因，慎用。
						server_name	~^www\d+\.magedu\.com$
					4.匹配优先级机制从高到低：
						a.首先是字符串精确匹配 如：www.magedu.com
						b.左侧*通配符 如：*.magedu.com
						c.右侧*通配符 如：www.magedu.*
						d.正则表达式 如：~^www\d+\.magedu\.com$
						e.default_server
				4）tcp_nodelay on|off;
					在keepalived模式下的连接是否启用TCP_NODELAY选项
						当为off是，延迟发送，合并多个请求后再发送。
						默认On时，不延迟发送
						可用于：http,server,location
				5)sendfile on|off;
					是否开启sendfile功能，在内核中封装报文直接发送，默认Off。
				6）server_tokens on | off | build | string
					是否在响应报文的Server首部显示nginx版本
			定义路径相关的配置：
				7）root
					设置web资源的路径映射；用于指明请求的URL所对应的文档的目录路径，可用于http，server,location,if in location
						server{
							...
							root /data/www/vhost1;
						}
						示例：
							http://www.mafedu.com/images/logo.jpg
								-->/data/www/vhosts/images/logo.jpg
				8)location [=|~|~*|^~] uri{...}
					=:	对URI做精确匹配；
						location =/{
							...
						}
						http://www.magedu.com/ 	匹配
						http://www.magedu.com/index.html   不匹配
					^~:	对URI的最左边部分做匹配检查，不区分字符大小写
					~:	对URI的做正则表达式模式匹配，区分字符大小写
					~*:	对URI的做正则表达式模式匹配，不区分字符大小写					
					不带符号：	匹配起始于此uri的所有uri
					匹配优先级从高到低：
						=，^~,~/~*,不带符号。
					location @name{...}
					在一个server中location配置段可存在多个，用于实现从uri到文件系统的路径映射；
					nginx会根据用户请求的URI来检查定义的所有location，并找出一个最佳匹配，而后应用其配置。
					示例：
						server{
							...
							server_name www.magedu.com; 
							location /images/{
								root /data/imgs/;
							}
						}
						http://www.magedu.com/images/logo.jpg
							-->/data/imgs/images/logo.jpg
							
				9）alias path;	
					路径别名，文档映射的另一种机制；仅能用于location上下文。
					示例：
						http://www.magedu.com/bbs/index.php
						location /bbs/{
							alias /web/forum/;
						}	-->/web/forum/index.html
						location /bbs/{
							root /web/forum/;
						}	-->/web/forum/bbs/index.html
						注意：location中使用root指令和alias指令的意义不同
							a.root,给定的路径应于location中的/uri/左侧的/
							b.alias,给定的路径对应于location中的uri/右侧的/
				
				10）index file...;
					指定默认网页文件，注意：ngx_http_index_module模块
				
				11）error_page cpde...=[=[response]] uri;
					模块：ngx_http_core_module
					定义错误页，以指定响应状态码进行响应
					可用位置：http,server,location,if in location
						error_page 404 /404.html
						error_page 404 =200 /404.html
				
				12)try——files file...uri;
					try_files file...=code;
					按顺序检查文件是否存在，返回第一个找到的文件或文件夹（结尾加斜线表示为文件夹），如果所有的文件或文件夹都找不到，会进行一个内部重定向到最后一个参数。只有最后一个参数可以引起一个内部重定向，之前的参数只设置内部URI的指向。最后一个参数是回退URI且必须存在，否则会出现内部500错误
					location /images/{
						try_files $uri /images/default.gif;
					}
					location / {
						try_files $uri $uri/index.html $uri.html =404;
					}
				
				定义客户端请求相关配置
				13)keepalive_timeout timeout [heaer_timeout];
					设定保持连接超时时长，0表示禁止连接，默认为75s
				14) keepalive_requests number;
					再一次长连接上所允许请求的资源的最大数量，默认为100.
				15）keepalive_disable none | browser ...
					对哪种浏览器禁用长连接
				16）send_timeout time;
					向客户端发送响应报文的超时时长，此处是指两次写操作之间的间隔时长，而非整个响应过程的传输时长。
				17）client_body_buffer_size size;
					用于接收每个客户端请求报文的body部分的缓冲区大小；默认为16k；超出此大小时，其将被暂存到磁盘上的由下面client_body_temp_path指令所定义的位置。
				
				18）client_body_temp_path path [level1[level2[level3]]];
					设定存储客户端请求报文的body部分的临时存储路径及子目录结构和数量，目录名为16进制的数字；
					client_body_temp_path /var/temp/client_body 1 2 2
					1 1级目录占1位16进制，即2^4=16个目录0-f
					2 2级目录占2位16进制，即2^8=256个目录00-ff
					2 3级目录占2位16进制，即2^8=256个目录00-ff
				
				对客户端进行限制的相关配置
				19）limit_rate rate;
					限制响应给客户端的传输速率，单位是bytes/second，默认值0表示无限制
					
				20）limit_except method...{...},仅用于location
					限制客户端使用除了指定的请求方法之外的其他方法
					method:GET,HEAD,POST,PUT,DELETE，MKCOL,COPY,MOVE,OPTIONS,PROPFIND,PROPPATCH,LOCK,UNLOCK,PATCH
					limit_except GET{
						allow 192.16.1.0/24;
						deny all;
					} 除了GET和HEAD之外其他方法仅允许192.168.1.0/24网段主机使用。
					
				文件操作优化的配置
					异步io
				21） aio on|off |threads[=port];
					是否启用aio功能
					
					同步io
				22） directio size|off;
					当文件大于等于给定大小时，例如directio 4m，同步（直接）写磁盘，而非写缓存。
				23） open_file_cache off;
					open_file_cache max=N [inactive=time];
					nginx可以缓存以下三种信息：
						a.文件元数据：文件的描述符、文件大小、最近一次的修改时间
						b.打开的目录结构
						c.没有找到的或者没有权限访问的文件的相关信息
						max=N:可缓存的缓存项上限；达到上限后会使用LRU（最近最少算法）算法实现管理
						inactive=time:缓存项的非活动时长，在此处指定的时长内未被命中的或命中的次数少于open_file_cache_min_uses指令所指定的次数的缓存项，极为非活动项，将被删除。
				24） open_file_cache_errors on|off;
					是否缓存查找时发生错误的文件一类的信息，默认值为off
				25） open_file_cache_min_uses number;
					open_file_cache指令的inactive参数指定的时长内，至少被命中此处指定的次数方可被归类为活动项，默认值为1
				26） open_file_cache_valid time;
					缓存项有效性额检查频率，默认值为60s。
			
			ngx_http_access_module
			ngx_http_access_module 模块，实现基于IP的访问控制功能
				1）allow address|CIDR|unix：|all;
				2)deny address|CIDR|unix:|all;
					http,server,location,limit_except	
					自上而下检查，一旦匹配，将生效，条件严格的置前
					示例：
					location /{
						deny 192.168.1.1;
						allow 192.168.1.0/24;
						allow 10.1.1.0/16;
						allow 2001:0db8::/32;
						deny all;
					}
		
			ngx_http_auth_basic_module
				实现基于用户的访问控制，使用basic机制进行用户认证
				1）auth_basic string|off;
				2）auth_basic_user_file file;
					location /admin/{
						auth_basic "Admin Area";
						auth_basic_user_file /etc/nginx/.ngxpasswd;
					}
					用户口令文件：
						1.明文文本：
							格式 name:password:comment
						2.加密文本:
							由htpasswd命令实现，httpd-tools所提供
			
			ngx_http_stub_status_module
				用于输出nginx的基本状态信息
					输出信息示例：
						Active connections:291
						server accepts handled requests
							16630948 16630948 31070465
							上面三个数组分别对应accepts,handled,requests三个值
						Reading:6 Writing:179 Waiting:106
			
				Active connections:当前状态，活动状态的连接数
				accepts:统计总值，已经接受的客户端请求的总数
				handled:统计总值，已经处理完成的客户端请求的总数
				requests:统计总值，客户端发来的总的请求数
				Reading:当前状态，正在读取客户端请求报文首部的连接的连接数
				Writing:当前状态，正在向客户端发送响应报文过程中的连接数
				Waiting:当前状态，正在等待客户端发出请求的空闲连接数
				1）stud_status；
					示例：
						location /status{
							stub_status;
							allow 172.16.0.0/16;
							deny all;
						}
			
			ngx_http_log_module
				指定日志格式记录请求。
				1）log_format name string ...;
					string可以使用nginx核心模块及其他模块内嵌的变量
				2）access_log path [format[buffer=size][gzip[=level]][flush=time][if=condition]]
					access_log off;
					访问日志文件路径，格式及相关的缓冲配置
						buffer=size
						flush=time
				示例：
					log_format compression '$remote_addr-$remote_user[$time_local]'
						'"$request" $status $bytes_sent'
						'"$http_referer" "http_user_agent" "gzip_ratio"';
					access_log /spool/logs/nginx-access.log compression buffer=32k;	
				3)open_log_file_cache ma=N[inactive=time][min_uses=N][valid=time];
					open_log_file_cache off;
						缓存个日志文件相关的元数据信息
						max:缓存的最大文件描述符数量
						min_uses:在inactive指定的时长内访问大于等于此值方可被当作活动项
						inactive：非活动时常
						valid：验证缓存中各缓存项是否为活动项的时间间隔
			
			ngx_http_gzip_module
				用gzip方法压缩响应数据，节约带宽
				1）gzip on|off;
					启用或禁止gzip压缩
				2）gzip_comp_level level;
					压缩比由低到高： 1~9，默认1
				3）gzip_disable regex ...;
					匹配到客户端浏览器不执行压缩
				4）gzip_min_length length;
					启用压缩功能的响应报文大小阀值。
				5）gzip_http_version 1.0|1.1
					设定启用压缩功能时，协议的最小版本，默认 1.1
				6）gzip_buffers number size;
					支持实现压缩功能时缓冲区数量及每个缓存区的大小，默认32 4k 或16 8k
				7)gzip_types mime-type ...;
					指明仅对哪些类型的资源执行压缩操作；即压缩过滤器默认包含有text/html,不用显式指定，否则报错。
				8）gzip_vary on|off;
					如果启用压缩，是否在响应报文首部插入 “Vary:Accept-Encoding”
				9) gzip_proxied off|expired|no-cache|no-store|private|no_last_modified | no_etag|auth|any...;
					nginx充当代理服务器时，对于后端服务器的响应报文，在何种条件下启动压缩功能
					off:不启用压缩
					expired,no-cache,no-store,private:对后端服务器的响应报文首部Cache-Control值任何一个，启用压缩功能。
					
					示例：
						gzip on;
						gzip_comp_level 6;
						gzip_min_length 64;
						gzip_proxied any;
						gzip_types text/xml text/css application/javascript;


​		