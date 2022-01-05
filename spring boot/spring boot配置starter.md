# spring boot 配置文件提示

[官网 Configuration Metadata (spring.io)](https://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html)

[spring boot starter 江南一点雨-万字详解](https://mp.weixin.qq.com/s/AfotfXlVT2HE4Q8jmkkK4A)

在编辑spring的配置文件时，我们发现有些属性是有提示的。接下来，我们将自己编写一个自动配置类.

## 遗留问题记录

1. 集合类型无法编写提示



## 前置条件

### 引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <version>2.1.6.RELEASE</version>
    <optional>true</optional>
</dependency>
```

### 编写Properties类

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
>5. @NestedConfigurationProperty注解：代表该属性不是简单的类型，加上后丰富提示信息

### 配置文件简单介绍

> 注意：
>
> 如果上面的步骤都已经完成，那么再build项目后，会在target/classes/META-INF 下自动创建`spring-configuration-metadata.json`。这样我们可以不用编写自定义的配置文件。但是为了更好的体验，我们也可以创建一个自定义文件`additional-spring-configuration-metadata.json`。

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



## 属性

### groups

“组”是较高级别的项，它们本身不指定值，而是为属性提供上下文分组。例如，server.port 和 server.address 属性是服务器组的一部分。

| 属性名       | 类型   | 作用                                                         |
| ------------ | ------ | ------------------------------------------------------------ |
| name         | String | 组的全名。这个属性是强制的。                                 |
| type         | String | 组的数据类型的类名。例如，如果该组基于带@ConfigurationProperties 注释的类，则该属性将包含该类的完全限定名。如果它基于@Bean 方法，那么它将是该方法的返回类型。如果类型未知，则可以省略该属性。 |
| description  | String | 可以显示给用户的组的简短说明。如果没有描述，可以省略。建议说明应该是简短的段落，第一行提供一个简明的摘要。描述中的最后一行应该以句号(。)结束. |
| sourceType   | String | 提供此组的源的类名。例如，如果该组基于用@ConfigurationProperties 注释的@Bean 方法，则该属性将包含包含该方法的@Configuration 类的完全限定名。如果不知道源类型，则可以省略该属性。 |
| sourceMethod | String | 提供该组的方法的全名(包括括号和参数类型)(例如，带注释的@ConfigurationProperties 的名称@Bean 方法)。如果不知道源方法，则可以省略它。 |

### properties

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

### hints

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

