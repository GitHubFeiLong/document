## Feign组件

#### Feign组件入门

##### 导入依赖

```xml
 <!--springcloud整合的openFeign-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
```



##### 配置调用接口

```java
/**
 * 声明需要调用的微服务名称
 * @FeignClient
 *  #name:服务提供者名称
 */
@FeignClient(name="product-service")
public interface Prouduct {

    /**
     * 配置u要调用的微服务接口
     */
    @RequestMapping(value="/user/{id}",method= RequestMethod.GET)
    public User findById(@PathVariable("id") Integer id);
}
```

- 定义各种类型的参数，可以接受springmvc中的注解（@PathVariable，@RequestParam，@RequestHeader）不能省略value的配置
- @FeignClien：通过name指定需要调用的微服务的名称，会创建ribbon的负载均衡器。会通过动态代理的形式创建ProductFeignClient接口的实现类

##### 启动类上激活feign

```java
@SpringBootApplication
@EntityScan("com.mf.entity")
// 激活Feign
@EnableFeignClients
public class OrderApplication extends SpringBootServletInitializer {

```



##### 通过自动的接口调用远程微服务

```java
   @Autowired
   private ProuductFeignClient prouductFeignClient;

    @RequestMapping(value="/{id}")
    public User selectUserById(@PathVariable String id){
        User user = prouductFeignClient.findById(id);
        return user;
    }
```

##### Feign配置：

```yaml
#配置feign日志的输出
#日志类别：NONE(不输入，性能最高),  BASIC（适用生产环境追踪问题）,  HEADERS（在BASIC的基础上，记录请求和响应头的信息）, FULL（记录所有）;
feign:
  client:
    config:
      product-service: # 需要调用的服务名称
        loggerLevel: FULL

#日志
logging:
  level:
    #root: debug
    #输出指定类的日志
    com.mf.feign.ProuductFeignClient: debug
```

#### Feign和Ribbon的区别

Ribbon是一个客户端的负载均衡器

Feign是在ribbon的基础上进行了封装

Feign源码分析	

## Hystrix组件

#### 对RestTemplate的支持

引入hystrix的依赖

```xml
<!--hystrix依赖-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
```



在启动类中激活Hystrix

```java
// 激活Hystrix
@EnableCircuitBreaker
```

配置熔断触发的降级逻辑

```java
/**
     * 降级方法
     * 和需要受到保护的方法的返回值一致
     * 方法的参数一致
     */
    public User orderFallBack(String id){
        User user = new User();
        user.setName("触发了降级方法");
        return user;
    }

    /**
     * 指定统一的降级方法
     *  参数： 不能有方法参数
     */
    public User defaultFallBack(){
        User user = new User();
        user.setName("触发了降级方法");
        return user;
    }
```



在需要收到保护的接口上使用@HystrixCommand配置

```java
/**
 * @DefaultProperties ：指定此接口的公共的熔断设置
 *      如果 @DefaultProperties 指定了公共的降级方法
 *      在@HystrixCommand不需要单独指定
 */
@DefaultProperties(defaultFallback = "defaultFallBack")
public class OrderController {
    @Autowired
    private RestTemplate restTemplate;

/**
     * 基于ribbon的形式调用远程微服务
     * 1.使用@LoadBalanced声明RestTemplate
     * 2.使用服务名替换ip地址
     * 使用注解配置熔断保护，
     *      fallbackmethod：配置熔断之后的降级方法
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "orderFallBack")
    //@HystrixCommand()
    @RequestMapping(value="/{id}")
    public User selectUserById(@PathVariable String id) throws InterruptedException {
       Thread.sleep(2000);
        User user = restTemplate.getForObject("http://product-service/user/"+id,User.class);
        return user;
    }
```

配置连接超时时间

```yaml
# hystrix 配置连接超时时间
hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 3000 #默认的连接超时时间1秒，若1秒没有返回数据，自动触发降级逻辑

```

#### 对Feign的支持

引入依赖（feign中以经集成了Hystrix）

在feign中配置开启Hystrix

```yaml
#开启对Hystrix的支持
feign:
  hystrix:
    enabled: true
```



自定义一个接口的实现类，这个实现类就是熔断触发的降级逻辑

```java
@Component
public class ProuductFeignClientCallBack implements ProuductFeignClient {
    /**
     * 熔断降级的方法
     * @param id
     * @return
     */
    public User findById(String id) {
        User user = new User();
        user.setName("feign调用，出发熔断降级");
        return user;
    }
```



修改feignClient接口，添加降级方法的支持

```java
/**
 * 声明需要调用的微服务名称
 * @FeignClient
 *  #name:服务提供者名称
 *  #fallback:配置熔断发生降级的方法，实现类
 */
@FeignClient(name="product-service", fallback = ProuductFeignClientCallBack.class)
public interface ProuductFeignClient {

    /**
     * 配置u要调用的微服务接口
     */
    @RequestMapping(value="/user/{id}",method= RequestMethod.GET)
    User findById(@PathVariable("id") String id);
}
```

#### hystrix的超时时间

```yaml
# hystrix 配置连接超时时间
hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 3000 #默认的连接超时时间1秒，若1秒没有返回数据，自动触发降级逻辑

```

当在规定的时间内，没有获取到微服务的数据，这个时候会自动的触发熔断降级方法

#### hystrix设置监控信息：

```xml
 <!--引入hystrix监控中心-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
 		<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
        </dependency>
```

在启动类上激活hystrix

```java
// 激活Hystrix
@EnableCircuitBreaker
```

暴露所有actuator监控的端点

```yaml
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

在页面上进行访问：

http://localhost:9002/actuator/hystrix.stream

hystrix可以对请求失败的请求，以及被拒绝，或者连接超时的请求进行统一的降级处理

#### 断路器

Closed(关闭)，Open(开启)，Half Open(半开)

Closed(默认关闭)，所有请求都可以正常访问

Open(开启)：所有的请求会进入到降级方法中

Half Open(半开)：维持Open状态一段时间（默认5秒），5秒之后就会进入到半开状态，尝试释放一个请求到远程微服务发起调用，如果释放的请求可以正常访问，就会关闭断路器；如果释放的请求不能访问，继续开启断路器（Open）。



环境准备：

1.在订单系统中加入逻辑

​	判断请求的id：

​		如果请求id=1：正常执行（正常调用远程微服务）

​		如果请求id！=1：抛出异常

2. 默认Hystrix中有触发断路器状态转化的阈值

   ​	触发熔断的最小请求次数：20次（默认）

   ​	触发熔断的请求失败的比率： 50%（默认）

   ​	断路器开启的时长： 5s（默认）

```yaml
# hystrix 配置连接超时时间
hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 3000 #默认的连接超时时间1秒，若1秒没有返回数据，自动触发降级逻辑
      circuitBreaker:
        requestVolumeThreshold: 5 # 触发熔断的最小请求次数，默认20 /10秒
        sleepWindowInMilliseconds: 10000 #熔断多少秒之后尝试请求，Open状态的时间
        errorThresholdParcentage: 50 #触发熔断的失败请求最小占比，默认50%
# 打开端电
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

#### 熔断器的隔离策略

微服务使用Hystrix熔断器实现了服务的自动降级，让微服务具备自我保护的能力，提高了系统的稳定性，也较好的解决了雪崩效应。其使用方式目前支持两种策略：

​	1.线程池隔离策略：使用一个线程池来存储当前的请求，线程池对请求作处理，设置任务返回处理超时时间，堆积的请求堆积入线程池队列。这种方式需要为每个以来的服务申请线程池，有一定的资源消耗，好处是可以应对突发流量（流量洪峰来临时，处理不完可将数据存储到线程池队里慢慢处理）

​	2.信号量隔离策略：使用一个原子计数器（或信号量）来记录当前有多少个线程在运行，请求来先判断计数器的数值，若超过设置的最大线程个数则丢弃该类型的新请求，若不超过则执行计数操作，请求来计数器+1，请求返回计数器-1。这种方式是严格的控制线程且立即返回模式，无法应对突发流量（流量洪峰来临时，处理的线程超过数量，其他的请求会直接返回，不继续请求依赖的服务）

![1574595163963](C:\Users\msi\AppData\Roaming\Typora\typora-user-images\1574595163963.png)

## Sentinel组件

1.管理控制台：

```scheme
java -Dserver.port=8080 -Dcsp.sentinel.dashboard.server=localhost:8080 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard.jar
```

启动成功：localhost:8080/

访问的用户名/密码：sentinel/sentinel

2.将所有的服务交给控制台管理

客户端介入Sentinel管理控制台

在客户端（需要管理微服务上）引入坐标

在客户端配置启动参数