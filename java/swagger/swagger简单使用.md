# Swagger 的简单使用



## 使用前的简单配置swagger 

		1. maven导包

```xml
 <!-- swagger2API文档支持 -->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
</dependency>
```

2. 写一个配置类 Swagger2Config.java

```java
package com.fashang.exam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 配置 swagger
 * @ClassName Swagger2Config
 * @Author msi
 * @Date 2020/9/16 9:08
 * @Version 1.0
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    /**
     * 是否开启swagger，正式环境一般是需要关闭的
     * 在 application.yml 配置文件设置属性值
     */
    @Value("${swagger.enabled}")
    private boolean enableSwagger;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("考试练习模块")
                .apiInfo(apiInfo())
                //是否开启 (true 开启  false隐藏。生产环境建议隐藏)
                .enable(enableSwagger)
                .select()
                //扫描的路径包,设置basePackage会将包下的所有被@Api标记类的所有方法作为api
                .apis(RequestHandlerSelectors.basePackage("com.fashang.exam"))
                //指定路径处理PathSelectors.any()代表所有的路径
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContextList = new ArrayList<>();
        List<SecurityReference> securityReferenceList = new ArrayList<>();
        securityReferenceList.add(new SecurityReference("custom-token", scopes()));
        securityContextList.add(SecurityContext
                .builder()
                .securityReferences(securityReferenceList)
                .forPaths(PathSelectors.any())
                .build()
        );
        return securityContextList;
    }

    private AuthorizationScope[] scopes() {
        return new AuthorizationScope[]{new AuthorizationScope("global", "accessAnything")};
    }

    private List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeyList = new ArrayList<>();
        apiKeyList.add(new ApiKey("custom-token", "token", "header"));
        return apiKeyList;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //设置文档标题(API名称)
                .title("考试相关")
                //文档描述
                .description("考试")
                //服务条款URL
//                .termsOfServiceUrl("http://127.0.0.1:8080/")
                //联系信息
                .contact("cfl")
                //版本号
                .version("1.0.0")
                .build();
    }

}
```

> 注意：这里是因为本人使用了全局的token认证，所以添加了一个全局 token。



## 简单使用

### 常用注解

​		Swagger在使用过程中，本人经常使用的注解有以下几个：

| 注解                                                         | 描述                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| @EnableSwagger2                                              | 放在swagger配置类上，用于配置swagger                         |
| @Api(value = "", tags = "")                                  | 我通常是放在Controller上，表明这个控制器会被Swagger生成接口文档 |
| @ApiOperation(value = "接口简短说明", notes = "接口详细描述") | 放在控制器中的方法上                                         |
| @ApiImplicitParam(name = "", value = "", required = true, dataType = "") | 描述接口需要的参数说明                                       |
| @ApiImplicitParams({@ApiImplicitParam ...})                  | 描述接口需要的参数说明(多个参数时使用)                       |
| @ApiModel                                                    | 放在实体类上                                                 |
| @ApiModelProperty(name = "", value = "", required = true)    | 放在实体类的属性上                                           |
| @ApiIgnore                                                   | 放在方法上，用于swagger忽略该方法                            |

> 注意
>
> 1. 如果接口的参数是一个自定义对象的话，那么方法就不要使用 @ApiImplicitParam 和 @ApiImplicitParams ，只需要在该对象的类上添加注解 @ApiModel 在 其属性上添加@ApiModelProperty,这样就会生成 详细的接口文档。