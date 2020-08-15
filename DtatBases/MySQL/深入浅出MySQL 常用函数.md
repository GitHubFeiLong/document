# MySQL 常用函数

经常编写程序的朋友一定体会得到函数的重要性，丰富的函数往往能使用户的工作事半功倍。函数能帮助用户做很多事情，比如说字符串的处理、数值的运算、日期的运算等，在这方面MySQL 提供了多种内建函数帮助开发人员编写简单快捷的SQL 语句，其中常用的函数有字符串函数、日期函数和数值函数。
在MySQL 数据库中，函数可以用在SELECT 语句及其子句（例如where、order by、having 等）中，也可以用在UPDATE、DELETE 语句及其子句中。本章将配合一些实例对这些常用函数进行详细的介绍。



## 1、字符串函数

字符串函数是最常用的一种函数了，如果大家编写过程序的话，不妨回过头去看看自己使用过的函数，可能会惊讶地发现字符串处理的相关函数占已使用过的函数很大一部分。MySQL中字符串函数也是最丰富的一类函数：

<table>
    <thead>
    	<tr>
        	<td>函数</td>
            <td>功能</td>
        </tr>
    </thead>
    <tbody>
    	<tr>
        	<td>CANCAT(S1,S2,…Sn)</td>
            <td>连接S1,S2,…Sn 为一个字符串</td>
        </tr>
        <tr>
        	<td>INSERT(str,x,y,instr)</td>
            <td>将字符串str 从第x 位置开始，y 个字符长的子串替换为字符串instr</td>
        </tr>
        <tr>
        	<td>LOWER(str)</td>
            <td>将字符串str 中所有字符变为小写</td>
        </tr>
        <tr>
        	<td>UPPER(str)</td>
            <td>将字符串str 中所有字符变为大写</td>
        </tr>
        <tr>
        	<td>LEFT(str ,x)</td>
            <td>返回字符串str 最左边的x 个字符</td>
        </tr>
        <tr>
        	<td>RIGHT(str,x)</td>
            <td>返回字符串str 最右边的x 个字符</td>
        </tr>
        <tr>
        	<td>LPAD(str,n ,pad) </td>
            <td>用字符串pad 对str 最左边进行填充，直到长度为n 个字符长度</td>
        </tr>
        <tr>
        	<td>RPAD(str,n ,pad) </td>
            <td>用字符串pad 对str 最右边进行填充，直到长度为n 个字符长度</td>
        </tr>
        <tr>
        	<td>LTRIM(str)</td>
            <td>去掉字符串str 左侧的空格</td>
        </tr>
        <tr>
        	<td>RTRIM(str)</td>
            <td>去掉字符串str 行尾的空格</td>
        </tr>
         <tr>
        	<td>REPEAT(str,x)</td>
            <td>返回str 重复x 次的结果</td>
        </tr>
        <tr>
        	<td>REPLACE(str,a,b) </td>
            <td>用字符串b 替换字符串str 中所有出现的字符串a
</td>
        </tr>
        <tr>
        	<td>STRCMP(s1,s2)</td>
            <td>比较字符串s1 和s2</td>
        </tr>
        <tr>
        	<td>TRIM(str)</td>
            <td>去掉字符串行尾和行头的空格</td>
        </tr>
         <tr>
        	<td>SUBSTRING(str,x,y) </td>
            <td>返回从字符串str x 位置起y 个字符长度的字串</td>
        </tr>
    </tbody>
</table>

下面通过具体的实例来逐个地研究每个函数的用法，需要注意的是这里的例子仅仅在于说明各个函数的使用方法，所以函数都是单个出现的，但是在一个具体的应用中通常可能需要综合几个甚至几类函数才能实现相应的应用。

+ **CANCAT(S1,S2,…Sn)函数**：把传入的参数连接成为一个字符串。

  下面的例把“aaa”、“bbb”、“ccc”3 个字符串连接成了一个字符串“aaabbbccc”。另外，任何字符串与NULL 进行连接的结果都将是NULL。

  ```mysql
  mysql> select concat('aaa','bbb','ccc'),concat('aaa',null);
  +---------------------------+--------------------+
  | concat('aaa','bbb','ccc') | concat('aaa',null) |
  +---------------------------+--------------------+
  | aaabbbccc                 | NULL               |
  +---------------------------+--------------------+
  1 row in set (0.00 sec)
  ```

+ **INSERT(str ,x,y,instr)函数**：将字符串str 从第x 位置开始，y 个字符长的子串替换为字符串instr。
  下面的例子把字符串“beijing2008you”中的从第12 个字符开始以后的3 个字符替换成“me”。
  
```mysql
  mysql> select insert('beijing2008you',12,3,'me');
  +------------------------------------+
  | insert('beijing2008you',12,3,'me') |
  +------------------------------------+
  | beijing2008me                      |
  +------------------------------------+
  1 row in set (0.00 sec)
  ```
  
+ **LOWER(str)和UPPER(str)函数**：把字符串转换成小写或大写。
  在字符串比较中，通常要将比较的字符串全部转换为大写或者小写，如下例所示：

  ```mysql
  mysql> select lower('BEIJING2008'),upper('beijing2008');
  +----------------------+----------------------+
  | lower('BEIJING2008') | upper('beijing2008') |
  +----------------------+----------------------+
  | beijing2008          | BEIJING2008          |
  +----------------------+----------------------+
  1 row in set (0.00 sec)
  ```

+ **LEFT(str,x)和RIGHT(str,x)函数**：分别返回字符串最左边的x 个字符和最右边的x 个字符。如果第二个参数是NULL，那么将不返回任何字符串。
  下例中显示了对字符串“beijing2008”应用函数后的结果。
  
```mysql
  mysql> select LEFT('beijing2008',7),LEFT('beijing',null),RIGHT('beijing2008',4);
  +-----------------------+----------------------+------------------------+
  | LEFT('beijing2008',7) | LEFT('beijing',null) | RIGHT('beijing2008',4) |
  +-----------------------+----------------------+------------------------+
  | beijing               | NULL                 | 2008                   |
  +-----------------------+----------------------+------------------------+
  1 row in set (0.00 sec)
  ```
  
+ **LPAD(str,n ,pad)和RPAD(str,n ,pad)函数**：用字符串pad 对str 最左边和最右边进行填充,直到长度为n 个字符长度。
  下例中显示了对字符串“2008”和“beijing”分别填充后的结果。
  
```mysql
  mysql> select lpad('2008',20,'beijing'),rpad('beijing',20,'2008');
  +---------------------------+---------------------------+
  | lpad('2008',20,'beijing') | rpad('beijing',20,'2008') |
  +---------------------------+---------------------------+
  | beijingbeijingbe2008      | beijing2008200820082      |
  +---------------------------+---------------------------+
  1 row in set (0.00 sec)
  ```
  
+ **LTRIM(str)和RTRIM(str)函数**：去掉字符串str 左侧和右侧空格。
  下例中显示了字符串“beijing”加空格进行过滤后的结果。

  ```mysql
  mysql> select ltrim('    |beijing'), rtrim('beijing|    ');
  +-----------------------+-----------------------+
  | ltrim('    |beijing') | rtrim('beijing|    ') |
  +-----------------------+-----------------------+
  | |beijing              | beijing|              |
  +-----------------------+-----------------------+
  1 row in set (0.00 sec)
  ```

+ **REPEAT(str,x)函数**：返回str 重复x 次的结果。
  下例中对字符串“mysql”重复显示了3 次。

  ```mysql
  mysql> select repeat('mysql',3);
  +-------------------+
  | repeat('mysql',3) |
  +-------------------+
  | mysqlmysqlmysql   |
  +-------------------+
  1 row in set (0.00 sec)
  ```

+ **REPLACE(str,a,b)函数**：用字符串b 替换字符串str 中所有出现的字符串a。
  下例中用字符串“2008”代替了字符串“beijing_2010”中的“_2010”。

  ```mysql
  mysql> select replace('beijing_2010','_2010','2008');
  +----------------------------------------+
  | replace('beijing_2010','_2010','2008') |
  +----------------------------------------+
  | beijing2008                            |
  +----------------------------------------+
  1 row in set (0.00 sec)
  ```

+ **STRCMP(s1,s2)函数**：比较字符串s1 和s2 的ASCII 码值的大小。如果s1 比s2 小，那么返回-1；如果s1 与s2 相等，那么返回0；如果s1 比s2 大，那么返回1。如下例：
  
```mysql
  mysql> select strcmp('a','b'),strcmp('b','b'),strcmp('c','b');
  +-----------------+-----------------+-----------------+
  | strcmp('a','b') | strcmp('b','b') | strcmp('c','b') |
  +-----------------+-----------------+-----------------+
  |              -1 |               0 |               1 |
  +-----------------+-----------------+-----------------+
  1 row in set (0.00 sec)
  ```
  
+ **TRIM(str)函数**：去掉目标字符串的开头和结尾的空格。
  下例中对字符串“   $ beijing2008 $    ”进行了前后空格的过滤。

  ```mysql
  mysql> select trim('   $ beijing2008 $   ');
  +-------------------------------+
  | trim('   $ beijing2008 $   ') |
  +-------------------------------+
  | $ beijing2008 $               |
  +-------------------------------+
  1 row in set (0.00 sec)
  ```

+ **SUBSTRING(str,x,y)函数**：返回从字符串str 中的第x 位置起y 个字符长度的字串。
  此函数经常用来对给定字符串进行字串的提取，如下例所示。

  ```mysql
  mysql> select substring('beijing2008',8,4), substring('beijing2008',1,7);
  +------------------------------+------------------------------+
  | substring('beijing2008',8,4) | substring('beijing2008',1,7) |
  +------------------------------+------------------------------+
  | 2008                         | beijing                      |
  +------------------------------+------------------------------+
  1 row in set (0.00 sec)
  ```

  



## 2、数值函数

MySQL 中另外一类很重要的函数就是数值函数，这些函数能处理很多数值方面的运算。可以想象，如果没有这些函数的支持，用户在编写有关数值运算方面的代码时将会困难重重，举个例子，如果没有ABS 函数的话，如果要取一个数值的绝对值，就需要进行好多次判断才能返回这个值，而数字函数能够大大提高用户的工作效率。

<table>
    <thead>
    	<tr>
        	<td>函数</td>
            <td>功能</td>
        </tr>
    </thead>
    <tbody>
    	<tr>
        	<td>ABS(x)</td>
            <td>返回x 的绝对值</td>
        </tr>
        <tr>
        	<td>CEIL(x) </td>
            <td>返回大于x 的最大整数值</td>
        </tr>
        <tr>
        	<td>FLOOR(x) </td>
            <td>返回小于x 的最大整数值</td>
        </tr>
        <tr>
        	<td>MOD(x，y) </td>
            <td>返回x/y 的模</td>
        </tr>
        <tr>
        	<td>RAND() </td>
            <td>返回0 到1 内的随机值</td>
        </tr>
        <tr>
        	<td>ROUND(x,y) </td>
            <td>返回参数x 的四舍五入的有y 位小数的值</td>
        </tr>
        <tr>
        	<td>TRUNCATE(x,y) </td>
            <td>返回数字x 截断为y 位小数的结果</td>
        </tr>
    </tbody>
</table>

下面将结合实例对这些函数进行介绍。

+ **ABS(x)函数**：返回x 的绝对值。

  下例中显示了对正数和负数分别取绝对值之后的结果。

  ```mysql
  mysql> select ABS(-0.8),ABS(0.8);
  +-----------+----------+
  | ABS(-0.8) | ABS(0.8) |
  +-----------+----------+
  |       0.8 |      0.8 |
  +-----------+----------+
  1 row in set (0.00 sec)
  ```

+ **CEIL(x)函数**：返回大于x 的最大整数。

  下例中显示了对0.8 和－0.8 分别CEIL 后的结果。

  ```mysql
  mysql> select ceil(-0.8),ceil(0.8);
  +------------+-----------+
  | ceil(-0.8) | ceil(0.8) |
  +------------+-----------+
  |          0 |         1 |
  +------------+-----------+
  1 row in set (0.00 sec)
  ```

+ **FLOOR(x)函数**：返回小于x 的最大整数，和CEIL 的用法刚好相反。
  下例中显示了对0.8 和－0.8 分别FLOOR 后的结果。

  ```mysql
  mysql> select floor(-0.8),floor(0.8);
  +-------------+------------+
  | floor(-0.8) | floor(0.8) |
  +-------------+------------+
  |          -1 |          0 |
  +-------------+------------+
  1 row in set (0.00 sec)
  ```

+ **MOD(x，y)函数**：返回x/y 的模。
  和x%y 的结果相同，模数和被模数任何一个为NULL 结果都为NULL。如下例所示：

  ```mysql
  mysql> select MOD(15,10),MOD(1,11),MOD(NULL,10);
  +------------+-----------+--------------+
  | MOD(15,10) | MOD(1,11) | MOD(NULL,10) |
  +------------+-----------+--------------+
  |          5 |         1 |         NULL |
  +------------+-----------+--------------+
  1 row in set (0.00 sec)
  ```

+ **RAND()函数**：返回0 到1 内的随机值。
  每次执行结果都不一样，如下例所示：

  ```mysql
  mysql> select RAND(),RAND();
  +--------------------+-----------------------+
  | RAND()             | RAND()                |
  +--------------------+-----------------------+
  | 0.7870859902231824 | 0.0031945509865829265 |
  +--------------------+-----------------------+
  1 row in set (0.00 sec)
  ```

  利用此函数可以取任意指定范围内的随机数，比如需要产生0～100 内的任意随机整数，可
  以操作如下：

  ```mysql
  mysql> select ceil(100*rand()), ceil(100*rand());
  +------------------+------------------+
  | ceil(100*rand()) | ceil(100*rand()) |
  +------------------+------------------+
  |               66 |               27 |
  +------------------+------------------+
  1 row in set (0.00 sec)
  ```

+ **ROUND(x,y)函数**：返回参数x 的四舍五入的有y 位小数的值。
  如果是整数，将会保留y 位数量的0（8.0.16 只保留整数）；如果不写y，则默认y 为0，即将x 四舍五入后取整。适合于将所有数字保留同样小数位的情况。如下例所示。

  ```mysql
  mysql> select ROUND(1.1), ROUND(1.1,2),ROUND(1,2);
  +------------+--------------+------------+
  | ROUND(1.1) | ROUND(1.1,2) | ROUND(1,2) |
  +------------+--------------+------------+
  |          1 |         1.10 |          1 |
  +------------+--------------+------------+
  1 row in set (0.00 sec)
  ```

+ **TRUNCATE(x,y)函数**：返回数字x 截断为y 位小数的结果。
  注意TRUNCATE 和ROUND 的区别在于TRUNCATE 仅仅是截断，而不进行四舍五入。下例中描述了二者的区别：
  
```mysql
  mysql> select round(1.235,2), truncate(1.235,2);
  +----------------+-------------------+
  | round(1.235,2) | truncate(1.235,2) |
  +----------------+-------------------+
  |           1.24 |              1.23 |
  +----------------+-------------------+
  1 row in set (0.00 sec)
  
  ```
  




## 3、日期和时间函数

有时我们可能会遇到这样的需求：当前时间是多少、下个月的今天是星期几、统计截止到当前日期前3 天的收入总和等。这些需求就需要日期和时间函数来实现

<table>
    <thead>
    	<tr>
        	<td>函数</td>
            <td>功能</td>
        </tr>
    </thead>
    <tbody>
    	<tr>
        	<td>CURDATE()</td>
            <td>返回当前日期</td>
        </tr>
        <tr>
        	<td>CURTIME() </td>
            <td>返回当前时间</td>
        </tr>
        <tr>
        	<td>NOW() </td>
            <td>返回当前的日期和时间</td>
        </tr>
        <tr>
        	<td>UNIX_TIMESTAMP(date) </td>
            <td>返回日期date 的UNIX 时间戳</td>
        </tr>
        <tr>
        	<td>FROM_UNIXTIME </td>
            <td>返回UNIX 时间戳的日期值</td>
        </tr>
        <tr>
        	<td>WEEK(date) </td>
            <td>返回日期date 为一年中的第几周</td>
        </tr>
        <tr>
        	<td>YEAR(date) </td>
            <td>返回日期date 的年份</td>
        </tr>
        <tr>
        	<td>HOUR(time) </td>
            <td>返回time 的小时值</td>
        </tr>
             <tr>
    	<td>MINUTE(time) </td>
        <td>返回time 的分钟值</td>
    </tr>
    <tr>
    	<td>MONTHNAME(date) </td>
        <td>返回date 的月份名</td>
    </tr>
     <tr>
    	<td>DATE_FORMAT(date,fmt) </td>
        <td>返回按字符串fmt 格式化日期date 值</td>
    </tr>
     <tr>
    	<td>DATE_ADD(date,INTERVAL expr type) </td>
        <td>返回一个日期或时间值加上一个时间间隔的时间值</td>
    </tr>
     <tr>
    	<td>DATEDIFF(expr,expr2) </td>
        <td>返回起始时间expr 和结束时间expr2 之间的天数</td>
    </tr>
</tbody>
</table>

下面结合一些实例来逐个讲解每个函数的使用方法。

+ **CURDATE()函数**：返回当前日期，只包含年月日。

  ```mysql
  mysql> select CURDATE();
  +------------+
  | CURDATE()  |
  +------------+
  | 2020-08-14 |
  +------------+
  1 row in set (0.00 sec)
  ```

+ **CURTIME()函数**：返回当前时间，只包含时分秒。

  ```mysql
  mysql> select CURTIME();
  +-----------+
  | CURTIME() |
  +-----------+
  | 17:16:04  |
  +-----------+
  1 row in set (0.00 sec)
  ```

+ **NOW()函数**：返回当前的日期和时间，年月日时分秒全都包含。

  ```mysql
  mysql> select now();
  +---------------------+
  | now()               |
  +---------------------+
  | 2020-08-14 17:16:26 |
  +---------------------+
  1 row in set (0.00 sec)
  ```

+ **UNIX_TIMESTAMP(date)函数**：返回日期date 的UNIX 时间戳。

  ```mysql
  mysql> select unix_timestamp(now());
  +-----------------------+
  | unix_timestamp(now()) |
  +-----------------------+
  |            1597396659 |
  +-----------------------+
  1 row in set (0.00 sec)
  ```

+ **FROM_UNIXTIME （unixtime ）函数**：返回UNIXTIME 时间戳的日期值，和UNIX_TIMESTAMP(date)互为逆操作
  
```mysql
  mysql> select from_unixtime(1597396659);
  +---------------------------+
  | from_unixtime(1597396659) |
  +---------------------------+
  | 2020-08-14 17:17:39       |
  +---------------------------+
  1 row in set (0.00 sec)
  ```
  
+ **WEEK(DATE)和YEAR(DATE)函数**：前者返回所给的日期是一年中的第几周，后者返回所给的日期是哪一年。
  
```mysql
  mysql> select week(now()),year(now());
  +-------------+-------------+
  | week(now()) | year(now()) |
  +-------------+-------------+
  |          32 |        2020 |
  +-------------+-------------+
  1 row in set (0.00 sec)
  ```
  
+ **HOUR(time)和MINUTE(time)函数**：前者返回所给时间的小时，后者返回所给时间的分钟。

  ```mysql
  mysql> select HOUR(curtime()), MINUTE(curtime());
  +-----------------+-------------------+
  | HOUR(curtime()) | MINUTE(curtime()) |
  +-----------------+-------------------+
  |              17 |                20 |
  +-----------------+-------------------+
  1 row in set (0.00 sec)
  ```

+ **MONTHNAME(date)函数**：返回date 的英文月份名称。

  ```mysql
  mysql> select MONTHNAME(now());
  +------------------+
  | MONTHNAME(now()) |
  +------------------+
  | August           |
  +------------------+
  1 row in set (0.00 sec)
  ```

+ **DATE_FORMAT(date,fmt)函数**：按字符串fmt 格式化日期date 值，此函数能够按指定的格式显示日期，可以用到的格式符如表

  <table>
      <thead>
      	<tr>
          	<td>格式符</td>
              <td>格式说明</td>
          </tr>
      </thead>
      <tbody>
      	<tr>
          	<td>%S,%s </td>
              <td>两位数字形式的秒（00,01,...,59）</td>
          </tr>
          <tr>
          	<td>%i </td>
              <td>两位数字形式的分（00,01,...,59）</td>
          </tr>
          <tr>
          	<td>%H </td>
              <td>两位数字形式的小时，24 小时（00,01,...,23）</td>
          </tr>
          <tr>
          	<td>%h,%I </td>
              <td>两位数字形式的小时，12 小时（01,02,...,12）</td>
          </tr>
          <tr>
          	<td>%k </td>
              <td>数字形式的小时，24 小时（0,1,...,23）</td>
          </tr>
          <tr>
          	<td>%l </td>
              <td>数字形式的小时，12 小时（1,2,...,12）</td>
          </tr>
          <tr>
          	<td>%T </td>
              <td>24 小时的时间形式（hh:mm:ss）</td>
          </tr>
          <tr>
          	<td>%r </td>
              <td>12 小时的时间形式（hh:mm:ssAM 或hh:mm:ssPM）</td>
          </tr>
          <tr>
          	<td>%p </td>
              <td>AM 或PM</td>
          </tr>
          <tr>
          	<td>%W </td>
              <td>一周中每一天的名称（Sunday,Monday,...,Saturday）</td>
          </tr>
          <tr>
          	<td>%a </td>
              <td>一周中每一天名称的缩写（Sun,Mon,...,Sat）</td>
          </tr>
          <tr>
          	<td>%d </td>
              <td>两位数字表示月中的天数（00,01,...,31）</td>
          </tr>
          <tr>
          	<td>%e </td>
              <td>数字形式表示月中的天数（1,2，...,31）</td>
          </tr>
          <tr>
          	<td>%D </td>
              <td>英文后缀表示月中的天数（1st,2nd,3rd,...）</td>
          </tr>
          <tr>
          	<td>%w </td>
              <td>以数字形式表示周中的天数（0=Sunday,1=Monday,...,6=Saturday）</td>
          </tr>
          <tr>
          	<td>%j </td>
              <td>以3 位数字表示年中的天数（001,002,...,366）</td>
          </tr>
          <tr>
          	<td>%U </td>
              <td>周（0,1,52），其中Sunday 为周中的第一天</td>
          </tr>
          <tr>
          	<td>%u </td>
              <td>周（0,1,52），其中Monday 为周中的第一天</td>
          </tr>
          <tr>
          	<td>%M </td>
              <td>月名（January,February,...,December）</td>
          </tr>
          <tr>
          	<td>%b </td>
              <td>缩写的月名（January,February,...,December）</td>
          </tr>
          <tr>
          	<td>%m </td>
              <td>两位数字表示的月份（01,02,...,12）</td>
          </tr>
          <tr>
          	<td>%c </td>
              <td>数字表示的月份（1,2,...,12）</td>
          </tr>
          <tr>
          	<td>%Y </td>
              <td>4 位数字表示的年份</td>
          </tr>
          <tr>
          	<td>%y </td>
              <td>两位数字表示的年份</td>
          </tr>
          <tr>
          	<td>%% </td>
              <td>直接值“%”</td>
          </tr>
      </tbody>
  </table>

  下面的例子将当前时间显示为“月，日，年”格式：

  ```mysql
  mysql> select DATE_FORMAT(NOW(), '%M,%D,%Y');
  +--------------------------------+
  | DATE_FORMAT(NOW(), '%M,%D,%Y') |
  +--------------------------------+
  | August,14th,2020               |
  +--------------------------------+
  1 row in set (0.00 sec)
  ```

+ **DATE_ADD(date,INTERVAL expr type)函数**：返回与所给日期date 相差INTERVAL 时间段的日期。其中INTERVAL 是间隔类型关键字，expr 是一个表达式，这个表达式对应后面的类型，type是间隔类型，MySQL 提供了13 种间隔类型:
  
<table>
      <thead>
      	<tr>
          	<td>表达式类型</td>
              <td>描述</td>
              <td>格式</td>
          </tr>
      </thead>
      <tbody>
      	<tr>
          	<td>HOUR </td>
              <td>小时</td>
              <td>hh</td>
          </tr>
          <tr>
          	<td>MINUTE</td>
              <td>分</td>
              <td>mm</td>
          </tr>
          <tr>
          	<td>SECOND</td>
              <td>秒</td>
              <td>ss</td>
          </tr>
          <tr>
          	<td>YEAR</td>
              <td>年</td>
              <td>YY</td>
          </tr>
          <tr>
          	<td>MONTH</td>
              <td>月</td>
              <td>MM</td>
          </tr>
          <tr>
          	<td>DAY</td>
              <td>日</td>
              <td>DD</td>
          </tr>
          <tr>
          	<td>YEAR_MONTH</td>
              <td>年和月</td>
              <td>YY_MM</td>
          </tr>
          <tr>
          	<td>DAY_HOUR</td>
              <td>日和小时</td>
              <td>DD hh</td>
          </tr>
         <tr>
          	<td>DAY_MINUTE</td>
              <td>日和分钟</td>
              <td>DD hh:mm</td>
          </tr>
          <tr>
          	<td>DAY_SECOND</td>
              <td>日和秒</td>
              <td>DD hh:mm:ss</td>
          </tr>
          <tr>
          	<td>HOUR_MINUTE</td>
              <td>小时和分</td>
              <td>hh:ss</td>
          </tr>
          <tr>
          	<td>HOUR_SECOND</td>
              <td>小时和秒</td>
              <td>hh:ss</td>
          </tr>
          <tr>
          	<td>MINUTE_SECOND</td>
              <td>分钟和秒</td>
              <td>mm:ss</td>
          </tr>
      </tbody>
  </table>
  
来看一个具体的例子，在这个例子中第1 列返回了当前日期时间，第2 列返回距离当前日期31 天后的日期时间，第3 列返回距离当前日期一年两个月后的日期时间。
  
```mysql
  mysql> select now() current, date_add(now(),INTERVAL 31 day) after31days, date_add(now(), interval '1_2' year_month) after_oneyear_twomonth;
  +---------------------+---------------------+------------------------+
  | current             | after31days         | after_oneyear_twomonth |
  +---------------------+---------------------+------------------------+
  | 2020-08-14 17:46:38 | 2020-09-14 17:46:38 | 2021-10-14 17:46:38    |
  +---------------------+---------------------+------------------------+
  1 row in set (0.00 sec)
  ```
  
同样也可以用负数让它返回之前的某个日期时间，如下第1 列返回了当前日期时间，第2列返回距离当前日期31 天前的日期时间，第3 列返回距离当前日期一年两个月前的日期时间。
  
```mysql
  mysql> select now() current, date_add(now(),INTERVAL -31 day) before31days, date_add(now(), interval '-1_-2' year_month) before_oneyear_twomonth;
  +---------------------+---------------------+-------------------------+
  | current             | before31days        | before_oneyear_twomonth |
  +---------------------+---------------------+-------------------------+
  | 2020-08-14 17:48:19 | 2020-07-14 17:48:19 | 2019-06-14 17:48:19     |
  +---------------------+---------------------+-------------------------+
  1 row in set (0.00 sec)
  ```
  
+ **DATEDIFF（date1，date2）函数：**用来计算两个日期之间相差的天数。

  ```mysql
  mysql> select datediff('2020-10-1', now());
  +------------------------------+
  | datediff('2020-10-1', now()) |
  +------------------------------+
  |                           48 |
  +------------------------------+
  1 row in set (0.00 sec)
  ```

  



## 4、流程函数

流程函数也是常用的一类函数，用户可以使用这类函数在一个SQL语句中实现条件选择，这样做能够提高语句的效率。下表列出了MySQL中跟条件选择有关的流程函数。下面将通过具体的事例来讲解每个函数的用法

| 函数                                                         | 功能                                            |
| ------------------------------------------------------------ | ----------------------------------------------- |
| IF(value, t, f)                                              | 如果value是真，返回t；否则返回f                 |
| IFNULL(value1,value2)                                        | 如果value1不为空，返回value1；否则返回value2    |
| CASE WHEN [value1] THEN [result1]... ELSE [default] END      | 如果value是真，返回resule1，否则返回 default    |
| CASE[expr] WHEN [value] THEN [result1] .... ELSE [default] END | 如果expr等于value1,返回result1，否则返回default |

下面的例子中模拟了对职员薪资进行分类，这里首先创建并初始化一个职员薪水表

```mysql
mysql> create table salary(userid int,salary decimal(9,2));
Query OK, 0 rows affected (0.07 sec)
```

插入一些测试数据

```mysql
mysql> insert into salary values(1,1000),(2,2000),(3,3000),(4,4000),(5,5000),(1,null);
Query OK, 6 rows affected (0.01 sec)
Records: 6  Duplicates: 0  Warnings: 0

mysql> select * from salary;
+--------+---------+
| userid | salary  |
+--------+---------+
|      1 | 1000.00 |
|      2 | 2000.00 |
|      3 | 3000.00 |
|      4 | 4000.00 |
|      5 | 5000.00 |
|      1 |    NULL |
+--------+---------+
6 rows in set (0.00 sec)
```

接下来通过这个表来介绍各个函数的应用

+ **IF(value, t ，f)函数**：这里认为月薪在2000 元以上的职员属于高薪，用“high”来表示，而200元以下的职员属于低薪，用“low”来表示。

  ```mysql
  mysql> select userid, salary, if(salary>2000, 'high', 'low') as slary_level from salary;
  +--------+---------+-------------+
  | userid | salary  | slary_level |
  +--------+---------+-------------+
  |      1 | 1000.00 | low         |
  |      2 | 2000.00 | low         |
  |      3 | 3000.00 | high        |
  |      4 | 4000.00 | high        |
  |      5 | 5000.00 | high        |
  |      1 |    NULL | low         |
  +--------+---------+-------------+
  6 rows in set (0.00 sec)
  ```

+ **IFNULL(value1,value2) 函数**：这个函数一般用来替换NULL值，我们知道NULL值是不能参与数值运算的。下面这个语句就是把NULL值用0来替换：

  ```mysql
  mysql> select userid,salary,ifnull(salary,0) from salary;
  +--------+---------+------------------+
  | userid | salary  | ifnull(salary,0) |
  +--------+---------+------------------+
  |      1 | 1000.00 |          1000.00 |
  |      2 | 2000.00 |          2000.00 |
  |      3 | 3000.00 |          3000.00 |
  |      4 | 4000.00 |          4000.00 |
  |      5 | 5000.00 |          5000.00 |
  |      1 |    NULL |             0.00 |
  +--------+---------+------------------+
  6 rows in set (0.00 sec)
  ```

+ **CASE [expr] WHEN [value] THEN [result1] .... ELSE [default] END 函数**：这是case的简单函数用法，case后面跟列名或者列的表达式，when 后面枚举这个表达式所有可能的值，但不能是值的范围。如果要实现上面例子中高薪低薪的问题，写法如下：

  ```mysql
  mysql> select userid,salary,case salary when 1000 then 'low' when 2000 then 'low' else 'high' end salary_level from salary;
  +--------+---------+--------------+
  | userid | salary  | salary_level |
  +--------+---------+--------------+
  |      1 | 1000.00 | low          |
  |      2 | 2000.00 | low          |
  |      3 | 3000.00 | high         |
  |      4 | 4000.00 | high         |
  |      5 | 5000.00 | high         |
  |      1 |    NULL | high         |
  +--------+---------+--------------+
  6 rows in set (0.00 sec)
  ```

  

+ **CASE WHEN [value1] THEN [result1]... ELSE [default] END 函数**：这是case的搜索函数用法，直接在when后面写条件表达式，并且只返回第一个符合条件的值，使用起来更加灵活。上例可以改写如下：

  ```mysql
  mysql> select userid,salary, case when salary<=2000 then 'low' else 'high' end from salary;
  +--------+---------+---------------------------------------------------+
  | userid | salary  | case when salary<=2000 then 'low' else 'high' end |
  +--------+---------+---------------------------------------------------+
  |      1 | 1000.00 | low                                               |
  |      2 | 2000.00 | low                                               |
  |      3 | 3000.00 | high                                              |
  |      4 | 4000.00 | high                                              |
  |      5 | 5000.00 | high                                              |
  |      1 |    NULL | high                                              |
  +--------+---------+---------------------------------------------------+
  6 rows in set (0.00 sec)
  ```





## 5、JSON 函数

自MySQL5.7.8 新引入了JSON文档类型，对于JSON文档的操作，除了简单的读写之外，通常还会有各种各样的查询、修改等需求，为此MySQL 也提供了很多相应的函数:

<table>
    <tr>
    	<td>函数类型</td>
        <td>名称</td>
        <td>功能</td>
    </tr>
    <tr>
    	<td rowspan=3>创建JSON</td>
        <td>JSON_ARRAY()</td>
        <td>创建JSON数组</td>
    </tr>
    <tr>
        <td>JSON_OBJECT()</td>
    <td>创建JSON对象</td>
</tr>
<tr>
    <td>JSON_QUOTE() / JSON_UNQUOTE()</td>
<td>加上 / 去掉JSON文档两边的双引号</td>
    </tr>
    <tr>
    	<td rowspan=5>查询JSON</td>
        <td>JSON_CONTAINS()</td>
        <td>查询文档中是否包含指定的元素</td>
    </tr>
    <tr>
        <td>JSON_CONTAINS_PATH()</td>
    <td>查询文档中是否包含指定的路径</td>
</tr>
<tr>
    <td>JSON_EXTRACT()/->/->></td>
<td>根据条件提取文档中的数据</td>
    </tr>
     <tr>
        <td>JSON_KEYS()</td>
    <td>提取所有key的集合</td>
</tr>
<tr>
    <td>JSON_SEARCH()</td>
<td>返回所有符合条件的路径集合</td>
    </tr>
     <tr>
    	<td rowspan=6>修改JSON</td>
        <td>JSON_MERGE()(deprecated 5.7.22)/JSON_MERGE_PRESERVE</td>
        <td>将两个文档合并</td>
    </tr>
    <tr>
        <td>JSON_ARRAY_APPEND()</td>
    <td>数组尾部追加元素</td>
</tr>
<tr>
    <td>JSON_ARRAY_INSERT()</td>
<td>在数组的指定位置插入元素</td>
    </tr>
     <tr>
        <td>JSON_REMOVE()</td>
    <td>删除文档中指定位置的元素</td>
</tr>
<tr>
    <td>JSON_REPLACE()</td>
<td>替换文档中指定位置的元素</td>
    </tr>
    <tr>
    <td>JSON_SET()</td>
<td>给文档中指定位置的元素设置新值，如果元素不存在，则进行插入</td>
    </tr>
     <tr>
    	<td rowspan=4>查询JSON元数据</td>
        <td>JSON_DEPTH()</td>
        <td>JSON文档的深度（元素最大嵌套层数）</td>
    </tr>
    <tr>
        <td>JSON_LENGTH()</td>
    <td>JSON文档的长度（元素个数）</td>
</tr>
<tr>
    <td>JSON_TYPE()</td>
<td>JSON文档类型（数组、对象、标量类型）</td>
    </tr>
     <tr>
        <td>JSON_VALID()</td>
    <td>JOSN格式是否合法</td>
</tr>
<tr>
    	<td rowspan=6>其它函数</td>
        <td>JSON_PRETTY()</td>
        <td>美化JSON格式</td>
    </tr>
    <tr>
        <td>JSON_STORAGE_SIZE()</td>
    <td>JSON文档占用的存储空间</td>
</tr>
<tr>
    <td>JSON_STORAGE_FREE()</td>
<td>JSON文档更新操作后剩余的空间，MySQL8.0新增</td>
    </tr>
     <tr>
        <td>JSON_TABLE()</td>
    <td>将JSON文档转换为表格，MySQL8.0新增</td>
</tr>
<tr>
    <td>JSON_ARRAYAGG()</td>
<td>将聚合后参数中的多个值转换为JSON数组</td>
    </tr>
    <tr>
    <td>JSON_OBJECTAGG()</td>
<td>把连个列或者是表达式解释为一个Key 和一个value 返回一个JSON对象</td>
    </tr>
</table>


这些函数安装功能可以分为以下几类：

+ 创建JSON函数
+ 查询JSON函数
+ 修改JSON函数
+ chaxunJSONyuanshujuhanshu其它函数

下面将详细介绍这些函数



### 5.1、创建JSON函数

#### 5.1.1、 JSON_ARRAY([val[,val]...])

此函数可以返回包含参数中所有值列表的JSON数组。

以下示例创建一个包含数字、字符串、null、布尔、日期类型在内的混合数组，需要注意的是，参数中的null 和 true/false 大小写不敏感。

```mysql
mysql> select JSON_ARRAY(1,"abc",null,TRUE,curtime());
+-------------------------------------------+
| JSON_ARRAY(1,"abc",null,TRUE,curtime())   |
+-------------------------------------------+
| [1, "abc", null, true, "00:02:21.000000"] |
+-------------------------------------------+
1 row in set (0.00 sec)
```



#### 51.2、 JSON_OBJECT([key,val[,key,val] ...])

此函数可以返回包含参数中所有键值对的对象列表。canshuzhongdeKey不能为null，参数个数也不能为奇数，否则报语法错误。

以下示例使用了正确的语法：

```mysql
mysql> select JSON_OBJECT('id',100,'name','jack');
+-------------------------------------+
| JSON_OBJECT('id',100,'name','jack') |
+-------------------------------------+
| {"id": 100, "name": "jack"}         |
+-------------------------------------+
1 row in set (0.00 sec)
```

以下示例则使用了错误的语法：

```mysql
mysql> select JSON_OBJECT('id',100,'name');
ERROR 1582 (42000): Incorrect parameter count in the call to native function 'JSON_OBJECT'
```



#### 5.1.3、 JSON_QUOTE(string)

此函数可以将参数中的JSON文档转换为双引号引起来的字符串,如果JSON文档中包含双引号，则转换后的字符串自动加上转义字符"\"，如以下示例：

```mysql
mysql> select JSON_QUOTE('[1,2,3]'),JSON_QUOTE('"null"');
+-----------------------+----------------------+
| JSON_QUOTE('[1,2,3]') | JSON_QUOTE('"null"') |
+-----------------------+----------------------+
| "[1,2,3]"             | "\"null\""           |
+-----------------------+----------------------+
1 row in set (0.00 sec)
```

如果需要将非JSON文档转换为JSON文档，或则反过来，可以使用CONVERT或者CAST函数进行强制转换，这两个函数可以在不同数据类型之间进行强制转换，具体用法参考官方文档。



### 5.2、查询JSON函数

#### 5.2.1、JSON_CONTAINS(target,candidate[,path])

此函数可以查询指定的元素（candidate）是否包含在目标JSON文档（target）中，包含则返回1，否则返回0，path参数可选。如果有参数为NULL 或path不存在，则返回NULL。

以下示例分别要查询元素‘abc’,1,10是否包含在JSON文档中：

```mysql
mysql> select json_contains('[1,2,3,"abc",null]','"abc"');
+---------------------------------------------+
| json_contains('[1,2,3,"abc",null]','"abc"') |
+---------------------------------------------+
|                                           1 |
+---------------------------------------------+
1 row in set (0.00 sec)

mysql> select json_contains('[1,2,3,"abc",null]','1');
+-----------------------------------------+
| json_contains('[1,2,3,"abc",null]','1') |
+-----------------------------------------+
|                                       1 |
+-----------------------------------------+
1 row in set (0.00 sec)

mysql> select json_contains('[1,2,3,"abc",null]','10');
+------------------------------------------+
| json_contains('[1,2,3,"abc",null]','10') |
+------------------------------------------+
|                                        0 |
+------------------------------------------+
1 row in set (0.00 sec)
```

显然结果符合我们的预期。

元素如果是数组也是可以的：

```mysql
mysql> select json_contains('[1,2,3,"abc",null]','[1,3]');
+---------------------------------------------+
| json_contains('[1,2,3,"abc",null]','[1,3]') |
+---------------------------------------------+
|                                           1 |
+---------------------------------------------+
1 row in set (0.00 sec)
```

path参数是可选的，可以指定在特定的路径下查询。如果JSON文档为对象，则路径格式通常类似于$.a或者$.a.b这种格式。$.a很好理解，表示Key为a;$.a.b通常用在value也是对象列表的情况，表示键a下层的键b，比如{"id":{"id1":1,"id2":2}}。如果JSON文档为数组，则路径通常写为$[i] 这种格式，表示数组中第i个元素。

在下例中，要查询JSON文档 j 中是否包含value为10的对象，并指定路径为$.jack(key='jack'),如果包含则返回1，如果不包含则返回0.那么SQL代码可以这么写：

```mysql
mysql> set @j='{"jack":10,"tom":20,"lisa":30}';
Query OK, 0 rows affected (0.00 sec)

mysql> set @j2='10';
Query OK, 0 rows affected (0.00 sec)

mysql> select json_contains(@j,@j2,'$.jack');
+--------------------------------+
| json_contains(@j,@j2,'$.jack') |
+--------------------------------+
|                              1 |
+--------------------------------+
1 row in set (0.00 sec)
```

​	返回1，表示在路径key="jack"下，存在value为10的值。将查询路径改为tom后，再次查询：

```mysql
mysql> select json_contains(@j,@j2,'$.tom');
+-------------------------------+
| json_contains(@j,@j2,'$.tom') |
+-------------------------------+
|                             0 |
+-------------------------------+
1 row in set (0.00 sec)
```

​	此时返回0，则表示JSON文档中不包含{"tom":10}的元素



#### 5.2.2、JSON_CONTAINS_PATH(json_doc, one_or_all, path[, path]...)

此函数可以查询JSON文档中是否存在指定路径，存在则返回1，否则返回0.one_or_all只能取值 one  或 all， one表示只要有一个存在即可；all 表示所有的都存在才行。如果有参数为NULL或path不存在，则返回NULL。

​	比如，要查询给定的3个path 是否至少一个存在或者必须全部存在，可以分别写SQL代码如下：

```mysql
mysql> select json_contains_path('{"k1":"jack","k2":"tom","k3":"lisa"}','one','$.k1','$.k4') one_path;
+----------+
| one_path |
+----------+
|        1 |
+----------+
1 row in set (0.00 sec)

mysql> select json_contains_path('{"k1":"jack","k2":"tom","k3":"lisa"}','all','$.k1','$.k4') one_path;
+----------+
| one_path |
+----------+
|        0 |
+----------+
1 row in set (0.00 sec)

mysql> select json_contains_path('{"k1":"jack","k2":"tom","k3":"lisa"}','all','$.k1','$.k3') one_path;
+----------+
| one_path |
+----------+
|        1 |
+----------+
1 row in set (0.00 sec)
```



#### 5.2.3、JSON_EXTRACT(json_doc, path[, path]...)

此函数可以从JSON文档里抽取数据。如果有参数有NULL或者path不存在，则返回NULL。如果抽取出多个path，则返回的数据合并在一个JSON ARRAY里。

以下示例从JSON文档的第一和第二个元素中提取出对应的value：

```mysql
mysql> select JSON_EXTRACT('[10,20,[30,40]]','$[0]','$[1]');
+-----------------------------------------------+
| JSON_EXTRACT('[10,20,[30,40]]','$[0]','$[1]') |
+-----------------------------------------------+
| [10, 20]                                      |
+-----------------------------------------------+
1 row in set (0.00 sec)
```

可以看到，返回的两个值以数组的形式进行了合并。如果要取第三个数组值，path可以写为 $2或者\$\[2][*]:

```mysql
mysql> select JSON_EXTRACT('[10,20,[30,40]]','$[0]','$[2]');
+-----------------------------------------------+
| JSON_EXTRACT('[10,20,[30,40]]','$[0]','$[2]') |
+-----------------------------------------------+
| [10, [30, 40]]                                |
+-----------------------------------------------+
1 row in set (0.00 sec)

mysql> select JSON_EXTRACT('[10,20,[30,40]]','$[0]','$[2][0]');
+--------------------------------------------------+
| JSON_EXTRACT('[10,20,[30,40]]','$[0]','$[2][0]') |
+--------------------------------------------------+
| [10, 30]                                         |
+--------------------------------------------------+
1 row in set (0.00 sec)
```

在MySQL 5.7.9版本之后，可以用一种更简单的函数"->"来替代JSON_EXTRACT,语法如下：

```mysql
column->path
```

​	注意左边只能是列名，不能是表达式；右边是要匹配的JSON路径。上面的例子可以改写为：

```mysql
mysql> desc t1;
+-------+------+------+-----+---------+-------+
| Field | Type | Null | Key | Default | Extra |
+-------+------+------+-----+---------+-------+
| id1   | json | YES  |     | NULL    |       |
+-------+------+------+-----+---------+-------+
1 row in set (0.01 sec)

mysql> insert into t1 values('[10,20,[30,40]]');
Query OK, 1 row affected (0.01 sec)

mysql> select id1,id1->"$[1]" from t1 where id1->"$[0]"=10;
+--------------------+-------------+
| id1                | id1->"$[1]" |
+--------------------+-------------+
| [10, 20, [30, 40]] | 20          |
+--------------------+-------------+
1 row in set (0.00 sec)
```

​	如果JSON文档查询的结果是字符串，则显示结果默认会包含双引号，在很多情况下是不需要的，为了解决这个问题，MySQL提供了另外两个函数 JSON_UNQUOTE 和 “->>”, 用法类似于 JSON_EXTRACT 和 ‘->’ ，简单举例如下：

```mysql
mysql> insert into t1 values('{"k1":"jack"}');
Query OK, 1 row affected (0.01 sec)

mysql> select json_extract (id1,'$.k1'),json_unquote(id1->'$.k1'),id1->'$.k1',id1->>'$.k1' from t1 where id1->'$.k1'='jack';
+---------------------------+---------------------------+-------------+--------------+
| json_extract (id1,'$.k1') | json_unquote(id1->'$.k1') | id1->'$.k1' | id1->>'$.k1' |
+---------------------------+---------------------------+-------------+--------------+
| "jack"                    | jack                      | "jack"      | jack         |
+---------------------------+---------------------------+-------------+--------------+
1 row in set (0.00 sec)
```

​	及下面3种写法效果是一样的：

1. JSON_UNQUOTE(JSON_EXTRACT(column, path))
2. JSON_UNQUOTE(column->path)
3. column ->> path



#### 5.2.4、JSON_KEYS(json_doc [,path])

此函数可以获取JSON文档在指定路径下的所有键值，返回一个JSON ARRAY。如果有参数为null 或path不存在，则返回null（8.0.16 path不存在，则返回所有的key）

​	参数path 通常使用在嵌套对象列表中，如下例所示：

```mysql
mysql> select JSON_KEYS('{"a":1,"b":{"c":30}}');
+-----------------------------------+
| JSON_KEYS('{"a":1,"b":{"c":30}}') |
+-----------------------------------+
| ["a", "b"]                        |
+-----------------------------------+
1 row in set (0.00 sec)

mysql> select JSON_KEYS('{"a":1,"b":{"c":30}}','$.b');
+-----------------------------------------+
| JSON_KEYS('{"a":1,"b":{"c":30}}','$.b') |
+-----------------------------------------+
| ["c"]                                   |
+-----------------------------------------+
1 row in set (0.00 sec)
```

​	如果元素中都是数组 ARRAY，则返回为NULL。



#### 5.2.5、JSON_SEARCH(json_doc, one_or_all, search_str[, escape_char [,path]])

此函数可以查询包含指定字符串的路径，并作为一个JSON ARRAY返回。如果有参数为NULL，或path不存在，则返回NULL，各参数含义如下：

+ one_or_all: one表示查询到一个即返回，all表示查询所有
+ search_str:要查询的字符串，可以用LIKE里的‘%’ 或‘_'匹配
+ path：表示在指定path下进行查询。

以下示例给出了如何查询JSON文档中以字母t开头的元素的第一个路径:

```mysql
mysql> select json_search('{"k1":"jack","k2":"tom","k3":"lisa","k4":"tony"}','one','t%');
+----------------------------------------------------------------------------+
| json_search('{"k1":"jack","k2":"tom","k3":"lisa","k4":"tony"}','one','t%') |
+----------------------------------------------------------------------------+
| "$.k2"                                                                     |
+----------------------------------------------------------------------------+
1 row in set (0.00 sec)
```

​	可以看出，满足条件的第一个元素是“k2”:"tom",path描述为"$.k2"

下面讲条件“one” 改成"all",在看看结果：

```mysql
mysql> select json_search('{"k1":"jack","k2":"tom","k3":"lisa","k4":"tony"}','all','t%');
+----------------------------------------------------------------------------+
| json_search('{"k1":"jack","k2":"tom","k3":"lisa","k4":"tony"}','all','t%') |
+----------------------------------------------------------------------------+
| ["$.k2", "$.k4"]                                                           |
+----------------------------------------------------------------------------+
1 row in set (0.00 sec)
```

​	此时，满足条件的所有元素是："k2":"tom" 和 “k4”:"tony",路径描述为["$.k2", "$.k4"]  数组。

​	如果将JSON文档改为数组，则返回路径也将成为数组的描述格式，如下例所示：

```mysql
mysql> select json_search('["tom","lisa","jack",{"name":"tony"}]',"all","t%");
+-----------------------------------------------------------------+
| json_search('["tom","lisa","jack",{"name":"tony"}]',"all","t%") |
+-----------------------------------------------------------------+
| ["$[0]", "$[3].name"]                                           |
+-----------------------------------------------------------------+
1 row in set (0.00 sec)
```



### 5.3、修改JSON的函数

#### 5.31、JSON_ARRAY_APPEND(json_doc, path, val[,path,val]...)

​	此函数可以再指定path的是json array尾部追加val。如果指定path是一个json object,则将其封装成一个json array再追加。如果有参数为null，则返回null。

​	以下示例在JSON文档的不同path处分别追加字符 “1”；

```mysql
mysql> select JSON_ARRAY_APPEND('["a",["b","c"],"d"]', '$[0]', "1");
+-------------------------------------------------------+
| JSON_ARRAY_APPEND('["a",["b","c"],"d"]', '$[0]', "1") |
+-------------------------------------------------------+
| [["a", "1"], ["b", "c"], "d"]                         |
+-------------------------------------------------------+
1 row in set (0.00 sec)

mysql> select JSON_ARRAY_APPEND('["a",["b","c"],"d"]', '$[1]',"1");
+------------------------------------------------------+
| JSON_ARRAY_APPEND('["a",["b","c"],"d"]', '$[1]',"1") |
+------------------------------------------------------+
| ["a", ["b", "c", "1"], "d"]                          |
+------------------------------------------------------+
1 row in set (0.00 sec)

mysql> select JSON_ARRAY_APPEND('["a",["b","c"],"d"]', '$[1][0]',"1");
+---------------------------------------------------------+
| JSON_ARRAY_APPEND('["a",["b","c"],"d"]', '$[1][0]',"1") |
+---------------------------------------------------------+
| ["a", [["b", "1"], "c"], "d"]                           |
+---------------------------------------------------------+
1 row in set (0.00 sec)

mysql> select json_array_append('{"a":1,"b":[2,3],"c":4}', "$.b", "1");
+----------------------------------------------------------+
| json_array_append('{"a":1,"b":[2,3],"c":4}', "$.b", "1") |
+----------------------------------------------------------+
| {"a": 1, "b": [2, 3, "1"], "c": 4}                       |
+----------------------------------------------------------+
1 row in set (0.00 sec)
```



#### 5.3.2、JSON_ARRAY_INSERT(json_doc, path, val[,path, val]...)

​	此函数可以在path指定json array 元素插入val ，原位置及以右的元素顺序右移。如果path指定的数据非json array 元素，则略过此val；如果指定的元素下标超过 json array的长度，则插入尾部。

​	将上面例子中的4个SQL语句改成JSON_ARRAY_INSERT ,看一下结果：

```mysql
mysql> select JSON_ARRAY_INSERT('["a",["b","c"],"d"]', '$[0]', '1');
+-------------------------------------------------------+
| JSON_ARRAY_INSERT('["a",["b","c"],"d"]', '$[0]', '1') |
+-------------------------------------------------------+
| ["1", "a", ["b", "c"], "d"]                           |
+-------------------------------------------------------+
1 row in set (0.00 sec)

mysql> select JSON_ARRAY_INSERT('["a",["b","c"],"d"]', '$[1]', '1');
+-------------------------------------------------------+
| JSON_ARRAY_INSERT('["a",["b","c"],"d"]', '$[1]', '1') |
+-------------------------------------------------------+
| ["a", "1", ["b", "c"], "d"]                           |
+-------------------------------------------------------+
1 row in set (0.00 sec)

mysql> select JSON_ARRAY_INSERT('["a",["b","c"],"d"]', '$[1][0]', '1');
+----------------------------------------------------------+
| JSON_ARRAY_INSERT('["a",["b","c"],"d"]', '$[1][0]', '1') |
+----------------------------------------------------------+
| ["a", ["1", "b", "c"], "d"]                              |
+----------------------------------------------------------+
1 row in set (0.00 sec)

mysql> select json_array_insert('{"a":1,"b":[2,3],"c":4}','$.b', '1');
ERROR 3165 (42000): A path expression is not a path to a cell in an array.
```

​	最后一个SQL报错，提示路径不对，将"$.b" 改为“$[0]” 试一试：

```mysql
mysql> select json_array_insert('{"a":1,"b":[2,3],"c":4}','$[0]', '1');
+----------------------------------------------------------+
| json_array_insert('{"a":1,"b":[2,3],"c":4}','$[0]', '1') |
+----------------------------------------------------------+
| {"a": 1, "b": [2, 3], "c": 4}                            |
+----------------------------------------------------------+
1 row in set (0.00 sec)
```

​	插入路径正确，但是字符并没有插入JSON文档中，因为所有元素都是对象，跳过忽略。



#### 5.3.3、JSON_REPLACE(json_doc, path, val[, path, val]...)

​	此函数可以替换指定路径的数据，如果某个路径不存在，则略过（存在才替换），如果有参数为null，则返回null。

​	下面将JSON文档中的第一个元素和第二个元素分别替换为“1” 和“2”

```mysql
mysql> select json_replace('["a",["b","c"],"d"]', "$[0]","1", "$[1]", 2);
+------------------------------------------------------------+
| json_replace('["a",["b","c"],"d"]', "$[0]","1", "$[1]", 2) |
+------------------------------------------------------------+
| ["1", 2, "d"]                                              |
+------------------------------------------------------------+
1 row in set (0.00 sec)
```

​	下例将JOSN文档中的key为a和d的对象value分别替换为“10” 和“20”：

```mysql
mysql> select json_replace('{"a":1,"b":[2,3],"c":4 }', '$.a',"10",'$.d', "20");
+------------------------------------------------------------------+
| json_replace('{"a":1,"b":[2,3],"c":4 }', '$.a',"10",'$.d', "20") |
+------------------------------------------------------------------+
| {"a": "10", "b": [2, 3], "c": 4}                                 |
+------------------------------------------------------------------+
1 row in set (0.00 sec)
```



#### 5.3.4、JSON_SET(json_doc, path, val[, path,val]...)

​	此函数可以设置指定路径的数据（不管是否存在）。如果有参数为null则返回null，和JSON_REPLACE功能有些类似，最主要的区别是指定的路径不存在时，会在文档中自动添加，如下例所示：

```mysql
mysql> select JSON_SET('{"a":1,"b":[2,3],"c":4}','$.a',10,"$.d",20);
+-------------------------------------------------------+
| JSON_SET('{"a":1,"b":[2,3],"c":4}','$.a',10,"$.d",20) |
+-------------------------------------------------------+
| {"a": 10, "b": [2, 3], "c": 4, "d": 20}               |
+-------------------------------------------------------+
1 row in set (0.00 sec)
```



#### 5.3.5、JSON_MERGE_PRESERVE(json_doc, json_doc[, json_doc]...)

​	此函数可以将多个JSON文档进行合并，合并规则如下：

+ 如果是 json array,则结果自动 merge 为一个json array
+ 如果是 json object ,则结果自动 merge 为一个json object
+ 如果有多种类型，则将非json array的元素封装成一个 json array再按照规则进行merge。

下例分别将两个数组合并、两个对象合并、数组和对象合并

```mysql
mysql> select json_merge_preserve('[1,2]','[3,4]');
+--------------------------------------+
| json_merge_preserve('[1,2]','[3,4]') |
+--------------------------------------+
| [1, 2, 3, 4]                         |
+--------------------------------------+
1 row in set (0.00 sec)

mysql> select json_merge_preserve('{"key1":"tom"}','{"key2":"lisa"}');
+---------------------------------------------------------+
| json_merge_preserve('{"key1":"tom"}','{"key2":"lisa"}') |
+---------------------------------------------------------+
| {"key1": "tom", "key2": "lisa"}                         |
+---------------------------------------------------------+
1 row in set (0.00 sec)

mysql> select json_merge_preserve('[1,2]','{"key1":"tom"}');
+-----------------------------------------------+
| json_merge_preserve('[1,2]','{"key1":"tom"}') |
+-----------------------------------------------+
| [1, 2, {"key1": "tom"}]                       |
+-----------------------------------------------+
1 row in set (0.00 sec)
```



#### 5.3.6、JSON_REMOVE(json_doc, path[,path]...)

​	此函数可以移除指定路径的数据，如果某个路径不存在则略过此路径。如果有参数为NULL，则返回NULL

​	下例中把JSON文档中的第二个和第三个元素删除：

```mysql
mysql> select json_remove('[1,2,3,4]',"$[1]","$[2]");
+----------------------------------------+
| json_remove('[1,2,3,4]',"$[1]","$[2]") |
+----------------------------------------+
| [1, 3]                                 |
+----------------------------------------+
1 row in set (0.00 sec)
```

​	结果有些意外，‘$[1]’,‘$[2]’ 分别为2和3，删除后不是应该为[1，4]，吗?这里要注意，如果指定了多个path，则删除操作是串行操作的，即先删除“$[1]”后JSON文档变为[1,3,4] ,然后在[1,3,4] 上删除'$[2]'后变为[1,3]



### 5.4、查询JSON元数据函数

#### 5.4.1、JSON_DEPTH(json_doc)

​	此函数用来获取json文档的深度。

​	如果文档是空数组，空对象、null、true/false，则深度为：1；如果非空数组，非空对象里面包含的都是深度为1的对象，则整个文档省深度为2；依次类推，整个文档的深度取决于最大元素的深度。如下例所示：

```mysql
mysql> select json_depth('{}'),json_depth('[]'),json_depth('true');
+------------------+------------------+--------------------+
| json_depth('{}') | json_depth('[]') | json_depth('true') |
+------------------+------------------+--------------------+
|                1 |                1 |                  1 |
+------------------+------------------+--------------------+
1 row in set (0.00 sec)

mysql> select json_depth('[10,20]'),json_depth('[[],{}]');
+-----------------------+-----------------------+
| json_depth('[10,20]') | json_depth('[[],{}]') |
+-----------------------+-----------------------+
|                     2 |                     2 |
+-----------------------+-----------------------+
1 row in set (0.00 sec)


mysql> select json_depth('[10,{"a":20}]');
+-----------------------------+
| json_depth('[10,{"a":20}]') |
+-----------------------------+
|                           3 |
+-----------------------------+
1 row in set (0.00 sec)
```



#### 5.4.2、JSON_LENGTH(json_doc[,path])

此函数可以获取指定路径下的文档长度。长度的计算规则如下：

+ 标量（字符串、数字）的长度为1
+ json array的长度为元素的个数
+ json object的长度为对象的个数
+ 嵌套数组或则嵌套对象不计算长度

见下例所示：

```mysql
mysql> select json_length('1'),json_length('[1,2,[3,4]]'), json_length('{"key":"tom"}');
+------------------+----------------------------+------------------------------+
| json_length('1') | json_length('[1,2,[3,4]]') | json_length('{"key":"tom"}') |
+------------------+----------------------------+------------------------------+
|                1 |                          3 |                            1 |
+------------------+----------------------------+------------------------------+
1 row in set (0.00 sec)
```



#### 5.4.3、JSON_TYPE(json_val)

​	此函数可以获取JSON文档的具体类型，可以是数组、对象或者标量类型。

```mysql
mysql> select json_type('[1,3]'),json_type('{"id":"tom"}');
+--------------------+---------------------------+
| json_type('[1,3]') | json_type('{"id":"tom"}') |
+--------------------+---------------------------+
| ARRAY              | OBJECT                    |
+--------------------+---------------------------+
1 row in set (0.00 sec)

mysql> select json_type('1'),json_type('"abc"'),json_type('null'),json_type('true');
+----------------+--------------------+-------------------+-------------------+
| json_type('1') | json_type('"abc"') | json_type('null') | json_type('true') |
+----------------+--------------------+-------------------+-------------------+
| INTEGER        | STRING             | NULL              | BOOLEAN           |
+----------------+--------------------+-------------------+-------------------+
1 row in set (0.00 sec)
```



#### 5.4.4、JSON_VALID(val)

​	此函数判断val是否为有效的JSON格式，有效为1，否则为0.

```mysql
mysql> select json_valid('abc'),json_valid('"abc"'),json_valid('[1,2]'),json_valid('[1,2');
+-------------------+---------------------+---------------------+--------------------+
| json_valid('abc') | json_valid('"abc"') | json_valid('[1,2]') | json_valid('[1,2') |
+-------------------+---------------------+---------------------+--------------------+
|                 0 |                   1 |                   1 |                  0 |
+-------------------+---------------------+---------------------+--------------------+
1 row in set (0.00 sec)
```

​	显然，字符串两边不加双引号是无效的JSON格式， ‘[1,2’ 少了右中括号也是无效的，都返回0。



### 5.5 JSON工具函数

#### 5.5.1、JSON_PRETTY(json_val)

​	此函数是在 5.7.22版本中新增的，用来美化JSON的输出格式，使得结果更加易读。对于数组、对象，每一行显示一个元素，多层嵌套的元素会在新行中进行缩进，清楚地显示层级关系，如下例所示：

```mysql
mysql> select json_pretty('{"a":"10","b":"15","x":{"x1":1,"x2":2,"x3":3}}');
+----------------------------------------------------------------------------------+
| json_pretty('{"a":"10","b":"15","x":{"x1":1,"x2":2,"x3":3}}')                    |
+----------------------------------------------------------------------------------+
| {
  "a": "10",
  "b": "15",
  "x": {
    "x1": 1,
    "x2": 2,
    "x3": 3
  }
} |
+----------------------
```



#### 5.5.2、JSON_STORAGE_SIZE(json_val) / JSON_STORAGE_FREE(json_val)

​	JSON_STORAGE_SIZE(json_val) 函数可以获取JSON文档占用的存储空间（byte），而JSON_STORAGE_FREE(json_val)函数可以获取由于JSON_SET、JSON_REPLACE、JSON_REMOVE操作导致释放的空间。

​	其中，JSON_STORAGE_FREE 是8.0 版本新增的函数。用户可以在MySQL8.0环境下测试以下示例。下面的例子显示了对JSON字段update操作前和操作后，两个函数的显示结果。

update前：

```mysql
mysql> create table jtable(jcol json);
Query OK, 0 rows affected (0.05 sec)

mysql> insert into jtable values('{"name":"homer","Stupid":"True"}');
Query OK, 1 row affected (0.01 sec)

mysql> select JSON_STORAGE_SIZE(jcol),JSON_STORAGE_FREE(jcol), jcol from jtable;
+-------------------------+-------------------------+-------------------------------------+
| JSON_STORAGE_SIZE(jcol) | JSON_STORAGE_FREE(jcol) | jcol                                |
+-------------------------+-------------------------+-------------------------------------+
|                      40 |                       0 | {"name": "homer", "Stupid": "True"} |
+-------------------------+-------------------------+-------------------------------------+
1 row in set (0.00 sec)
```

JSON_STORAGE_SIZE显示了jcol列所占用的空间为40字节，由于没有字段更新，所以JSON_STORAGE_FREE显示为0

update后：

```mysql
mysql> update jtable set jcol=json_set(jcol,'$.Stupid',1);
Query OK, 1 row affected (0.01 sec)
Rows matched: 1  Changed: 1  Warnings: 0

mysql> select JSON_STORAGE_SIZE(jcol),JSON_STORAGE_FREE(jcol),jcol from jtable;
+-------------------------+-------------------------+--------------------------------+
| JSON_STORAGE_SIZE(jcol) | JSON_STORAGE_FREE(jcol) | jcol                           |
+-------------------------+-------------------------+--------------------------------+
|                      40 |                       5 | {"name": "homer", "Stupid": 1} |
+-------------------------+-------------------------+--------------------------------+
1 row in set (0.00 sec)
```

​	从结果上看，update操作释放了5个字节的空间，但JSON_STORAGE_SIZE(jcol)返回的结果并没有改变，仍然是40个字节，这是由于MySQL规定局部更新（使用JSON_SET/JSON_REPLACE/JSON_REMOVE函数进行操作）后的文档存储只能大于等于更新前的size。如果更新值大于原值，则JSON_STORAGE_SIZE则会大于原文档的size，如下例所示：

```mysql
mysql> update jtable set jcol=json_set(jcol,'$.Stupid','True123');
Query OK, 1 row affected (0.01 sec)
Rows matched: 1  Changed: 1  Warnings: 0

mysql> select JSON_STORAGE_SIZE(jcol),JSON_STORAGE_FREE(jcol),jcol from jtable;
+-------------------------+-------------------------+----------------------------------------+
| JSON_STORAGE_SIZE(jcol) | JSON_STORAGE_FREE(jcol) | jcol                                   |
+-------------------------+-------------------------+----------------------------------------+
|                      43 |                       0 | {"name": "homer", "Stupid": "True123"} |
+-------------------------+-------------------------+----------------------------------------+
1 row in set (0.00 sec)
```

​	由于更新操作没有释放空间，所以JSON_STORAGE_FREE 返回0，但JSON_STORAGE_SIZE已经显示增大后的size 43。对于非局部更新（即不使用 JSON_SET/JSON_REPLACE/JSON_REMOVE操作进行更新），上面的函数不满足之前的逻辑，如下例所示：

```mysql
mysql> select JSON_STORAGE_SIZE(jcol),JSON_STORAGE_FREE(jcol),jcol from jtable;
+-------------------------+-------------------------+-------------------------------------+
| JSON_STORAGE_SIZE(jcol) | JSON_STORAGE_FREE(jcol) | jcol                                |
+-------------------------+-------------------------+-------------------------------------+
|                      40 |                       0 | {"Name": "Homer", "Stupid": "TRUE"} |
+-------------------------+-------------------------+-------------------------------------+
1 row in set (0.00 sec)

mysql> update jtable set jcol='{"Name":"Homer", "Stupid":1}';
Query OK, 1 row affected (0.01 sec)
Rows matched: 1  Changed: 1  Warnings: 0

mysql> select JSON_STORAGE_SIZE(jcol),JSON_STORAGE_FREE(jcol),jcol from jtable;
+-------------------------+-------------------------+--------------------------------+
| JSON_STORAGE_SIZE(jcol) | JSON_STORAGE_FREE(jcol) | jcol                           |
+-------------------------+-------------------------+--------------------------------+
|                      35 |                       0 | {"Name": "Homer", "Stupid": 1} |
+-------------------------+-------------------------+--------------------------------+
1 row in set (0.00 sec)

```

​	显然，两个函数的结果和之前的结果都不一样，JSON_STORAGE_SIZE显示的都是JSON文档的实际size，JSON_STORAGE_FREE则永远为0。



#### 5.5.3、JSON_TABLE(expr, path COLUMNS(column_list) [AS] alias)

​	此函数可以将JSON文档映射为表格。参数中expr可以是表达式或者列；path是用来过滤的JSON路径；COLUMNS是常量关键字；column list 是转换后的字段列表。

​	这个函数是MySQL 8.0.4后新增的一个重要的函数，可以将复杂的JSON文档转换为表格数据，转换后的表格可以像正常表一样做链接、排序、create table as select 等操作，对JSON的数据展示、数据迁移等很多应用领域带来极大的灵活性和便利性。

​	下面的例子将JSON文档中的全部数据转换为表格，并按表格中的ac字段进行排序：

```mysql
mysql> select * from JSON_TABLE('[{"a":"3"},{"a":"2"},{"b":1},{"a":0},{"a":[1,2]}]', "$[*]" COLUMNS( rowid for ordinality, ac varchar(100) path "$.a" default '999' on error default '111' on empty, aj json path "$.a" default '{"x":333}' on EMPTY, bx int exists path "$.b" ) ) as tt order by ac;
+-------+------+------------+------+
| rowid | ac   | aj         | bx   |
+-------+------+------------+------+
|     4 | 0    | 0          |    0 |
|     3 | 111  | {"x": 333} |    1 |
|     2 | 2    | "2"        |    0 |
|     1 | 3    | "3"        |    0 |
|     5 | 999  | [1, 2]     |    0 |
+-------+------+------------+------+
5 rows in set (0.00 sec)
```

​	对例子中的参数简单介绍一下。

​	（1）expr，即JSON对象数组 '[{"a":"3"},{"a":"2"},{"b":1},{"a":0},{"a":[1,2]}]'

​	（2）过滤路径（path），其中 "$[*]" 表示文档中所有的数据，如果改为 “[$0]”,则表示只转换文档中的第一个元素{“a”:"3"}。

​	（3）column list 包含4个部分的内容。

+ rowid FOR ORDINALITY:rowid是转换后的列名，FOR ORDINALITY表示按照序列顺序加一，类似于MySQL中的自增列。数据类型为UNSIGNED INT 初始值为1
+ ac VARCHAR(100) PATH “$.a” DEFAULT '999' ON ERROR DEFAULT '111' ON EMPTY: ac是转换后的列名；VARCHAR(100) 是转换后的列类型；PATH “$.a” 说明此字段只记录对象的key=“a” 的value；DEFAULT‘999’ ON ERROR 说明发生error，则转换为默认值999，比如 {“a”:[1,2]}, value 为JSON数组，和VARCHAR不匹配，所以此对象转换后为“999”；DEFAULT‘111’ ON EMPTY 说明对应的key不匹配 ‘a’ ,此对象转换后为 “111”，比如{“b”:1}.
+ aj和ac类似，只是转换后的列类型为JSON
+ bx INT EXISTS PTH "$.b": bx是转换后列名，如果存在路径“$.b”,即key=‘b’ 的对象，则转换为1；否则为0。



#### 5.5.4、JSON_ARRAYAGG(col_or_expr)

​	此函数可以将聚合后参数中的多个值转换为JSON数组。

​	下面的例子中按照o_id聚合后的属性列表转换为一个字符串JSON数组：

```mysql
mysql> create table t (o_id int, attribute varchar(10), value varchar(10));
Query OK, 0 rows affected (0.05 sec)

mysql> insert into t values(2,'color','red'),(2,'fabric','silk'),(3,'color','green'),(3,'shape','square');
Query OK, 4 rows affected (0.01 sec)
Records: 4  Duplicates: 0  Warnings: 0

mysql> select * from t;
+------+-----------+--------+
| o_id | attribute | value  |
+------+-----------+--------+
|    2 | color     | red    |
|    2 | fabric    | silk   |
|    3 | color     | green  |
|    3 | shape     | square |
+------+-----------+--------+
4 rows in set (0.00 sec)

mysql> select o_id, JSON_ARRAYAGG(attribute) as attributes from t group by o_id;
+------+---------------------+
| o_id | attributes          |
+------+---------------------+
|    2 | ["color", "fabric"] |
|    3 | ["color", "shape"]  |
+------+---------------------+
2 rows in set (0.00 sec)
```



#### 5.5.5、JSON_OBJECTAGG(key,value)

​	此函数可以把两个列或者是表达式解释为一个key和一个value，返回一个JSON对象。

​	还是上例的数据，这次按照o_id 聚合后的attribute/value 作为对象的key/value组成一个JSON对象文档。

```mysql
mysql> select o_id, JSON_OBJECTAGG(attribute, value) from t group by o_id;
+------+---------------------------------------+
| o_id | JSON_OBJECTAGG(attribute, value)      |
+------+---------------------------------------+
|    2 | {"color": "red", "fabric": "silk"}    |
|    3 | {"color": "green", "shape": "square"} |
+------+---------------------------------------+
2 rows in set (0.00 sec)
```



## 6、窗口函数

日常开发中，经常会遇到下面这些需求。

1. 去医院看病，怎样知道上次就医距现在的时长？
2. 环比如何计算
3. 怎样得到各部门工资排名前N名的员工列表
4. 如何查找组内每人工资占总工资的百分比？

这类需求都有一个共同点，为了得到结果，都需要在某个结果集内做一些特定的函数操作。为了方便的解决这一问题，MySQL8.0中引入了窗口函数。窗口函数的概念非常重要，它可以理解为记录集合，窗口函数也就是在满足某种条件的记录集合上执行的特殊函数，对于每条记录都要在此窗口内执行函数。有的函数，随着记录不同的窗口，窗口大小都是固定的，这种属于静态窗口；有的函数则相反，不同的记录对应着不同的窗口，这种动态变化的窗口叫滑动窗口。

​	窗口函数和聚合函数有些类似，两者最大的区别是聚合函数是多行聚合为一行，窗口函数则是多行聚合为相同的行数，每行会多一个聚合后的新列。窗口函数在其他数据库中（比如 oracle）也称为分析函数，功能也都大体相似。

​	MySQL中支持的窗口函数如下：

| 函数           | 功能                             |
| -------------- | -------------------------------- |
| ROW_NUMBER()   | 分区中当前行号                   |
| RANK()         | 当前行在分区中的排名，含序号间隙 |
| DENSE_RANK()   | 当前行在分区的排名，没有序号间隙 |
| PERCENT_RANK() | 百分比等级值                     |
| CUME_DIST()    | 累计分配值                       |
| FIRST_VALUE()  | 窗口中第一行的参数值             |
| LAST_VALUE()   | 窗口中最后一行的参数值           |
| LAG()          | 分区中指定行落后于当前行的参数值 |
| LEAD()         | 分区中领先当前行的参数值         |
| NTH_VALUE()    | 从第N行窗口框架的参数值          |
| NTILE(N)       | 分区中当前行的桶号               |

​	下面以订单表order_tab为例，逐个讲解这些函数的使用。测试表中的数据如下，各字段含义按顺序分别为订单号、用户id、订单金额、订单创建日期：

```mysql
 create table order_tab(order_id int, user_no int(3) zerofill, amount int, create_date datetime); 

mysql>  insert into order_tab values(1, 1,100,'2018-01-01 00:00:00'),(2, 1,300,'2018-01-02 00:00:00'),(3, 1,500,'2018-01-02 00:00:00'),(4, 1,800,'2018-01-03 00:00:00'),(5, 1,900,'2018-01-04 00:00:00'),(6,2,500,'2018-01-03 00:00:00'),(7, 2,600,'2018-01-04 00:00:00'),(8, 2,300,'2018-01-16 00:00:00'),(9, 2,800,'2018-01-22 00:00:00'),(10, 2,800,'2018-01-04 00:00:00');
Query OK, 10 rows affected (0.01 sec)
Records: 10  Duplicates: 0  Warnings: 0

mysql> select * from order_tab;
+----------+---------+--------+---------------------+
| order_id | user_no | amount | create_date         |
+----------+---------+--------+---------------------+
|        1 |     001 |    100 | 2018-01-01 00:00:00 |
|        2 |     001 |    300 | 2018-01-02 00:00:00 |
|        3 |     001 |    500 | 2018-01-02 00:00:00 |
|        4 |     001 |    800 | 2018-01-03 00:00:00 |
|        5 |     001 |    900 | 2018-01-04 00:00:00 |
|        6 |     002 |    500 | 2018-01-03 00:00:00 |
|        7 |     002 |    600 | 2018-01-04 00:00:00 |
|        8 |     002 |    300 | 2018-01-16 00:00:00 |
|        9 |     002 |    800 | 2018-01-22 00:00:00 |
|       10 |     002 |    800 | 2018-01-04 00:00:00 |
+----------+---------+--------+---------------------+
10 rows in set (0.00 sec)
```



### 6.1、ROW_NUMBER()

​	如果要查询每个用户最新的一笔订单，我们希望的结果是order_id分别为5和10的记录，此时可以使用ROW_NUMBER()函数按照用户进行分组并按照订单日期进行由大到小的排序，最后查找每组中序号为1的记录，SQL语句如下：

```mysql
mysql> select * from (select row_number() over(partition by user_no order by create_date desc) as row_num,order_id,user_no,amount,create_date from order_tab) t where row_num=1;
+---------+----------+---------+--------+---------------------+
| row_num | order_id | user_no | amount | create_date         |
+---------+----------+---------+--------+---------------------+
|       1 |        5 |     001 |    900 | 2018-01-04 00:00:00 |
|       1 |        9 |     002 |    800 | 2018-01-22 00:00:00 |
+---------+----------+---------+--------+---------------------+
2 rows in set (0.00 sec)
```

其中，row_number() 后面的over是关键字，用来指定函数执行的窗口范围，如果后面的括号中什么都不写，则意味着窗口包含所有行，窗口函数在所有行上进行计算；如果不为空，则支持以下4种语法。

+ window_name:给窗口指定一个别名，如果SQL 中涉及的窗口较多，采用别名则更清晰易读。上面的例子中如果指定一个别名w,则改写代码如下： 

  ```mysql
  mysql> select * from (select row_number() over w as row_num,order_id,user_no,amount,create_date from order_tab window w as (partition by user_no order by create_date desc)) t where row_num=1;
  +---------+----------+---------+--------+---------------------+
  | row_num | order_id | user_no | amount | create_date         |
  +---------+----------+---------+--------+---------------------+
  |       1 |        5 |     001 |    900 | 2018-01-04 00:00:00 |
  |       1 |        9 |     002 |    800 | 2018-01-22 00:00:00 |
  +---------+----------+---------+--------+---------------------+
  2 rows in set (0.00 sec)
  ```

+ partition子句：窗口按照哪些字段进行分组，窗口函数在不同的分组上分別执行。上面的例子就按照用户id进行分组。在每个用户id上，分别执行从1开始的顺序编号。

+ order by 子句：按照哪些字段进行排序，窗口函数将按照排序后的记录顺序进行编号。既可以和partition子句配合使用，也可以单独使用。上例中二者同时使用。

+ frame 子句：frame 是当前分区的一个子集，子句用来定义子集的规则，通常用来作为滑动窗口使用。比如要根据每个订单动态计算包括本订单和按时间顺序前后两个订单的平均订单金额，则可以设置如下 frame子句来创建滑动窗口：

  ```mysql
  mysql> select * from (select order_id,user_no,amount,avg(amount)over w as avg_num, create_date from order_tab window w as (partition by user_no order by create_date desc ROWS BETWEEN 1 PRECEDING AND 1 FOLLOWING)) t;
  +----------+---------+--------+----------+---------------------+
  | order_id | user_no | amount | avg_num  | create_date         |
  +----------+---------+--------+----------+---------------------+
  |        5 |     001 |    900 | 850.0000 | 2018-01-04 00:00:00 |
  |        4 |     001 |    800 | 666.6667 | 2018-01-03 00:00:00 |
  |        2 |     001 |    300 | 533.3333 | 2018-01-02 00:00:00 |
  |        3 |     001 |    500 | 300.0000 | 2018-01-02 00:00:00 |
  |        1 |     001 |    100 | 300.0000 | 2018-01-01 00:00:00 |
  |        9 |     002 |    800 | 550.0000 | 2018-01-22 00:00:00 |
  |        8 |     002 |    300 | 566.6667 | 2018-01-16 00:00:00 |
  |        7 |     002 |    600 | 566.6667 | 2018-01-04 00:00:00 |
  |       10 |     002 |    800 | 633.3333 | 2018-01-04 00:00:00 |
  |        6 |     002 |    500 | 650.0000 | 2018-01-03 00:00:00 |
  +----------+---------+--------+----------+---------------------+
  10 rows in set (0.00 sec)
  ```

  ​	从结果可以看出，order id 为5订单属于边界值，没有前一行，因此平均订单金额为（900+800)/2=850;order id 为4的订单前后都有订单，所以平均订单金额为（900+800+300)／3=666.6667,以此类推就可以得到一个基于滑动窗口的动态平均订单值。

  ​	对于滑动窗口的范围指定，有如下两种方式。
  ​	（1)基于行：通常使用 BETWEEN frame start AND frame_end 语法来表示行范围,frame start和 frame end 可以支持如下关键字，来确定不同的动态行记录：

  ```tex
  CURRENT ROW				边界是当前行，一般和其他范围关键字一起使用
  UNBOUNDED PRECEDING		边界是分区中的第一行
  UNBOUNDED FOLLOWING		边界是分区中的最后一行
  expr PRECEDING			边界是当前行减去expr的值
  expr FOLLOWING			边界是当前行加上expr的值
  ```

  比如，下面都是合法的范围：

  ```tex
  rows BETWEEN 1 PRECEDING AND 1 FOLLOWING 窗口范围是当前行、前一行、后一行一共3行记录
  rowS UNBOUNDED FOLLOWING 窗口范围是当前行到分区中的最后一行
  rows BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING 窗口范围是当前分区中所有行，等同于不写
  ```


  ​	（2)基于范围：和基于行类似，但有些范围不是直接可以用行数来表示的，比如希望窗口范围是一周前的订单开始，截止到当前行，则无法使用rows来直接表示，此时就可以使用范围来表示窗口：INTERVAL 7 DAY PRECEDING.Linux中常见的计算最近1分钟、5分钟、15分钟负载就是一个典型的应用场景。





### 6.2、RANK()/DENSE_RANK()

RANK() 和 DENSE_RANK() 这两个函数与row_number()非常类似，只是在出现重复值时处理逻辑有所不同。这里稍微改一上面的示例，假设需要查询不同用户的订单，按照订单金额进行排序，显示出相应的排名序号，SQL语句中用 row_number()、rank()、dense_rank() 分别显示序号，我们来看一下有什么区别。

```mysql
mysql> select * from (select row_number()over(partition by user_no order by amount desc) as row_num1, rank() over(partition by user_no order by amount desc) as row_num2, dense_rank() over(partition by user_no order by amount desc) as row_num3, order_id,user_no,amount,create_date from order_tab)t;
+----------+----------+----------+----------+---------+--------+---------------------+
| row_num1 | row_num2 | row_num3 | order_id | user_no | amount | create_date         |
+----------+----------+----------+----------+---------+--------+---------------------+
|        1 |        1 |        1 |        5 |     001 |    900 | 2018-01-04 00:00:00 |
|        2 |        2 |        2 |        4 |     001 |    800 | 2018-01-03 00:00:00 |
|        3 |        3 |        3 |        3 |     001 |    500 | 2018-01-02 00:00:00 |
|        4 |        4 |        4 |        2 |     001 |    300 | 2018-01-02 00:00:00 |
|        5 |        5 |        5 |        1 |     001 |    100 | 2018-01-01 00:00:00 |
|        1 |        1 |        1 |        9 |     002 |    800 | 2018-01-22 00:00:00 |
|        2 |        1 |        1 |       10 |     002 |    800 | 2018-01-04 00:00:00 |
|        3 |        3 |        2 |        7 |     002 |    600 | 2018-01-04 00:00:00 |
|        4 |        4 |        3 |        6 |     002 |    500 | 2018-01-03 00:00:00 |
|        5 |        5 |        4 |        8 |     002 |    300 | 2018-01-16 00:00:00 |
+----------+----------+----------+----------+---------+--------+---------------------+
10 rows in set (0.00 sec)
```

​	上面的记录中倒数第3、4、5行的斜体显示了3个函数的区别，row number() 在amount 都是800的两条记录上随机排序，但序号按照1、2递增，后面amount为600的的序号继续递增为3,中间不会产生序号间隙；rank()/dense_rank()则把 amount为800的两条记录序号都设置为1,但后续amount为600的需要则分别设置为3(rank)和2(dense rank),即rank()会产生序号相同的记录，同时可能产生序号间隙；而 dense rank()也会产生序号相同的记录，但不会产生序号间隙。



### 6.3、PERCENT_RANK()/CUME_DIST()

PERCENT_RANK()和 CUME_DIST() 这两个函数都是计算数据分布的函数，PERCENT_RANK() 和之前的RANK() 函数相关，每行按照以下公式进行计算：

```mysql
（rank-1)/(rows -1)
```

​	其中，rank为 RANK()函数产生的序号，rows为当前窗口的记录总行数，上面的例子修改如下：

```mysql
mysql> select * from (select rank() over w as row_num, percent_rank() over w as percent,order_id,user_no,amount,create_date from order_tab WINDOW w as (partition by user_no order by amount desc)) t;
+---------+---------+----------+---------+--------+---------------------+
| row_num | percent | order_id | user_no | amount | create_date         |
+---------+---------+----------+---------+--------+---------------------+
|       1 |       0 |        5 |     001 |    900 | 2018-01-04 00:00:00 |
|       2 |    0.25 |        4 |     001 |    800 | 2018-01-03 00:00:00 |
|       3 |     0.5 |        3 |     001 |    500 | 2018-01-02 00:00:00 |
|       4 |    0.75 |        2 |     001 |    300 | 2018-01-02 00:00:00 |
|       5 |       1 |        1 |     001 |    100 | 2018-01-01 00:00:00 |
|       1 |       0 |        9 |     002 |    800 | 2018-01-22 00:00:00 |
|       1 |       0 |       10 |     002 |    800 | 2018-01-04 00:00:00 |
|       3 |     0.5 |        7 |     002 |    600 | 2018-01-04 00:00:00 |
|       4 |    0.75 |        6 |     002 |    500 | 2018-01-03 00:00:00 |
|       5 |       1 |        8 |     002 |    300 | 2018-01-16 00:00:00 |
+---------+---------+----------+---------+--------+---------------------+
10 rows in set (0.00 sec)
```

​	从结果中可以看出，percent列按照公式（rank -1) / (rows-1) 代入 rank 值 (row_num列）和 rows 值 (user_no 为 '001' 和 ‘002’ 的值均为5) 此函数主要应用在分析领域，日常应用场景较少。

​	相比PERCENT_RANK().CUME_DIST() 函数的应用场景更多，它的作用是分组内小于等于当前rank值的行数／分组内总行数，上例中，统计大于等于当前订单金额的订单数，占总订单数的比例，SQL代码如下：

```mysql
mysql> select * from (select rank() over w as row_num, cume_dist()over w as cume,order_id,user_no,amount,create_date from order_tab WINDOW w as (partition by user_no order by amount desc)) t;
+---------+------+----------+---------+--------+---------------------+
| row_num | cume | order_id | user_no | amount | create_date         |
+---------+------+----------+---------+--------+---------------------+
|       1 |  0.2 |        5 |     001 |    900 | 2018-01-04 00:00:00 |
|       2 |  0.4 |        4 |     001 |    800 | 2018-01-03 00:00:00 |
|       3 |  0.6 |        3 |     001 |    500 | 2018-01-02 00:00:00 |
|       4 |  0.8 |        2 |     001 |    300 | 2018-01-02 00:00:00 |
|       5 |    1 |        1 |     001 |    100 | 2018-01-01 00:00:00 |
|       1 |  0.4 |        9 |     002 |    800 | 2018-01-22 00:00:00 |
|       1 |  0.4 |       10 |     002 |    800 | 2018-01-04 00:00:00 |
|       3 |  0.6 |        7 |     002 |    600 | 2018-01-04 00:00:00 |
|       4 |  0.8 |        6 |     002 |    500 | 2018-01-03 00:00:00 |
|       5 |    1 |        8 |     002 |    300 | 2018-01-16 00:00:00 |
+---------+------+----------+---------+--------+---------------------+
10 rows in set (0.00 sec)
```

​	列cume显示了预期的结果。



### 6.4、NTILE(N)

NFILE() 函数的功能是对一个数据分区中的有序结果集进行划分,将其分为N个组,并为每个小组分配一个唯一的组编号。继续上面的例子,对每个用户的订单记录分为3组,NFILE() 函数记录每组组编号,SOL代码如下

```mysql
mysql> select * from (select ntile(3) over w as nf, order_id, user_no, amount, create_date from order_tab WINDOW w as (partition by user_no order by amount desc)) t;
+------+----------+---------+--------+---------------------+
| nf   | order_id | user_no | amount | create_date         |
+------+----------+---------+--------+---------------------+
|    1 |        5 |     001 |    900 | 2018-01-04 00:00:00 |
|    1 |        4 |     001 |    800 | 2018-01-03 00:00:00 |
|    2 |        3 |     001 |    500 | 2018-01-02 00:00:00 |
|    2 |        2 |     001 |    300 | 2018-01-02 00:00:00 |
|    3 |        1 |     001 |    100 | 2018-01-01 00:00:00 |
|    1 |        9 |     002 |    800 | 2018-01-22 00:00:00 |
|    1 |       10 |     002 |    800 | 2018-01-04 00:00:00 |
|    2 |        7 |     002 |    600 | 2018-01-04 00:00:00 |
|    2 |        6 |     002 |    500 | 2018-01-03 00:00:00 |
|    3 |        8 |     002 |    300 | 2018-01-16 00:00:00 |
+------+----------+---------+--------+---------------------+
10 rows in set (0.00 sec)
```

此函数在数据分析中应用较多,比如由于数据量大,需要将数据分配到N个并行的进程分别计算,此时就可以用 NFILE(N)对数据进行分组,由于记录数不一定被N整除,所以每组记录数不一定完全一致,然后将不同组号的数据再分配。



### 6.5、NTH_VALUE(expr, N)

NTH_VALUE(expr,N)函数可以返回窗口中第N个expr的值,expr既可以是表达式,也可以是列名。这个函数不太好理解,来看下面的例子

```mysql
mysql> select * from (select ntile(3) over w as nf,nth_value(order_id,3) over w as nth, order_id, user_no, amount, create_date from order_tab window w as (partition by user_no order by amount desc))t;
+------+------+----------+---------+--------+---------------------+
| nf   | nth  | order_id | user_no | amount | create_date         |
+------+------+----------+---------+--------+---------------------+
|    1 | NULL |        5 |     001 |    900 | 2018-01-04 00:00:00 |
|    1 | NULL |        4 |     001 |    800 | 2018-01-03 00:00:00 |
|    2 |    3 |        3 |     001 |    500 | 2018-01-02 00:00:00 |
|    2 |    3 |        2 |     001 |    300 | 2018-01-02 00:00:00 |
|    3 |    3 |        1 |     001 |    100 | 2018-01-01 00:00:00 |
|    1 | NULL |        9 |     002 |    800 | 2018-01-22 00:00:00 |
|    1 | NULL |       10 |     002 |    800 | 2018-01-04 00:00:00 |
|    2 |    7 |        7 |     002 |    600 | 2018-01-04 00:00:00 |
|    2 |    7 |        6 |     002 |    500 | 2018-01-03 00:00:00 |
|    3 |    7 |        8 |     002 |    300 | 2018-01-16 00:00:00 |
+------+------+----------+---------+--------+---------------------+
10 rows in set (0.00 sec)
```

nth列返回了分组排序后的窗口中 order_id的第三个值, '001'用户返回3,'002'用户返回7,对于前N-1列,本函数返回NULL。





### 6.6、LAG(expr, N)/LEAD(expr,N)

LAG(expr, N)和LEAD(expr,N) 这两个函数的功能是获取当前数据行按照某种排序规则的上N行(LAG)/下N行(LEAD)数据的某个字段。比如,每个订单中希望增加一个字段,用来记录本订单距离上一个订单的时间间隔,那么就可以用LAG函数来实现,SQL代码如下：

```mysql
mysql> select order_id,user_no,amount,create_date,last_date,datediff(create_date,last_date) as diff from (select order_id,user_no,amount,create_date,lag(create_date,1) over w as last_date from order_tab window w as (partition by user_no order by create_date)) t;
+----------+---------+--------+---------------------+---------------------+------+
| order_id | user_no | amount | create_date         | last_date           | diff |
+----------+---------+--------+---------------------+---------------------+------+
|        1 |     001 |    100 | 2018-01-01 00:00:00 | NULL                | NULL |
|        2 |     001 |    300 | 2018-01-02 00:00:00 | 2018-01-01 00:00:00 |    1 |
|        3 |     001 |    500 | 2018-01-02 00:00:00 | 2018-01-02 00:00:00 |    0 |
|        4 |     001 |    800 | 2018-01-03 00:00:00 | 2018-01-02 00:00:00 |    1 |
|        5 |     001 |    900 | 2018-01-04 00:00:00 | 2018-01-03 00:00:00 |    1 |
|        6 |     002 |    500 | 2018-01-03 00:00:00 | NULL                | NULL |
|        7 |     002 |    600 | 2018-01-04 00:00:00 | 2018-01-03 00:00:00 |    1 |
|       10 |     002 |    800 | 2018-01-04 00:00:00 | 2018-01-04 00:00:00 |    0 |
|        8 |     002 |    300 | 2018-01-16 00:00:00 | 2018-01-04 00:00:00 |   12 |
|        9 |     002 |    800 | 2018-01-22 00:00:00 | 2018-01-16 00:00:00 |    6 |
+----------+---------+--------+---------------------+---------------------+------+
10 rows in set (0.00 sec)
```

内层SQL先通过lag 函数得到上一次订单的日期,外层SQL再将本次订单和上次订单日期做差得到时间间隔。



### 6.7、FIRST_VALUE (expr)/LAST_VALUE (expr)

FIRST_VALUE(exp)函数和 LAST_VALUE(expr)函数的功能分别是获得滑动窗口范围内的参数字段中第一个( FIRST_VALUE)和最后一个( LAST_VALUE)的值。下例中,每个用户在每个订单记录中希望看到截止到当前订单为止,按照日期排序最早订单和最晚订单的订单金额,SQL语句如下:

```mysql
mysql> select * from (select order_id,user_no,amount,create_date,first_value(amount) over w as first_amount, last_value(amount) over w as last_amount from order_tab window w as (partition by user_no order by create_date)) t;
+----------+---------+--------+---------------------+--------------+-------------+
| order_id | user_no | amount | create_date         | first_amount | last_amount |
+----------+---------+--------+---------------------+--------------+-------------+
|        1 |     001 |    100 | 2018-01-01 00:00:00 |          100 |         100 |
|        2 |     001 |    300 | 2018-01-02 00:00:00 |          100 |         500 |
|        3 |     001 |    500 | 2018-01-02 00:00:00 |          100 |         500 |
|        4 |     001 |    800 | 2018-01-03 00:00:00 |          100 |         800 |
|        5 |     001 |    900 | 2018-01-04 00:00:00 |          100 |         900 |
|        6 |     002 |    500 | 2018-01-03 00:00:00 |          500 |         500 |
|        7 |     002 |    600 | 2018-01-04 00:00:00 |          500 |         800 |
|       10 |     002 |    800 | 2018-01-04 00:00:00 |          500 |         800 |
|        8 |     002 |    300 | 2018-01-16 00:00:00 |          500 |         300 |
|        9 |     002 |    800 | 2018-01-22 00:00:00 |          500 |         800 |
+----------+---------+--------+---------------------+--------------+-------------+
10 rows in set (0.00 sec)
```

结果和预期一致,比如 order_id为4的记录, first_amount和 last_amount分别记录了用户 ‘001’ 截到时间 2018-01-03 00:00:00 止,第一条订单金额100 和最后一条订单金额800,注意这里是按时间排序的最早订单和最晚订单。并不是最小金额和最大金额订单



### 6.8、聚合函数作为窗口函数

​	除了前面介绍的各类窗口函数外，我们经常使用的各种聚合函数（SUM/AVG/MAX/MIN/COUNT）也可以作为窗口函数来使用。比如要统计每个用户按照订单id，截止到当前时间的累积订单/平均订单金额/最大订单金额/最小订单金额/订单数是多少，可以用聚合函数作为窗口函数实现如下：

```mysql
mysql> select order_id,user_no,amount,create_date,sum(amount) over w as sum1,avg(amount) over w as avg1,max(amount) over w as max1,min(amount) over w as min1,count(amount) over w as count1 from order_tab window w as (partition by user_no order by order_id);
+----------+---------+--------+---------------------+------+----------+------+------+--------+
| order_id | user_no | amount | create_date         | sum1 | avg1     | max1 | min1 | count1 |
+----------+---------+--------+---------------------+------+----------+------+------+--------+
|        1 |     001 |    100 | 2018-01-01 00:00:00 |  100 | 100.0000 |  100 |  100 |      1 |
|        2 |     001 |    300 | 2018-01-02 00:00:00 |  400 | 200.0000 |  300 |  100 |      2 |
|        3 |     001 |    500 | 2018-01-02 00:00:00 |  900 | 300.0000 |  500 |  100 |      3 |
|        4 |     001 |    800 | 2018-01-03 00:00:00 | 1700 | 425.0000 |  800 |  100 |      4 |
|        5 |     001 |    900 | 2018-01-04 00:00:00 | 2600 | 520.0000 |  900 |  100 |      5 |
|        6 |     002 |    500 | 2018-01-03 00:00:00 |  500 | 500.0000 |  500 |  500 |      1 |
|        7 |     002 |    600 | 2018-01-04 00:00:00 | 1100 | 550.0000 |  600 |  500 |      2 |
|        8 |     002 |    300 | 2018-01-16 00:00:00 | 1400 | 466.6667 |  600 |  300 |      3 |
|        9 |     002 |    800 | 2018-01-22 00:00:00 | 2200 | 550.0000 |  800 |  300 |      4 |
|       10 |     002 |    800 | 2018-01-04 00:00:00 | 3000 | 600.0000 |  800 |  300 |      5 |
+----------+---------+--------+---------------------+------+----------+------+------+--------+
10 rows in set (0.00 sec)
```

​	可以看到sum1/avg1/max1/min1/count1 的结果完全符合预期





## 7、其他常用函数

MySQL 提供的函数很丰富，除了前面介绍的字符串函数、数字函数、日期函数、流程函数以外还有很多其他函数，在此不再一一列举，有兴趣的读者可以参考MySQL 官方手册。

<table>
    <tr>
    	<td>函数</td>
        <td>功能</td>
    </tr>
    <tr>
    	<td>DATABASE() </td>
        <td>返回当前数据库名</td>
    </tr>
    <tr>
    	<td>VERSION() </td>
        <td>返回当前数据库版本</td>
    </tr>
    <tr>
    	<td>USER() </td>
        <td>返回当前登录用户名</td>
    </tr>
    <tr>
    	<td>INET_ATON(IP) </td>
        <td>返回IP 地址的数字表示</td>
    </tr>
    <tr>
    	<td>INET_NTOA(num) </td>
        <td>返回数字代表的IP 地址</td>
    </tr>
    <tr>
    	<td>PASSWORD(str) </td>
        <td>返回字符串str 的加密版本</td>
    </tr>
    <tr>
    	<td>MD5() </td>
        <td>返回字符串str 的MD5 值</td>
    </tr>
</table>

下面结合实例简单介绍一下这些函数的用法。

+ DATABASE()函数：返回当前数据库名。

  ```mysql
  mysql> select database();
  +------------+
  | database() |
  +------------+
  | test       |
  +------------+
  1 row in set (0.00 sec)
  ```

+ VERSION()函数：返回当前数据库版本。

  ```mysql
  mysql> select version();
  +-----------+
  | version() |
  +-----------+
  | 8.0.16    |
  +-----------+
  1 row in set (0.00 sec)
  ```

+ USER()函数：返回当前登录用户名。

  ```mysql
  mysql> select user();
  +----------------+
  | user()         |
  +----------------+
  | root@localhost |
  +----------------+
  1 row in set (0.00 sec)
  ```

+ INET_ATON(IP)函数：返回IP 地址的网络字节序表示。

  ```mysql
  mysql> select INET_ATON('192.168.0.185');
  +----------------------------+
  | INET_ATON('192.168.0.185') |
  +----------------------------+
  |                 3232235705 |
  +----------------------------+
  1 row in set (0.00 sec)
  ```

+ INET_NTOA(num)函数：返回网络字节序代表的IP 地址。

  ```mysql
  mysql> select INET_NTOA(3232235705);
  +-----------------------+
  | INET_NTOA(3232235705) |
  +-----------------------+
  | 192.168.0.185         |
  +-----------------------+
  1 row in set (0.00 sec)
  ```

  INET_ATON(IP)和INET_NTOA(num)函数主要的用途是将字符串的IP 地址转换为数字表示的网络字节序，这样可以更方便地进行IP 或者网段的比较。比如在下面的表t 中，要想知道在“192.168.1.3”和“192.168.1.20”之间一共有多少IP 地址：

  ```mysql
  mysql> select * from t;
  +--------------+
  | ip |
  +--------------+
  | 192.168.1.1  |
  | 192.168.1.3  |
  | 192.168.1.6  |
  | 192.168.1.10 |
  | 192.168.1.20 |
  | 192.168.1.30 |
  +--------------+
  6 rows in set (0.00 sec)
  ```

  按照正常的思维，应该用字符串来进行比较，下面是字符串的比较结果：

  ```mysql
  mysql> select * from t where ip>='192.168.1.3' and ip<='192.168.1.20';
  Empty set (0.01 sec)
  ```

  结果没有如我们所愿，竟然是个空集。其实原因就在于字符串的比较是一个字符一个字符的比较，当对应字符相同时候，就比较下一个，直到遇到能区分出大小的字符，才停止比较，后面的字符也将忽略。显然，在此例中，“192.168.1.3”其实比“192.168.1.20”要“大”，因为“3”比“2”大，而不能用我们日常的思维3<20，所以“ip>='192.168.1.3' and ip<='192.168.1.20'”必然是个空集。

  在这里，如果要想实现上面的功能，就可用函数INET_ATON 来实现，将IP 转换为字节序后
  再比较，如下所示:

  ```mysql
  mysql> select * from t where inet_aton(ip)>=inet_aton('192.168.1.3') and
  inet_aton(ip)<=inet_aton('192.168.1.20');
  +--------------+
  | ip |
  +--------------+
  | 192.168.1.3 |
  | 192.168.1.6 |
  | 192.168.1.10 |
  | 192.168.1.20 |
  +--------------+
  4 rows in set (0.00 sec)
  ```

  结果完全符合我们的要求。

+ PASSWORD(str)函数：返回字符串str 的加密版本，一个41 位长的字符串。此函数只用来设置系统用户的密码，但是不能用来对应用的数据加密。如果应用方面有加密的需求，可以使用MD5 等加密函数来实现。
  下例中显示了字符串“123456”的PASSWORD 加密后的值：

  ```mysql
  
  ```

+ MD5(str)函数：返回字符串str 的MD5 值，常用来对应用中的数据进行加密。
  下例中显示了字符串“123456”的MD5 值：

  ```mysql
  mysql> select MD5('123456');
  +----------------------------------+
  | MD5('123456')                    |
  +----------------------------------+
  | e10adc3949ba59abbe56e057f20f883e |
  +----------------------------------+
  1 row in set (0.00 sec)
  ```

  

## 小结

本章主要对MySQL 常用的各类常用函数通过实例做了介绍。MySQL 有很多内建函数，这些内建函数实现了很多应用需要的功能并且拥有很好的性能，如果用户在工作中需要实现某种功能，最好先查一下MySQL 官方文档或者帮助，看是否已经有相应的函数实现了我们需要的功能，可以大大提高工作效率。由于篇幅所限，本章并没有介绍所有的函数，读者可以去进一步查询相关文档。

