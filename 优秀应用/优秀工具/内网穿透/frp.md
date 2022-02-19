# FRP

[官网](https://gofrp.org/docs/)

## 前言

最近买了一台云服务器，配置比较低，部署一个单体项目还可以，但是用来部署微服务就资源不够了。本来想着的是，买一台台式机，然后再找运营商要一个外网IP。后面在和朋友的聊天中，偶然知道了内网穿透这个词，也是第一次听到 frp这个工具，于是就进行百度了解。



以下内容全摘抄官网文档

## frp 是什么？

frp 是一个专注于内网穿透的高性能的反向代理应用，支持 TCP、UDP、HTTP、HTTPS 等多种协议。可以将内网服务以安全、便捷的方式通过具有公网 IP 节点的中转暴露到公网。

## 为什么使用 frp？

通过在具有公网 IP 的节点上部署 frp 服务端，可以轻松地将内网服务穿透到公网，同时提供诸多专业的功能特性，这包括：

- 客户端服务端通信支持 TCP、KCP 以及 Websocket 等多种协议。
- 采用 TCP 连接流式复用，在单个连接间承载更多请求，节省连接建立时间。
- 代理组间的负载均衡。
- 端口复用，多个服务通过同一个服务端端口暴露。
- 多个原生支持的客户端插件（静态文件查看，HTTP、SOCK5 代理等），便于独立使用 frp 客户端完成某些工作。
- 高度扩展性的服务端插件系统，方便结合自身需求进行功能扩展。
- 服务端和客户端 UI 页面。

## 安装

frp 采用 Golang 编写，支持跨平台，仅需下载对应平台的二进制文件即可执行，没有额外依赖。由于采用 Golang 编写，所以系统需求和最新的 Golang 对系统和平台的要求一致，具体可以参考 [Golang System requirements](https://golang.org/doc/install#requirements)。

### 下载

目前可以在 Github 的 [Release](https://github.com/fatedier/frp/releases) 页面中下载到最新版本的客户端和服务端二进制文件，所有文件被打包在一个压缩包中。

> 需要下载相同版本的
>
> 例如：
>
> [frp_0.36.2_freebsd_386.tar.gz](https://github.com/fatedier/frp/releases/download/v0.36.2/frp_0.36.2_freebsd_386.tar.gz)  和 [frp_0.36.2_windows_386.zip](https://github.com/fatedier/frp/releases/download/v0.36.2/frp_0.36.2_windows_386.zip)

### 部署

解压缩下载的压缩包，将其中的 frpc 拷贝到内网服务所在的机器上，将 frps 拷贝到具有公网 IP 的机器上，放置在任意目录。

## 开始使用

编写配置文件，先通过 `./frps -c ./frps.ini` 启动服务端，再通过 `./frpc -c ./frpc.ini` 启动客户端。如果需要在后台长期运行，建议结合其他工具使用，例如 `systemd` 和 `supervisor`。

如果是 Windows 用户，需要在 cmd 终端中执行命令。

配置文件如何编写可以参考 [示例](https://gofrp.org/docs/examples/) 中的内容。



## 例如

外网访问内网服务

1. frps.ini 内容如下：

   ```properties
   [common]
   bind_port = 7000
   ```

2. 在需要暴露到外网的机器上部署 frpc，且配置如下：

   ```properties
   [common]
   server_addr = 47.108.31.41
   server_port = 7000
   
   [ssh]
   type = tcp
   local_ip = 127.0.0.1
   local_port = 80
   remote_port = 80
   ```

3. 分别启动

   + 外网启动服务端

     ```bash
     [root@iZ2vc28obhvfh8zjqb9lc9Z frp]# ./frps -c frps.ini 
     2021/05/20 14:50:48 [I] [root.go:200] frps uses config file: frps.ini
     2021/05/20 14:50:48 [I] [service.go:192] frps tcp listen on 0.0.0.0:7000
     2021/05/20 14:50:48 [I] [root.go:209] frps started successfully
     ```

   + 内网启动客户端

     ```bash
     D:\workspace\fs-workspaces\tools\frp_0.36.2_windows_386>frpc.exe -c frpc.ini
     2021/05/20 14:54:44 [I] [service.go:304] [cf0d4fc3871f5681] login to server success, get run id [cf0d4fc3871f5681], server udp port [0]
     2021/05/20 14:54:44 [I] [proxy_manager.go:144] [cf0d4fc3871f5681] proxy added: [ssh]
     2021/05/20 14:54:44 [I] [control.go:180] [cf0d4fc3871f5681] [ssh] start proxy success
     ```

   > 注意：
   >
   > 当出现 `start error: port unavailable`，表示该端口在外网服务端已经被使用。

