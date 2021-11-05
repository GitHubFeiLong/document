# knife4j

[knifefj官网](https://doc.xiaominfo.com/knife4j/documentation/)，该项目是将swagger的前端界面进行美化，在使用上更加方便。

## 引入依赖

我这是使用了spring boot进行搭建的web项目。

```xml
<!--增强swagger-->
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-spring-boot-starter</artifactId>
    <version>${knife4j-spring-boot-starter.version}</version>
</dependency>
```



## 修改配置文件

```yml
knife4j:
  enable: true         # 是否开启Knife4j增强模式，默认值为false
  production: false    # 生产环境开启
  basic:
    enable: true       # BasicHttp功能，默认为false
    username: knife4j  # 用户名
    password: knife4j  # 密码
  setting:             # 前端Ui的个性化配置属性
    enableDebug: true  # 启用调试，默认为true 无效
```



## 访问地址

http://ip:port/doc.html

