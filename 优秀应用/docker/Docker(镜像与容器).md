# Docker(镜像与容器)介绍及安装

## 1、什么是Docker

+ 使用最广泛的开源容器引擎
+ 一种操作系统级虚拟化技术
+ 依赖于Linux 内核特性：Namespace 和 Cgroups
+ 一个简单的应用程序打包工具

### 1.1、Docker 设计目标

1. 提供简单的应用程序打包工具
2. 开发人员和运维人员职责逻辑分离
3. 多环境保持一致

### 1.2、Docker 基本组成

1. Docker Client ： 客户端
2. Ddocker Daemon： 守护进程
3. Docker Images ：镜像
4. Docker Container ：容器
5. Docker Registry：镜像仓库

![image-20200829160156957](Docker(镜像与容器).assets/image-20200829160156957-1607146715475.png)



### 1.3、容器 VS 虚拟机

|          | Container                                | VM               |
| -------- | ---------------------------------------- | ---------------- |
| 启动速度 | 秒级                                     | 分钟级           |
| 运行性能 | 接近原生                                 | 5%左右损失       |
| 磁盘占用 | MB                                       | GB               |
| 数量     | 成百上千                                 | 一般几十台       |
| 隔离性   | 进程级别                                 | 系统级（更彻底） |
| 操作系统 | 只支持Linux                              | 几乎所有         |
| 封装程度 | 只打包项目代码和依赖关系，共享宿主机内核 | 完整的操作系统   |

### 1.4、Docker 应用场景

1. 应用程序打包和发布
2. 应用程序隔离
3. 持续集成
4. 部署微服务
5. 快速搭建测试环境
6. 提供PaaS产品（平台即服务）



## 2、安装卸载docker

[docker下载官网地址]: https://docs.docker.com/get-docker/	"选择下载的平台"

### 2.1、卸载docker

```bash
# 查看已安装的docker列表
yum list installed |grep docker

#删除已安装的docker
yum -y remove docker-ce.x86_64
```

> 完全卸载
>
> ```bash
> $ sudo yum remove docker \
>                   docker-client \
>                   docker-client-latest \
>                   docker-common \
>                   docker-latest \
>                   docker-latest-logrotate \
>                   docker-logrotate \
>                   docker-engine
> ```
>

### 2.2、安装docker

```bash
$ sudo yum install -y yum-utils

$ sudo yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo

# 安装最新版
$ sudo yum install docker-ce docker-ce-cli containerd.io   

# 列出docker 客户端版本
yum list docker-ce --showduplicates | sort -r

## 安装指定版本的docker 例如 docker-ce-18.09.1
$ sudo yum install docker-ce-<VERSION_STRING> docker-ce-cli-<VERSION_STRING> containerd.io
```

> 注意：
>
> centos8安装时出现错误信息
>
> ```bash
> [root@localhost: ~]#yum install docker-ce docker-ce-cli containerd.io
> Docker CE Stable - x86_64                                                                            591  B/s | 3.5 kB     00:05    
> Error: 
>  Problem 1: problem with installed package podman-1.0.0-2.git921f98f.module_el8.0.0+58+91b614e7.x86_64
>   - package podman-1.0.0-2.git921f98f.module_el8.0.0+58+91b614e7.x86_64 requires runc, but none of the providers can be installed
>   - package podman-1.6.4-10.module_el8.2.0+305+5e198a41.x86_64 requires runc >= 1.0.0-57, but none of the providers can be installed
>   - package containerd.io-1.3.7-3.1.el8.x86_64 conflicts with runc provided by runc-1.0.0-55.rc5.dev.git2abd837.module_el8.0.0+58+91b614e7.x86_64
>   - package containerd.io-1.3.7-3.1.el8.x86_64 obsoletes runc provided by runc-1.0.0-55.rc5.dev.git2abd837.module_el8.0.0+58+91b614e7.x86_64
>   - package containerd.io-1.3.7-3.1.el8.x86_64 conflicts with runc provided by runc-1.0.0-65.rc10.module_el8.2.0+305+5e198a41.x86_64
>   - package containerd.io-1.3.7-3.1.el8.x86_64 obsoletes runc provided by runc-1.0.0-65.rc10.module_el8.2.0+305+5e198a41.x86_64
>   - conflicting requests
>   - package runc-1.0.0-56.rc5.dev.git2abd837.module_el8.2.0+303+1105185b.x86_64 is filtered out by modular filtering
>   - package runc-1.0.0-64.rc10.module_el8.2.0+304+65a3c2ac.x86_64 is filtered out by modular filtering
>  Problem 2: problem with installed package buildah-1.5-3.gite94b4f9.module_el8.0.0+58+91b614e7.x86_64
>   - package buildah-1.5-3.gite94b4f9.module_el8.0.0+58+91b614e7.x86_64 requires runc >= 1.0.0-26, but none of the providers can be installed
>   - package buildah-1.11.6-7.module_el8.2.0+305+5e198a41.x86_64 requires runc >= 1.0.0-26, but none of the providers can be installed
>   - package containerd.io-1.3.7-3.1.el8.x86_64 conflicts with runc provided by runc-1.0.0-55.rc5.dev.git2abd837.module_el8.0.0+58+91b614e7.x86_64
>   - package containerd.io-1.3.7-3.1.el8.x86_64 obsoletes runc provided by runc-1.0.0-55.rc5.dev.git2abd837.module_el8.0.0+58+91b614e7.x86_64
>   - package containerd.io-1.3.7-3.1.el8.x86_64 conflicts with runc provided by runc-1.0.0-65.rc10.module_el8.2.0+305+5e198a41.x86_64
>   - package containerd.io-1.3.7-3.1.el8.x86_64 obsoletes runc provided by runc-1.0.0-65.rc10.module_el8.2.0+305+5e198a41.x86_64
>   - package docker-ce-3:19.03.13-3.el8.x86_64 requires containerd.io >= 1.2.2-3, but none of the providers can be installed
>   - conflicting requests
>   - package runc-1.0.0-56.rc5.dev.git2abd837.module_el8.2.0+303+1105185b.x86_64 is filtered out by modular filtering
>   - package runc-1.0.0-64.rc10.module_el8.2.0+304+65a3c2ac.x86_64 is filtered out by modular filtering
> (try to add '--allowerasing' to command line to replace conflicting packages or '--skip-broken' to skip uninstallable packages or '--nobest' to use not only best candidate packages)
> 
> ```
>
> 此时只需要执行下面的命令即可：
>
> ```bash
> [root@localhost: ~]#yum install --allowerasing docker-ce
> ```



### 2.3、启动docker

```bash
$ sudo systemctl start docker
```

1. 通过运行hello-world映像，验证Docker引擎已正确安装。

   ```bash
   $ sudo docker run hello-world
   ```

2. 停止docker

   ```bash
   $ sudo systemctl stop docker
   ```



## 3、docker 帮助命令使用

1. 查看docker 的帮助命令,最后有一句`Run 'docker COMMAND --help' for more information on a command.` 查看docker 子命令的帮助文档使用`docker `

   ```bash
   [root@VM-0-9-centos: ~]#docker --help
   
   Usage:	docker [OPTIONS] COMMAND
   
   A self-sufficient runtime for containers
   
   Options:
   ...
   
   Management Commands:
     builder     Manage builds
   ...
   
   Commands:
   ...
     images      List images
     ps          List containers
     pull        Pull an image or a repository from a registry
    ...
   Run 'docker COMMAND --help' for more information on a command.
   ```



## 4、镜像

### 4.1、镜像是什么？

+ 一个分层存储的文件
+ 一个软件的环境
+ 一个镜像可以创建N个容器
+ 一种标准化的交互
+ 一个不包含Linux 内核而又精简的Linux 操作系统

​		镜像不是一个单一个文件，而是有多层构成。我们可以通过 “docker history <ID/NAME>” 查看镜像中各层内容及大小，每层对应着Dockerfile 中的一条指令。Docker镜像默认存储在 /var/lib/docker/\\<storage-driver\\>中。

### 4.2、镜像从哪里来？

​		Docker Hub 是由Docker 公司负责维护的公共注册中心，包含大量的容器镜像，Docker 工具默认从这个公共镜像库下载镜像。

​		地址：https://hub.docker.com/explore

​		配置镜像加速器：https://www.daocloud.io/mirror

```bash
curl -sSL https://get.daocloud.io/daotools/set_mirror.sh | sh -s http://f1361db2.m.daocloud.io
```

### 4.3、镜像与容器联系

​		如图，容器其实是在镜像的最上面加了一层读写层，在运行容器里文件改动时，会先从镜像里要写的文件复制到容器自己的文件系统中（读写层）。

​		如果容器删除了，最上面的读写层也就删除了，改动也就丢失了。所以无论多少个容器共享一个镜像，所做的写操作都是从镜像的文件系统中复制过来操作的，并不会修改镜像的源文件，这种方式提高磁盘利用率。

​		若想持久化这些改动，可以通过docker commit 将容器保存成一个新镜像。

![image-20200829175017114](Docker(镜像与容器).assets/image-20200829175017114.png)

### 4.4、查看镜像列表

```bash
$ sudo docker image list
or
$ sudo docker images
```

### 4.5、配置阿里云镜像加速器

```bash
# 如果不存在/etc/docker文件夹就创建
$ sudo mkdir -p /etc/docker
# 不存在daemon.json 文件就创建
$ sudo vim /etc/docker/daemon.json
# 粘贴下面的内容
{
  "registry-mirrors": ["https://ivy3rays.mirror.aliyuncs.com"]
}

# 重启服务
sudo systemctl daemon-reload
sudo systemctl restart docker
```

### 4.6、拉取镜像

```bash
# 拉取镜像
$ sudo docker pull image-name[:version]
```

### 4.7、删除查找镜像

#### 4.7.1、查找镜像

```bash
$ sudo docker search tomcat
```

#### 4.7.2、删除镜像

删除镜像，可以使用镜像的名称，镜像的id 多个使用空格隔开。

```bash
$ sudo docker rmi image-name [IMAGE ID]
```

## 5、容器

### 5.1、创建容器

```bash
$ sudo Usage:	docker run [OPTIONS] IMAGE [COMMAND] [ARG...]
```

#### 5.1.1、创建交互式容器

```bash
# 创建 centos 容器 (--name c1 也可以)
docker run -it --name=c1 centos /bin/bash
# -i: 交互式容器
# -t: tty终端，操作容器
```

#### 5.1.2、创建守护式容器

```bash
$ sudo docker run -itd --name c2 centos /bin/bash

# 连接守护式容器
$ sudo docker exec -it c2 /bin/bash
```

#### 5.1.3、创建端口映射容器

```bash
# -p：端口映射
$ sudo docker run -itd --name=t1 -p 8888:8080 new-containner /bin/bash
```

#### 5.1.4、创建目录挂载容器
我们可以在创建容器的时候，将宿主机的目录与容器内的目录进行映射，这样我们就可以通过修改宿主机某个目录的文件从而去影响容器。

创建容器添加-v参数后边为 宿主机目录:容器目录

```bash
$ sudo docker run -id --name=c1 -v /opt/:/usr/local/myhtml centos
```



### 5.2、查看容器

#### 5.2.1、查看正在运行的容器

```bash
$ sudo docker ps
```

#### 5.2.2、查看所有容器

```bash
$ sudo docker ps -a
```

#### 5.2.3、查看最后一次运行的容器

```bash
$ sudo docker ps -l
```

#### 5.2.4、查看容器名称

```bash
$ sudo docker ps -aq
```

#### 5.2.5、查看容器的详细信息

```bash
$ sudo docker inspect container-name

# 查看具体属性（例：查看容器IP地址）
$ sudo docker inspect -f='{{.NetworkSettings.IPAddress}}' container-name
```

#### 5.2.6、容器启动、停止、重启

```bash
$ sudo docker start container-name [container-name1]

$ sudo docker stop container-name [container-name1]

$ sudo docker restart container-name [container-name1]
```

#### 5.2.7、删除容器

可以删除多个，但不能删除正在运行的容器。

```bash
$ sudo docker rm container-name [container-id]
# 删除所有容器
$ sudo docker rm `docker ps -aq`
```

#### 5.2.8、查看容器日志

```bash
$ sudo docker logs container-name/container-id
```

### 5.3、容器拷贝

将宿主机上的文件拷贝到容器中取，或把容器中的文件拷贝到宿主机上。

注意：停止的容器也能进行操作

#### 5.3.1、拷入容器

```bash
$ sudo docker cp 需要拷贝的文件或目录 容器名称:容器目录
#例如：
$ sudo docker cp 1.txt c1:/root
```

#### 5.3.2、拷入宿主机

```bash
$ sudo docker cp 容器名称:容器目录 需要拷贝到文件目录
#例如：
$ sudo docker cp  c1:/root/1.txt /root
```
