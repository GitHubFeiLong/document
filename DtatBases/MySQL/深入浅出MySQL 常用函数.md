# MySQL 常用函数

经常编写程序的朋友一定体会得到函数的重要性，丰富的函数往往能使用户的工作事半功倍。函数能帮助用户做很多事情，比如说字符串的处理、数值的运算、日期的运算等，在这方面MySQL 提供了多种内建函数帮助开发人员编写简单快捷的SQL 语句，其中常用的函数有字符串函数、日期函数和数值函数。
在MySQL 数据库中，函数可以用在SELECT 语句及其子句（例如where、order by、having 等）中，也可以用在UPDATE、DELETE 语句及其子句中。本章将配合一些实例对这些常用函数进行详细的介绍。



## 字符串函数

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

下面通过具体的实例来逐个地研究每个函数的用法，需要注意的是这里的例子仅仅在于说明
各个函数的使用方法，所以函数都是单个出现的，但是在一个具体的应用中通常可能需要综
合几个甚至几类函数才能实现相应的应用。

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

+ **INSERT(str ,x,y,instr)函数**：将字符串str 从第x 位置开始，y 个字符长的子串替换为字符
  串instr。
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

+ **LEFT(str,x)和RIGHT(str,x)函数**：分别返回字符串最左边的x 个字符和最右边的x 个字符。
  如果第二个参数是NULL，那么将不返回任何字符串。
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

+ **LPAD(str,n ,pad)和RPAD(str,n ,pad)函数**：用字符串pad 对str 最左边和最右边进行填充,
  直到长度为n 个字符长度。
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

+ **STRCMP(s1,s2)函数**：比较字符串s1 和s2 的ASCII 码值的大小。如果s1 比s2 小，那么返回-1；
  如果s1 与s2 相等，那么返回0；如果s1 比s2 大，那么返回1。如下例：

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
  下例中对字符串“$ beijing2008 $ ”进行了前后空格的过滤。

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

  



## 数值函数

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
  如果是整数，将会保留y 位数量的0；如果不写y，则默认y 为0，即将x 四舍五入后取整。适合于将所有数字保留同样小数位的情况。如下例所示。

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
  注意TRUNCATE 和ROUND 的区别在于TRUNCATE 仅仅是截断，而不进行四舍五入。下例中
  描述了二者的区别：

  ```mysql
  mysql> select round(1.235,2), truncate(1.235,2);
  +----------------+-------------------+
  | round(1.235,2) | truncate(1.235,2) |
  +----------------+-------------------+
  |           1.24 |              1.23 |
  +----------------+-------------------+
  1 row in set (0.00 sec)
  
  ```

  



## 日期和时间函数

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

+ **FROM_UNIXTIME （unixtime ）函数**：返回UNIXTIME 时间戳的日期值，和
  UNIX_TIMESTAMP(date)互为逆操作

  ```mysql
  mysql> select from_unixtime(1597396659);
  +---------------------------+
  | from_unixtime(1597396659) |
  +---------------------------+
  | 2020-08-14 17:17:39       |
  +---------------------------+
  1 row in set (0.00 sec)
  ```

+ **WEEK(DATE)和YEAR(DATE)函数**：前者返回所给的日期是一年中的第几周，后者返回所
  给的日期是哪一年。

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

+ **DATE_ADD(date,INTERVAL expr type)函数**：返回与所给日期date 相差INTERVAL 时间段的
  日期。其中INTERVAL 是间隔类型关键字，expr 是一个表达式，这个表达式对应后面的类型，type是间隔类型，MySQL 提供了13 种间隔类型:

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

  



## 流程函数

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

+ IF(value, t f)函数：这里认为月薪在2000 元以上的职员属于高薪，用“high”来表示，而200元以下的职员属于低薪，用“low”来表示。

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

+ IFNULL(value1,value2) 函数：这个函数一般用来替换NULL值，我们知道NULL值是不能参与数值运算的。下面这个语句就是把NULL值用0来替换：

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

+ CASE [expr] WHEN [value] THEN [result1] .... ELSE [default] END 函数：这是case的简单函数用法，case后面跟列名或者列的表达式，when 后面枚举这个表达式所有可能的值，但不能是值的范围。如果要实现上面例子中高薪低薪的问题，写法如下：

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

  

+ CASE WHEN [value1] THEN [result1]... ELSE [default] END 函数：这是case的搜索函数用法，直接在when后面写条件表达式，并且只返回第一个符合条件的值，使用起来更加灵活。上例可以改写如下：

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





## JSON 函数

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
    <td>JSON_QUOTE() / JSON_UNIQUOTE()</td>
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
    	<td rowspan=6>查询JSON</td>
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



### 创建JSON函数

#### 1. JSON_ARRAY([val[,val]...])

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



#### 2. JSON_OBJECT([key,val[,key,val] ...])

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



#### 3.  JSON_QUOTE(string)

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



### 查询JSON函数

#### 1 JSON_CONTAINS(target,candidate[,path])

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

在下例中，要查询JSON文档j









## 其他常用函数

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

本章主要对MySQL 常用的各类常用函数通过实例做了介绍。MySQL 有很多内建函数，这些
内建函数实现了很多应用需要的功能并且拥有很好的性能，如果用户在工作中需要实现某种
功能，最好先查一下MySQL 官方文档或者帮助，看是否已经有相应的函数实现了我们需要
的功能，可以大大提高工作效率。由于篇幅所限，本章并没有介绍所有的函数，读者可以去
进一步查询相关文档。

