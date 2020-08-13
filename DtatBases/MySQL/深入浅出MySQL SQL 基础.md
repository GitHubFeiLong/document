# Mysql

## SQL 基础

### SQL 分类

SQL 语句主要可以划分为以下3个类别

+ DDL（Data Definition Language）语句：数据定义语言，这些语句定义了不同的数据段、数据库、数据表、列、索引等数据库对象，常用的语句关键主要包括create,drop,alter等。
+ DML（Data Manipulation Language）语句：数据操纵语句，用于添加、删除、更新和查询数据库记录，并检查数据完整性，常用的语句关键字主要包括insert、delete、update和select等
+ DCL（Data Control Language）语句：数据控制语句，用于控制不同数据段直接的许可和访问级别的语句，这些语句定义了数据库、表、字段、用户的访问权限和安全级别。常用的语句关键字主要包括grant、revoke等

### DDL语句

#### 创建数据库

```mysql
CREATE DATABASE dbname;
```

##### 查看数据库

##### 查看系统中存在哪些数据库

```mysql
mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| mysql              |
| performance_schema |
| recode             |
| sys                |
+--------------------+
10 rows in set (0.01 sec)
```

这四个数据库是安装就自动创建的

+ information_schema：主要存储系统中的一些数据库对象信息，比如用户表信息、列信息、权限信息、字符集信息、分区信息等，每个用户都可以查看这个数据库，但根据权限的不同看到的内容不同
+ performance_schema：MySQL5.5引入的系统库，用于存储系统性能相关的动态参数表。
+ sys：MySQL 5.7 引入的系统库，本身不记录系统数据，基于information_schema和performance_schema之上，封装了一层更加易于调优和诊断的系统试图。
+ mysql：存储系统的用户权限信息。



```mysql
#选择要操作的数据库
USE dbname
#查看数据库中的所有数据表
show tables
```

#### 删除数据库

语法

```mysql
drop database dbname;
```

##### 创建表

```mysql
CREATE TABLE tablename(
	column_name_1 column_type_1 constraints,
    column_name_2 column_type_2 constraints,
    ...
    column_name_3 column_type_3 constraints
)
```

+ column_name 是列的名称
+ column_type 是列的数据类新
+ constraints 是列的约束条件

例如创建emp表：

```mysql
mysql> CREATE TABLE emp(ename varchar(10),hiredate date,sal decimal(10,2),deptno int(2));
Query OK, 0 rows affected (0.06 sec)
```

##### 查看表定义

```mysql
mysql> DESC emp;
+----------+---------------+------+-----+---------+-------+
| Field    | Type          | Null | Key | Default | Extra |
+----------+---------------+------+-----+---------+-------+
| ename    | varchar(10)   | YES  |     | NULL    |       |
| hiredate | date          | YES  |     | NULL    |       |
| sal      | decimal(10,2) | YES  |     | NULL    |       |
| deptno   | int(2)        | YES  |     | NULL    |       |
+----------+---------------+------+-----+---------+-------+
4 rows in set (0.00 sec)
```

##### 查看创建表的SQL语句

```mysql
mysql> show create table emp \G;
*************************** 1. row ***************************
       Table: emp
Create Table: CREATE TABLE `emp` (
  `ename` varchar(10) DEFAULT NULL,
  `hiredate` date DEFAULT NULL,
  `sal` decimal(10,2) DEFAULT NULL,
  `deptno` int(2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8
1 row in set (0.00 sec)

ERROR:
No query specified
```

“\G”选项的含义是使得记录能够按照字段竖向排列，以便更好地显示内容较长得记录。

##### 删除表

```mysql
DROP TABLE tablename
```

例如 删除emp表

```mysql
mysql> drop table emp;
Query OK, 0 rows affected (0.04 sec)
```

##### 修改表

表结构修改使用 alter table语句，以下是一些常用得命令。

1. 修改表类型，语法如下

   ```mysql
   ALTER TABLE tablename MODIFY [COLUMN] column_definition [FIRST] [AFTER col_name]
   ```

   例如,修改emp得ename字段定义，将varchar(10)改为varchar(20)

   ```mysql
   mysql> desc emp;
   +----------+---------------+------+-----+---------+-------+
   | Field    | Type          | Null | Key | Default | Extra |
   +----------+---------------+------+-----+---------+-------+
   | ename    | varchar(10)   | YES  |     | NULL    |       |
   | hiredate | date          | YES  |     | NULL    |       |
   | sal      | decimal(10,2) | YES  |     | NULL    |       |
   | deptno   | int(2)        | YES  |     | NULL    |       |
   +----------+---------------+------+-----+---------+-------+
   4 rows in set (0.00 sec)
   
   mysql> alter table emp MODIFY ename varchar(20);
   Query OK, 0 rows affected (0.02 sec)
   
   mysql> desc emp;
   +----------+---------------+------+-----+---------+-------+
   | Field    | Type          | Null | Key | Default | Extra |
   +----------+---------------+------+-----+---------+-------+
   | ename    | varchar(20)   | YES  |     | NULL    |       |
   | hiredate | date          | YES  |     | NULL    |       |
   | sal      | decimal(10,2) | YES  |     | NULL    |       |
   | deptno   | int(2)        | YES  |     | NULL    |       |
   +----------+---------------+------+-----+---------+-------+
   4 rows in set (0.00 sec)
   ```

2. 增加表字段

   ```mysql
   ALTER TABLE tablename ADD [COLUMN] column_definition [FIRST | AFTER col_umn]
   ```

   例如，在表emp中新增加字段age，类型为int(3)

   ```mysql
   mysql> desc emp;
   +----------+---------------+------+-----+---------+-------+
   | Field    | Type          | Null | Key | Default | Extra |
   +----------+---------------+------+-----+---------+-------+
   | ename    | varchar(20)   | YES  |     | NULL    |       |
   | hiredate | date          | YES  |     | NULL    |       |
   | sal      | decimal(10,2) | YES  |     | NULL    |       |
   | deptno   | int(2)        | YES  |     | NULL    |       |
   +----------+---------------+------+-----+---------+-------+
   4 rows in set (0.00 sec)
   
   mysql> alter table emp ADD COLUMN age int(3);
   Query OK, 0 rows affected (0.04 sec)
   Records: 0  Duplicates: 0  Warnings: 0
   
   mysql> desc emp;
   +----------+---------------+------+-----+---------+-------+
   | Field    | Type          | Null | Key | Default | Extra |
   +----------+---------------+------+-----+---------+-------+
   | ename    | varchar(20)   | YES  |     | NULL    |       |
   | hiredate | date          | YES  |     | NULL    |       |
   | sal      | decimal(10,2) | YES  |     | NULL    |       |
   | deptno   | int(2)        | YES  |     | NULL    |       |
   | age      | int(3)        | YES  |     | NULL    |       |
   +----------+---------------+------+-----+---------+-------+
   5 rows in set (0.00 sec)
   ```

3. 删除表字段

   ```mysql
   ALTER TABLE tablename DROP [COLUMN] col_name
   ```

   例如，将字段age删除掉

   ```mysql
   mysql> alter table emp DROP COLUMN age;
   Query OK, 0 rows affected (0.08 sec)
   Records: 0  Duplicates: 0  Warnings: 0
   
   mysql> desc emp;
   +----------+---------------+------+-----+---------+-------+
   | Field    | Type          | Null | Key | Default | Extra |
   +----------+---------------+------+-----+---------+-------+
   | ename    | varchar(20)   | YES  |     | NULL    |       |
   | hiredate | date          | YES  |     | NULL    |       |
   | sal      | decimal(10,2) | YES  |     | NULL    |       |
   | deptno   | int(2)        | YES  |     | NULL    |       |
   +----------+---------------+------+-----+---------+-------+
   4 rows in set (0.00 sec)
   ```

4. 字段改名，语法如下

   ```mysql
   ALTER TABLE tablename CHANGE [COLUMN] old_col_name column_definition [FIRST|AFTER col_name]
   ```

   例如，将age改名为age1，同时修改字段类型为int(4)

   ```mysql
   mysql> desc emp;
   +----------+---------------+------+-----+---------+-------+
   | Field    | Type          | Null | Key | Default | Extra |
   +----------+---------------+------+-----+---------+-------+
   | ename    | varchar(20)   | YES  |     | NULL    |       |
   | hiredate | date          | YES  |     | NULL    |       |
   | sal      | decimal(10,2) | YES  |     | NULL    |       |
   | deptno   | int(2)        | YES  |     | NULL    |       |
   | age      | int(3)        | YES  |     | NULL    |       |
   +----------+---------------+------+-----+---------+-------+
   5 rows in set (0.00 sec)
   
   mysql> alter table emp CHANGE age age1 int(4);
   Query OK, 0 rows affected (0.02 sec)
   Records: 0  Duplicates: 0  Warnings: 0
   
   mysql> desc emp;
   +----------+---------------+------+-----+---------+-------+
   | Field    | Type          | Null | Key | Default | Extra |
   +----------+---------------+------+-----+---------+-------+
   | ename    | varchar(20)   | YES  |     | NULL    |       |
   | hiredate | date          | YES  |     | NULL    |       |
   | sal      | decimal(10,2) | YES  |     | NULL    |       |
   | deptno   | int(2)        | YES  |     | NULL    |       |
   | age1     | int(4)        | YES  |     | NULL    |       |
   +----------+---------------+------+-----+---------+-------+
   5 rows in set (0.00 sec)
   ```

   注意：change和modify都可以修改表的定义，不同的是change后面需要写两次列名，不方便。但是change的优点是可以修改列名称，modify则不能

5. 修改字段排列顺序

   前面介绍的字段增加和修改语法（ADD/CHANGE/MODIFY）中，都有一个可选项 first|after column_name,这个选项可以用来修改字段在表中的位置，ADD增加的新字段默认是加在表的最后位置，而CHANGE/MODIFY默认都不会改变字段的位置。

   ```mysql
   mysql> alter table emp add birth date after ename;
   Query OK, 0 rows affected (0.08 sec)
   Records: 0  Duplicates: 0  Warnings: 0
   
   mysql> desc emp;
   +----------+---------------+------+-----+---------+-------+
   | Field    | Type          | Null | Key | Default | Extra |
   +----------+---------------+------+-----+---------+-------+
   | ename    | varchar(20)   | YES  |     | NULL    |       |
   | birth    | date          | YES  |     | NULL    |       |
   | hiredate | date          | YES  |     | NULL    |       |
   | sal      | decimal(10,2) | YES  |     | NULL    |       |
   | deptno   | int(2)        | YES  |     | NULL    |       |
   | age1     | int(4)        | YES  |     | NULL    |       |
   +----------+---------------+------+-----+---------+-------+
   6 rows in set (0.00 sec)
   
   ## 修改字段age，将它放在最前面：
   mysql> alter table emp modify age1 int(4) first;
   Query OK, 0 rows affected (0.08 sec)
   Records: 0  Duplicates: 0  Warnings: 0
   mysql> desc emp;
   +----------+---------------+------+-----+---------+-------+
   | Field    | Type          | Null | Key | Default | Extra |
   +----------+---------------+------+-----+---------+-------+
   | age1     | int(4)        | YES  |     | NULL    |       |
   | ename    | varchar(20)   | YES  |     | NULL    |       |
   | birth    | date          | YES  |     | NULL    |       |
   | hiredate | date          | YES  |     | NULL    |       |
   | sal      | decimal(10,2) | YES  |     | NULL    |       |
   | deptno   | int(2)        | YES  |     | NULL    |       |
   +----------+---------------+------+-----+---------+-------+
   6 rows in set (0.00 sec)
   ```

6. 更改表名，语法如下

   ```mysql
   ALTER TABLE tablename RENAME [TO] new_tablename
   ```

   例如，将emp改名为emp1

   ```mysql
   mysql> alter table emp rename emp1;
   Query OK, 0 rows affected (0.03 sec)
   ```



### DML 语句

DML操作是指对数据库中表记录的操作，主要包括表记录的插入（insert）、更新（update）、删除（delete）和查询（select）

1. 插入记录

   ```mysql
   # 插入一条记录
   INSERT INTO tablename (filed1,filed2,...,filedn) values(value1,value2,...,valuen);
   # 插入多条记录
   INSERT INTO tablename (filed1,filed2,...,filedn) values(value1,value2,...,valuen),(value1,value2,...,valuen);
   # 不指定列 插入所有列
   INSERT INTO tablename values(value1,value2,...,valuen);
   ```

2. 更新记录

   语法

   ```mysql
   UPDATE tablename SET filed1=value1,filed2=value2,...,filedn=valuen [WHERE CONDITION]
   ```

   更新多个表数据

   ```mysql
   UPDATE t1,t2,...,tn set t1.filed1=expr1,t2.filed1=expr1,tn.filedn=exprn [WHERE CONDITION]
   ```

   注意：多表更新的语法更多的用于根据一个表的字段来动态地更新零一表的字段。

3. 删除记录

   语法：

   ```mysql
   DELETE FROM tablename [WHERE CONDITION]
   ```

   删除多张表

   ```mysql
   delete t1,t2,..,tn from t1,t2,...,tn [WHERE CONDITION]
   
   # 例如
   mysql> delete a,b from table1 a left join table2 b on a.id=b.bid where a.id=10;
   Query OK, 0 rows affected (0.00 sec)
   ```

4. 查询记录

   ```mysql
   SELECT * FROM tablename [where condition]
   ```

   **查询不重复**

   ```mysql
   select distinct * from tablename [where condition]
   ```

   **条件查询**

   上面的例子中，where后面的条件是一个字段的=比较，除了=外，还可以使用>,<,>=,<=,!= 等比较运算符；多个条件之间还可以使用or、and等逻辑运算符进行多条件联合查询

   **排序和限制**

   desc 降序，asc 升序

   ```mysql
   # ORDER BY
   select * from tablename [where condition] [order by field1 [desc|asc],field2 [desc|asc],..,filedn [desc|asc]]
   ```

   **limit**

   ```mysql
   select * from tablename order by field limt n1,n2
   ```

   n1 是偏移量（起始是0），n2 是检索多少条数据

   **聚合**

   ```mysql
   SELECT [field1,field2,..,fieldn] fun_name FROM tablename [WHERE where_contition]
   [GROUP BY field1,..,filedn]
   [WITH ROLLUP]
   [HAVING where_contition]
   ```

   + fun_name 表示要做的聚合操作，也就是聚合函数，常用的有 sum、count(*),avg,max,min.
   + GROUP BY 关键字表示要进行分类聚合的字段
   + WITH ROLLUP 是可选参数，表明是否对分类聚合后的结果进行再汇总。
   + HAVING 关键字表示对分类后的结果进行再次条件过滤

   **表连接**

   1. 外连接
      + 左连接：包含所有的左边表中的记录甚至是右边表中没有和它匹配的记录。
      + 右连接：包含所有右边表中的记录甚至是左边表中没有和它匹配的记录。
   2. 内连接

   **子查询**

   用于子查询的关键字主要包括in,not in,=,!=,exists,not exists等

   注意：表连接在很多情况下用于优化子查询

   **记录联合**

   将多个查询的结果一起显示出来使用union,union all

   ```mysql
   SELECT * FROM t1
   UNION | UNION ALL
   select * from t2
   ...
   UNION | UNION ALL
   select * from tn;
   ```

   UNION ALL:  是把结果集直接合并在一起

   UNION：是将UNION  ALL 后的结果进行一次DISTINCT，去除重复记录后的结果

### DCL 语句

DCL语句主要是DBA用来管理系统中的对象权限时使用。

例子：

创建一个数据库用户 zl，具有对sakila数据库中所有表的SELECT/INSERT权限

```mysql
grant select,insert on sakila.* to 'zl' @‘localhost’ identified by '123'
```

由于权限变更，需要将zl的权限变更，收回INSERT，只能对数据进行SELECT操作

```mysql
revoke insert on sakila.* from 'zl'@'localhost';
```

### 帮助和使用

再mysql使用过程中，可能会遇到以下问题：

1. 某个操作语法忘了，如何快速查找？
2. 如何快速知道当前版本上某个字段类型的取值范围？
3. 当前版本都支持那些函数？
4. 当前版本是否支持某个功能？

### 按照层次看帮助

如果不知道帮助能够提供些什么，那么就可以用"? contents" 命令来显示所有可供查询的分类，如下例所示：

```mysq
mysql> ? contents;
You asked for help about help category: "Contents"
For more information, type 'help <item>', where <item> is one of the following
categories:
   Account Management
   Administration
   Components
   Compound Statements
   Data Definition
   Data Manipulation
   Data Types
   Functions
   Functions and Modifiers for Use with GROUP BY
   Geographic Features
   Help Metadata
   Language Structure
   Plugins
   Storage Engines
   Table Maintenance
   Transactions
   User-Defined Functions
   Utility
```

对于列出的分类，可以使用"? 类别名称"的方式针对用户感兴趣的内容做进一步的查看。例如，想看看MySQL中都支持哪些数据类型，可以执行“ ? data type” 命令：

```mysql
mysql> ? data types;
You asked for help about help category: "Data Types"
For more information, type 'help <item>', where <item> is one of the following
topics:
   AUTO_INCREMENT
   BIGINT
   BINARY
   BIT
   BLOB
   BLOB DATA TYPE
   BOOLEAN
   CHAR
   CHAR BYTE
   DATE
   DATETIME
   DEC
   DECIMAL
   DOUBLE
   DOUBLE PRECISION
   ENUM
   FLOAT
   INT
   INTEGER
   LONGBLOB
   LONGTEXT
   MEDIUMBLOB
   MEDIUMINT
   MEDIUMTEXT
   SET DATA TYPE
   SMALLINT
   TEXT
   TIME
   TIMESTAMP
   TINYBLOB
   TINYINT
   TINYTEXT
   VARBINARY
   VARCHAR
   YEAR DATA TYPE
```

上面列出了此版本支持的所有数据类型，如果想知道int类型的具体介绍，也可以利用上面的方法进一步查看：

```mysql
mysql> ? int
Name: 'INT'
Description:
INT[(M)] [UNSIGNED] [ZEROFILL]

A normal-size integer. The signed range is -2147483648 to 2147483647.
The unsigned range is 0 to 4294967295.

URL: http://dev.mysql.com/doc/refman/8.0/en/numeric-type-overview.html
```

### 快速查阅帮助

在实际应用中，如果需要快速查阅某项语法时，可以使用关键字进行快速查询。例如，想知道show命令都能看些什么，可以用以下命令：

```mysql
mysql> ? show
Name: 'SHOW'
Description:
SHOW has many forms that provide information about databases, tables,
columns, or status information about the server. This section describes
those following:

SHOW {BINARY | MASTER} LOGS
SHOW BINLOG EVENTS [IN 'log_name'] [FROM pos] [LIMIT [offset,] row_count]
SHOW CHARACTER SET [like_or_where]
SHOW COLLATION [like_or_where]
SHOW [FULL] COLUMNS FROM tbl_name [FROM db_name] [like_or_where]
SHOW CREATE DATABASE db_name
SHOW CREATE EVENT event_name
SHOW CREATE FUNCTION func_name
SHOW CREATE PROCEDURE proc_name
SHOW CREATE TABLE tbl_name
SHOW CREATE TRIGGER trigger_name
SHOW CREATE VIEW view_name
SHOW DATABASES [like_or_where]
SHOW ENGINE engine_name {STATUS | MUTEX}
SHOW [STORAGE] ENGINES
SHOW ERRORS [LIMIT [offset,] row_count]
SHOW EVENTS
SHOW FUNCTION CODE func_name
SHOW FUNCTION STATUS [like_or_where]
SHOW GRANTS FOR user
SHOW INDEX FROM tbl_name [FROM db_name]
SHOW MASTER STATUS
SHOW OPEN TABLES [FROM db_name] [like_or_where]
SHOW PLUGINS
SHOW PROCEDURE CODE proc_name
SHOW PROCEDURE STATUS [like_or_where]
SHOW PRIVILEGES
SHOW [FULL] PROCESSLIST
SHOW PROFILE [types] [FOR QUERY n] [OFFSET n] [LIMIT n]
SHOW PROFILES
SHOW RELAYLOG EVENTS [IN 'log_name'] [FROM pos] [LIMIT [offset,] row_count]
SHOW SLAVE HOSTS
SHOW SLAVE STATUS [FOR CHANNEL channel]
SHOW [GLOBAL | SESSION] STATUS [like_or_where]
SHOW TABLE STATUS [FROM db_name] [like_or_where]
SHOW [FULL] TABLES [FROM db_name] [like_or_where]
SHOW TRIGGERS [FROM db_name] [like_or_where]
SHOW [GLOBAL | SESSION] VARIABLES [like_or_where]
SHOW WARNINGS [LIMIT [offset,] row_count]

like_or_where:
    LIKE 'pattern'
  | WHERE expr

If the syntax for a given SHOW statement includes a LIKE 'pattern'
part, 'pattern' is a string that can contain the SQL % and _ wildcard
characters. The pattern is useful for restricting statement output to
matching values.

Several SHOW statements also accept a WHERE clause that provides more
flexibility in specifying which rows to display. See
http://dev.mysql.com/doc/refman/8.0/en/extended-show.html.

URL: http://dev.mysql.com/doc/refman/8.0/en/show.html
```



查看创建数据库的create database 语法

```mysql
mysql> ? create database
Name: 'CREATE DATABASE'
Description:
Syntax:
CREATE {DATABASE | SCHEMA} [IF NOT EXISTS] db_name
    [create_specification] ...

create_specification:
    [DEFAULT] CHARACTER SET [=] charset_name
  | [DEFAULT] COLLATE [=] collation_name

CREATE DATABASE creates a database with the given name. To use this
statement, you need the CREATE privilege for the database. CREATE
SCHEMA is a synonym for CREATE DATABASE.

URL: http://dev.mysql.com/doc/refman/8.0/en/create-database.html

```



### 查询元数据信息

在日常工作中，我们经常会遇到类似下面的应用场景：

1. 删除数据库test1下所有前缀为tmp的表
2. 将数据库test1下所有存储引擎为myisam的表改为innodb

对于这类需求，在mysql 5.0之前只能通过 show table,show create table  或者show table status 等命令来得到指定数据库下的表名和存储引擎，但通过这些命令显示的内容有限且不适合进行字符串的批量编辑。如果表很多，则操作起来非常低效。

MySQL5.0之后提供了一个新的数据库 information_schema,用来记录MySQL中的元数据信息。元数据指的是数据的数据，比如，表名、列名、列类型、索引名等表的各种属性名称。这个库比较特殊，它是一个虚拟数据库，物理上并不存在相关的目录和文件；库里 show tables显示的各种 “表” 也并不是实际存在的物理表，而全部是视图。对于上面的两个需求，可以简单的通过两个命令得到需要的SQL语句：

```mysql
select concat('drop table test1.',table_name, ';') from tables where table_schema='test1' and table_name like 'tmp%'
```

```mysql
select concat('alter table test1.',table_name, 'engine=innodb;') from tables where table_schema='test1' and engine='myisam';
```

下面列出一些比较常用的视图

1. SCHEMATA: 该表提供了当前MySQL实例中所有数据库的信息， `show databases; `的结果取之此表。
2. TABLES：该表提供了关于数据库中的表的信息（包括视图），详细表述了某个表属于哪个schema、表类型、表引擎、创建时间等信息。show tables from schemaname 的结果取之此表
3. COLUMNS：该表提供了表中的列信息，详细表述了某张表的所有列以及每个列的信息。`show columns from test.emp;` 的结果取之此表
4. STATISTICS：该表提供了关于表索引的信息。`show index from schemaname.tablename`的结果取之此表


