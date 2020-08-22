# 表类型（存储引擎）的选择

和大多数数据库不同，MySQL 中有一个存储引擎的概念，针对不同的存储需求可以选择最优的存储引擎。本章将详细介绍存储引擎的概念、分类以及实际应用中的选择原则。



## MySQL 存储引擎概述

​		插件式存储引擎是 MySQL 数据库最重要的特性之一，用户可以根据应用的需要选择如何存储和索引数据、是否使用事务等。MySQL 默认支持多种存储引擎，以适用于不同领域的数据库应用需要，用户可以通过选择使用不同的存储引擎提高应用的效率，提供灵活的存储，用户甚至可以按照自己的需要定制和使用自己的存储引擎，以实现最大程度的可定制性。
​		MySQL 5.7 支持的存储引擎包括 MyISAM、InnoDB、BDB、MEMORY、MERGE（MRG_MyISAM）、EXAMPLE、NDB 、ARCHIVE、CSV、BLACKHOLE、FEDERATED 等，其中 InnoDB 和 NDB 提供事务安全表，其他存储引擎都是非事务安全表。
​	默认情况下，创建新表不指定表的存储引擎，则新表是默认存储引擎，MySQL 5.5之前的默认存储引擎是 MyISAM, 5.5版本之后改为 InnoDB。如果需要修改默认的存储引擎，则可以在参数文件中设置 default_storage_engine。查看当前的默认存储引擎，可以使用以下命令:

```mysql
mysql> show variables like 'default_storage_engine';
+------------------------+--------+
| Variable_name          | Value  |
+------------------------+--------+
| default_storage_engine | InnoDB |
+------------------------+--------+
1 row in set, 1 warning (0.00 sec)
```

​	可以通过以下方法查询当前数据库支持的存储引擎：

```mysql
mysql> show engines \G;
*************************** 1. row ***************************
      Engine: MEMORY
     Support: YES
     Comment: Hash based, stored in memory, useful for temporary tables
Transactions: NO
          XA: NO
  Savepoints: NO
*************************** 2. row ***************************
      Engine: MRG_MYISAM
     Support: YES
     Comment: Collection of identical MyISAM tables
Transactions: NO
          XA: NO
  Savepoints: NO
*************************** 3. row ***************************
      Engine: CSV
     Support: YES
     Comment: CSV storage engine
Transactions: NO
          XA: NO
  Savepoints: NO
*************************** 4. row ***************************
      Engine: FEDERATED
     Support: NO
     Comment: Federated MySQL storage engine
Transactions: NULL
          XA: NULL
  Savepoints: NULL
*************************** 5. row ***************************
      Engine: PERFORMANCE_SCHEMA
     Support: YES
     Comment: Performance Schema
Transactions: NO
          XA: NO
  Savepoints: NO
*************************** 6. row ***************************
      Engine: MyISAM
     Support: YES
     Comment: MyISAM storage engine
Transactions: NO
          XA: NO
  Savepoints: NO
*************************** 7. row ***************************
      Engine: InnoDB
     Support: DEFAULT
     Comment: Supports transactions, row-level locking, and foreign keys
Transactions: YES
          XA: YES
  Savepoints: YES
*************************** 8. row ***************************
      Engine: BLACKHOLE
     Support: YES
     Comment: /dev/null storage engine (anything you write to it disappears)
Transactions: NO
          XA: NO
  Savepoints: NO
*************************** 9. row ***************************
      Engine: ARCHIVE
     Support: YES
     Comment: Archive storage engine
Transactions: NO
          XA: NO
  Savepoints: NO
9 rows in set (0.00 sec)
```

​		通过上面的结果可以查看当前支持的哪些存储引擎，其中 Support 不同值得含义分别为：DEFAULT -- 支持并启用，并且为默认引擎；YES--支持并启用；NO--不支持；DISABLED--支持，但是数据库启动得时候被禁用。

​		在创建新表的时候，可以通过增加 ENGINE 关键字设置新建表的存储引擎，例如，在下面的例子中，表 ai 就是 MyISAM 存储引擎的，而 country 表就是InnoDB 存储引擎的：

```mysql
mysql> create table ai (i bigint(20) not null auto_increment, primary key(i)) ENGINE=MyISAM DEFAULT CHARSET=utf8;
Query OK, 0 rows affected, 1 warning (0.02 sec)

mysql> create table country(country_id smallint unsigned not null auto_increment, country varchar(50) not null, last_update timestamp not null default current_timestamp on update current_timestamp,primary key (country_id))ENGINE=InnoDB DEFAULT CHARSET=utf8;
Query OK, 0 rows affected, 1 warning (0.05 sec)
```

​		也可以使用 ALTER TABLE 语句，将一个已经存在的表修改成其他的存储引擎。下面的例子介绍了如何将表 ai 从 MyISAM 存储引擎修改成 InnoDB 存储引擎：

```mysql
mysql> alter table ai engine=innodb;
Query OK, 0 rows affected (0.09 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> show create table ai \G;
*************************** 1. row ***************************
       Table: ai
Create Table: CREATE TABLE `ai` (
  `i` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`i`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
1 row in set (0.00 sec)
```

这样修改后，ai 表成为 InnoDB 存储引擎，可以使用 InnoDB 存储引擎的相关特性。

> 注意：修改表的存储引擎需要锁表并复制数据，对于线上环境得表进行这个操作非常危险，除非你非常了解可能造成得影响，否则在线上环境请使用其他方式，例如 percona 得OSC工具。



## 各种存储引擎的特性

下面重点介绍几种常用的存储引擎,并对比各个存储引擎之间的区别，以帮助读者理解不同存储引擎的使用方式。

| 特点             | MyISAM | Memory   | InnoDB | Archive | NDB   |
| ---------------- | ------ | -------- | ------ | ------- | ----- |
| B树索引          | 支持   | 支持     | 支持   | -       | -     |
| 备份/时间点恢复  | 支持   | 支持     | 支持   | 支持    | 支持  |
| 支持集群         | -      | -        | -      | -       | 支持  |
| 聚簇索引         | -      | -        | 支持   | -       | -     |
| 数据压缩         | 支持   | -        | 支持   | 支持    | -     |
| 数据缓存         | -      | N/A      | 支持   | -       | 支持  |
| 数据加密         | 支持   | 支持     | 支持   | 支持    | 支持  |
| 支持外键         | -      | -        | 支持   | -       | 支持  |
| 全文索引         | 支持   | -        | 支持   | -       | -     |
| 地理坐标数据类型 | 支持   | -        | 支持   | 支持    | 支持  |
| 地理坐标索引     | 支持   | -        | 支持   | -       | -     |
| 哈希索引         | -      | 支持     | -      | -       | 支持  |
| 索引缓存         | 支持   | N/A      | 支持   | -       | 支持  |
| 锁粒度           | 表级   | 表级     | 行级   | 行级    | 行级  |
| MVCC多版本控制   | -      | -        | 支持   | -       | -     |
| 支持复制         | 支持   | 有限支持 | 支持   | 支持    | 支持  |
| 存储限制         | 256TB  | RAM      | 64TB   | None    | 384EB |
| T树索引          | -      | -        | -      | -       | 支持  |
| 支持事务         | -      | -        | 支持   | -       | 支持  |
| 统计信息         | 支持   | 支持     | 支持   | 支持    | 支持  |

​	下面将重点介绍最常使用的4 种存储引擎：MyISAM、InnoDB、MEMORY 和Archive。



### MyISAM

MyISAM 是 MySQL 5.5之前版本的默认存储引擎。MyISAM 既不支持事务，也不支持外键，在5.5之前的版本中，MyISAM在某些场景中相对InnoDB的访问速度有明显的优势，对事物完整性没有要求或者以SELECT、INSERT 为主的应用可以使用这个引擎来创建表。MySQL5.6之后，MyISAM已经越来越少地被使用。

​	每个MyISAM 在磁盘上存储成3 个文件，其文件名都和表名相同，但扩展名分别是：

+ .frm（存储表定义）；
+ .MYD（MYData，存储数据）；
+ .MYI （MYIndex，存储索引）。

​	数据文件和索引文件可以放置在不同的目录，平均分布IO，获得更快的速度。
​	要指定索引文件和数据文件的路径，需要在创建表的时候通过DATA DIRECTORY 和INDEX DIRECTORY 语句指定，也就是说不同MyISAM 表的索引文件和数据文件可以放置到不同的路径下。文件路径需要是绝对路径，并且具有访问权限。

​	MyISAM 类型的表可能会损坏，原因可能是多种多样的，损坏后的表可能不能访问，会提示需要修复或者访问后返回错误的结果。MyISAM 类型的表提供修复的工具，可以用CHECK TABLE 语句来检查MyISAM 表的健康，并用REPAIR TABLE 语句修复一个损坏的MyISAM 表。表损坏可能导致数据库异常重新启动，需要尽快修复并尽可能地确认损坏的原因。

​	MyISAM 的表又支持3 种不同的存储格式，分别是：

+ 静态（固定长度）表；
+ 动态表；
+ 压缩表。

其中，静态表是默认的存储格式。静态表中的字段都是非变长字段，这样每个记录都是固定长度的，这种存储方式的优点是存储非常迅速，容易缓存，出现故障容易恢复；缺点是占用的空间通常比动态表多。静态表的数据在存储的时候会按照列的宽度定义补足空格，但是在应用访问的时候并不会得到这些空格，这些空格在返回给应用之前已经去掉。

​	但是也有些需要特别注意的问题，如果需要保存的内容后面本来就带有空格，那么在返回结果的时候也会被去掉，开发人员在编写程序的时候需要特别注意，因为静态表是默认的存储格式，开发人员可能并没有意识到这一点，从而丢失了尾部的空格。以下例子演示了插入的记录包含空格时处理的情况：

```mysql
mysql> create table myisam_char (name char(10)) engine=myisam;
Query OK, 0 rows affected (0.01 sec)

mysql> insert into myisam_char values('abcde'),('abcde  '),('  abcde'),('   abcde  ');
Query OK, 4 rows affected (0.01 sec)
Records: 4  Duplicates: 0  Warnings: 0

mysql> select name, length(name) from myisam_char;
+----------+--------------+
| name     | length(name) |
+----------+--------------+
| abcde    |            5 |
| abcde    |            5 |
|   abcde  |            7 |
|    abcde |            8 |
+----------+--------------+
4 rows in set (0.00 sec)
```

​	从上面的例子可以看出，插入记录后面的空格都被去掉了，前面的空格保留。
​	

​	动态表中包含变长字段，记录不是固定长度的，这样存储的优点是占用的空间相对较少，但是频繁地更新删除记录会产生碎片，需要定期执行OPTIMIZE TABLE 语句或myisamchk -r命令来改善性能，并且出现故障的时候恢复相对比较困难。
​	

​	压缩表由myisampack 工具创建，占据非常小的磁盘空间。因为每个记录是被单独压缩的，所以只有非常小的访问开支。



### InnoDB

InnoDB作为MySQL5.5 之后的默认存储引擎，提供了具有提交、回滚和崩溃恢复能力的事务安全，同时提供了更小的锁粒度和更强的并发能力，拥有 自己独立的缓存和日志，在MySQL5.6 和5.7 版本中性能有较大提升。

​	对比MyISAM的存储引擎，InnoDB 写的处理效率差一些并且会占用更多的磁盘空间以保留数据和索引。但是在大多数场景下，InnoDB都会是更好的选择，这也是为何MySQL将默认存储引擎改为InnoDB，并且在最新的MySQL8.0 中，将所有系统表也都改为InnoDB存储引擎。

​	下面将重点介绍InnoDB 存储引擎的表使用过程中不同于其他存储引擎的特点，以及如何更好地使用存储引擎。

#### 1. 自动增长列

InnoDB 表的自动增长列可以手工插入，但是插入的值如果是空，则实际插入的将是自动增长后的值。下面定义新表autoincre_demo，其中列i 使用自动增长列，对该表插入记录，然后查看自动增长列的处理情况，可以发现插入空时，实际插入的都将是自动增长后的值：

```mysql
mysql> create table autoincre_demo(i smallint not null auto_increment,name varchar(10),primary key(i)) engine=innodb;
Query OK, 0 rows affected (0.04 sec)

mysql> insert into autoincre_demo values(null,'1'),(2,'2'),(null,'3');
Query OK, 3 rows affected (0.01 sec)
Records: 3  Duplicates: 0  Warnings: 0

mysql> select * from autoincre_demo;
+---+------+
| i | name |
+---+------+
| 1 | 1    |
| 2 | 2    |
| 3 | 3    |
+---+------+
3 rows in set (0.00 sec)
```

​	可以通过“ALTER TABLE *** AUTO_INCREMENT = n;”语句强制设置自动增长列的初识值，默认从1 开始，但是在MySQL8.0之前，对于InnoDB存储引擎来说，这个值只保留在内存中，如果数据库重新启动，那么这个值就会丢失，数据库会自动将AUTO_INCREMENT重置为自增列当前存储的最大值+1这可能会导致在数据库重启后，自增列记录的值和预期不一致，从而导致数据冲突。、以下示例演示在MySQL 5.7中，AUTO_INCREMENT值在重启前后的表现。

​	首先，创建测试表test_auto_incre，id 为自增主键：

```mysql
mysql> create table test_auto_incre(id int(11) not null auto_increment, name varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL, primary key(id)) ENGINE=innodb default charset=utf8 collate=utf8_unicode_ci;
Query OK, 0 rows affected, 3 warnings (0.04 sec)

mysql> show create table test_auto_incre \G;
*************************** 1. row ***************************
       Table: test_auto_incre
Create Table: CREATE TABLE `test_auto_incre` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci
1 row in set (0.00 sec)
```

​	修改AUTO_INCREMENT=100

```mysq
mysql> alter table test_auto_incre auto_increment=100;
Query OK, 0 rows affected (0.02 sec)
Records: 0  Duplicates: 0  Warnings: 0
```

​	尝试写入一条数据，看下是否生效：

```mysql
mysql> insert into test_auto_incre values(null, 'abc');
Query OK, 1 row affected (0.01 sec)

mysql> select * from test_auto_incre;
+-----+------+
| id  | name |
+-----+------+
| 100 | abc  |
+-----+------+
1 row in set (0.00 sec)
```

​	id值为预期的100，接下来再次修改该AUTO_INCREMENT的值为200：

```mysql
mysql> alter table test_auto_incre auto_increment=200;
Query OK, 0 rows affected (0.01 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> show create table test_auto_incre \G;
*************************** 1. row ***************************
       Table: test_auto_incre
Create Table: CREATE TABLE `test_auto_incre` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci
1 row in set (0.00 sec)
```

​	然后重启MySQL实例：

```mysql
mysql> show create table test_auto_incre \G;
*************************** 1. row ***************************
       Table: test_auto_incre
Create Table: CREATE TABLE `test_auto_incre` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci
1 row in set (0.01 sec)
```

​	可以看到，重启之后，AUTO_INCREMENT的值变成了 101，是当前自增列的最大值+1，而不是重启前设置的200，这种情况可能导致在某些历史数据归档或者复制环境中发生数据冲突。在MySQL8.0中，这个BUG得到修复，具体实现方式是将自增主键的计数持久化到REDO LOG中，每次计数器发生改变都会将其写入REDO LOG。如果数据库发生重启，InnoDB会根据REDO LOG中的计数器信息来初始化其内存值。

​	可以使用 LAST_INSERT_ID() 查询当前线程最后插入记录使用的值。如果一次插入了多条记录，那么返回的是第一条记录使用的自动增长值。需要注意的是，如果人为指定自增列的值    ，那么LAST_INSERT_ID() 的值不会更新。下面的例子演示了使用LAST_INSERT_ID()的情况：

```mysql
mysql> select last_insert_id();
+------------------+
| last_insert_id() |
+------------------+
|                1 |
+------------------+
1 row in set (0.00 sec)

mysql> insert into autoincre_demo values(4,'4');
Query OK, 0 rows affected (0.01 sec)

mysql> select last_insert_id();
+------------------+
| last_insert_id() |
+------------------+
|                1 |
+------------------+
1 row in set (0.00 sec)
```

​	可以看到，手工指定自增列的值为4，LAST_INSERT_ID() 的值并不会更新。接下来一次插入3行，这次自增列的值将自动生成。由于此时自增列的最大值是4，对于插入的这3行自增列会自动分配5、6、7这3个值。

```mysql
mysql> insert into autoincre_demo(name) values ('5'),('6'),('7');
Query OK, 0 rows affected (0.03 sec)

mysql> select last_insert_id();
+------------------+
| last_insert_id() |
+------------------+
|                5 |
+------------------+
1 row in set (0.00 sec)
```

​	对于InnoDB 表，自动增长列必须是索引。如果是组合索引，也必须是组合索引的第一列，但是对于MyISAM 表，自动增长列可以是组合索引的其他列，这样插入记录后，自动增长列是按照组合索引的前面几列进行排序后递增的。例如，创建一个新的MyISAM 类型的表autoincre_demo，自动增长列d1 作为组合索引的第二列，对该表插入一些记录后，可以发现自动增长列是按照组合索引的第一列d2 进行排序后递增的：

```mysql
mysql> create table autoincre_demo(d1 smallint not null auto_increment,d2 smallint not null, name varchar(10),index(d2,d1))engine=myisam;
Query OK, 0 rows affected (0.01 sec)

mysql> insert into autoincre_demo(d2,name)values(2,'2'),(3,'3'),(4,'4'),(2,'2'),(3,'3'),(4,'4');
Query OK, 6 rows affected (0.01 sec)
Records: 6  Duplicates: 0  Warnings: 0

mysql> select * from autoincre_demo;
+----+----+------+
| d1 | d2 | name |
+----+----+------+
|  1 |  2 | 2    |
|  1 |  3 | 3    |
|  1 |  4 | 4    |
|  2 |  2 | 2    |
|  2 |  3 | 3    |
|  2 |  4 | 4    |
+----+----+------+
6 rows in set (0.00 sec)
```



#### 2. 外键约束

​	MySQL 支持外键的常用存储引擎只有InnoDB，在创建外键的时候，要求父表必须有对应的索引，子表在创建外键的时候也会自动创建对应的索引。
​	下面是样例数据库中的两个表，country 表是父表，country_id 为主键索引，city 表是子表，country_id 字段对country 表的country_id 有外键。

```mysql
CREATE TABLE `country` (
  `country_id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `country` varchar(50) NOT NULL,
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`country_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

CREATE TABLE `city` (
  `city_id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `city` varchar(50) NOT NULL,
  `country_id` smallint(5) unsigned NOT NULL,
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`city_id`),
  KEY `idx_fx_country_id` (`country_id`),
  CONSTRAINT `fk_city_country` FOREIGN KEY (`country_id`) REFERENCES `country` (`country_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8

```

​	在创建索引的时候，可以指定在删除、更新父表时，对子表进行的相应操作，包括RESTRICT、CASCADE、SET NULL 和NO ACTION。其中RESTRICT 和NO ACTION 相同，是指限制在子表有关联记录的情况下父表不能更新；CASCADE 表示父表在更新或者删除时，更新或者删除子表对应记录；SET NULL 则表示父表在更新或者删除的时候，子表的对应字段被SET NULL。选择后两种方式的时候要谨慎，可能会因为错误的操作导致数据的丢失。
​	例如对上面创建的两个表，子表的外键指定是ON DELETE RESTRICT ON UPDATE  CASCADE方式的，那么在主表删除记录的时候，如果子表有对应记录，则不允许删除，主表在更新记录的时候，如果子表有对应记录，则子表对应更新：

```mysql
mysql> insert into country values(null, 'afghanistan',now());
Query OK, 1 row affected (0.01 sec)

mysql> select * from country where country_id=1;
+------------+-------------+---------------------+
| country_id | country     | last_update         |
+------------+-------------+---------------------+
|          1 | afghanistan | 2020-08-22 12:05:49 |
+------------+-------------+---------------------+
1 row in set (0.00 sec)


mysql> insert into city values(251,'kabul',1, now());
Query OK, 1 row affected (0.01 sec)

mysql> select * from city;
+---------+-------+------------+---------------------+
| city_id | city  | country_id | last_update         |
+---------+-------+------------+---------------------+
|     251 | kabul |          1 | 2020-08-22 12:06:48 |
+---------+-------+------------+---------------------+
1 row in set (0.00 sec)

mysql> delete from country where country_id=1;
ERROR 1451 (23000): Cannot delete or update a parent row: a foreign key constraint fails (`test`.`city`, CONSTRAINT `fk_city_country` FOREIGN KEY (`country_id`) REFERENCES `country` (`country_id`) ON DELETE RESTRICT ON UPDATE CASCADE)
mysql> update country set country_id=10000 where country_id=1;
Query OK, 1 row affected (0.01 sec)
Rows matched: 1  Changed: 1  Warnings: 0

mysql> select * from country where country='Afghanistan';
+------------+-------------+---------------------+
| country_id | country     | last_update         |
+------------+-------------+---------------------+
|      10000 | afghanistan | 2020-08-22 12:08:41 |
+------------+-------------+---------------------+
1 row in set (0.00 sec)

mysql> select * from city where city_id=251;
+---------+-------+------------+---------------------+
| city_id | city  | country_id | last_update         |
+---------+-------+------------+---------------------+
|     251 | kabul |      10000 | 2020-08-22 12:06:48 |
+---------+-------+------------+---------------------+
1 row in set (0.00 sec)
```

​	当某个表被其他表创建了外键参照，那么该表的对应索引或者主键禁止被删除。
​	在导入多个表的数据时，如果需要忽略表之前的导入顺序，可以暂时关闭外键的检查；同样，在执行LOAD DATA 和ALTER TABLE 操作的时候，可以通过暂时关闭外键约束来加快处
理的速度，关闭的命令是“SET FOREIGN_KEY_CHECKS = 0;”，执行完成之后，通过执行“SET
FOREIGN_KEY_CHECKS = 1;”语句改回原状态。
​	对于InnoDB 类型的表，外键的信息通过使用show create table 或者show table status 命令都可以显示。

```mysql
mysql> show table status like 'city' \G;
*************************** 1. row ***************************
           Name: city
         Engine: InnoDB
        Version: 10
     Row_format: Dynamic
           Rows: 1
 Avg_row_length: 16384
    Data_length: 16384
Max_data_length: 0
   Index_length: 16384
      Data_free: 0
 Auto_increment: 252
    Create_time: 2020-08-21 22:03:01
    Update_time: 2020-08-22 12:08:41
     Check_time: NULL
      Collation: utf8_general_ci
       Checksum: NULL
 Create_options:
        Comment:
1 row in set (0.01 sec)
```

> 注意外键需要注意的细节较多，一旦使用不当，可能会带来性能下降或者数据不一致的问题，尤其在OLTP 类型的应用中需要谨慎使用外键。



#### 3.主键和索引

​	不同于其他存储引擎，InnoDB 的数据文件本身就是以聚簇索引的形式保存的，这个聚簇索引也被称为主索引，并且也是 InnoDB 表的主键，InnoDB 表的每行数据都保存在主索引的叶子节点上。因此，所有 InnoDB  表都必须包含主键，如果创建表时候，没有显式指定主键，那么 InnoDB  存储引擎会自动创建一个长度为6 个字节的long类型隐藏字段作为主键。
​	考虑到聚族索引的特点和对于查询的优化效果，所有 InnoDB 表都应该显式的指定主键，一般来说，主键应该按照下列原则来选择:

+ 满足唯一和非空约束;
+ 优先考虑使用最经常被当作查询条件的字段或者自增字段;
+ 字段值基本不会被修改
+ 使用尽可能短的字段。

​	在InnoDB 表上，除了主键之外的其他索引都叫作辅助索引或者二级索引，二级索引会指向主索引，并通过主索引获取最终数据。因此，主键是否合理的创建，会对所有索引的效率产生影响。

​	关于索引使用的更多内容，会在第9章做更加详细的介绍。



#### 4.存储方式

InnoDB存储表和索引有以下两种方式。

+ 使用共享表空间存储: 这种方式创建的表的表结构保存在 frm 文件中， 数据和索引 保存在 innodb_data_ home_dir 和 innodb _data_file_path 定义的表空间中，可以是多个文件。

+ 使用多表空间存储: 这种方式创建的表的表结构仍然保存在 frm文件中， 但是每个表的数据和索引单独保存在.ibd中。如果是一个分区表，则每个分区对应单独的.ibd文件，文件名是“表名+分区名”，可以在创建分区的时候指定每个分区的数据文件的位置，以此将表的 IO 均匀分布在多个磁盘上。

​		使用共享表空间时，随着数据的不断增长，表空间的管理维护会变得越来越困难，所以一般都建议使用多表空间。 要使用多表空间的存储方式，需设置参数 innodb_file_per_table.在MySQL 5.7中，此参数默认设置为ON,即新建的表默认都是按照多表空间的方式创建。如果修改此参数为OFF.则新创建的表都会改为共享表空间存储，但已有的多表空间的表仍然保存原来的访问方式。

​	一些老版本中的表， 很多是共享表空间，可以通过下面的命令改为多表空间:

```mysql
mysql> set globa innodb_file_per_table=1;
mysql> alter table table_name ENGINE=InnoDB;
```

​		多表空间的数据文件没有大小限制，既不需要设置初始大小，也不需要设置文件的最大限制、扩展大小等参数。

​		对于使用多表空间特性的表，可以比较方便地进行单表备份和恢复操作，但是直接复制 .ibd文件是不行的，因为没有共享表空间的数据字典信息，直接复制的  .ibd 文件和 .frm文件恢复时是不能被正确识别的，但可以通过以下命令:

```mysql
alter table tab_name DISCARD TABLESPACE;
alter table tab_name IMPORT TABLESPACE;
```

将备份恢复到数据库中。

> 注意，即使在多表空间的存储方式下，共享表空间仍然是必须的，InnoDB 把内部数据词典和在线重做日志放在这个文件





### MEMORY

​		MEMORY存储引擎使用存在于内存中的内容来创建表。每个 MEMORY 表只实际对应一个磁盘文件，格式是 .frm ， MEMORY 类型的表访问非常地快，因为它的数据是存放在内存中的，并且默认使用HASH索引，但是一旦服务关闭，表中的数据就会丢失掉。

​		下面的例子创建了一个MEMORY 的表，并从 city表中获得记录：

```mysql
mysql> create table tab_memory ENGINE=MEMORY select city_id,city,country_id from city group by city_id;
Query OK, 1 row affected (0.02 sec)

mysql> select * from tab_memory;
+---------+-------+------------+
| city_id | city  | country_id |
+---------+-------+------------+
|     251 | kabul |      10000 |
+---------+-------+------------+
1 row in set (0.00 sec)

mysql> show table status like 'tab_memory' \G;
*************************** 1. row ***************************
           Name: tab_memory
         Engine: MEMORY
        Version: 10
     Row_format: Fixed
           Rows: 1
 Avg_row_length: 155
    Data_length: 127040
Max_data_length: 16252835
   Index_length: 0
      Data_free: 0
 Auto_increment: 1
    Create_time: 2020-08-22 13:33:58
    Update_time: NULL
     Check_time: NULL
      Collation: utf8_general_ci
       Checksum: NULL
 Create_options:
        Comment:
1 row in set (0.00 sec)

ERROR:
No query specified
```

​		给MEMORY 表创建索引的时候，可以指定使用HASH索引还是 BTREE 索引：

```mysql
mysql> create index mem_hash using HASH on tab_memory(city_id);
Query OK, 1 row affected (0.04 sec)
Records: 1  Duplicates: 0  Warnings: 0

mysql> SHOW INDEX FROM tab_memory \G;
*************************** 1. row ***************************
        Table: tab_memory
   Non_unique: 1
     Key_name: mem_hash
 Seq_in_index: 1
  Column_name: city_id
    Collation: NULL
  Cardinality: 0
     Sub_part: NULL
       Packed: NULL
         Null:
   Index_type: HASH
      Comment:
Index_comment:
      Visible: YES
   Expression: NULL
1 row in set (0.01 sec)

ERROR:
No query specified

mysql> drop index mem_hash on tab_memory;
Query OK, 1 row affected (0.04 sec)
Records: 1  Duplicates: 0  Warnings: 0


mysql> create index mem_hash USING BTREE on tab_memory (city_id);
Query OK, 1 row affected (0.04 sec)
Records: 1  Duplicates: 0  Warnings: 0

mysql> show index from tab_memory \G;
*************************** 1. row ***************************
        Table: tab_memory
   Non_unique: 1
     Key_name: mem_hash
 Seq_in_index: 1
  Column_name: city_id
    Collation: A
  Cardinality: 0
     Sub_part: NULL
       Packed: NULL
         Null:
   Index_type: BTREE
      Comment:
Index_comment:
      Visible: YES
   Expression: NULL
1 row in set (0.00 sec)

ERROR:
No query specified
```

​		在启动MySQL服务的时候使 --init-file 选项，把 INSERT INTO .... SELECT 或LOAD DATA   INFILE这样的语句放入这个文件中，就可以在服务启动时从持久稳固的数据源装载表。

​		服务器需要足够的内存来维持所有在同一时间使用的 MEMORY表，当不再需 MEMORY 表的内容之时，要释放被MEMORY表使用的内存，应该执行DELETE FROM或 TRUNCATE  TABLE，或者整个删除表(使用DROP TABLE操作)。

​		每个MEMORY表中可以放置的数据量的大小，受到 max_heap_table_size 系统变量的约束，这个系统变量的初始值是16MB.可以根据需要加大。此外，在定义MEMORY表的时候，可以通过 MAX_ROWS 子句指定表的最大行数。

​		MEMORY类型的存储引擎主要用于那些内容变化不频繁的代码表，或者作为统计操作的中间结果表，便于高效地对中间结果进行分析并得到最终的统计结果。对存储引擎为MEMORY的表进行更新操作要谨慎，因为数据并没有实际写人到磁盘中，所以一定要对下次重新启动服务后如何获得这些修改后的数据有所考虑。



### MERGE

​		MERGE 存储引擎是一组MyISAM 表的组合，这些MyISAM 表必须结构完全相同，MERGE 表本身并没有数据，对MERGE 类型的表可以进行查询、更新、删除的操作，这些操作实际上是对内部的实际的MyISAM 表进行的。对于MERGE 类型表的插入操作，是通过
INSERT_METHOD 子句定义插入的表，可以有3 个不同的值，使用 FIRST 或 LAST 值使得插入操作被相应地作用在第一或最后一个表上，不定义这个子句或者定义为NO，表示不能对这
个MERGE 表执行插入操作。

​		可以对MERGE 表进行DROP 操作，这个操作只是删除MERGE 的定义，对内部的表没有
任何的影响。MERGE 表在磁盘上保留两个文件，文件名以表的名字开始，一个.frm 文件存储表定义，另一个.MRG 文件包含组合表的信息，包括MERGE 表由哪些表组成、插入新的数据时的依据。可以通过修改.MRG 文件来修改MERGE 表，但是修改后要通过FLUSH TABLES 刷新。

​		下面是一个创建和使用MERGE 表的例子。

（1）创建3 个测试表payment_2006、payment_2007 和payment_all，其中payment_all
是前两个表的MERGE 表：

```mysql
mysql> create table payment_2006(country_id smallint, payment_date datetime,amount decimal(15,2), KEY idx_fx_country_id (country_id) ) engine=myisam;
Query OK, 0 rows affected (0.01 sec)

mysql> create table payment_2007(country_id smallint, payment_date datetime,amount decimal(15,2), KEY idx_fx_country_id (country_id) ) engine=myisam;
Query OK, 0 rows affected (0.01 sec)

mysql> create table payment_all(country_id smallint, payment_date datetime,amount decimal(15,2), INDEX(country_id) ) engine=merge union(payment_2006,payment_2007) INSERT_METHOD=LAST;
Query OK, 0 rows affected (0.01 sec)
```

（2）分别向payment_2006 和payment_2007 表中插入测试数据：

```mysql
mysql> insert into payment_2006 values(1, '2006-05-01', 100000),(2,'2006-08-15',150000);
Query OK, 2 rows affected (0.00 sec)
Records: 2  Duplicates: 0  Warnings: 0

mysql> insert into payment_2007 values(1, '2007-02-20', 35000),(2,'2007-07-15',220000);
Query OK, 2 rows affected (0.00 sec)
Records: 2  Duplicates: 0  Warnings: 0
```

（3）分别查看这3 个表中的记录：

```mysql
mysql> select * from payment_2006;
+------------+---------------------+------------+
| country_id | payment_date        | amount     |
+------------+---------------------+------------+
|          1 | 2006-05-01 00:00:00 | 100000.00 |
|          2 | 2006-08-15 00:00:00 | 150000.00 |
+------------+---------------------+------------+
2 rows in set (0.00 sec)

mysql> select * from payment_2007;
+------------+---------------------+------------+
| country_id | payment_date        | amount     |
+------------+---------------------+------------+
|          1 | 2007-02-20 00:00:00 |   35000.00 |
|          2 | 2007-07-15 00:00:00 |  220000.00 |
+------------+---------------------+------------+
2 rows in set (0.00 sec)

mysql> select * from payment_all;
+------------+---------------------+-----------+
| country_id | payment_date        | amount    |
+------------+---------------------+-----------+
|          1 | 2006-05-01 00:00:00 | 100000.00 |
|          2 | 2006-08-15 00:00:00 | 150000.00 |
|          1 | 2006-05-01 00:00:00 |  35000.00 |
|          2 | 2006-08-15 00:00:00 | 220000.00 |
+------------+---------------------+-----------+
4 rows in set (0.00 sec)
```

​		可以发现，payment_all 表中的数据是payment_2006 和 payment_2007 表的记录合并后的结果集。
​		下面向MERGE 表插入一条记录，由于MERGE 表的定义是INSERT_METHOD=LAST，就会向最后一个表中插入记录，所以虽然这里插入的记录是2006 年的，但仍然会写到 payment_2007 表中。

```mysql
mysql> insert into payment_all values(3, '2006-03-31', 112200);
Query OK, 1 row affected (0.01 sec)

mysql> select * from payment_all;
+------------+---------------------+-----------+
| country_id | payment_date        | amount    |
+------------+---------------------+-----------+
|          1 | 2006-05-01 00:00:00 | 100000.00 |
|          2 | 2006-08-15 00:00:00 | 150000.00 |
|          1 | 2006-02-20 00:00:00 |  35000.00 |
|          2 | 2006-07-15 00:00:00 | 220000.00 |
|          3 | 2006-03-31 00:00:00 | 112200.00 |
+------------+---------------------+-----------+
5 rows in set (0.00 sec)

mysql> select * from payment_2007;
+------------+---------------------+-----------+
| country_id | payment_date        | amount    |
+------------+---------------------+-----------+
|          1 | 2007-02-20 00:00:00 |  35000.00 |
|          2 | 2007-07-15 00:00:00 | 220000.00 |
|          3 | 2006-03-31 00:00:00 | 112200.00 |
+------------+---------------------+-----------+
3 rows in set (0.00 sec)
```



​	这也是MERGE 表和分区表的区别，MERGE 表并不能智能地将记录写到对应的表中，而分区表是可以的（分区功能在5.1 版中正式推出，经过多个版本的更新，目前已经比较完善）。通常我们使用MERGE 表来透明地对多个表进行查询和更新操作，而对这种按照时间记录的操作日志表则可以透明地进行插入操作。



### TokuDB

​		前面介绍的都是 MySQL 自带的存储引擎，除了这些之外，还有一些常 见的第三方存储引擎，在某些特定应用中也有广泛使用，比如列式存储引擎 Infobright 以及高写性能，高压缩的 TokuDB 就是其中非常有代表性的两种。本节将简单介绍TokuDB。

​		TokuDB 是一个高性能、支持事务处理的存储引擎，在 MySQL 5.6 版本之前，可以安装到 MySQL 和 MariaDB 中，被 Percona 公司收购之后，目前最新版本可以在 Percona Server for MySQL 之中使用。TokuDB 存储引擎具有高扩展性、高压缩率、高效的写人性能，支持大多数在线 DDL 操作。最新版本已经开源，读者可以从 Percona 官方网站中进行下载和安装 https://percona.com/sofware/mysql-database/perconan-serer)。

​		针对 TokuDB 存储引擎的主要特性，Tokutek 网站公布了这款优秀存储引擎与经典的InnoDB 存储引擎的对比结果，如图6.1所示。由于本书内容以 MySQL 社区版为主，因此下面的内容是针对 MySOL 5.6 和其对应的 TokuDB 版本。

​		通过对比，可以看出TokuDB主要有以下几项特性:

+ 使用 Fractal 树索引保证高效的插入性能;

+ 优秀的压缩特性。比 InnoDB 高近10倍;
+ Hot Schema Changes 特色支持在线创建索引和添加、删除属性列等DDL操作；
+ 使用 Bulk Loader 达到快速加载大量数据；
+ 提供了主从延迟消除技术;
+ 支持ACID和MVCC



|                                                    | InnoDB           | TokuDB                                               |
| -------------------------------------------------- | ---------------- | ---------------------------------------------------- |
| Index Type                                         | B-tree           | Fractal Tree index                                   |
| Insertion Rate at Scale                            | 100s / second    | 10,000s / second                                     |
| Compression                                        | ~2x              | 5x - 10x typical, up to 25x possible                 |
| Hot Indexing                                       | No(hrs+)         | Yes (secs to mins)                                   |
| Hot Column Addition / Deletion/ Expansion / Rename | No(hrs+)         | Yes (secs to mins)                                   |
| Fast Loader                                        | No               | Yes                                                  |
| Fragmentation Immunity                             | No               | Yes(no dump/reload downtime -no index fragmentation) |
| Clustering Indexes                                 | Primary Key Only | Multiple                                             |
| Fast Recovery Time                                 | No               | Yes                                                  |
| Eliminates Slave Lag                               | No               | Yes                                                  |
| MariaDB Compatible                                 | Yes              | Yes                                                  |
| ACID                                               | Yes              | Yes                                                  |
| MVCC                                               | Yes              | Yes                                                  |



​		关于 ToKuDB最新各特性的具体性能测试数据，读者可以从 Percona 网站获得（https://www.percona.com/doc/percona-server/LASTEST/index.html）。

​		通过上面的介绍，可以发现 TokuDB 特别适用以下几种场景：

+ 日志数据，因为日志通常插入频繁且存储量大；
+ 历史数据，通常不会再有写操作，可以利用 TokuDB 的高压缩特性进行存储；
+ 在线DDL 较频繁的场景，使用 TokuDB 可以大大增加系统的可用性。





## 如何选择合适的存储引擎

​		在选择存储引擎时，应根据应用特点选择合适的存储引擎。对于复杂的应用系统，还可以根据实际情况选择多种存储引擎进行组合。

​		下面是几种常用的存储引擎的适用环境。

+ **MyISAM**: MySQL 5.5 之前版本默认的存储引擎。如果应用是以读操作和插入操作为主，只有极少的更新和删除操作，并且对事务的完整性没有要求、没有并发写操作，那么选择这个存储引擎是适合的。OLTP 环境一般建议不要再使用 MyISAM。

+ **InoDB**: MySQL5.5 之后版本的默认存储引擎，用于事务处理应用程序，支持外键，对于大多数的应用系统，InnoDB 都是合适的选择，如果应用对事物的完整性有比较高的要求，在并发条件下要求数据的一致性。数据操作除了插入和查询以外，还包括很多的更新、删除操作，那么优先选择 InnoDB 存储引擎。InnoDM 存储引擎除了有效地降低由于删除和更新导致的锁定，还可以确保事务的完整提交（commit）和回滚（rollback）。

+ **MEMORY：**将所有数据保存在RAM中，在需要快速定位记录和其他类似数据的环境下，可提供极快的访问速度。MEMORY 的缺陷是对表的大小有限制，太大的表无法缓存在内存中，其次要确保表中的数据可以恢复，数据库异常终止后表中的数据是可以恢复的。MEMORY 表通常用于更新不太频繁的小表，用以快速得到访问结果。

+ **MERGE：** 用于将一系列等同的 MyISAM 表以逻辑方式组合在一起，并作为一个对象引用它们。MERGE表的优点在于可以突破对单个 MyISAM 表大小的限制，并且通过将不同的表分布在多个磁盘上，可以有效地改善 MERGE 表的访问效率。这对于诸如数据仓储等 VLDB 环境十分适合。

  

> 注意:以上只是我们按照实施经验提出的关于存储引擎选择的一些建议，但是不同应用的特点是千差万别的。选择使用哪种存储引擎才是最佳方案也不是绝对的，这需要根据用户各自的应用进行测试，从而得到最适合自己的结果。



## 小结

​		本章重点介绍了 MySQL 提供的几种主要的存储引擎及其使用特性，以及如何根据应用的需要选择合适的存储引擎。这些存储引擎有各自的优势和适用的场合，正确地选择存储引擎对改善应用的效率可以起到事半功倍的效果。
​		正确地选择了存储引擎之后，还需要正确选择表中的数据类型。