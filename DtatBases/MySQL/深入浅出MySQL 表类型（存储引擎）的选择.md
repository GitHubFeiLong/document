# 表类型（存储引擎）的选择

和大多数数据库不同，MySQL 中有一个存储引擎的概念，针对不同的存储需求可以选择最
优的存储引擎。本章将详细介绍存储引擎的概念、分类以及实际应用中的选择原则。



## MySQL 存储引擎概述

​	插件式存储引擎是MySQL 数据库最重要的特性之一，用户可以根据应用的需要选择如何存储和索引数据、是否使用事务等。MySQL 默认支持多种存储引擎，以适用于不同领域的数据库应用需要，用户可以通过选择使用不同的存储引擎提高应用的效率，提供灵活的存储，用户甚至可以按照自己的需要定制和使用自己的存储引擎，以实现最大程度的可定制性。
​	MySQL 5.7 支持的存储引擎包括MyISAM、InnoDB、BDB、MEMORY、MERGE（MRG_MyISAM）、EXAMPLE、NDB 、ARCHIVE、CSV、BLACKHOLE、FEDERATED 等，其中InnoDB 和 NDB 提供事务安全表，其他存储引擎都是非事务安全表。
​	默认情况下，创建新表不指定表的存储引擎，则新表是默认存储引擎，MySQL 5.5之前的默认存储引擎是MyISAM, 5.5版本之后改为InnoDB。如果需要修改默认的存储引擎，则可以在参数文件中设置 default_storage_engine。查看当前的默认存储引擎，可以使用以下命令:

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

​	通过上面的结果可以查看当前支持的哪些存储引擎，其中 Support 不同值得含义分别为：DEFAULT -- 支持并启用，并且为默认引擎；YES--支持并启用；NO--不支持；DISABLED--支持，但是数据库启动得时候被禁用。

​	在创建新表的时候，可以通过增加ENGINE 关键字设置新建表的存储引擎，例如，在下面的例子中，表ai 就是MyISAM 存储引擎的，而country 表就是InnoDB 存储引擎的：

```mysql
mysql> create table ai (i bigint(20) not null auto_increment, primary key(i)) ENGINE=MyISAM DEFAULT CHARSET=utf8;
Query OK, 0 rows affected, 1 warning (0.02 sec)

mysql> create table country(country_id smallint unsigned not null auto_increment, country varchar(50) not null, last_update timestamp not null default current_timestamp on update current_timestamp,primary key (country_id))ENGINE=InnoDB DEFAULT CHARSET=utf8;
Query OK, 0 rows affected, 1 warning (0.05 sec)
```

​	也可以使用ALTER TABLE 语句，将一个已经存在的表修改成其他的存储引擎。下面的例子介绍了如何将表ai 从MyISAM 存储引擎修改成InnoDB 存储引擎：

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

这样修改后，ai 表成为InnoDB 存储引擎，可以使用InnoDB 存储引擎的相关特性。

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

MyISAM 是MySQL 5.5之前版本的默认存储引擎。MyISAM既不支持事务，也不支持外键，在5.5之前的版本中，MyISAM在某些场景中相对InnoDB的访问速度有明显的优势，对事物完整性没有要求或者以SELECT、INSERT 为主的应用可以使用这个引擎来创建表。MySQL5.6之后，MyISAM已经越来越少地被使用。

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

​	可以使用LAST_INSERT_ID()查询当前线程最后插入记录使用的值。如果一次插入了多条记录，那么返回的是第一条记录使用的自动增长值。需要注意的是，如果人为指定自增列的值    ，那么LAST_INSERT_ID() 的值不会更新。下面的例子演示了使用LAST_INSERT_ID()的情况：

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

​	可以看到，手工指定自增列的值为4，LAST_INSERT_ID()的值并不会更新。接下来一次插入3行，这次自增列的值将自动生成。由于此时自增列的最大值是4，对于插入的这3行自增列会自动分配5、6、7这3个值。

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

