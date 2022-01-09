# spring boot starter

1. spring boot configuration processor 配置处理器
2. Auto-configuration 自动配置

## 1. spring boot configuration processor 配置处理器

[官网 Configuration Metadata (spring.io)](https://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html)

[spring boot starter 江南一点雨-万字详解](https://mp.weixin.qq.com/s/AfotfXlVT2HE4Q8jmkkK4A)

在编辑spring的配置文件时，我们发现有些属性是有提示的。接下来，我们将自己编写一个自动配置类.

### 遗留问题记录

1. 集合类型无法编写提示



### 前置条件

#### 引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <version>2.1.6.RELEASE</version>
    <optional>true</optional>
</dependency>
```

#### 编写Properties类

这里我只是简单的介绍使用方式和注意事项，所以就只有一个key（file.upload.root-dir）

FileProperties.java:

```java
@Data
@Component
@ConfigurationProperties(prefix = "file", ignoreUnknownFields = true)
public class FileProperties {

    /**
     * 文件上传的配置
     */
    @NestedConfigurationProperty
    private FileUploadProperties upload;
}
```

FileUploadProperties.java

```java
@Data
@Component
@ConfigurationProperties(prefix = "file.upload", ignoreUnknownFields = true)
public class FileUploadProperties {

    /**
     * 上传文件的外层目录
     */
    private String rootDir="files";
}
```

>注意事项：
>
>1. 使用@ConfigurationProperties注解后，需要将其放入Spring容器中，不然预编译时会报错。
>
>2. 使用文档注释，在项目构建时会自动将文档注释解析成属性注释。
>
>3. 必须要有getter、setter方法。
>
>4. rootDir="files"：当配置文件不配置该属性值时，使用默认值。
>5. @NestedConfigurationProperty注解来指示应该将正则(非内部)类视为嵌套类

### 配置文件简单介绍

> 注意：
>
> 如果上面的步骤都已经完成，那么再build项目后，会在target/classes/META-INF 下自动创建`spring-configuration-metadata.json`。这样我们可以不用编写自定义的配置文件。但是为了更好的体验，我们也可以创建一个自定义文件`additional-spring-configuration-metadata.json`,进行手动配置一些我们需要的提示信息。

​		在resources目录下，创建一个META-INF目录，然后新建配置文件**additional-spring-configuration-metadata.json**。先构建一下项目，将target/classes/META-INF/spring-configuration-metadata.json 的文件内容复制到我们自定义配置文件中。

自动生成的配置文件内容，看下面代码示例：

```json
{
  "groups": [
    {
      "name": "file",
      "type": "com.goudong.file.properties.FileProperties",
      "sourceType": "com.goudong.file.properties.FileProperties"
    },
    {
      "name": "file.upload",
      "type": "com.goudong.file.properties.FileUploadProperties",
      "sourceType": "com.goudong.file.properties.FileProperties"
    },
    {
      "name": "file.upload",
      "type": "com.goudong.file.properties.FileUploadProperties",
      "sourceType": "com.goudong.file.properties.FileUploadProperties"
    }
  ],
  "properties": [
    {
      "name": "file.upload.root-dir",
      "type": "java.lang.String",
      "description": "上传文件的外层目录",
      "sourceType": "com.goudong.file.properties.FileUploadProperties",
      "defaultValue": "files"
    }
  ],
  "hints": []
}
```

可以看到，自动生成的文件还是比较齐全。所以我们将其内容完整复制到`additional-spring-configuration-metadata.json`中，创建一些额外属性。



### 属性

#### groups

“组”是较高级别的项，它们本身不指定值，而是为属性提供上下文分组。例如，server.port 和 server.address 属性是服务器组的一部分。

| 属性名       | 类型   | 作用                                                         |
| ------------ | ------ | ------------------------------------------------------------ |
| name         | String | 组的全名。这个属性是强制的。                                 |
| type         | String | 组的数据类型的类名。例如，如果该组基于带@ConfigurationProperties 注释的类，则该属性将包含该类的完全限定名。如果它基于@Bean 方法，那么它将是该方法的返回类型。如果类型未知，则可以省略该属性。 |
| description  | String | 可以显示给用户的组的简短说明。如果没有描述，可以省略。建议说明应该是简短的段落，第一行提供一个简明的摘要。描述中的最后一行应该以句号(。)结束. |
| sourceType   | String | 提供此组的源的类名。例如，如果该组基于用@ConfigurationProperties 注释的@Bean 方法，则该属性将包含包含该方法的@Configuration 类的完全限定名。如果不知道源类型，则可以省略该属性。 |
| sourceMethod | String | 提供该组的方法的全名(包括括号和参数类型)(例如，带注释的@ConfigurationProperties 的名称@Bean 方法)。如果不知道源方法，则可以省略它。 |

#### properties

每一个属性key都是一个properties

| 属性名       | 类型             | 作用                                                         |
| ------------ | ---------------- | ------------------------------------------------------------ |
| name         | String           | 属性的全名。名称以小写的句点分隔形式(例如，server.address)。这个属性是强制的。 |
| type         | String           | 属性数据类型的完整签名(例如 java.lang)。也可以是一个完整的泛型类型(例如 java.util)。可以使用此属性指导用户输入值的类型。为了一致性，原语的类型是通过使用它的包装器对应物来指定的(例如，boolean 变为 java.lang。布尔值)。请注意，这个类可能是一个复杂类型，在绑定值时从 String 转换而来。如果不知道类型，可以省略它。 |
| description  | String           | 提供此属性的源的类名。例如，如果属性来自用@ConfigurationProperties 注释的类，则该属性将包含该类的完全限定名。如果源类型未知，则可以省略它。 |
| sourceType   | String           | 提供此属性的源的类名。例如，如果属性来自用@ConfigurationProperties 注释的类，则该属性将包含该类的完全限定名。如果源类型未知，则可以省略它。 |
| defaultValue | Object           | 默认值，如果未指定属性，则使用该值。如果属性的类型是数组，则它可以是值的数组。如果默认值未知，则可以省略该值。 |
| deprecation  | Deprecation 弃用 | 指定属性是否已弃用。如果该字段未被弃用，或者该信息未知，则可以省略该字段。下一个表提供了有关弃用属性的更多细节。 |

每个 properties 元素的 deprecation 属性中包含的 JSON 对象可以包含以下属性:

| 属性名      | 类型   | 作用                                                         |
| ----------- | ------ | ------------------------------------------------------------ |
| level       | String | 不推荐的级别，可以是警告(默认值 warning)或错误(error)。当属性具有警告弃用级别时，它仍然应该绑定在环境中。但是，如果属性具有错误弃用级别，则不再管理该属性，也不再绑定该属性。 |
| reason      | String | 关于不推荐使用该属性的原因的简短说明。如果没有可用的理由，可以省略。建议说明应该是简短的段落，第一行提供一个简明的摘要。描述中的最后一行应该以句号(.)结束. |
| replacement | String | 替换此已弃用属性的属性的全名。如果没有替换此属性，则可省略该属性。 |

#### hints

最后的hints用于定义配置项的帮助信息，当用户使用该属性时提示用户可用的值有哪些，以及该值的作用描述等信息。例如当开发者配置spring.jpa.hibernate.ddl-auto属性时，一些开发IDE会使用这些信息给与自动完成的帮助,会显示该配置项可用的值：none, validate, update, create, 和 create-drop。

| 属性名    | 类型            | 作用                                                         |
| --------- | --------------- | ------------------------------------------------------------ |
| name      | String          | 此提示所引用的属性的全名。名称采用小写的句点分隔形式(如 spring.mvc.servlet.path)。如果属性引用映射(例如 system.contexts) ，则提示应用于映射的键(system.contexts.keys)或值(system.contexts.values)。这个属性是强制的。 |
| values    | ValueHint[]     | ValueHint 对象定义的有效值列表(在下表中进行描述)。每个条目定义值并可能有一个描述。 |
| providers | ValueProvider[] | ValueProvider 对象定义的提供程序列表(在本文后面进行描述)。每个条目定义提供程序的名称及其参数(如果有的话)。 |

每个 hint 元素的 values 属性中包含的 JSON 对象可以包含下表中描述的属性:

| 属性名      | 类型   | 作用                                                         |
| ----------- | ------ | ------------------------------------------------------------ |
| value       | Object | 提示所引用的元素的有效值。如果属性的类型是数组，那么它也可以是值的数组。这个属性是强制的。 |
| description | String | 关于可以显示给用户的值的简短说明。如果没有描述，可以省略。建议说明应该是简短的段落，第一行提供一个简明的摘要。描述中的最后一行应该以句号(.)结束. |

每个 hint 元素的 provider 属性中包含的 JSON 对象可以包含下表中描述的属性:

| 属性名     | 类型        | 作用                                                         |
| ---------- | ----------- | ------------------------------------------------------------ |
| name       | String      | 提供程序的名称，该提供程序用于为提示所引用的元素提供额外的内容帮助。 |
| parameters | JSON object | 提供程序支持的任何附加参数(有关详细信息，请查看提供程序的文档)。 |

每个provider元素的parameters属性，引用提供程序自动完成项目中可用的类。此提供程序支持以下参数:

| 属性     | **类型** | **默认值** | **描述**                                                     |
| -------- | -------- | ---------- | ------------------------------------------------------------ |
| target   | String   | *none*     | 应该分配给选定值的类的完全限定名。通常用于过滤掉非候选类。请注意，类型本身可以通过公开具有适当上界的类来提供此信息。 |
| concrete | boolean  | *none*     | 指定是否只有具体的类别才被认为是有效的候选人。               |



## 2. Auto-configuration 自动配置

官方文档：[创建您自己的自动配置](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration)，下面的文档，我会简述官方的内容，并添加一些自己的理解。

### 2.1 [理解自动配置的bean](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration.understanding-auto-configured-beans)

在底层，自动配置是通过标准的@Configuration 类来实现的。当应用自动配置时，使用额外的@Conditional 注释来约束。通常，自动配置类使用@ConditionalOnClass @ConditionalOnMissingBean 注释。这可以确保自动配置仅在找到相关类并且没有声明自己的@Configuration 时应用。

You can browse the source code of [`spring-boot-autoconfigure`](https://github.com/spring-projects/spring-boot/tree/v2.6.2/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure) to see the `@Configuration` classes that Spring provides (see the [`META-INF/spring.factories`](https://github.com/spring-projects/spring-boot/tree/v2.6.2/spring-boot-project/spring-boot-autoconfigure/src/main/resources/META-INF/spring.factories) file).

> 您可以浏览 Spring-boot-autoconfigure 的源代码，查看 Spring 提供的@Configuration 类(参见 META-INF/Spring.factories 文件)。

### 2.2 [定位自动配置的候选对象](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration.locating-auto-configuration-candidates)

spring boot 会检查项目所集成的所有jar中是否存在`META-INF/spring.factories`文件，该文件中通过建(key):`org.springframework.boot.autoconfigure.EnableAutoConfiguration`列出了配置类，如下示例：

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.mycorp.libx.autoconfigure.LibXAutoConfiguration,\
com.mycorp.libx.autoconfigure.LibXWebAutoConfiguration
```

如果有需要按照特定的顺序进行加载应用，可以使用`@AutoConfigureAfter` 或`@AutoConfigureBefore` 注释。

如果不清楚具体顺序，也可以使用`@AutoConfigureOrder`。该注释具有与常规`@Order` 注释相同的语义，但为自动配置类提供了专用顺序。

与标准@Configuration 类一样，应用自动配置类的顺序只会影响其 bean 的定义顺序。随后创建这些 bean 的顺序不受影响，并由每个 bean 的依赖关系和任何`@DependsOn` 关系决定。

> @DependsOn,比如Bean A 需要 Bean B 才能产出Bean A，那么在Bean A的方法上加上该注解
>
> ```java
> 
> @Bean
> @DependsOn("beanb")
> @ConditionalOnMissingBean
> public BeanA beana (BeanB beanb) {
>     return new BeanA(beanb);    
> }
> 
> @Bean
> public BeanB beanb(){
>     return new Beanb();
> }
> ```

### 2.3 [条件注释](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration.condition-annotations)

您几乎总是希望在自动配置类中包含一个或多个@Conditional 注释。@ConditionalOnMissingBean 注释是一个常见的示例，用于允许开发人员在不满意您的默认设置时重写自动配置。

Spring Boot 包含许多@Conditional 注释，您可以通过注释@Configuration 类或单独的@Bean 方法在自己的代码中重用它们。这些注释包括:

- [Class Conditions](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration.condition-annotations.class-conditions) 类条件
- [Bean Conditions](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration.condition-annotations.bean-conditions) Bean条件
- [Property Conditions](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration.condition-annotations.property-conditions) 属性条件
- [Resource Conditions](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration.condition-annotations.resource-conditions) 资源条件
- [Web Application Conditions](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration.condition-annotations.web-application-conditions) Web 应用程序条件
- [SpEL Expression Conditions](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration.condition-annotations.spel-conditions) SpEL表达条件



#### 2.3.1 [ Class Conditions ](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration.condition-annotations)

@ConditionalOnClass和@ConditionalOnMissingClass注释允许基于特定类的存在或不存在来包含@Configuration类。 由于注释元数据是使用ASM解析的，所以可以使用value属性引用实际的类，即使该类实际上可能不会出现在运行的应用程序类路径上。 如果希望使用String值指定类名，还可以使用name属性。  

这种机制并不以同样的方式应用于@Bean方法，因为通常返回类型是条件的目标:在方法的条件应用之前，JVM将加载类，并可能处理方法引用，如果类不存在，则会失败。  

要处理这种情况，可以使用一个单独的@Configuration类来隔离条件，如下面的例子所示:  

```java
@Configuration(proxyBeanMethods = false)
// Some conditions ...
public class MyAutoConfiguration {

    // Auto-configured beans ...

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(SomeService.class)
    public static class SomeServiceConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public SomeService someService() {
            return new SomeService();
        }

    }

}
```

> 如果您使用@ConditionalOnClass或@ConditionalOnMissingClass作为元注释的一部分来组成您自己的复合注释，在这种情况下，您必须使用name作为引用类的名称，因为该类没有被处理。  

#### 2.3.2 [Bean Conditions](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration.condition-annotations.bean-conditions)

@ConditionalOnBean和@ConditionalOnMissingBean注释允许根据特定bean的存在或不存在来包含bean。 您可以使用value属性按类型指定bean，也可以使用名称指定bean。 search属性允许您限制在搜索bean时应该考虑的ApplicationContext层次结构。  

当放在@Bean方法上时，目标类型默认为该方法的返回类型，如下例所示:  

```java
@Configuration(proxyBeanMethods = false)
public class MyAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SomeService someService() {
        return new SomeService();
    }

}
```

在前面的示例中，如果 ApplicationContext 中没有类型为 someService 的 bean，那么将创建 someService bean。

> 您需要非常注意添加bean定义的顺序，因为这些条件是根据到目前为止已经处理的内容进行评估的。 因此，我们建议在自动配置类上只使用@ConditionalOnBean和@ConditionalOnMissingBean注解(因为这些注解保证在添加任何用户定义的bean定义之后加载)。  
>
> @ConditionalOnBean和@ConditionalOnMissingBean不会阻止@Configuration类被创建。 在类级别使用这些条件与用注释标记每个包含的@Bean方法之间的唯一区别是，如果条件不匹配，前者会阻止将@Configuration类注册为bean。 
>
>  在声明@Bean方法时，在方法的返回类型中提供尽可能多的类型信息。 例如，如果bean的具体类实现了一个接口，那么bean方法的返回类型应该是具体类而不是接口。 当使用bean条件时，在@Bean方法中提供尽可能多的类型信息尤为重要，因为它们的评估只能依赖于方法签名中可用的类型信息。  

#### 2.3.3 [Property Conditions]([Core Features (spring.io)](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration.condition-annotations.property-conditions))

@ConditionalOnProperty注释允许基于Spring Environment属性包含配置。 使用前缀和name属性指定应该检查的属性。 默认情况下，任何存在且不等于false的属性都会被匹配。 您还可以使用havingValue和matchIfMissing属性创建更高级的检查。  

#### 2.3.4 [Resource Conditions]([Core Features (spring.io)](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration.condition-annotations.resource-conditions))

@ConditionalOnResource注释允许只在存在特定资源时才包含配置。 资源可以通过使用常用的Spring约定来指定，如下例所示:`file:/home/user/test.dat`  

#### 2.3.5 [Web Application Conditions]([Core Features (spring.io)](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration.condition-annotations.web-application-conditions))

@ConditionalOnWebApplication和@ConditionalOnNotWebApplication注释允许根据应用程序是否是“web应用程序”来包含配置。 基于servlet的web应用程序是任何使用Spring WebApplicationContext、定义会话范围或具有ConfigurableWebEnvironment的应用程序。 一个响应式web应用是任何使用了ReactiveWebApplicationContext的应用，或者有一个ConfigurableReactiveWebEnvironment的应用。  

@ConditionalOnWarDeployment注释允许根据应用程序是否是部署到容器中的传统WAR应用程序来包含配置。 此条件不适用于在嵌入式服务器上运行的应用程序。  

#### 2.3.6 [SpEL Expression Conditions]([Core Features (spring.io)](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration.condition-annotations.spel-conditions))

@ConditionalOnExpression注释允许基于一个[SpEL表达式]( https://docs.spring.io/spring-framework/docs/5.3.14/reference/html/core.html#expressions)的结果包含配置。  

### 2.4 [测试自动配置](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration.testing)

自动配置可能受到许多因素的影响:用户配置(@Bean定义和环境定制)、条件评估(特定库的存在)，以及其他因素。 具体地说，每个测试都应该创建一个定义良好的ApplicationContext，它表示这些定制的组合。 ApplicationContextRunner提供了一个很好的实现方法。  

 

ApplicationContextRunner通常定义为测试类的一个字段，用于收集基本的公共配置。 下面的例子确保了MyServiceAutoConfiguration总是被调用:  

```java
private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
.withConfiguration(AutoConfigurations.of(MyServiceAutoConfiguration.class));
```

> 如果必须定义多个自动配置，则不需要对它们的声明进行排序，因为它们的调用顺序与运行应用程序时完全相同。  

每个测试都可以使用运行器来表示一个特定的用例。 例如，下面的示例调用一个用户配置(UserConfiguration)，并检查自动配置是否正确地退出。 调用run提供了一个可以与AssertJ一起使用的回调上下文。  

```java
@Test
void defaultServiceBacksOff() {
    this.contextRunner.withUserConfiguration(UserConfiguration.class).run((context) -> {
        assertThat(context).hasSingleBean(MyService.class);
        assertThat(context).getBean("myCustomService").isSameAs(context.getBean(MyService.class));
    });
}

@Configuration(proxyBeanMethods = false)
static class UserConfiguration {

    @Bean
    MyService myCustomService() {
        return new MyService("mine");
    }

}
```

也可以轻松定制环境，如下例所示:  

```java
@Test
void serviceNameCanBeConfigured() {
    this.contextRunner.withPropertyValues("user.name=test123").run((context) -> {
        assertThat(context).hasSingleBean(MyService.class);
        assertThat(context.getBean(MyService.class).getName()).isEqualTo("test123");
    });
}
```

运行程序还可以用于显示ConditionEvaluationReport。 报告可以在INFO或DEBUG级别打印。 下面的示例显示如何使用ConditionEvaluationReportLoggingListener在自动配置测试中打印报表。  

```java
class MyConditionEvaluationReportingTests {

    @Test
    void autoConfigTest() {
        new ApplicationContextRunner()
            .withInitializer(new ConditionEvaluationReportLoggingListener(LogLevel.INFO))
            .run((context) -> {
                    // Test something...
            });
    }

}
```

#### 2.4.1 模拟Web环境

如果你需要测试一个只在servlet或响应式web应用上下文中运行的自动配置，分别使用WebApplicationContextRunner或ReactiveWebApplicationContextRunner。  

#### 2.4.2 压倒一切的类路径

还可以测试在运行时不存在特定的类和/或包时会发生什么。 Spring Boot附带了一个FilteredClassLoader，它可以很容易地被运行器使用。 在下面的例子中，我们断言如果MyService不存在，自动配置被正确禁用:  

```java
@Test
void serviceIsIgnoredIfLibraryIsNotPresent() {
    this.contextRunner.withClassLoader(new FilteredClassLoader(MyService.class))
            .run((context) -> assertThat(context).doesNotHaveBean("myService"));
}
```

### 2.5 创建自定义Starter

一个典型的Spring Boot启动器包含一些代码，用来自动配置和定制特定技术的基础设施，让我们称之为“acme”。 为了使其易于扩展，可以将专用名称空间中的许多配置键公开给环境。 最后，提供了一个单一的“starter”依赖项，以帮助用户尽可能容易地开始。  

具体来说，一个自定义启动器可以包含以下内容:  

+ 包含“acme”的自动配置代码的自动配置模块。  
+ starter模块，它提供了对autoconfigure模块的依赖，以及“acme”和任何其他通常有用的依赖。 简而言之，添加启动器应该提供开始使用该库所需的一切。  

这种两个模块的分离是完全没有必要的。 如果“acme”有几种风格、选项或可选特性，那么最好将自动配置分开，因为您可以清楚地表示有些特性是可选的。 此外，您还可以编写一个启动程序，提供关于那些可选依赖项的意见。 与此同时，其他人只能依靠自动配置模块，以不同的意见制作自己的起动器。  

如果自动配置相对简单，并且没有可选特性，那么合并启动器中的两个模块肯定是一个选择。  

#### 2.5.1 命名

您应该确保为初学者提供适当的名称空间。 不要使用spring-boot启动你的模块名，即使你使用不同的Maven groupId。 我们可能会在将来为您自动配置的东西提供官方支持。  

根据经验，应该以启动器的名字命名一个组合模块。 例如，假设您正在为“acme”创建一个启动器，并将自动配置模块命名为acme-spring-boot，将启动器命名为acme-spring-boot-starter。 如果你只有一个结合了这两个模块的模块，命名为acme-spring-boot-starter。  

#### 2.5.2 配置Keys

如果您的启动器提供配置键，请为它们使用唯一的名称空间。 特别是，不要在Spring Boot使用的名称空间(如服务器、管理、Spring等)中包含键。 如果您使用相同的名称空间，将来我们可能会以破坏模块的方式修改这些名称空间。 根据经验，使用您拥有的名称空间(例如acme)作为所有键的前缀。  

确保通过为每个属性添加字段javadoc来记录配置键，如下例所示  

```java
@ConfigurationProperties("acme")
public class AcmeProperties {

    /**
     * Whether to check the location of acme resources.
     */
    private boolean checkLocation = true;

    /**
     * Timeout for establishing a connection to the acme server.
     */
    private Duration loginTimeout = Duration.ofSeconds(3);

    // getters/setters ...

}
```

> 您应该只对@ConfigurationProperties字段Javadoc使用纯文本，因为在将它们添加到JSON之前不进行处理。  

以下是我们内部遵循的一些规则，以确保描述的一致性:  

+ 描述不要以“the”或“A”开头。
+ 对于布尔类型，描述以“Whether”或“Enable”开始。
+ 对于基于集合的类型，以“逗号分隔列表”开始描述  
+ 使用java.time.Duration而不是long，并描述默认单位，如果它不同于毫秒，例如“如果没有指定持续时间后缀，将使用秒”。
+ 除非必须在运行时确定，否则不要在描述中提供默认值。  

确保触发元数据生成，这样IDE也可以为您的键提供帮助。 您可能需要检查生成的元数据(META-INF/spring-configuration-metadata.json)，以确保您的键被正确地记录。 在兼容的IDE中使用自己的启动器也是验证元数据质量的一个好主意。  

#### 2.5.3  “自动配置”模块

autoconfigure模块包含了开始使用库所需的所有内容。 它还可能包含配置键定义(例如@ConfigurationProperties)和任何回调接口，这些接口可用于进一步定制组件的初始化方式。  

> 您应该将库的依赖项标记为可选，以便您可以更容易地在项目中包含autoconfigure模块。 如果您这样做，则不会提供库，并且默认情况下，Spring Boot会退出。  

Spring Boot使用注释处理器来收集元数据文件(META-INF/ Spring - autoconfiguration -metadata.properties)中的自动配置条件。 如果该文件存在，它将被用于主动过滤不匹配的自动配置，这将提高启动时间。 建议在包含自动配置的模块中添加以下依赖项:  

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-autoconfigure-processor</artifactId>
    <optional>true</optional>
</dependency>
```

如果你在应用程序中直接定义了自动配置，请确保配置spring-boot-maven-plugin，以防止重新打包的目标将依赖添加到fat jar中:  

```xml
<project>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-autoconfigure-processor</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

#### 2.5.4 Starter Module

酵头其实是个空罐子。 它的唯一目的是为使用库提供必要的依赖项。 你可以把它看作是一种固执己见的观点，认为开始需要什么。  

不要对添加了starter的项目做任何假设。 如果您正在自动配置的库通常需要其他启动者，也要提到他们。 如果可选依赖项的数量很大，那么提供一组合适的默认依赖项可能会很困难，因为您应该避免包含对于库的典型使用来说不必要的依赖项。 换句话说，您不应该包含可选的依赖项。  

无论哪种方式，您的启动器都必须直接或间接地引用核心Spring Boot启动器(Spring - Boot -starter)(如果您的启动器依赖于另一个启动器，则不需要添加它)。 如果一个项目只使用你的自定义启动器创建，那么Spring Boot的核心特性将会因为核心启动器的出现而受到尊重。  
