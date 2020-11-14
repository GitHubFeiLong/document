# Docker 镜像制作

1. docker 镜像制作
2. docker 仓库
3. docker 网络管理
4. 搭建 docker swarm 集群
5. docker compose 编排工具
6. docker 的web可视化管理工具

## 1.Docker 镜像制作

构建docker 镜像方式有两种：

1. 使用 docker commit 命令
2. 使用 docker build 和 Dockerfile 文件

### 1.1 docker commit

#### 1.1.1 制作步骤

```bash
#提交一个正在运行的容器为一个新的镜像
$ sudo docker commit

#例如：将正在运行的 run-container 制作成一个镜像，镜像名叫做new-containner
$ sudo docker commit run-container new-containner
```

#### 1.1.2 端口映射

```bash
# -p：端口映射
$ sudo docker run -itd --name=t1 -p 8888:8080 new-containner /bin/bash
# 启动容器中的服务
$ sudo docker exec new-containner /usr/local/tomcat/bin/startup.sh
```



#### 1.1.3 镜像/容器 打包

```bash
# 镜像打包
# 1. 镜像打包：
$ sudo docker save -o /root/image-name.tar container-name
# 2.将打包的镜像上传到其他服务器
$ sudo  scp image-name.tar ip:/root
# 3. 导入镜像
$ sudo docker load -i /root/image-name.tar

# 容器打包
# 1. 容器打包
$ sudo dockerexport -o /root/container-name.tar container-name
# 2. 导入容器
docker import container-name.tar container-name:laster

```



### 1.2 docker builder

Dockerfile 使用基本的基于 DSL 语法的指令来构建一个Docker镜像，之后使用docker builder命令基于该Dockerfile 中的指令构建一个新的镜像。

#### 1.2.1 DSL语法

![image-20200915221406253](Docker(镜像制作).assets/image-20200915221406253.png)

```bash
DSL语法：

1）FROM（指定基础image）
构建指令，必须指定且需要在Dockerfile其他指令的前面。后续的指令都依赖于该指令指定的image。FROM指令指定的基础image可以是官方远程仓库中的，也可以位于本地仓库。
FROM命令告诉docker我们构建的镜像是以哪个(发行版)镜像为基础的。
第一条指令必须是FROM指令。并且，如果在同一个Dockerfile中创建多个镜像时，可以使用多个 FROM 指令。
该指令有两种格式：
FROM <image>
指定基础image为该image的最后修改的版本。或者：
FROM <image>:<tag>
指定基础image为该image的一个tag版本。
RUN后面接要执行的命令，比如，我们想在镜像中安装vim，只需在Dockfile中写入 RUN yum install ‐y vim

2）MAINTAINER（用来指定镜像创建者信息）
构建指令，用于将image的制作者相关的信息写入到image中。当我们对该image执行docker inspect命令时，输出中有相应的字段记录该信息。
格式：
MAINTAINER <name>

3）RUN（安装软件用）
构建指令，RUN可以运行任何被基础image支持的命令。如基础image选择了ubuntu，那么软件管理部分只能使用ubuntu的命令。
该指令有两种格式：
RUN <command>
RUN ["executable", "param1", "param2" ... ]

4）CMD（设置container启动时执行的操作）
设置指令，用于container启动时指定的操作。该操作可以是执行自定义脚本，也可以是执行系统命令。
该指令只能在文件中存在一次，如果有多个，则只执行最后一条。
该指令有三种格式：
CMD ["executable","param1","param2"]
CMD command param1 param2
当Dockerfile指定了ENTRYPOINT，那么使用下面的格式：
CMD ["param1","param2"]
其中：
ENTRYPOINT指定的是一个可执行的脚本或者程序的路径，该指定的脚本或者程序将会以param1和param2作为参数执行。
所以如果CMD指令使用上面的形式，那么Dockerfile中必须要有配套的ENTRYPOINT。

5）ENTRYPOINT（设置container启动时执行的操作）
设置指令，指定容器启动时执行的命令，可以多次设置，但是只有最后一个有效。
两种格式:
ENTRYPOINT ["executable", "param1", "param2"]
ENTRYPOINT command param1 param2
该指令的使用分为两种情况，一种是独自使用，另一种和CMD指令配合使用。
当独自使用时，如果你还使用了CMD命令且CMD是一个完整的可执行的命令，那么CMD指令和
ENTRYPOINT会互相覆盖，只有最后一个CMD或者ENTRYPOINT有效。
# CMD指令将不会被执行，只有ENTRYPOINT指令被执行
CMD echo “Hello, World!”
ENTRYPOINT ls ‐l
另一种用法和CMD指令配合使用来指定ENTRYPOINT的默认参数，这时CMD指令不是一个完整的可执行命令，仅仅是参数部分；
ENTRYPOINT指令只能使用JSON方式指定执行命令，而不能指定参数。
FROM ubuntu
CMD ["‐l"]
ENTRYPOINT ["/usr/bin/ls"]

6）USER（设置container容器的用户）
设置指令，设置启动容器的用户，默认是root用户。
# 指定memcached的运行用户
ENTRYPOINT ["memcached"]
USER daemon
或者
ENTRYPOINT ["memcached", "‐u", "daemon"]

7）EXPOSE（指定容器需要映射到宿主机器的端口）
设置指令，该指令会将容器中的端口映射成宿主机器中的某个端口。当你需要访问容器的时候，可以不是用容器的IP地址而是使用宿主机器的IP地址和映射后的端口。
要完成整个操作需要两个步骤，首先在Dockerfile使用EXPOSE设置需要映射的容器端口，然后在运行容器的时候指定‐p选项加上EXPOSE设置的端口，这样EXPOSE设置的端口号会被随机映射成宿主机器中的一个端口号。
也可以指定需要映射到宿主机器的那个端口，这时要确保宿主机器上的端口号没有被使用。
EXPOSE指令可以一次设置多个端口号，相应的运行容器的时候，可以配套的多次使用‐p选
项。
格式:
EXPOSE <port> [<port>...]
# 映射一个端口
EXPOSE port1
# 相应的运行容器使用的命令
docker run ‐p port1 image
# 映射多个端口
EXPOSE port1 port2 port3
# 相应的运行容器使用的命令
docker run ‐p port1 ‐p port2 ‐p port3 image
# 还可以指定需要映射到宿主机器上的某个端口号
docker run ‐p host_port1:port1 ‐p host_port2:port2 ‐p host_port3:port3
image
端口映射是docker比较重要的一个功能，原因在于我们每次运行容器的时候容器的IP地址不能指定而是在桥接网卡的地址范围内随机生成的。
宿主机器的IP地址是固定的，我们可以将容器的端口的映射到宿主机器上的一个端口，免去每次访问容器中的某个服务时都要查看容器的IP的地址。
对于一个运行的容器，可以使用docker port 加上容器中需要映射的端口和容器的ID来查看该端口号在宿主机器上的映射端口。

8）ENV（用于设置环境变量）
主要用于设置容器运行时的环境变量
格式:
ENV <key> <value>
设置了后，后续的RUN命令都可以使用，container启动后，可以通过docker inspect查看这个环境变量，也可以通过在docker run ‐‐env key=value时设置或修改环境变量。
假如你安装了JAVA程序，需要设置JAVA_HOME，那么可以在Dockerfile中这样写：
ENV JAVA_HOME /path/to/java/dirent

9）ADD（从src复制文件到container的dest路径）
主要用于将宿主机中的文件添加到镜像中
构建指令，所有拷贝到container中的文件和文件夹权限为0755，uid和gid为0；如果是一个目录，那么会将该目录下的所有文件添加到container中，不包括目录；
如果文件是可识别的压缩格式，则docker会帮忙解压缩（注意压缩格式）；如果<src>是文件且<dest>中不使用斜杠结束，则会将<dest>视为文件，<src>的内容会写入<dest>；如果<src>是文件且<dest>中使用斜杠结束，则会<src>文件拷贝到<dest>目录下。
格式:
ADD <src> <dest>
<src> 是相对被构建的源目录的相对路径，可以是文件或目录的路径，也可以是一个远程的文件url;
<dest> 是container中的绝对路径

10）VOLUME（指定挂载点)）
设置指令，使容器中的一个目录具有持久化存储数据的功能，该目录可以被容器本身使用，也可以共享给其他容器使用。我们知道容器使用的是AUFS，这种文件系统不能持久化数据，当容器关闭后，所有的更改都会丢失。当容器中的应用有持久化数据的需求时可以在Dockerfile中使用该指令。
格式:
VOLUME ["<mountpoint>"]
例如：
FROM base
VOLUME ["/tmp/data"]
运行通过该Dockerfile生成image的容器，/tmp/data目录中的数据在容器关闭后，里面的
数据还存在。
例如另一个容器也有持久化数据的需求，且想使用上面容器共享的/tmp/data目录，那么可
以运行下面的命令启动一个容器：
docker run ‐t ‐i ‐rm ‐volumes‐from container1 image2 bash
其中：container1为第一个容器的ID，image2为第二个容器运行image的名字。

11）WORKDIR（切换目录）
设置指令，可以多次切换(相当于cd命令)，对RUN,CMD,ENTRYPOINT生效。
格式:
WORKDIR /path/to/workdir
# 在/p1/p2下执行vim a.txt
WORKDIR /p1 WORKDIR p2 RUN vim a.txt

12）ONBUILD（在子镜像中执行）
格式：
ONBUILD <Dockerfile关键字>
ONBUILD 指定的命令在构建镜像时并不执行，而是在它的子镜像中执行。
```



#### 1.2.2 案例

```bash
通过dockerfile构建镜像步骤：
1、创建一个目录
2、在目录下创建Dockerfile文件以及其他文件
3、通过docker build构建镜像
4、通过构建的镜像启动容器
栗子：
1、创建一个目录：/usr/local/rw_test
2、编辑Dockerfile文件,vim Dockerfile
3、编辑内容如下

===================
#pull down centos image
FROM docker.io/centos
MAINTAINER ruanwen onlien033_login@126.com
#install nginx
RUN yum install ‐y pcre pcre‐devel openssl openssl‐devel gcc gcc+ wget vim net‐tools
RUN useradd www ‐M ‐s /sbin/nologin
RUN cd /usr/local/src && wget http://nginx.org/download/nginx‐1.8.0.tar.gz && tar ‐zxvf nginx‐1.8.0.tar.gz
RUN cd /usr/local/src/nginx‐1.8.0 && ./configure ‐‐prefix=/usr/local/nginx ‐‐user=www ‐‐group=www ‐‐with‐http_stub_status_module ‐‐with‐http_ssl_module && make && make install

ENTRYPOINT /usr/local/nginx/sbin/nginx && tail ‐f
/usr/local/nginx/logs/access.log
===================

4、在rw_test目录下构建镜像：
docker build ‐t rw_nginx ‐‐rm=true .
‐t 表示选择指定生成镜像的用户名，仓库名和tag
‐‐rm=true 表示指定在生成镜像过程中删除中间产生的临时容器。
注意：上面构建命令中最后的.符号不要漏了，表示使用当前目录下的Dockerfile构建镜像

5、测试
docker run ‐ti ‐d ‐‐name test_nginx ‐p 8899:80 rw_nginx /bin/bash
docker exec test_nginx /bin/bash
通过浏览器访问：http://ip:8899
```



## 2 docker 仓库

Docker仓库（Repository）类似与代码仓库，是Docker集中存放镜像文件的地方。

### 2.1 docker hub

```tex
1、打开 https://registry.hub.docker.com/
2、注册账号：略
3、创建仓库（Create Repository）：略
4、设置镜像标签
	docker tag local‐image:tagname new‐repo:tagname（设置tag）
	eg:docker tag hello‐world:latest 108001509033/test‐hello‐world:v1
5、登录docker hub
	docker login(回车，输入账号以及密码)
6、推送镜像
	docker push new‐repo:tagname
	eg：docker push 108001509033/test‐hello‐world:v1
```

### 2.2 阿里云

略：参考官方文档

```bash
步骤：
1、创建阿里云账号
2、创建命名空间
3、创建镜像仓库
4、操作指南
$ sudo docker login ‐‐username=[账号名称] registry.cn‐
hangzhou.aliyuncs.com
$ sudo docker tag [ImageId] registry.cn‐
hangzhou.aliyuncs.com/360buy/portal:[镜像版本号]
$ sudo docker push registry.cn‐hangzhou.aliyuncs.com/360buy/portal:[镜像版
本号]
```

### 2.3 搭建私有仓库

```bash

```

