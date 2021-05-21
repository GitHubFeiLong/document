# Sentinel

[官方文档](https://sentinelguard.io/zh-cn/docs/introduction.html)

> 分布式系统的流量防卫兵

## dashboard 控制台

Sentinel 控制台包含如下功能:

- **查看机器列表以及健康情况**：收集 Sentinel 客户端发送的心跳包，用于判断机器是否在线。
- **监控 (单机和集群聚合)**：通过 Sentinel 客户端暴露的监控 API，定期拉取并且聚合应用监控信息，最终可以实现秒级的实时监控。
- **规则管理和推送**：统一管理推送规则。
- **鉴权**：生产环境中鉴权非常重要。这里每个开发者需要根据自己的实际情况进行定制。



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

## 客户端接入控制台

使用两个步骤将应用连接到 dashboard

### 引入JAR包

根据情况引入下面的jar

```xml
<!--使用spring cloud alibaba-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
    <version>x.y.z</version>
</dependency>

<!--单独使用-->
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-transport-simple-http</artifactId>
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



## Gateway 限流

[网关限流](https://sentinelguard.io/zh-cn/docs/api-gateway-flow-control.html)

S从 1.6.0 版本开始，Sentinel 提供了 Spring Cloud Gateway 的适配模块，可以提供两种资源维度的限流：

- route 维度：即在 Spring 配置文件中配置的路由条目，资源名为对应的 routeId
- 自定义 API 维度：用户可以利用 Sentinel 提供的 API 来自定义一些 API 分组

1. 修改pom文件

   ```xml
   <!--sentinel-spring-cloud-gateway-adapter-->
   <dependency>
       <groupId>com.alibaba.csp</groupId>
       <artifactId>sentinel-spring-cloud-gateway-adapter</artifactId>
       <version>x.y.z</version>
   </dependency>
   ```

2. 注入实例

   > 使用时只需注入对应的 `SentinelGatewayFilter` 实例以及 `SentinelGatewayBlockExceptionHandler` 实例即可。比如：

```java
@Configuration
public class GatewayConfiguration {

    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    public GatewayConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
        // Register the block exception handler for Spring Cloud Gateway.
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }

    @Bean
    @Order(-1)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    @PostConstruct
    public void doInit() {
        initCustomizedApis();
        initGatewayRules();
        initBlockHandler();
    }

    /**
     * Api 接口路径资源配置
     */
    private void initCustomizedApis() {
        Set<ApiDefinition> definitions = new HashSet<>();
        // 定义一组 api， apiName 就是sentinel的资源名称（goudong-message-server-code）
        ApiDefinition api1 = new ApiDefinition("goudong-message-server-code")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    add(new ApiPathPredicateItem().setPattern("/api/message/code/**")
                            // 设置匹配方式，下面设置的是路径前缀匹配
                            .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                }});
        definitions.add(api1);
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }

    /**
     * 初始化限流规则
     */
    private void initGatewayRules() {

        Set<GatewayFlowRule> rules = new HashSet<>();

        // 资源名称，可以是网关中的 route 名称或者用户自定义的 API 分组名称。goudong-oauth2-server 为网关路由id
        rules.add(new GatewayFlowRule("goudong-oauth2-server")
                // resourceMode：规则是针对 API Gateway 的 route（RESOURCE_MODE_ROUTE_ID）还是用户在 Sentinel 中定义的 API 分组（RESOURCE_MODE_CUSTOM_API_NAME），默认是 route。
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_ROUTE_ID)
                // grade：限流指标维度，同限流规则的 grade 字段。(限流阈值类型，QPS 或线程数模式)
                .setGrade(RuleConstant.FLOW_GRADE_QPS)
                // count：限流阈值
                .setCount(1)
                // intervalSec：统计时间窗口，单位是秒，默认是 1 秒。
                .setIntervalSec(1)
                // controlBehavior：流量整形的控制效果，同限流规则的 controlBehavior 字段，目前支持快速失败和匀速排队两种模式，默认是快速失败。
                .setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT)
                // burst：应对突发请求时额外允许的请求数目。
                .setBurst(2)
                // maxQueueingTimeoutMs：匀速排队模式下的最长排队时间，单位是毫秒，仅在匀速排队模式下生效。默认500ms
                .setMaxQueueingTimeoutMs(500)
                // paramItem：参数限流配置。若不提供，则代表不针对参数进行限流，该网关规则将会被转换成普通流控规则；否则会转换成热点规则。其中的字段：
                .setParamItem(new GatewayParamFlowItem()
                        // parseStrategy：从请求中提取参数的策略，目前支持提取来源 IP(PARAM_PARSE_STRATEGY_CLIENT_IP）、Host（PARAM_PARSE_STRATEGY_HOST）、任意 Header（PARAM_PARSE_STRATEGY_HEADER）和任意 URL 参数（PARAM_PARSE_STRATEGY_URL_PARAM）四种模式。
                        .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)
                        // fieldName：若提取策略选择 Header 模式或 URL 参数模式，则需要指定对应的 header 名称或 URL 参数名称。
                        .setFieldName("username")
                        // pattern：参数值的匹配模式，只有匹配该模式的请求属性值会纳入统计和流控；若为空则统计该请求属性的所有值。（1.6.2 版本开始支持）
                        .setPattern("admin")
                        // matchStrategy：参数值的匹配策略，目前支持精确匹配（PARAM_MATCH_STRATEGY_EXACT）、子串匹配（PARAM_MATCH_STRATEGY_CONTAINS）和正则匹配（PARAM_MATCH_STRATEGY_REGEX）。（1.6.2 版本开始支持）
                        .setMatchStrategy(SentinelGatewayConstants.PARAM_MATCH_STRATEGY_EXACT)
                )
        );
        // 手动加载网关规则
        GatewayRuleManager.loadRules(rules);
    }

    /**
     * 自定义限流异常处理
     * ResultSupport 为自定义的消息封装类，代码略
     */
    private void initBlockHandler() {
        GatewayCallbackManager.setBlockHandler(new BlockRequestHandler() {
            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange,
                                                      Throwable throwable) {
                return ServerResponse.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(BodyInserters.fromObject(
                                Result.ofFail("限流")));
            }
        });
    }
}

```



