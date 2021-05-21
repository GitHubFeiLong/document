# Sentinel:分布式系统的流量防卫兵

[官方文档](https://sentinelguard.io/zh-cn/docs/introduction.html)

## dashboard 控制台

### 下载dashboard

+ 下载已经发布的 [releases 页面](https://github.com/alibaba/Sentinel/releases) 版本 
+ 下载源代码，然后`mvn clean package` 自己构建。 

### 启动 dashboard

使用下面的命令启动：

```bash
java -Dserver.port=8080 -Dcsp.sentinel.dashboard.server=localhost:8080 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard.jar
```

> -Dserver.port=8080 项目端口号
>
> -Dcsp.sentinel.dashboard.server=localhost:8080 设置仪表盘的服务地址，供客户端连接
>
> 注：若您的应用为 Spring Boot 或 Spring Cloud 应用，您可以通过 Spring 配置文件来指定配置，详情请参考 [Spring Cloud Alibaba Sentinel 文档](https://github.com/spring-cloud-incubator/spring-cloud-alibaba/wiki/Sentinel)。

从 Sentinel 1.6.0 起，Sentinel 控制台引入基本的**登录**功能，默认用户名和密码都是 `sentinel`。可以参考 [鉴权模块文档](https://sentinelguard.io/zh-cn/docs/dashboard.html#鉴权) 配置用户名和密码。

## 客户端

使用两个步骤将应用连接到 dashboard

### 下载依赖

```xml
<!--sentinel 流量防卫兵-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
    <version>x.y.z</version>
</dependency>
```

### 配置dashboard 的IP地址

启动应用时添加 dashboard IP地址: `-Dcsp.sentinel.dashboard.server=localhost:8080`。

```yaml
spring:
  cloud:
     sentinel:
      transport:
        dashboard: localhost:8080 #配置Sentinel dashboard地址
        port: 8719 #默认端口，如果被占用会加一。用于连接dashboard
```

## 基本使用 - 资源与规则

[资源和规则](https://sentinelguard.io/zh-cn/docs/basic-api-resource-rule.html)

### 定义资源

+ 主流框架的默认适配

+ 抛出异常的方式定义资源

+ 返回布尔值方式定义资源

+ 注解方式定义资源

+ 异步调用支持

### 规则

**流量控制规则**、**熔断降级规则**、**系统保护规则**、**来源访问控制规则** 和 **热点参数规则**。



## @SentinelResource 注解

[@SentinelResource 注解讲解](https://sentinelguard.io/zh-cn/docs/annotation-support.html)

> 注意：注解方式埋点不支持 private 方法。



## 使用Nacos 推模式动态规则扩展

```xml
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-nacos</artifactId>
    <version>x.y.z</version>
</dependency>
```



## 网关限流

[网关限流](https://sentinelguard.io/zh-cn/docs/api-gateway-flow-control.html)

Sentinel 支持对 Spring Cloud Gateway、Zuul 等主流的 API Gateway 进行限流。