# 自定义Maven插件

在复杂应用中，有时我们需要自己编写maven插件。例如，我之前有一个想法，公共项目（commons模块）进行install供其它模块使用时，需要检查类路径下的RSA密钥文件是否存在，不存在就随机生成密钥并保存到文件中。

> 虽然感觉上面的逻辑有点搞笑，但是我们可以先想法实现它

## 新建maven项目

新建一个maven module，命名为`goudong-maven-plugin`

### pom.xml

新建一个maven项目，并将其打包方式设置成`<packaging>maven-plugin</packaging>`,并引入必要依赖。

完整pom.xml如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <packaging>maven-plugin</packaging>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>goudong-maven-plugin</artifactId>
    <groupId>com.goudong</groupId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <maven-plugin-api.version>3.5.0</maven-plugin-api.version>
        <maven-plugin-annotations.version>3.5</maven-plugin-annotations.version>
        <log4j.version>2.17.1</log4j.version>
        <junit-jupiter-api.version>5.6.3</junit-jupiter-api.version>
    </properties>


    <dependencies>
        <!--maven自定义插件主要使用的两个jar包-->
        <dependency>
            <groupId>org.apache.maven</groupId>
            <artifactId>maven-plugin-api</artifactId>
            <version>${maven-plugin-api.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.maven.plugin-tools</groupId>
            <artifactId>maven-plugin-annotations</artifactId>
            <scope>provided</scope>
            <version>${maven-plugin-annotations.version}</version>
        </dependency>

        <!--日志-->
        <!-- https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-api -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>${log4j.version}</version>
            <scope>provided</scope>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>${log4j.version}</version>
            <scope>provided</scope>
        </dependency>
        <!--使用log4j2的适配器进行绑定-->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j-impl</artifactId>
            <version>${log4j.version}</version>
            <scope>provided</scope>
        </dependency>
        <!--测试包-->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>${junit-jupiter-api.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins </groupId>
                <artifactId>maven-compiler-plugin </artifactId>
                <version>3.8.0</version>
                <!-- 插件执行命令前缀 -->
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

> 注意事项：
>
> 1. 不能是子模块，意思是不能有parent标签，必须是独立的一个模块
>
> 2. 注意依赖jiar的scope值需要使用provided或test

### 编写maven插件

编写maven插件

```java
@Mojo(name = "hello")
public class HelloMojo extends AbstractMojo {
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("hello world");
    }
}
```

> 插件名称为：**hello**，所用就是在控制台中输出"hello world"一句话

## 引入插件

在需要使用我们自定义的插件的模块，进行引入新建的插件

```xml
<build>
        <plugins>
            <!--引入自定义的插件，创建公钥私钥文件-->
            <plugin>
                <groupId>com.goudong</groupId>
                <artifactId>goudong-maven-plugin</artifactId>
                <version>1.0-SNAPSHOT</version>
                <executions>
                    <execution>
                        <phase>pre-clean</phase>
                        <goals>
                            <goal>hello</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

> phase值就是maven的生命周期中的阶段，例如上面表示的是：执行**clean**生命周期的**pre-clean**阶段后执行自定义插件的**hello**。
>
> 当目标项目执行`mvn clean` 时会输出：
>
> ```txt
> [INFO] --------------------------------[ pom ]---------------------------------
> [INFO] 
> [INFO] --- maven-clean-plugin:3.1.0:clean (default-clean) @ goudong-java ---
> [INFO] 
> [INFO] --------------------< com.goudong:goudong-commons >---------------------
> [INFO] Building goudong-commons 1.0-SNAPSHOT                              [2/8]
> [INFO] --------------------------------[ jar ]---------------------------------
> [INFO] 
> [INFO] --- goudong-maven-plugin:1.0-SNAPSHOT:hello (default) @ goudong-commons ---
> hello world
> [INFO] 
> [INFO] --- maven-clean-plugin:3.1.0:clean (default-clean) @ goudong-commons ---
> [INFO] Deleting D:\workspaces\github-repository\goudong-java\goudong-commons\target
> ```



## Maven生命周

[maven生命周期](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#Lifecycle_Reference)

maven一共有三个生命周期，分别为：clean、default、site。每个生命周期都包含若干个阶段(Phase)

### clean生命周期

| 阶段(Phase) | 描述                             |
| ----------- | -------------------------------- |
| pre-clean   | 在项目清理前需要执行的流程       |
| clean       | 清理项目上一次构建生成的所有文件 |
| post-clean  | 执行完成项目清理所需的流程       |

### Default生命周期

| 阶段(Phase)             | 描述                                                         |
| ----------------------- | ------------------------------------------------------------ |
| validate                | 验证项目是正确的，所有必要的信息是可用的                     |
| initialize              | 初始化构建状态，例如设置属性或创建目录                       |
| generate-sources        | 生成任何源代码以供编译时包含                                 |
| process-sources         | 处理源代码，例如过滤任何值                                   |
| generate-resources      | 生成包含在包中的资源                                         |
| process-resources       | 将资源复制并处理到目标目录中，以便进行打包                   |
| compile                 | 编译项目的源代码                                             |
| process-classes         | 对编译生成的文件进行后期处理，例如对 Java 类进行字节码增强   |
| generate-test-sources   | 生成编译中包含的任何测试源代码                               |
| process-test-sources    | 处理测试源代码，例如过滤任何值                               |
| generate-test-resources | 创建测试资源                                                 |
| process-test-resources  | 将资源复制并处理到测试目标目录中                             |
| test-compile            | 将测试源代码编译到测试目标目录中                             |
| process-test-classes    | 对测试编译生成的文件进行后期处理，例如对 Java 类进行字节码增强 |
| test                    | 使用合适的单元测试框架运行测试。这些测试不需要打包或部署代码 |
| prepare-package         | 执行任何必要的操作，准备包装之前，实际包装。这通常会导致包装的未打包、处理版本 |
| package                 | 获取已编译的代码，并将其打包成可分发的格式，例如 JAR         |
| pre-integration-test    | 在执行集成测试之前执行所需的操作。这可能涉及诸如设置所需的环境之类的事情 |
| integration-test        | 如果需要的话，将包部署到可以运行集成测试的环境中             |
| post-integration-test   | 执行完集成测试后所需的操作，包括清理环境                     |
| verify                  | 运行任何检查，以验证包是有效的，并符合质量标准               |
| install                 | 将包安装到本地存储库中，以便在本地其他项目中作为依赖项使用   |
| deploy                  | 在集成或发布环境中完成，将最终包复制到远程存储库，以便与其他开发人员和项目共享 |

### Site生命周期

| 阶段(Phase) | 描述                                             |
| ----------- | ------------------------------------------------ |
| pre-site    | 在实际项目现场生成之前，执行所需的流程           |
| site        | 生成项目的现场文档                               |
| post-site   | 执行完成站点生成所需的流程，并为站点部署做好准备 |
| site-deploy | 将生成的站点文档部署到指定的 web 服务器          |

