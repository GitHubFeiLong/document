## 三、初识Spring Security

#### 3.1 Spring security概念

​		Spring Security是spring采用AOP思想，基于servlet过滤器实现的安全框架。它提供了完善的认证机授权功能。是—款非常优秀的权限管理框架。

#### 3.2 Spring Security简单入门

​		Spring Security博大精深，设计巧妙，功能繁杂，一言难尽，咱们还是直接上代码吧!

#### 3.2.1创建web工程并导入jar包

> Spring Security主要jar包功能介绍
> spring-security-core.jar
> 核心包，任何Spring Security功能都需要此包。
>
> spring-security-web.jar
> web工程必备，包含过滤器和相关的Web安全基础结构代码。
>
> spring-security-config.jar
> 用于解析xml配置文件，用到Spring Security的xml配置文件的就要用到此包。
>
> spring-security-taglibs.jar
> Spring Security提供的动态标签库，jsp页面可以用。

```xml
<!--Spring Security-->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-config</artifactId>
    <version>5.3.6.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-taglibs</artifactId>
    <version>5.3.6.RELEASE</version>
</dependency>
```

#### 3.2.2配置web.xml

```xml
<!-- Spring Security 过滤器链，注意过滤器名称必须叫 springSecurityFilterChain-->
<filter>
    <filter-name>springSecurityFilterChain</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
</filter>
<filter-mapping>
    <filter-name>springSecurityFilterChain</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

#### 3.2.3 配置 spring-security.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:security="http://www.springframework.org/schema/security"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans.xsd
            http://www.springframework.org/schema/contexl
            http://www.springframework.org/schema/context/spring-context.xsd
            http://www.springframework.org/schema/aop
            http://www.springframework.org/schema/aop/spring-aop.xsd
            http://www.springframework.org/schema/tx
            http://www.springframework.org/schema/tx/spring-tx.xsd
            http://www.springframework.org/schema/mvc
            http://www.springframework.org/schema/mvc/spring-mvc.xsd
            http://www.springframework.org/schema/security
            http://www.springframework.org/schema/security/spring-security.xsd">

    <!--配置SpringSecurity-->
    <!--auto-config="true" 表示自动加载springSecurity的配置文件-->
    <!--use-expressions="true"表示使用spring的el表达式来配置springsecurity-->
    <security:http auto-config="true" use-expressions="true">
        <!--拦截资源-->
        <!--
            pattern="/**" 表示拦截所有资源
            access="hasAnyRole('ROLE_USER')" 表示只有ROLE_USER角色才能访问资源
        -->
        <security:intercept-url pattern="/**" access="hasAnyRole('ROLE_USER')" />
    </security:http>

    <!--设置 Spring Security 认证用户信息的来源-->
    <security:authentication-manager>
        <security:authentication-provider>
            <security:user-service>
                <!--springsecurity默认的认证必须是加密的，加上{noop}才示不加密认证。-->
                <security:user name="user" password="{noop}user" authorities="ROLE_USER" />
                <security:user name="admin" password="{noop}admin" authorities="ROLE_ADMIN" />
            </security:user-service>
        </security:authentication-provider>
    </security:authentication-manager>
</beans>
```

#### 3.2.4 编辑 applicationContext.xml

```xml
<import resource="classpath:spring-security.xml" />
```

## 4 Spring Security 过滤器链

### 4.1 Spring security常用过滤器介绍

​		过滤器是一种典型的AOP思想，关于什么是过滤器，就不赘述了，谁还不知道凡是web工程都能用过滤器?接下来咱们就一起看看Spring Security中这些过滤器都是干啥用的，源码我就不贴出来了，有名字，大家可己在idea中Double Shift去。我也会在后续的学习程中穿插详细解释。

1. org.springframework.security.web.context.SecurityContextPersistenceFilter

   > 首当其冲的一个过滤器，作用之重要，自不必多言。
   > SecurityContextPersistenceFilter主要是使用SecurityContextRepository在session中保存或更新一个SecurityContext，并将SecurityContext给以后的过滤器使用，来为后续filter建立所需的上下文。SecurityContext中存储了当前用户的认证以及权限信息。

2. org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter

> 此过滤器用于集成SecurityContext到Spring异步执行机制中的WebAsyncManager

3. org.springframework.security.web.header.HeaderWriterFilter

   > 向请求的Header中添加相应的信息,可在http标签内部使用 security:headers 来控制

4. org.springframework.security.web.csrf.CsrfFilter

   > csrf又称跨域请求伪造，SpringSecurity会对所有post请求验证是否包含系统生成的csrf的token信息,如果不包含，则报错。起到防止csrf攻击的效果。

5. org.springframework.security.web.authentication.logout.LogoutFilter

   > 匹配URL为/logout的请求，实现用户退出,清除认证信息。

6. org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

> 认证操作全靠这个过滤器，默认匹配URL为/login且必须为POST请求。

7. org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter

8. org.springframework.security.web.authentication.ui.DefaultLogoutPageGeneratingFilter

   > 由此过滤器可以生产一个默认的退出登录页面

9. org.springframework.security.web.authentication.www.BasicAuthenticationFilter

   > 此过滤器会自动解析HTTP请求中头部名字为Authentication，且以Basic开头的头信息。

10. org.springframework.security.web.savedrequest.RequestCacheAwareFilter

    > 通过HttpSessionRequestCache内部维护了一个RequestCache，用于缓存HttpServletRequest

11. org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter

    > 针对ServletRequest进行了—次包装，使得request具有更加丰富的API

12. org.springframework.security.web.authentication.AnonymousAuthenticationFilter

    > 当SecurityContextHolder中认证信息为空,则会创建一个匿名用户存入到SecurityContextHolder中。spring security为了兼容未登录的访问，也走了一套认证流程，只不过是一个匿名的身份。

13. org.springframework.security.web.session.SessionManagementFilter

    > SecurityContextRepository限制同一用户开启多个会话的数量

14. org.springframework.security.web.access.ExceptionTranslationFilter

    > 异常转换过滤器位于整个springSecurityFilterChain的后方，用来转换整个链路中出现的异常

15. org.springframework.security.web.access.intercept.FilterSecurityInterceptor

    > 获取所配置资源访问的授权信息，根据SecurityContextHolder中存储的用户信息来决定其是否有权限。