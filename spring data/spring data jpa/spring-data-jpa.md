# 简单记录Spring data jpa的用法

## orm思想和hibernate以及jpa的概述和jpa的基本操作

### orm思想

1. 主要目的：操作实体类就相当于操作数据库表
2. 建立两个映射关系：
   1. 实体类和表的映射关系
   2. 实体类中属性和表中字段的映射关系
3. 不再重点关注：sql语句
4. 实现了ORM思想的框架：mybatis，hibernate

> ORM（Object-Relational Mapping） 表示对象关系映射。在面向对象的软件开发中，通过ORM，就可以把对象映射到关系型数据库中。只要有一套程序能够做到建立对象与数据库的关联，操作对象就可以直接操作数据库数据，就可以说这套程序实现了ORM对象关系映射
>
> 简单的说：ORM就是建立实体类和数据库表之间的关系，从而达到操作实体类就相当于操作数据库表的目的。
>
> 常见的orm框架：Mybatis（ibatis）、Hibernate、Jpa

### hibernate框架介绍

Hibernate是一个开放源代码的对象关系映射框架，它对JDBC进行了非常轻量级的对象封装，它将POJO与数据库表建立映射关系，是一个全自动的orm框架.hibernate可以自动生成SQL语句，自动执行，使得Java程序员可以随心所欲的使用对象编程思维来操纵数据库。

###  JPA规范

JPA的全称是Java Persistence API， 即Java 持久化API，是SUN公司推出的一套基于ORM的规范，内部是由一系列的接口和抽象类构成。

JPA通过JDK 5.0注解描述对象－关系表的映射关系，并将运行期的实体对象持久化到数据库中。

### jpa的基本操作

#### jpa操作的操作步骤

1. 加载配置文件创建实体管理器工厂
   			Persisitence：静态方法（根据持久化单元名称创建实体管理器工厂）createEntityMnagerFactory（持久化单元名称）
   			作用：创建实体管理器工厂

   ```java
   // 1. 加载配置文件创建工厂（实体管理器工厂）对象
   EntityManagerFactory factory = Persistence.createEntityManagerFactory("myJpa");
   ```

2. 根据实体管理器工厂，创建实体管理器
   		EntityManagerFactory ：获取EntityManager对象
   		方法：createEntityManager

   ```java
   // 2. 通过尸体管理类工厂获取实体管理器
   EntityManager entityManager = factory.createEntityManager();
   ```

   1. 内部维护的很多的内容
   2. 内部维护了数据库信息，
   3. 维护了缓存信息
   4. 维护了所有的实体管理器对象
      			

   >  在创建EntityManagerFactory的过程中会根据配置创建数据库表，EntityManagerFactory的创建过程比较浪费资源
   > 		特点：线程安全的对象，多个线程访问同一个EntityManagerFactory不会有线 程安全问题。
   >
   > 如何解决EntityManagerFactory的创建过程浪费资源（耗时）的问题？
   > 		思路：创建一个公共的EntityManagerFactory的对象
   >  静态代码块的形式创建EntityManagerFactory

3.创建事务对象，开启事务

```java
 // 3. 获取事务对象，开启事务
EntityTransaction transaction = entityManager.getTransaction();
// 开启事务
transaction.begin();
// 保存
entityManager.persist(customer);
// 更新
entityManager.merge(customer);
// 删除
entityManager.remove(customer);
// 查询
entityManager.find(Customer.class, 1L);
entityManager.getReference(Customer.class, 1L);

// 提交事务
transaction.commit();
// 回滚
transaction.rollback();
```

>  **EntityManager**对象：实体类管理器
>
> 注意：find 立即执行查询（立即加载）
>
> getReference 使用结果时才会执行查询（懒加载）

4. 释放资源

## JPA注解

### 实体相关

| 注解            | 作用                                               | 属性                                                         |
| --------------- | -------------------------------------------------- | ------------------------------------------------------------ |
| @Entity         | 指定当前类是实体类。                               | 无                                                           |
| @Table          | 指定实体类和表之间的对应关系。                     | name：指定数据库表的名称                                     |
| @Id             | 指定当前字段是主键。                               | 无                                                           |
| @GeneratedValue | 指定主键的生成方式                                 | strategy ：指定主键生成策略。(GenerationType.IDENTITY：自增，底层数据库必须支持自动增长；<br />GenerationType.SEQUENCE: 序列，底层数据库必须支持序列；<br />GenerationType.TABLE，jpa提供的一种机制，通过一张数据库表的形式帮助我们完成自增；<br />GenerationType.AUTO，程序自动的帮助我们选择主键生成策略) |
| @Column         | 指定实体类属性和数据库表之间的对应关系             | name：指定数据库表的列名称。<br />unique：是否唯一<br />nullable：是否可以为空<br />inserttable：是否可以插入<br />updateable：是否可以更新<br />columnDefinition: 定义建表时创建此列的DDL<br />secondaryTable: 从表名。如果此列不建在主表上（默认建在主表），该属性定义该列所在从表的名字搭建开发环境[重点] |
| @Query          | 自定义SQL或者JPQL                                  | value: sql语句或者jpql语句。<br />nativeQuery：值为布尔类型，true时value是sql，false时value是jpql。<br />countQuery: 分页时使用 |
| @Modifying      | 当dao方法是更新删除时，需要加上该注解              |                                                              |
| @Transactional  | 使用更新/删除操作，必须要该注解                    |                                                              |
| @Rollback       | 默认执行完成后，事务会自动回滚。导致数据库没更新。 | value：false，使用@Rollback(value = false) 关闭自动回滚      |



## Spring Data JPA 编写持久层

​		Spring Data JPA是spring提供的一款对于数据访问层（Dao层）的框架，使用Spring Data JPA，只需要按照框架的规范提供dao接口，不需要实现类就可以完成数据库的增删改查、分页查询等方法的定义，极大的简化了我们的开发过程。

只需要编写dao层接口，不需要编写dao层接口的实现类，dao层接口规范

​	1.需要继承两个接口（JpaRepository，JpaSpecificationExecutor）
​	2.需要提供响应的泛型
​	

### Spring Data JPA的实现过程

1. 注入的customerDao对象，本质上是通过JdkDynamicAopProxy生成的一个代理对象

> 当程序执行的时候，会通过JdkDynamicAopProxy的invoke方法，对customerDao对象生成动态代理对象。根据对Spring Data JPA介绍而知，要想进行findOne查询方法，最终还是会出现JPA规范的API完成操作，那么这些底层代码存在于何处呢？答案很简单，都隐藏在通过JdkDynamicAopProxy生成的动态代理对象当中，而这个动态代理对象就是SimpleJpaRepository
>
> 通过SimpleJpaRepository的源码分析，定位到了findOne方法，在此方法中，返回em.find()的返回结果，那么em又是什么呢？ 
>
> 带着问题继续查找em对象，我们发现em就是EntityManager对象，而他是JPA原生的实现方式，所以我们得到结论Spring Data JPA只是对标准JPA操作进行了进一步封装，简化了Dao层代码的开发
>
> springDataJpa的运行过程和原理剖析
> 	1.通过JdkDynamicAopProxy的invoke方法创建了一个动态代理对象
> 	2.SimpleJpaRepository当中封装了JPA的操作（借助JPA的api完成数据库的CRUD）
> 	3.通过hibernate完成数据库操作（封装了jdbc）



### jpql

 jpa query language  （jpq查询语言）

对于某些业务来说，我们还需要灵活的构造查询条件，这时就可以使用@Query注解，结合JPQL的语句方式完成查询

特点：语法或关键字和sql语句类似,查询的是类和类中的属性

> @Query 注解的使用非常简单，只需在方法上面标注该注解，同时提供一个JPQL查询语句即可



### 方法命名规则查询

按照Spring Data JPA 定义的规则，查询方法以findBy开头，涉及条件查询时，条件的属性用条件关键字连接，要注意的是：条件属性首字母需大写。框架在进行方法名解析时，会先把方法名多余的前缀截取掉，然后对剩下部分进行解析。

| **Keyword**       | **Sample**                                | **JPQL**                                                     |
| ----------------- | ----------------------------------------- | ------------------------------------------------------------ |
| And               | findByLastnameAndFirstname                | …  where x.lastname = ?1 and x.firstname = ?2                |
| Or                | findByLastnameOrFirstname                 | …  where x.lastname = ?1 or x.firstname = ?2                 |
| Is,Equals         | findByFirstnameIs,  findByFirstnameEquals | …  where x.firstname = ?1                                    |
| Between           | findByStartDateBetween                    | …  where x.startDate between ?1 and ?2                       |
| LessThan          | findByAgeLessThan                         | …  where x.age < ?1                                          |
| LessThanEqual     | findByAgeLessThanEqual                    | …  where x.age ⇐ ?1                                          |
| GreaterThan       | findByAgeGreaterThan                      | …  where x.age > ?1                                          |
| GreaterThanEqual  | findByAgeGreaterThanEqual                 | …  where x.age >= ?1                                         |
| After             | findByStartDateAfter                      | …  where x.startDate > ?1                                    |
| Before            | findByStartDateBefore                     | …  where x.startDate < ?1                                    |
| IsNull            | findByAgeIsNull                           | …  where x.age is null                                       |
| IsNotNull,NotNull | findByAge(Is)NotNull                      | …  where x.age not null                                      |
| Like              | findByFirstnameLike                       | …  where x.firstname like ?1                                 |
| NotLike           | findByFirstnameNotLike                    | … where  x.firstname not like ?1                             |
| StartingWith      | findByFirstnameStartingWith               | …  where x.firstname like ?1 (parameter bound with appended %) |
| EndingWith        | findByFirstnameEndingWith                 | …  where x.firstname like ?1 (parameter bound with prepended %) |
| Containing        | findByFirstnameContaining                 | …  where x.firstname like ?1 (parameter bound wrapped in %)  |
| OrderBy           | findByAgeOrderByLastnameDesc              | …  where x.age = ?1 order by x.lastname desc                 |
| Not               | findByLastnameNot                         | …  where x.lastname <> ?1                                    |
| In                | findByAgeIn(Collection ages)              | …  where x.age in ?1                                         |
| NotIn             | findByAgeNotIn(Collection age)            | …  where x.age not in ?1                                     |
| TRUE              | findByActiveTrue()                        | …  where x.active = true                                     |
| FALSE             | findByActiveFalse()                       | …  where x.active = false                                    |
| IgnoreCase        | findByFirstnameIgnoreCase                 | …  where UPPER(x.firstame) = UPPER(?1)                       |