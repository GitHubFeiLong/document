### Spring Boot 中使用MongoDB

​		[spring 项目列表](https://spring.io/projects)

​		[spring data mongodb](https://spring.io/projects/spring-data-mongodb)

​		[mongoTemplate 文档](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mongo.core)

​		https://docs.spring.io/spring-data/mongodb/docs/current/api/

## 环境介绍

​		以下介绍，都是在SpringBoot项目进行测试的，所以需要一些关键包：

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.2.6.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
	</dependency>
    
    <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    
    <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
</dependencies>

<build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
        <resources>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.xml</include>
                </includes>
            </resource>
        </resources>
    </build>


```



## 配置MongoDB

1. 使用maven构建一个springboot 项目，引入mongodb 依赖

   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-data-mongodb</artifactId>
   </dependency>
   ```

2. 配置mongodb的数据库连接

   ```yaml
   spring:
     # 配置mongodb
     data:
       mongodb:
         # 使用字符串的方式直接连接mongod服务
         uri: mongodb://root:123456@localhost:37017
         database: demo
   ```

## CRUD

​		以下都是使用Mongo

### 准备工作

> 官网的一个例子，数据库结构如下：
>
> ```json
> { item: "journal", qty: 25, tags: ["blank", "red"], size: { h: 14, w: 21, uom: "cm" } }
> ```
>
> 根据结构创建一个java类



  1. 创建一个与数据库集合 inventory 对应的实体类 inventory.class:

     ```java
     package com.cfl.jd.mongodb;
     
     import lombok.Builder;
     import lombok.Data;
     import org.springframework.data.annotation.Id;
     import org.springframework.data.mongodb.core.mapping.Document;
     import org.springframework.data.mongodb.core.mapping.Field;
     
     /**
      * 类描述：
      *  采用官网的例子
      * @ClassName Inventory
      * @Author msi
      * @Date 2020/11/22 11:29
      * @Version 1.0
      */
     @Data
     @Builder
     @Document(value = "inventory")
     public class Inventory {
         /**
          * 主键，数据库中是"_id"
          */
         @Id
         private String id;
         @Field("item")
         private String item;
         /**
          * 如果对象属性名称和集合的字段名称一致可以不写@Field.其中注解的value是数据库字段名称
          */
         // @Field("qty")
         private int qty;
         private String[] tags;
         private Size size;
     
         @Data
         @Builder
          public static class Size {
             private double h;
             private double w;
             private String uom;
         }
     }
     
     ```

     > 注意：
     >
     > 这里使用了 @Document(value = "inventory") 表明这个实体类和数据库demo中的inventory集合与之对应。
     >
     > @Data、@Builder都是Lombok注解，使用时需要安装lombok插件

### Insert

​		插入文档数据库中，有单条插入和多条插入，如下所示：

| method                                                       | description                                                  |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| <T> T insert(T objectToSave)                                 | 插入T类型的对象到T映射的数据库集合，并返回带有主键id的对象   |
| <T> T insert(T objectToSave, String collectionName)          | 插入T类型的对象到数据库中的collectionName集合，并返回带有主键id的对象 |
| <T> Collection<T> insertAll(Collection<? extends T> objectsToSave) | 批量新增到集合（集合中元素映射的集合），返回元素带有id属性的集合 |
| <T> Collection<T> insert(Collection<? extends T> batchToSave, Class<?> entityClass) | 批量新增，指定集合映射的Class对象，返回元素带有id属性的集合  |
| <T> Collection<T> insert(Collection<? extends T> batchToSave, String collectionName) | 批量新增，指定一个集合名称，返回元素带有id属性的集合         |

​		1. 插入单条数据到数据库中,例如：

```java
	@Autowired
    private MongoTemplate mongoTemplate;
    /**
     * 插入一条
     */
    @Test
    public void inserOne () {
        Inventory uom = Inventory.builder().item("insert-item-value1").qty(10).tags(new String[]{"insert-tags1", "insert-tags2"}).size(Inventory.Size.builder().h(12).w(12.8).uom("uom1").build()).build();
        // 直接插入（Inventory类映射的集合）
        Inventory insert = this.mongoTemplate.insert(uom);
        // 下面和上面结果是一样的
//        Inventory insert = this.mongoTemplate.insert(uom, "inventory");
        
        // insert = Inventory(id=5fba15b7209de75b8d3d4c06, item=insert-item-value1, qty=10, tags=[insert-tags1, insert-tags2], size=Inventory.Size(h=12.0, w=12.8, uom=uom1))
        System.out.println("insert = " + insert.toString());
    }
```



​		2. 插入集合到数据库的指定集合，例如：

```java
	@Autowired
    private MongoTemplate mongoTemplate;	
	
	@Test
    public void insertCollection () {
        List<Inventory> list = new ArrayList<>();
        list.add(Inventory.builder().item("insert-item-value2").qty(10).tags(new String[]{"insert-tags1", "insert-tags2"})
                .size(Inventory.Size.builder().h(12).w(12.8).uom("uom1").build()).build());

        list.add(Inventory.builder().item("insert-item-value3").qty(11).tags(new String[]{"insert-tags3", "insert-tags4"})
                .size(Inventory.Size.builder().h(15).w(13.8).uom("uom2").build()).build());

        Collection<Inventory> insert = this.mongoTemplate.insertAll(list);
//        Collection<Inventory> insert = this.mongoTemplate.insert(list, Inventory.class);

//        Collection<Inventory> insert = this.mongoTemplate.insert(list, "inventory");
        // insert = [Inventory(id=5fba161f9169a915d0b1ffe3, item=insert-item-value2, qty=10, tags=[insert-tags1, insert-tags2], size=Inventory.Size(h=12.0, w=12.8, uom=uom1)), Inventory(id=5fba161f9169a915d0b1ffe4, item=insert-item-value3, qty=11, tags=[insert-tags3, insert-tags4], size=Inventory.Size(h=15.0, w=13.8, uom=uom2))]
        System.out.println("insert = " + insert);
    }
```



### Query

​		查询全部，查询满足指定条件的集合。

| method                                                       | description                                          |
| ------------------------------------------------------------ | ---------------------------------------------------- |
| find(Query query, Class<T> entityClass)                      | 将实体类的集合上的特别查询结果映射到指定类型的列表。 |
| find(Query query, Class<T> entityClass, String collectionName) | 将指定集合上的特别查询结果映射到指定类型的列表。     |
| findAll(Class<T> entityClass)                                | 从实体类使用的集合中查询类型为T的对象列表。          |
| findAll(Class<T> entityClass, String collectionName)         | 从指定集合中查询类型为T的对象列表。                  |

​		以下代码演示上面的4个api，示例如下：

```java
	@Test
    public void queryTest () {
        // 查询全部
        this.mongoTemplate.findAll(Inventory.class);
        this.mongoTemplate.findAll(Inventory.class, "inventory");

        // 查询指定条件的列表
        // Criteria.where("key").[is|and|in|regex|...]
        Query query = Query.query(Criteria.where("item").is("insert-item-value1"));
        List<Inventory> inventories = this.mongoTemplate.find(query, Inventory.class);
        // 
        this.mongoTemplate.find(query, Inventory.class, "inventory");
    }
```

> 还有一些其他的api，例如findById 等等

### Update

​		通过Query 和 Update 进行修改集合

| method                                                       | description                                                  |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| updateFirst(Query query, UpdateDefinition update, Class<?> entityClass) | 更新实体类集合中找到的第一个对象，该对象与提供的更新文档匹配查询文档。 |
| updateFirst(Query query, UpdateDefinition update, Class<?> entityClass, String collectionName) | 将在指定集合中找到的与查询文档标准匹配的第一个对象与所提供的更新文档更新。 |
| updateFirst(Query query, UpdateDefinition update, String collectionName) | 将在指定集合中找到的与查询文档标准匹配的第一个对象与所提供的更新文档更新。 |
| updateMulti(Query query, UpdateDefinition update, Class<?> entityClass) | 将在集合中找到的与查询文档标准匹配的实体类的所有对象更新为所提供的更新文档。 |
| updateMulti(Query query, UpdateDefinition update, Class<?> entityClass, String collectionName) | 将在集合中找到的与查询文档标准匹配的实体类的所有对象更新为所提供的更新文档。 |
| updateMulti(Query query, UpdateDefinition update, String collectionName) | 用所提供的更新文档更新在指定集合中找到的与查询文档标准匹配的所有对象。 |

​		简单的示例：

```java
/**
     * 修改单条记录
     */
    @Test
    public void updateOne () {
        //模糊匹配
        Pattern pattern = Pattern.compile("^.*item.*$", Pattern.CASE_INSENSITIVE);
        Query query = Query.query(Criteria.where("item").regex(pattern));
        Update update = Update.update("item", "update-item-value").set("qty", 100);
        // 将item字段包含“item”字符串的都改成“update-item-value”，将 qty 字段改成 “100”
        UpdateResult updateResult = this.mongoTemplate.updateFirst(query, update, Inventory.class);
        // 以下的功能类似
//        this.mongoTemplate.updateFirst(query, update, "inventory");
//        this.mongoTemplate.updateFirst(query, update, Inventory.class, "inventory");
        System.out.println("updateResult = " + updateResult);
    }
```

### Delete

| method                                                       | description                                                  |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| remove(Class<T> domainType)                                  | 开始为给定的domainType创建移除操作。                         |
| `remove(Object object)`                                      | 通过id，删除单个集合中的对象                                 |
| `remove(Object object, String collectionName)`               | 通过id，删除单个集合中的对象                                 |
| `remove(Query query, Class<?> entityClass)`                  | 从用于存储entityClass的集合中删除与提供的查询文档标准匹配的所有文档。 |
| `remove(Query query, Class<?> entityClass, String collectionName)` | 从用于存储entityClass的集合中删除与提供的查询文档标准匹配的所有文档。 |
| remove(Query query, String collectionName)                   | 从指定集合中删除与提供的查询文档标准匹配的所有文档。         |

​		示例：

```java
@Test
    public void remove () {
        // 删除集合所有数据
        this.mongoTemplate.remove(Inventory.class);
        
        // 删除指定对象
        Inventory byId = this.mongoTemplate.findById("123456", Inventory.class);
        this.mongoTemplate.remove(byId);
        this.mongoTemplate.remove(byId, "inventory");
        
        // 删除集合中满足条件的所有对象
        Query id = Query.query(Criteria.where("_id").is("123455"));
        this.mongoTemplate.remove(id, Inventory.class);
        this.mongoTemplate.remove(id, "inventory");
        this.mongoTemplate.remove(id, Inventory.class, "inventory");
        
    }
```



> save 方法
>
> 如果插入的对象数据库不存在，就插入，如果存在就修改。
>
> ```java
>  @Test
>     public void save () {
>         Inventory byId = this.mongoTemplate.findById("5fba1db8919bd221a9fb5582", Inventory.class);
>         // 如果id值进行修改后
>         byId.setId("123456");
>         // 此时将新增一条数据
>         Inventory save = this.mongoTemplate.save(byId);
> 
>         // 如果id值不变，那么就修改数据
>         byId.setItem("我修改了");
>         this.mongoTemplate.save(save);
>     }
> ```



## 其他常用类的简单介绍

### Update Class

​		你可以使用 “语法糖” 来使用 `Update` 类, 因为它的方法是连接在一起的. 此外，您可以通过使用 `public static Update Update (String key, Object value)` 创建一个新的 `Update` 实例。

​		Update 类下有以下方法：

+ `Update` **addToSet** `(String key, Object value)`  使用 `$addToSet `的更新修饰符进行更新
+ `Update` **currentDate** `(String key)` 使用 `$currentDate ` 更新修饰符进行更新
+ `Update` **currentTimestamp** `(String key)` 使用带有 `$type` `timestamp` 的 `$currentDate` 更新修饰符进行更新
+ `Update` **inc** `(String key, Number inc)` 使用 `$inc` 更新修饰符进行更新
+ `Update` **max** `(String key, Object max)` 使用 `$max`更新修饰符进行更新
+ `Update` **min** `(String key, Object min)` 使用 `$min` 更新修饰符进行更新
+ `Update` **multiply** `(String key, Number multiplier)` Update using the `$mul` update modifier
+ `Update` **pop** `(String key, Update.Position pos)` Update using the `$pop` update modifier
+ `Update` **pull** `(String key, Object value)` Update using the `$pull` update modifier
+ `Update` **pullAll** `(String key, Object[] values)` Update using the `$pullAll` update modifier
+ `Update` **push** `(String key, Object value)` Update using the `$push` update modifier
+ `Update` **pushAll** `(String key, Object[] values)` Update using the `$pushAll` update modifier
+ `Update` **rename** `(String oldName, String newName)` Update using the `$rename` update modifier
+ `Update` **set** `(String key, Object value)` Update using the `$set` update modifier
+ `Update` **setOnInsert** `(String key, Object value)` Update using the `$setOnInsert` update modifier
+ `Update` **unset** `(String key)` Update using the `$unset` update modifier

> 一些更新修饰符，如$push和$addToSet，允许嵌套额外的操作符。



### Criteria Class

​		Criteria类提供了以下方法，所有这些方法都对应于MongoDB中的操作符:

+ `Criteria` **all** `(Object o)` Creates a criterion using the `$all` operator
+ `Criteria` **and** `(String key)` Adds a chained `Criteria` with the specified `key` to the current `Criteria` and returns the newly created one
+ `Criteria` **andOperator** `(Criteria… criteria)` Creates an and query using the `$and` operator for all of the provided criteria (requires MongoDB 2.0 or later)
+ `Criteria` **elemMatch** `(Criteria c)` Creates a criterion using the `$elemMatch` operator
+ `Criteria` **exists** `(boolean b)` Creates a criterion using the `$exists` operator
+ `Criteria` **gt** `(Object o)` Creates a criterion using the `$gt` operator
+ `Criteria` **gte** `(Object o)` Creates a criterion using the `$gte` operator
+ `Criteria` **in** `(Object… o)` Creates a criterion using the `$in` operator for a varargs argument.
+ `Criteria` **in** `(Collection<?> collection)` Creates a criterion using the `$in` operator using a collection
+ `Criteria` **is** `(Object o)` Creates a criterion using field matching (`{ key:value }`). If the specified value is a document, the order of the fields and exact equality in the document matters.
+ `Criteria` **lt** `(Object o)` Creates a criterion using the `$lt` operator
+ `Criteria` **lte** `(Object o)` Creates a criterion using the `$lte` operator
+ `Criteria` **mod** `(Number value, Number remainder)` Creates a criterion using the `$mod` operator
+ `Criteria` **ne** `(Object o)` Creates a criterion using the `$ne` operator
+ `Criteria` **nin** `(Object… o)` Creates a criterion using the `$nin` operator
+ `Criteria` **norOperator** `(Criteria… criteria)` Creates an nor query using the `$nor` operator for all of the provided criteria
+ `Criteria` **not** `()` Creates a criterion using the `$not` meta operator which affects the clause directly following
+ `Criteria` **orOperator** `(Criteria… criteria)` Creates an or query using the `$or` operator for all of the provided criteria
+ `Criteria` **regex** `(String re)` Creates a criterion using a `$regex`
+ `Criteria` **size** `(int s)` Creates a criterion using the `$size` operator
+ `Criteria` **type** `(int t)` Creates a criterion using the `$type` operator
+ `Criteria` **matchingDocumentStructure** `(MongoJsonSchema schema)` Creates a criterion using the `$jsonSchema` operator for [JSON schema criteria](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mongo.jsonSchema). `$jsonSchema` can only be applied on the top level of a query and not property specific. Use the `properties` attribute of the schema to match against nested fields.
+ `Criteria` **bits()** is the gateway to [MongoDB bitwise query operators](https://docs.mongodb.com/manual/reference/operator/query-bitwise/) like `$bitsAllClear`.

The Criteria class also provides the following methods for geospatial queries (see the [GeoSpatial Queries](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mongo.geospatial) section to see them in action):

- `Criteria` **within** `(Circle circle)` Creates a geospatial criterion using `$geoWithin $center` operators.
- `Criteria` **within** `(Box box)` Creates a geospatial criterion using a `$geoWithin $box` operation.
- `Criteria` **withinSphere** `(Circle circle)` Creates a geospatial criterion using `$geoWithin $center` operators.
- `Criteria` **near** `(Point point)` Creates a geospatial criterion using a `$near` operation
- `Criteria` **nearSphere** `(Point point)` Creates a geospatial criterion using `$nearSphere$center` operations. This is only available for MongoDB 1.7 and higher.
- `Criteria` **minDistance** `(double minDistance)` Creates a geospatial criterion using the `$minDistance` operation, for use with $near.
- `Criteria` **maxDistance** `(double maxDistance)` Creates a geospatial criterion using the `$maxDistance` operation, for use with $near.



###  Query class

​		Query类有一些为查询提供选项的附加方法:

+ `Query` **addCriteria** `(Criteria criteria)` used to add additional criteria to the query
+ `Field` **fields** `()` used to define fields to be included in the query results
+ `Query` **limit** `(int limit)` used to limit the size of the returned results to the provided limit (used for paging)
+ `Query` **skip** `(int skip)` used to skip the provided number of documents in the results (used for paging)
+ `Query` **with** `(Sort sort)` used to provide sort definition for the results