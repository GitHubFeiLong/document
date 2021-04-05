# Spring Cloud

搭建项目时，需要优先兼顾spring cloud的版本（44看完）

[查看官网的cloud版本](https://spring.io/projects/spring-cloud#overview)

![image-20210405143256087](Spring Cloud Alibaba.assets/image-20210405143256087.png)

找到要使用的cloud版本，查看spring boot版本

![image-20210405143357397](Spring Cloud Alibaba.assets/image-20210405143357397.png)

![image-20210405143407131](Spring Cloud Alibaba.assets/image-20210405143407131.png)

## 技术更换

### 服务注册中心

1. Eureka (停更 AP)

2. Zookeeper(CP)

   ```txt
   1. 需要安装软件
   ```

3. Consul(CP)

   ```tet
   1. 下载：https://www.consul.io/downloads
   2. 解压
   3. 启动命令：consul agent -dev
   4. 页面：localhost:8500
   ```

4. Nacos

### 服务调用

1. Ribbon 
2. LoadBalancer(代替Ribbon)
3. Feign(死了)
4. OpenFeign（自带Ribbon，自带负载均衡）

### 服务熔断降级

1. Hystrix（死）
2. resilience4j
3. sentinel(alibaba)

### 服务网关

1. Zuul（死）
2. gateway

### 服务配置

1. Config(死)
2. Nacos

## 服务总线

1. Bus(死)
2. Nacos

