# Docker(镜像与容器)



## 1.安装卸载docker

[docker下载官网地址]: https://docs.docker.com/get-docker/	"选择下载的平台"



### 1.1.卸载docker

```bash
# 查看已安装的docker列表
yum list installed |grep docker

#删除已安装的docker
yum -y remove docker-ce.x86_64
```

### 1.2.安装docker

```bash
$ sudo yum install -y yum-utils

$ sudo yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo

# 安装最新版
$ sudo yum install docker-ce docker-ce-cli containerd.io   

# 列出docker 版本
yum list docker-ce --showduplicates | sort -r

## 安装指定版本的docker 例如 docker-ce-18.09.1
$ sudo yum install docker-ce-<VERSION_STRING> docker-ce-cli-<VERSION_STRING> containerd.io
```

### 1.3.启动docker

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



## 2.docker 帮助命令使用

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



## 3.镜像

### 3.1.查看镜像列表

```bash
$ sudo docker image list
or
$ sudo docker images
```

### 3.2.配置镜像加速器

```bash
# 如果不存在/etc/docker文件夹就创建
$ sudo mkdir -p /etc/docker
# 不存在daemon.json 文件就创建
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://ivy3rays.mirror.aliyuncs.com"]
}
EOF
# 重启服务
sudo systemctl daemon-reload
sudo systemctl restart docker
```

### 3.3.拉取镜像

```bash
# 拉取镜像
$ sudo docker pull image-name[:version]
```

### 3.4.删除查找镜像

#### 3.4.1.查找镜像

```bash
$ sudo docker search tomcat
```

#### 3.4.2.删除镜像

删除镜像，可以使用镜像的名称，镜像的id 多个使用空格隔开。

```bash
$ sudo docker rmi image-name [IMAGE ID]
```



## 4.容器

### 4.1.创建容器

```bash
$ sudo Usage:	docker run [OPTIONS] IMAGE [COMMAND] [ARG...]
```

#### 4.1.1. 创建交互式容器

```bash
# 创建 centos 容器
docker run -it --name=c1 centos /bin/bash
# -i: 交互式容器
# -t: tty终端，操作容器
```

#### 4.1.2. 创建守护式容器

```bash
$ sudo docker run -itd --name c2 centos /bin/bash

# 连接守护式容器
$ sudo docker exec -it c2 /bin/bash
```

### 4.2.查看容器

#### 4.2.1.查看正在运行的容器

```bash
$ sudo docker ps
```

#### 4.2.2.查看所有容器

```bash
$ sudo docker ps -a
```

#### 4.2.3.查看最后一次运行的容器

```bash
$ sudo docker ps -l
```

#### 4.2.4.查看容器名称

```bash
$ sudo docker ps -aq
```

#### 4.2.5.查看容器的详细信息

```bash
$ sudo docker inspect container-name

# 查看具体属性（例：查看容器IP地址）
$ sudo docker inspect -f='{{.NetworkSettings.IPAddress}}' container-name
```



### 4.3.容器启动、停止、重启

```bash
$ sudo docker start container-name [container-name1]

$ sudo docker stop container-name [container-name1]

$ sudo docker restart container-name [container-name1]
```



### 4.4.删除容器

可以删除多个，但不能删除正在运行的容器。

```bash
$ sudo docker rm container-name [container-id]
# 删除所有容器
$ sudo docker rm `docker ps -aq`
```



### 4.5.查看容器日志

```bash
$ sudo docker logs container-name/container-id
```

### 4.6.容器拷贝

将宿主机上的文件拷贝到容器中取，或把容器中的文件拷贝到宿主机上。

注意：停止的容器也能进行操作

#### 4.6.1.拷入容器

```bash
$ sudo docker cp 需要拷贝的文件或目录 容器名称:容器目录
#例如：
$ sudo docker cp 1.txt c1:/root
```

#### 4.6.2.拷入宿主机

```bash
$ sudo docker cp 容器名称:容器目录 需要拷贝到文件目录
#例如：
$ sudo docker cp  c1:/root/1.txt /root
```



### 4.7.目录挂载

我们可以在创建容器的时候，将宿主机的目录与容器内的目录进行映射，这样我们就可以通过修改宿主机某个目录的文件从而去影响容器。

创建容器添加-v参数后边为 宿主机目录:容器目录

```bash
$ sudo docker run -id --name=c1 -v /opt/:/usr/local/myhtml centos
```

