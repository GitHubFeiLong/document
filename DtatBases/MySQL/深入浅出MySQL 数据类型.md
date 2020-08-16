# MySQL支持的数据类型

## 数值类型

MySQL 支持所有标准SQL 中的数值类型，其中包括严格数值类型（INTEGER、SMALLINT、DECIMAL 和NUMERIC），以及近似数值数据类型（FLOAT、REAL 和DOUBLE PRECISION），并在此基础上做了扩展。扩展后增加了TINYINT、MEDIUMINT 和BIGINT 这3 种长度不同的整型，并增加了BIT 类型，用来存放位数据。表3-1 中列出了MySQL 5.0 中支持的所有数值类型，其中INT 是INTEGER 的同名词，DEC 是DECIMAL 的同名词。

1. **整数类型**

   <table>
       <thead>
       	<tr>
           	<td>整数类型</td>
               <td>字节</td>
               <td>最小值</td>
               <td>最大值</td>
           </tr>
       </thead>
       <tbody>
       	<tr>
           	<td>TINYINT</td>
               <td>1</td>
               <td>有符号 -128<br>无符号 0
   			</td>
               <td>有符号 127<br>无符号 255</td>
           </tr>
           <tr>
           	<td>SMALLINT</td>
               <td>2</td>
               <td>有符号 -32768<br>无符号 0
   			</td>
               <td>有符号 32767<br>无符号 65535</td>
           </tr>
           <tr>
           	<td>MEDIUMINT </td>
               <td>3</td>
               <td>有符号 -8388608<br>无符号 0
   			</td>
               <td>有符号 8388607<br>无符号 1677215</td>
           </tr>
           <tr>
           	<td>INT、INTEGER </td>
               <td>4</td>
               <td>有符号 -2147483648<br>无符号 0
   			</td>
               <td>有符号 2147483647<br>无符号 4294967295</td>
           </tr>
            <tr>
           	<td>BIGINT </td>
               <td>8</td>
               <td>有符号 -9223372036854775808
   <br>无符号 0
   			</td>
               <td>有符号 9223372036854775807
   <br>无符号 18446744073709551615
   </td>
           </tr>
       </tbody>
   </table>

2. **浮点数类型**

   <table>
        <thead>
       	<tr>
           	<td>浮点数类型</td>
               <td>字节</td>
               <td>最小值</td>
               <td>最大值</td>
           </tr>
       </thead>
       <tbody>
       	<tr>
           	<td>FLOAT </td>
               <td>4</td>
               <td>±1.175494351E-38 </td>
               <td>±3.402823466E+38
   </td>
           </tr>
           <tr>
           	<td>DOUBLE  </td>
               <td>8 </td>
               <td>±2.2250738585072014E-308 </td>
               <td>±1.7976931348623157E+308
   </td>
           </tr>
       </tbody>
   </table>

3. **定点数类型**

   <table>
        <thead>
       	<tr>
           	<td>定点数类型</td>
               <td>字节</td>
               <td>描述</td>
           </tr>
       </thead>
       <tbody>
       	<tr>
           	<td>DEC(M,D)，DECIMAL(M,D) </td>
               <td>M+2 </td>
               <td>最大取值范围与DOUBLE 相同，给定DECIMAL 的有效取值范围由M 和D
   决定
   			</td>
           </tr>
       </tbody>
   </table>

4. **位类型**

   <table>
        <thead>
       	<tr>
           	<td>位类型</td>
               <td>字节</td>
               <td>最小值</td>
               <td>最大值</td>
           </tr>
       </thead>
       <tbody>
       	<tr>
           	<td>BIT(M) </td>
               <td>1~8</td>
               <td>BIT(1)</td>
               <td>BIT(64)</td>
           </tr>
       </tbody>
   </table>



### 整数类型

在整数类型中，按照取值范围和存储方式不同，分为tinyint、smallint、mediumint、int、bigint 这5 个类型。如果超出类型范围的操作，会发生“Out of range”错误提示。为了避免此类问题发生，在选择数据类型时要根据应用的实际情况确定其取值范围，最后根据确定的结果慎重选择数据类型。对于整型数据，MySQL 还支持在类型名称后面的小括号内指定显示宽度，例如int(5)表示当数值宽度小于5 位的时候在数字前面填满宽度，如果不显示指定宽度则默认为int(11)。一般配合zerofill 使用，顾名思义，zerofill 就是用“0”填充的意思，也就是在数字位数不够的空间用字符“0”填满。以下几个例子分别描述了填充前后的区别。

（1）创建表t1，有id1 和id2 两个字段，指定其数值宽度分别为int 和int(5)。

```mysql
mysql> create table t1 (id1 int,id2 int(5));
Query OK, 0 rows affected (0.06 sec)

mysql> desc t1;
+-------+---------+------+-----+---------+-------+
| Field | Type    | Null | Key | Default | Extra |
+-------+---------+------+-----+---------+-------+
| id1   | int(11) | YES  |     | NULL    |       |
| id2   | int(5)  | YES  |     | NULL    |       |
+-------+---------+------+-----+---------+-------+
2 rows in set (0.00 sec)
```

（2）在id1 和id2 中都插入数值1，可以发现格式没有异常。

```mysql
mysql> insert into t1 values(1,1);
Query OK, 1 row affected (0.01 sec)

mysql> select * from t1;
+------+------+
| id1  | id2  |
+------+------+
|    1 |    1 |
+------+------+
1 row in set (0.00 sec)
```

（3）分别修改id1 和id2 的字段类型，加入**zerofill** 参数：

```mysql
mysql> alter table t1 modify id1 int zerofill;
Query OK, 1 row affected (0.19 sec)
Records: 1  Duplicates: 0  Warnings: 0

mysql> alter table t1 modify id2 int(5) zerofill;
Query OK, 1 row affected (0.09 sec)
Records: 1  Duplicates: 0  Warnings: 0

mysql> select * from t1;
+------------+-------+
| id1        | id2   |
+------------+-------+
| 0000000001 | 00001 |
+------------+-------+
1 row in set (0.00 sec)
```

可以发现，在数值前面用字符“0”填充了剩余的宽度。大家可能会有所疑问，设置了宽度限制后，如果插入大于宽度限制的值，会不会截断或者插不进去报错？答案是肯定的：不会对插入的数据有任何影响，还是按照类型的实际精度进行保存，这是，宽度格式实际已经没有意义，左边不会再填充任何的“0”字符。下面在表t1 的字段id1 中插入数值1，id2 中插入数值1111111，位数为7，大于id2 的显示位数5，再观察一下测试结果：

```mysql
mysql> insert into t1 values(1,1111111);
Query OK, 1 row affected (0.01 sec)

mysql> select * from t1;
+------------+---------+
| id1        | id2     |
+------------+---------+
| 0000000001 |   00001 |
| 0000000001 | 1111111 |
+------------+---------+
2 rows in set (0.00 sec)
```

很显然，如上面所说，id2 中显示了正确的数值，并没有受宽度限制影响。所有的整数类型都有一个可选属性**UNSIGNED**（无符号），如果需要在字段里面保存非负数或者需要较大的上限值时，可以用此选项，它的取值范围是正常值的下限取0，上限取原值的2 倍，例如，**tinyint** 有符号范围是-128～+127，而无符号范围是0～255。如果一个列指定为zerofill，则MySQL 自动为该列添加UNSIGNED 属性。另外，整数类型还有一个属性：**AUTO_INCREMENT**。在需要产生唯一标识符或顺序值时，可利用此属性，这个属性只用于整数类型。AUTO_INCREMENT 值一般从1 开始，每行增加1。在插入NULL 到一个AUTO_INCREMENT 列时MySQL 插入一个比该列中当前最大值大1 的值。一个表中最多只能有一个AUTO_INCREMENT列。对于任何想要使用AUTO_INCREMENT 的列，应该定义为**NOT NULL**，并定义为**PRIMARY KEY** 或定义为**UNIQUE** 键。

例如，可按下列任何一种方式定义AUTO_INCREMENT 列：

```mysql
CREATE TABLE AI (ID INT AUTO_INCREMENT NOT NULL PRIMARY KEY);
CREATE TABLE AI(ID INT AUTO_INCREMENT NOT NULL ,PRIMARY KEY(ID));
CREATE TABLE AI (ID INT AUTO_INCREMENT NOT NULL ,UNIQUE(ID));
```



### 浮点数类型和定点数类型

对于小数的表示，MySQL 分为两种方式：**浮点数**和**定点数**。浮点数包括**float**（单精度）和**double**（双精度），而定点数则只有**decimal** 一种表示。定点数在MySQL 内部以字符串形式存放，比浮点数更精确，适合用来表示货币等精度高的数据。浮点数和定点数都可以用类型名称后加“(M,D)”的方式来进行表示，“(M,D)”表示该值一共显示M 位数字（整数位+小数位），其中D 位位于小数点后面，**M 和D 又称为精度和标度**。例如，定义为float(7,4)的一个列可以显示为-999.9999。MySQL 保存值时进行四舍五入，因此如果在float(7,4)列内插入999.00009，近似结果是999.0001。值得注意的是，浮点
数后面跟“(M,D)”的用法是非标准用法，如果要用于数据库的迁移，则最好不要这么使用。float 和double 在不指定精度时，默认会按照实际的精度（由实际的硬件和操作系统决定）来显示，而decimal 在不指定精度时，默认的整数位为10，默认的小数位为0。
下面通过一个例子来比较float、double 和decimal 三者之间的不同。

（1）创建测试表，分别将id1、id2、id3 字段设置为float(5,2)、double(5,2)、decimal(5,2)。

```mysql
mysql> create table t1 (
    id1 float(5,2) default null,
    id2 double(5,2) default null, 
    id3 decimal(5,2) default null
);
Query OK, 0 rows affected (0.06 sec)
```

（2）往id1、id2 和id3 这3 个字段中插入数据1.23。

```mysql
mysql> insert into t1 values(1.23,1.23,1.23);
Query OK, 1 row affected (0.01 sec)

mysql> select * from t1;
+------+------+------+
| id1  | id2  | id3  |
+------+------+------+
| 1.23 | 1.23 | 1.23 |
+------+------+------+
1 row in set (0.00 sec)
```

可以发现，数据都正确地插入了表t1。

（3）再向id1 和id2 字段中插入数据1.234，而id3 字段中仍然插入1.23。

```mysql
mysql> insert into t1 values(1.234,1.234,1.23);
Query OK, 1 row affected (0.01 sec)

mysql> select * from t1;
+------+------+------+
| id1  | id2  | id3  |
+------+------+------+
| 1.23 | 1.23 | 1.23 |
| 1.23 | 1.23 | 1.23 |
+------+------+------+
2 rows in set (0.00 sec)
```

可以发现，id1、id2、id3 都插入了表t1，但是id1 和id2 由于标度的限制，舍去了最后一位，数据变为了1.23。

（4）同时向id1、id2、id3 字段中都插入数据1.234。

```mysql
mysql> insert into t1 values(1.234,1.234,1.234);
Query OK, 1 row affected, 1 warning (0.01 sec)

mysql> show warnings;
+-------+------+------------------------------------------+
| Level | Code | Message                                  |
+-------+------+------------------------------------------+
| Note  | 1265 | Data truncated for column 'id3' at row 1 |
+-------+------+------------------------------------------+
1 row in set (0.00 sec)

mysql> select * from t1;
+------+------+------+
| id1 | id2 | id3 |
+------+------+------+
| 1.23 | 1.23 | 1.23 |
| 1.23 | 1.23 | 1.23 |
| 1.23 | 1.23 | 1.23 |
+------+------+------+
3 rows in set (0.00 sec)
```

此时发现，虽然数据都插入进去，但是系统出现了一个warning，报告id3 被截断。如果是在传统的SQLMode下，这条记录是无法插入的。

（5）将id1、id2、id3 字段的精度和标度全部去掉，再次插入数据1.23。

```mysql
mysql> alter table t1 modify id1 float;
Query OK, 0 rows affected (0.01 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> alter table t1 modify id2 double;
Query OK, 0 rows affected (0.04 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> alter table t1 modify id3 decimal;
Query OK, 3 rows affected, 3 warnings (0.10 sec)
Records: 3  Duplicates: 0  Warnings: 3

mysql> desc t1;
+-------+---------------+------+-----+---------+-------+
| Field | Type          | Null | Key | Default | Extra |
+-------+---------------+------+-----+---------+-------+
| id1   | float         | YES  |     | NULL    |       |
| id2   | double        | YES  |     | NULL    |       |
| id3   | decimal(10,0) | YES  |     | NULL    |       |
+-------+---------------+------+-----+---------+-------+
3 rows in set (0.00 sec)

mysql> insert into t1 values(1.234,1.234,1.234);
Query OK, 1 row affected, 1 warning (0.01 sec)

mysql> show warnings;
+-------+------+------------------------------------------+
| Level | Code | Message                                  |
+-------+------+------------------------------------------+
| Note  | 1265 | Data truncated for column 'id3' at row 1 |
+-------+------+------------------------------------------+
1 row in set (0.00 sec)

mysql> select * from t1;
+-------+-------+------+
| id1   | id2   | id3  |
+-------+-------+------+
|  1.23 |  1.23 |    1 |
|  1.23 |  1.23 |    1 |
|  1.23 |  1.23 |    1 |
| 1.234 | 1.234 |    1 |
+-------+-------+------+
4 rows in set (0.00 sec)
```

这个时候，可以发现id1、id2 字段中可以正常插入数据，而id3 字段的小数位被截断。

上面这个例子验证了上面提到的浮点数如果不写精度和标度，则会按照实际精度值显示，如果有精度和标度，则会自动将四舍五入后的结果插入，系统不会报错；定点数如果不写精度和标度，则按照默认值decimal(10,0)来进行操作，并且如果数据超越了精度和标度值，系统则会报错。



### 位类型

对于**BIT**（位）类型，用于存放位字段值，BIT(M)可以用来存放多位二进制数，M 范围从1～64，如果不写则默认为1 位。对于位字段，直接使用SELECT 命令将不会看到结果，可以用**bin()**（显示为二进制格式）或者**hex()**（显示为十六进制格式）函数进行读取。
下面的例子中，对测试表t2 中的bit 类型字段id 做insert 和select 操作，这里重点观察一下select 的结果：

```mysql
mysql> create table t2(id bit(1));
Query OK, 0 rows affected (0.05 sec)

mysql> desc t2;
+-------+--------+------+-----+---------+-------+
| Field | Type   | Null | Key | Default | Extra |
+-------+--------+------+-----+---------+-------+
| id    | bit(1) | YES  |     | NULL    |       |
+-------+--------+------+-----+---------+-------+
1 row in set (0.00 sec)

mysql> insert into t2 values(1);
Query OK, 1 row affected (0.01 sec)

mysql> select * from t2;
+------+
| id   |
+------+
|     |
+------+
1 row in set (0.00 sec)

mysql> select bin(id),hex(id) from t2;
+---------+---------+
| bin(id) | hex(id) |
+---------+---------+
| 1       | 1       |
+---------+---------+
1 row in set (0.00 sec)
```

结果可以正常显示为二进制数字和十六进制数字。

数据插入bit 类型字段时，首先转换为二进制，如果位数允许，将成功插入；如果位数小于实际定义的位数，则插入失败，下面的例子中，在t2 表插入数字2，因为它的二进制码是“10”，而id 的定义是bit(1)，将无法进行插入：

```mysql
mysql> insert into t2 values(2);
ERROR 1406 (22001): Data too long for column 'id' at row 1
```

将ID 定义修改为bit(2)后，重新插入，插入成功：

```mysql
mysql> alter table t2 modify id bit(2);
Query OK, 1 row affected (0.10 sec)
Records: 1  Duplicates: 0  Warnings: 0

mysql> insert into t2 values(2);
Query OK, 1 row affected (0.01 sec)

mysql> select bin(id),hex(id) from t2;
+---------+---------+
| bin(id) | hex(id) |
+---------+---------+
| 1       | 1       |
| 10      | 2       |
+---------+---------+
2 rows in set (0.00 sec)
```



## 日期时间类型

MySQL 中有多种数据类型可以用于日期和时间的表示，不同的版本可能有所差异，列出了MySQL 5.0 中所支持的日期和时间类型。

<table>
    <thead>
    	<tr>
        	<td>日期和时间类型</td>
            <td>字节</td>
            <td>最小值</td>
            <td>最大值</td>
        </tr>
    </thead>
    <tbody>
    	<tr>
        	<td>DATE</td>
            <td>4</td>
            <td>1000-01-01 </td>
            <td>9999-12-31</td>
        </tr>
        <tr>
        	<td>DATETIME</td>
            <td>8</td>
            <td>1000-01-01 00:00:00</td>
            <td>9999-12-31 23:59:59</td>
        </tr>
        <tr>
        	<td>TIMESTAMP </td>
            <td>4</td>
            <td>19700101080001 </td>
            <td>2038 年的某个时刻</td>
        </tr>
        <tr>
        	<td>TIME </td>
            <td>3</td>
            <td>-838:59:59 </td>
            <td>838:59:59</td>
        </tr>
        <tr>
        	<td>YEAR </td>
            <td>1</td>
            <td>1901</td>
            <td>2155</td>
        </tr>
    </tbody>
</table>

这些数据类型的主要区别如下：

+ 如果要用来表示年月日，通常用DATE 来表示。
+ 如果要用来表示年月日时分秒，通常用DATETIME 表示。
+ 如果只用来表示时分秒，通常用TIME 来表示。
+ 如果需要经常插入或者更新日期为当前系统时间，则通常使用TIMESTAMP 来表示。TIMESTAMP 值返回后显示为“YYYY-MM-DD HH:MM:SS”格式的字符串，显示宽度固定为19 个字符。如果想要获得数字值，应在TIMESTAMP 列添加+0。
+ 如果只是表示年份，可以用YEAR 来表示，它比DATE 占用更少的空间。YEAR 有2 位或4 位格式的年。默认是4 位格式。在4 位格式中，允许的值是1901～2155 和0000。在2 位格式中，允许的值是70～69，表示从1970～2069 年。MySQL 以YYYY 格式显示YEAR值。



每种日期时间类型都有一个有效值范围，如果超出这个范围，在默认的SQLMode 下，系统会进行错误提示，并将以零值来进行存储。不同日期类型零值的表示如下

<table>
	<thead>
    	<tr>
        	<td>数据类型</td>
            <td>零值表示</td>
        </tr>
    </thead> 
    <tbody>
    	<tr>
        	<td>DATETIME </td>
            <td>0000-00-00 00:00:00</td>
        </tr>
        <tr>
        	<td>DATE</td>
            <td>0000-00-00</td>
        </tr>
        <tr>
        	<td>TIMESTAMP</td>
            <td>00000000000000</td>
        </tr>
        <tr>
        	<td>TIME </td>
            <td>00:00:00</td>
        </tr>
        <tr>
        	<td>YEAR</td>
            <td>0000</td>
        </tr>
    </tbody> 
</table>    

### DATE、TIME 和DATETIME

DATE、TIME 和DATETIME 是最常使用的3 种日期类型，以下例子在3 种类型字段插入了相同的日期值，来看看它们的显示结果：

首先创建表t，字段分别为date、time、datetime 三种日期类型：

```mysql
mysql> create table t (d date, t time, dt datetime);
Query OK, 0 rows affected (0.05 sec)

mysql> desc t;
+-------+----------+------+-----+---------+-------+
| Field | Type     | Null | Key | Default | Extra |
+-------+----------+------+-----+---------+-------+
| d     | date     | YES  |     | NULL    |       |
| t     | time     | YES  |     | NULL    |       |
| dt    | datetime | YES  |     | NULL    |       |
+-------+----------+------+-----+---------+-------+
3 rows in set (0.00 sec)
```

用**now()**函数插入当前日期：

```mysql
mysql> insert into t values(now(),now(),now());
Query OK, 1 row affected, 1 warning (0.01 sec)
```

查看显示结果：

```mysql
mysql> select * from t;
+------------+----------+---------------------+
| d          | t        | dt                  |
+------------+----------+---------------------+
| 2020-08-12 | 17:09:49 | 2020-08-12 17:09:49 |
+------------+----------+---------------------+
1 row in set (0.00 sec)
```

显而易见，DATETIME是DATE和TIME的组合，用户可以根据不同的需要，来选择不同的日期或时间类型以满足不同的应用。



### TIMESTAMP

TIMESTAMP也用来表示日期，但是和DATETIME有所不同，后面的章节中会专门介绍。下例对TIMESTAMP类型的特性进行一些测试。

首先看一下 **explicit_defaults_for_timestamp**(5.6 版本后引入)参数的默认值：

```mysql
mysql> show variables like '%explicit%';
+---------------------------------+-------+
| Variable_name                   | Value |
+---------------------------------+-------+
| explicit_defaults_for_timestamp | ON    |
+---------------------------------+-------+
1 row in set, 1 warning (0.02 sec)
```



创建测试表t，字段id1为TIMESTAMP类型：

```mysql
mysql> create table t(id1 timestamp);
Query OK, 0 rows affected (0.05 sec)

mysql> desc t;
+-------+-----------+------+-----+---------+-------+
| Field | Type      | Null | Key | Default | Extra |
+-------+-----------+------+-----+---------+-------+
| id1   | timestamp | YES  |     | NULL    |       |
+-------+-----------+------+-----+---------+-------+
1 row in set (0.00 sec)
```

可以发现，默认值、not null 和 on update CURRENT_TIMESTAMP属性都不会自动设置。需要手工操作：

```mysql
mysql> set explicit_defaults_for_timestamp=off;
Query OK, 0 rows affected, 1 warning (0.00 sec)


mysql> drop table t;
Query OK, 0 rows affected (0.03 sec)

mysql> create table t(id1 timestamp);
Query OK, 0 rows affected (0.05 sec)

mysql> desc t;
+-------+-----------+------+-----+-------------------+-----------------------------------------------+
| Field | Type      | Null | Key | Default           | Extra                                         |
+-------+-----------+------+-----+-------------------+-----------------------------------------------+
| id1   | timestamp | NO   |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED on update CURRENT_TIMESTAMP |
+-------+-----------+------+-----+-------------------+-----------------------------------------------+
1 row in set (0.00 sec)
```

TIMESTAMP还有一个重要特点，就是和时区相关。当插入日期时，会先转换为本地时区后存放；而从数据库里面取出时，也同样需要将日期转换为本地时区后显示。这样，两个不同时区的用户看到的同一个日期可能是不一样的，下面的例子演示了这个差别。

（1）创建表t8，包含字段id1（TIMESTAMP）和id2（DATETIME），设置id2的目的是为了和id1做对比：

```mysql
mysql> create table t8(
    id1 timestamp not null default current_timestamp,
    id2 datetime default null
);
Query OK, 0 rows affected (0.05 sec)
```

（2）查看当前时区：

```mysql
mysql> show variables like 'time_zone';
+---------------+--------+
| Variable_name | Value  |
+---------------+--------+
| time_zone     | SYSTEM |
+---------------+--------+
1 row in set, 1 warning (0.00 sec)
```

可以发现，时区的值为“SYSTEM”，这个值默认是和主机的时区值一致的，因为我们在中国，
这里的“SYSTEM”实际是东八区（+8:00）。

（3）用now()函数插入当前日期：

```mysql
mysql> insert into t8 values(now(),now());
Query OK, 1 row affected (0.01 sec)

mysql> select * from t8;
+---------------------+---------------------+
| id1                 | id2                 |
+---------------------+---------------------+
| 2020-08-12 19:55:50 | 2020-08-12 19:55:50 |
+---------------------+---------------------+
1 row in set (0.00 sec)
```

结果显示id1 和id2 的值完全相同。

（4）修改时区为东九区，再次查看表中日期：

```mysql
mysql> set time_zone='+9:00';
Query OK, 0 rows affected (0.00 sec)

mysql> select * from t8;
+---------------------+---------------------+
| id1                 | id2                 |
+---------------------+---------------------+
| 2020-08-12 20:55:50 | 2020-08-12 19:55:50 |
+---------------------+---------------------+
1 row in set (0.00 sec)
```

结果中可以发现，id1 的值比id2 的值快了1 个小时，也就是说，东九区的人看到的“2020-08-12 20:55:50”是当地时区的实际日期，也就是东八区的“2020-08-12 19:55:50”，如果还是以“2020-08-12 19:55:50”理解时间必然导致误差。

TIMESTAMP的取值范围为19700101080001到2038年的某一天，因此它不适合存放比较久远的日期，下面简单测试一些这个范围：(记录 8.0.16 上下面的插入报错，这里就直接引入书本上的例子)

```mysql
mysql> insert into t values (19700101080001);
Query OK, 1 row affected (0.00 sec)
mysql> select * from t;
+---------------------+
| t |
+---------------------+
| 1970-01-01 08:00:01 |
+---------------------+
1 row in set (0.00 sec)
mysql> insert into t values (19700101080000);
Query OK, 1 row affected, 1 warning (0.00 sec)
```

其中19700101080000 超出了tm 的下限，系统出现警告提示。查询一下，发现插入值变成
了0 值。

```mysql
mysql> select * from t;
+---------------------+
| t |
+---------------------+
| 1970-01-01 08:00:01 |
| 0000-00-00 00:00:00 |
+---------------------+
2 rows in set (0.00 sec)
```

再来测试一下TIMESTAMP 的上限值：

```mysql
mysql> insert into t values('2038-01-19 11:14:07');
Query OK, 1 row affected (0.00 sec)
mysql> select * from t;
+---------------------+
| t |
+---------------------+
| 2038-01-19 11:14:07 |
+---------------------+
1 row in set (0.00 sec)
mysql> insert into t values('2038-01-19 11:14:08');
Query OK, 1 row affected, 1 warning (0.00 sec)
mysql> select * from t;
+---------------------+
| t |
+---------------------+
| 2038-01-19 11:14:07 |
| 0000-00-00 00:00:00 |
+---------------------+
2 rows in set (0.00 sec)
```

从上面例子可以看出，TIMESTAMP和DATETIME的表示方法非常类似，区别主要有以下几点。

+ TIMESTAMP支持的时间范围较小，其取值范围从19700101080001到2038年的某个时间，而DATETIME是从1000-01-01 00:00:00到9999-12-31 23:59:59，范围更大。
+ 表中的第一个TIMESTAMP列自动设置为系统时间。如果在一个TIMESTAMP列中插入NULL，则该列值将自动设置为当前的日期和时间。在插入或更新一行但不明确给TIMESTAMP列赋值时也会自动设置该列的值为当前的日期和时间，当插入的值超出取值范围时，MySQL认为该值溢出，使用“0000-00-00 00:00:00”进行填补。
+ TIMESTAMP的插入和查询都受当地时区的影响，更能反应出实际的日期。而DATETIME则只能反应出插入时当地的时区，其他时区的人查看数据必然会有误差的。
+ TIMESTAMP的属性受MySQL版本和服务器SQLMode的影响很大，本章都是以MySQL5.0为例进行介绍，在不同的版本下可以参考相应的MySQL帮助文档。



### YEAR

YEAR 类型主要用来表示年份，当应用只需要记录年份时，用YEAR 比DATE 将更节省空间。
下面的例子在表t 中定义了一个YEAR 类型字段，并插入一条记录：

```mysql
mysql> create table t(y year);
Query OK, 0 rows affected (0.05 sec)

mysql> desc t;
+-------+---------+------+-----+---------+-------+
| Field | Type    | Null | Key | Default | Extra |
+-------+---------+------+-----+---------+-------+
| y     | year(4) | YES  |     | NULL    |       |
+-------+---------+------+-----+---------+-------+
1 row in set (0.00 sec)

mysql> insert into t values(2100);
Query OK, 1 row affected (0.01 sec)

mysql> select * from t;
+------+
| y    |
+------+
| 2100 |
+------+
1 row in set (0.00 sec)
```

MySQL 以YYYY 格式检索和显示YEAR 值，范围是1901～2155。当使用两位字符串表示年份时，其范围为“00”到“99”。

+ “00”到“69”范围的值被转换为2000～2069 范围的YEAR 值
+ “70”到“99”范围的值被转换为1970～1999 范围的YEAR 值。

细心的读者可能发现，在上面的例子中，日期类型的插入格式有很多，包括整数（如2100）、字符串（如2038-01-19 11:14:08）、函数（如NOW()）等，大家可能会感到疑惑，到底什么样的格式才能够正确地插入到对应的日期字段中呢？下面以DATETIME 为例进行介绍。

1. YYYY-MM-DD HH:MM:SS 或YY-MM-DD HH:MM:SS 格式的字符串。允许“不严格”语法：任何标点符都可以用做日期部分或时间部分之间的间割符。例如，“98-12-31 11:30:45”、“98.12.31 11+30+45”、“98/12/31 11*30*45”和“98@12@31 11^30^45”是等价的。对于包括日期部分间割符的字符串值，如果日和月的值小于10，不需要指定两位数。“1979-6-9”与“1979-06-09”是相同的。同样，对于包括时间部分间割符的字符串值，如果时、分和秒的值小于10，不需要指定两位数。“1979-10-30 1:2:3”与“1979-10-30 01:02:03”相同。
2. YYYYMMDDHHMMSS 或YYMMDDHHMMSS 格式的没有间割符的字符串，假定字符串对于日期类型是有意义的。例如，“19970523091528”和“970523091528”被解释为“1997-05-23 09:15:28”，但“971122129015”是不合法的（它有一个没有意义的分钟部分），将变为“0000-00-00 00:00:00”。
3. YYYYMMDDHHMMSS 或YYMMDDHHMMSS 格式的数字，假定数字对于日期类型是有意义的。例如，19830905132800和830905132800被解释为“1983-09-05 13:28:00”。数字值应为6、8、12 或者14 位长。如果一个数值是8 或14 位长，则假定为YYYYMMDD 或YYYYMMDDHHMMSS 格式，前4 位数表示年。如果数字是6 或12位长，则假定为YYMMDD 或YYMMDDHHMMSS 格式，前2 位数表示年。其他数字被解释为仿佛用零填充到了最近的长度。
4. 函数返回的结果，其值适合DATETIME、DATE 或TIMESTAMP 上下文，例如**NOW()**或**CURRENT_DATE**。

对于其他数据类型，其使用原则与上面的内容类似，限于篇幅，这里就不再赘述。

最后通过一个例子，说明如何采用不同的格式将日期“2007-9-3 12:10:10”插入到DATETIME列中。

```mysql
mysql> create table t6(dt datetime);
Query OK, 0 rows affected (0.02 sec)

mysql> insert into t6 values('2020-08-12 20:20:00');
Query OK, 1 row affected (0.01 sec)

mysql> insert into t6 values('2020-8-12 20:20:00');
Query OK, 1 row affected (0.01 sec)

mysql> insert into t6 values('2020/8/12 20+20+00');
Query OK, 1 row affected (0.01 sec)

mysql> insert into t6 values('20200812202000');
Query OK, 1 row affected (0.01 sec)

mysql> select * from t6;
+---------------------+
| dt                  |
+---------------------+
| 2020-08-12 20:20:00 |
| 2020-08-12 20:20:00 |
| 2020-08-12 20:20:00 |
| 2020-08-12 20:20:00 |
+---------------------+
4 rows in set (0.00 sec)
```



## 字符串类型

MySQL 中提供了多种对字符数据的存储类型，不同的版本可能有所差异。以5.0 版本为例，MySQL 包括了CHAR、VARCHAR、BINARY、VARBINARY、BLOB、TEXT、ENUM 和SET 等多种字符串类型。下面列出了这些字符类型的比较。

<table>
    <thead>
        <tr>
        	<td>字符串类型</td>
            <td>字节</td>
            <td>描述及存储需求</td>
        </tr>
    </thead>
    <tbody>
    	<tr>
        	<td>CHAR（M）</td>
            <td>M</td>
            <td>M 为0～255 之间的整数</td>
        </tr>
        <tr>
        	<td>VARCHAR（M）</td>
            <td></td>
            <td>M 为0～65535 之间的整数，值的长度+1 个字节</td>
        </tr>
        <tr>
        	<td>TINYBLOB </td>
            <td></td>
            <td>允许长度0～255 字节，值的长度+1 个字节</td>
        </tr>
        <tr>
        	<td>BLOB</td>
            <td></td>
            <td>允许长度0～65535 字节，值的长度+2 个字节</td>
        </tr>
        <tr>
        	<td>MEDIUMBLOB </td>
            <td></td>
            <td>允许长度0～167772150 字节，值的长度+3 个字节</td>
        </tr>
        <tr>
        	<td>LONGBLOB </td>
            <td></td>
            <td>允许长度0～4294967295 字节，值的长度+4 个字节</td>
        </tr>
         <tr>
        	<td>TINYTEXT </td>
            <td></td>
            <td>允许长度0～255 字节，值的长度+2 个字节</td>
        </tr>
        <tr>
        	<td>TEXT </td>
            <td></td>
            <td>允许长度0～65535 字节，值的长度+2 个字节</td>
        </tr>
        <tr>
        	<td>MEDIUMTEXT</td>
            <td></td>
            <td>允许长度0～167772150 字节，值的长度+3 个字节</td>
        </tr>
        <tr>
        	<td>LONGTEXT </td>
            <td></td>
            <td>允许长度0～4294967295 字节，值的长度+4 个字节</td>
        </tr>
        <tr>
        	<td>VARBINARY（M）</td>
            <td></td>
            <td>允许长度0～M 个字节的变长字节字符串，值的长度+1 个字节</td>
        </tr>
        <tr>
        	<td>BINARY（M）</td>
            <td>M</td>
            <td>允许长度0～M 个字节的定长字节字符串</td>
        </tr>
    </tbody>
</table>

下面将分别对这些字符串类型做详细的介绍。

### CHAR 和VARCHAR 类型

CHAR 和VARCHAR 很类似，都用来保存MySQL 中较短的字符串。二者的主要区别在于存储方式的不同：CHAR 列的长度固定为创建表时声明的长度，长度可以为从0～255 的任何值；而VARCHAR 列中的值为可变长字符串，长度可以指定为0～255 （5.0.3 以前）或者65535 （5.0.3以后）之间的值。在检索的时候，CHAR 列删除了尾部的空格，而VARCHAR 则保留这些空格。下面的例子中通过给表vc 中的VARCHAR(4)和char(4)字段插入相同的字符串来描述这个区别。

（1）创建测试表vc，并定义两个字段v VARCHAR(4)和c CHAR(4)：

```mysql
mysql> create table vc(v varchar(4), c char(4));
Query OK, 0 rows affected (0.05 sec)
```

（2）v 和c 列中同时插入字符串“ab ”：

```mysql
mysql> insert into vc values('ab  ', 'ab  ');
Query OK, 1 row affected (0.01 sec)
```

（3）显示查询结果：

```mysql
mysql> select length(v),length(c) from vc;
+-----------+-----------+
| length(v) | length(c) |
+-----------+-----------+
|         4 |         2 |
+-----------+-----------+
1 row in set (0.00 sec)
```

可以发现，c 字段的length 只有2。给两个字段分别追加一个“+”字符看得更清楚：

```mysql
mysql> select concat(v,'+'), concat(c, '+') from vc;
+---------------+----------------+
| concat(v,'+') | concat(c, '+') |
+---------------+----------------+
| ab  +         | ab+            |
+---------------+----------------+
1 row in set (0.00 sec)
```

显然，CHAR 列最后的空格在做操作时都已经被删除，而VARCHAR 依然保留空格。



### BINARY 和VARBINARY 类型

BINARY 和VARBINARY 类似于CHAR 和VARCHAR，不同的是它们包含二进制字符串而不包含非二进制字符串。在下面的例子中，对表t 中的binary 字段c 插入一个字符，研究一下这个字符到底是怎么样存储的。

（1）创建测试表t，字段为c BINARY(3)：

```mysql
mysql> create table t (c binary(3));
Query OK, 0 rows affected (0.05 sec)
```

（2）往c 字段中插入字符“a”：

```mysql
mysql> insert into t set c='a';
Query OK, 1 row affected (0.01 sec)
```

（3）分别用以下几种模式来查看c 列的内容：

```mysql
mysql> select *,hex(c),c='a',c='a\0',c='a\0\0' from t;
+------+--------+-------+---------+-----------+
| c    | hex(c) | c='a' | c='a\0' | c='a\0\0' |
+------+--------+-------+---------+-----------+
| a    | 610000 |     0 |       0 |         1 |
+------+--------+-------+---------+-----------+
1 row in set (0.00 sec)
```

可以发现，当保存BINARY 值时，在值的最后通过填充“0x00”（零字节）以达到指定的字段定义长度。从上例中看出，对于一个BINARY(3)列，当插入时'a'变为'a\0\0'。



### ENUM 类型

ENUM 中文名称叫枚举类型，它的值范围需要在创建表时通过枚举方式显式指定，对1～255 个成员的枚举需要1 个字节存储；对于255～65535 个成员，需要2 个字节存储。最多允许有65535 个成员。下面往测试表t 中插入几条记录来看看ENUM 的使用方法。

（1）创建测试表t，定义gender 字段为枚举类型，成员为'M'和'F'：

```mysql
mysql> create table t (gender enum('M','F'));
Query OK, 0 rows affected (0.05 sec)
```

（2）插入4 条不同的记录：

```mysql
mysql> insert into t values('M'),('1'),('f'),(NULL);
Query OK, 4 rows affected (0.01 sec)
Records: 4  Duplicates: 0  Warnings: 0

mysql> select * from t;
+--------+
| gender |
+--------+
| M      |
| M      |
| F      |
| NULL   |
+--------+
4 rows in set (0.00 sec)
```

从上面的例子中，可以看出ENUM 类型是忽略大小写的，对'M'、'f'在存储的时候将它们都转成了大写，还可以看出对于插入不在ENUM 指定范围内的值时，并没有返回警告(8.0.16 版本上插入不在范围内的值报错，但是插入'1','2'会插入第一个、第二个值)，而是插入了enum('M','F')的第一值'M'，这点用户在使用时要特别注意。另外，ENUM 类型只允许从值集合中选取单个值，而不能一次取多个值。

### SET 类型

Set 和ENUM 类型非常类似，也是一个字符串对象，里面可以包含0～64 个成员。根据成员的不同，存储上也有所不同。

+ 1～8 成员的集合，占1 个字节。
+ 9～16 成员的集合，占2 个字节。
+ 17～24 成员的集合，占3 个字节。
+ 25～32 成员的集合，占4 个字节。
+ 33～64 成员的集合，占8 个字节。

Set 和ENUM 除了存储之外，最主要的区别在于Set 类型一次可以选取多个成员，而ENUM则只能选一个。下面的例子在表t 中插入了多组不同的成员：

```mysql
mysql> create table t (col set ('a','b','c','d'));
Query OK, 0 rows affected (0.04 sec)

mysql> insert into t values('a,b'),('a,d,a'),('a,b'),('a,c'),('a');
Query OK, 5 rows affected (0.01 sec)
Records: 5  Duplicates: 0  Warnings: 0

mysql> select * from t;
+------+
| col  |
+------+
| a,b  |
| a,d  |
| a,b  |
| a,c  |
| a    |
+------+
5 rows in set (0.00 sec)
```

SET 类型可以从允许值集合中选择任意1 个或多个元素进行组合，所以对于输入的值只要是在允许值的组合范围内，都可以正确地注入到SET 类型的列中。对于超出允许值范围的值例如（'a,d,f'）将不允许注入到上面例子中设置的SET 类型列中，而对于（'a,d,a'）这样包含重复成员的集合将只取一次，写入后的结果为“a,d”，这一点请注意。



## JSON类型

自5.7.8 版本起，MySQL开始支持JSON。JSON类型比字符类型有如下优点：

1. JSON数据会自动校验数据是否为JSON格式，如果不是JSON格式数据，则会报错。
2. MySQL提供了一组操作JSON数据的内置函数，可以方便地提取各类数据，可以修改特定的键值。
3. 优化的存储格式，存储在JSON列中的JSON数据被转换成内部的存储格式，允许快速读取。

简单地说，JSON就是Javascript的子集，支持的数据类型包括 NUMBER、STRING、BOOLEAN、NULL、ARRAY、OBJECT共6种，一个JSON中的元素可以是这6种类型元素的任意组合，其中BOOLEAN使用 true/false 的字面量文本表示；null 使用null的文本表示；字符串和日期类型都用双引号引起来表示；ARRAY要用中括号引起来，OBJECT保存KV对要用大括号引起来，其中K也要用双引号引起来。下面是几个格式正确的列子：

```json
["abc",10,null,true,false]
{"K1":"value","k2":10}
["12:18:29.000000","2020-08-12","2020-08-12 21:00:00.000000"]
```

ARRAY和OBJECT也可以嵌套引用，比如：

```json
[99,{"id":"HK500","cost":75.99},["host","cold"]]
{"k1":"value","k2":[10,20]}
```

下面我们举例看看JSON在MySQL中的使用。

（1）首先，创建表t1,列id1为JSON数据类型

```mysql
mysql> create table t1 (id1 json);
Query OK, 0 rows affected (0.04 sec)
```

（2）往表t1中插入如下JSON格式数据：

```mysql
mysql> insert into t1 values('{"age":20,"time":"2020-08-12 21:06:00"}');
Query OK, 1 row affected (0.01 sec)

# 如果插入的JSON格式有错，就会报错
mysql> insert into t1 values('[1,2');
ERROR 3140 (22032): Invalid JSON text: "Missing a comma or ']' after an array element." at position 4 in value for column 't1.id1'.
```

（3）通过JSON_TYPE函数可以看到插入的JSON数据类型是哪种类型

```mysql
mysql> select json_type('"abc"') js1,json_type('[1,2,"abc"]') js2, json_type('{"k1":"value"}') js3;
+--------+-------+--------+
| js1    | js2   | js3    |
+--------+-------+--------+
| STRING | ARRAY | OBJECT |
+--------+-------+--------+
1 row in set (0.00 sec)
```

JSON数据类型对于大小写是敏感的，'x' 和'X'是不同的两个JSON数据，常见的null、true、false必须小写才是合法的，通过JSON_VALID 函数可以判断一个JSON数据是否合法，如下例所示：

```mysql
mysql> select JSON_VALID('null'), JSON_VALID('NULL'), JSON_VALID('false'), JSON_VALID('FALSE');
+--------------------+--------------------+---------------------+---------------------+
| JSON_VALID('null') | JSON_VALID('NULL') | JSON_VALID('false') | JSON_VALID('FALSE') |
+--------------------+--------------------+---------------------+---------------------+
|                  1 |                  0 |                   1 |                   0 |
+--------------------+--------------------+---------------------+---------------------+
1 row in set (0.00 sec)
```

对大小写敏感的原因是JSON的默认排序规则是utf8mb4_bin所致。

还有一种特殊情况，如果JSON数据的value中字符串value中包括双引号或者单引号，则插入时需要加反斜线进行转义。

（1）显示插入

```mysql
mysql> desc t1;
+-------+------+------+-----+---------+-------+
| Field | Type | Null | Key | Default | Extra |
+-------+------+------+-----+---------+-------+
| id1   | json | YES  |     | NULL    |       |
+-------+------+------+-----+---------+-------+
1 row in set (0.00 sec)
mysql> insert into t1 values(json_object("name","ab\"c"));
Query OK, 1 row affected (0.01 sec)
```

（2）隐式插入：

```mysql
mysql> insert into t1 values('{"name":"ab\"c"}');
ERROR 3140 (22032): Invalid JSON text: "Missing a comma or '}' after an object member." at position 12 in value for column 't1.id1'.
mysql> insert into t1 values('{"name":"ab\\"c"}');
Query OK, 1 row affected (0.01 sec)

```

可以发现，隐式插入时，要多加一个反斜杠才可以正常识别。

MySQL对JSON的存储做了一些限制，JSON列不可有默认值，且文本的最大长度取决于系统常量：max_allowed_packet。该值仅在服务器进行存储的时候进行限制，在内存中进行计算的时候是允许超过该值的

对于JSON数据常用操作，MySQL提供了很多函数，比如上面提到的JSON_VALD/JSON_OBJECT等



## 小结

本章主要介绍了MySQL 支持的各种数据类型，并通过多个实例对它们的使用方法做了详细的说明。学完本章后，读者可以对每种数据类型的用途、物理存储、表示范围等有一个概要的了解。这样在面对具体应用时，就可以根据相应的特点来选择合适的数据类型，使得我们能够争取在满足应用的基础上，用较小的存储代价换来较高的数据库性能。







































































