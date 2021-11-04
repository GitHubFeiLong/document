# MySQL 的安装与配置

​		近几年，开源数据库逐渐流行起来。由于具有免费使用、配置简单、稳定性好、性能优良等优点，开源数据库在中低端应用上占据了很大的市场份额，而MySQL 正是开源数据库中的杰出代表。

​		MySQL 数据库隶属于MySQL AB 公司，总部位于瑞典。公司名中的“AB”是瑞典语“aktiebolag”或“股份公司”的首字母缩写。MySQL 支持几乎所有的操作系统，并且支持很大的表（MyISAM 存储引擎支持的最大表尺寸为65536TB），这些特性使得MySQL 的发展非常迅猛，目前已经广泛应用在各个行业中。



## MySQL 的下载

​		用户通常可以到官方网站www.mysql.com 下载最新版本的MySQL 数据库。按照用户群分类，MySQL 数据库目前分为社区版（Community Server）和企业版（Enterprise），它们最重要的区别在于：社区版是自由下载而且完全免费的，但是官方不提供任何技术支持，适用于大多数普通用户；而企业版则是收费的，不能在线下载，相应地，它提供了更多的功能和更完备的技术支持，更适合于对数据库的功能和可靠性要求较高的企业客户。
​		MySQL 的版本更新很快，目前最新的GA版本为8.0。这些不同版本之间的主要区别如下所示。

| 版本 | 中要改进                                                     |
| ---- | ------------------------------------------------------------ |
| 4.1  | 增加了子查询的支持：字符集中增加了对UTF-8的支持              |
| 5.0  | 增加了视图、过程、触发器的支持，增加了 information_schema 系统数据库 |
| 5.1  | 增加了表分区的支持：至此基于行的复制（row-based replication） |
| 5.5  | InnoDB 成为默认存储引擎：支持半同步复制：引入 performance_schema 动态性能视图 |
| 5.6  | 支持部分 Online DDL 操作：支持ICP/BKA/MRR 等优化器改进；引入GTID；支持多库并行复制 |
| 5.7  | 密码安全性提高：支持多线程并行复制、多源复制；支持JSON；引入sys系统库；引入MGR |
| 8.0  | 在线持久化全局参数；大幅提高数据字典性能；引入窗口函数、ROLE、直方图、降序索引、不可见索引；修复自增列重启BUG |

​		对于不同的操作系统平台，MySQL 提供了相应的版本。本章以 Windows 平台下的图形化安装 以及Linux 平台下的RPM包为例，说明 MySQL 的下载、安装、配置、启动和关闭过程。本章的测试环境分别是 64位 的Windows 10 和 x86-64 平台上的 ReadHat Linux 6，MySQL 版本为最新的8.011



### 在 Windows平台下下载MySQL

​		Windows 平台提供了两种类型的安装包

| 类 型            | 特 点                                                        |
| ---------------- | ------------------------------------------------------------ |
| noinstall 压缩包 | 安装简单，解压即可使用；灵活性差，无法自主选择组件           |
| MySQL Installer  | 安装简单，用向导式一步一步提示安装，可以灵活选择安装、删除、更改MySQL 提供的所有组件 |

​		其中，MySQL Install 为官方推荐安装方式，它的安装包又分为 mysql-install-web-community 和 mysql-install-community 两种类型，主要区别是前者安装包很小，安装时需要连接互联网，组件需要在线下载最新版本；而后者安装时则不需要联网，安装包中包含了完整的组件，安装包较前者大很多，下载过程慢，但安装过程快。以 8.0.11 版本为例，mysql-install-web-community的安装为 15.8MB，而mysql-install-community 为230MB。本节以 mysql-install-community 的安装包为例演示下载过程。

​		在 MySQL 官网的DOWNLOADS 页面中，单击下面的 [MySQL Community (GPL) Downloads »](https://dev.mysql.com/downloads/)， 然后在打开的页面组件列表中单击 [MySQL Installer for Windows](https://dev.mysql.com/downloads/windows/), 进入安装包列表页面

![image-20200822201835964](..\..\typora-user-images\image-20200822201835964.png)



### 在Linux平台下，MySQL下下载MySQL

​		在linux 平台下，MySQL 官方也提供了多种安装方式。不同的安装方式需要下载的安装包也不同，具体如下：

| 类型                        | 特点                                                         |
| --------------------------- | ------------------------------------------------------------ |
| Yum / APT / SUSE Repository | 安装仓库包极小，版本安装简单灵活，升级方便；其中Yum Repository 适合Red Hat Enterprise Linux/Oracle Linux/ Fedora 平台； APT Repository 适合 Debian/Ubuntu 平台； SUSE 适合 SUSE 适合SUSE Linux |
| RPM 包                      | 安装简单；灵活性差，无法灵活选择版本、升级                   |
| 通用二进制安装              | 安装较复杂，灵活性高，平台通用性好                           |
| 源码包                      | 安装最复杂，时间长，参数设置灵活，性能好                     |

​		要下载 MySQL RPM包，可以采用以下两种方式。

#### 1、 通用网页直接下载

（1）在  MySQL 官网的 DOWNLOADS 页面中，底部的 [MySQL Community (GPL) Downloads »](https://dev.mysql.com/downloads/)， 在 MySQL Community Downloads 页面中选择 选择左侧的 [MySQL Community Server](https://dev.mysql.com/downloads/mysql/) ，然后在页面下方的 General Availability (GA) Releases 区域中， 选择合适操作系统和对应版本。本例中分别选择 Red Hat Enterprise Linux / Oracle Linux 和  Red Hat Enterprise Linux 6 / Oracle Linux 6 (x86, 64-bit) ，下面的软件列表会自动进行过滤，只显示符合条件的RPM 包，如图 所示。

![image-20200823001755719](..\..\typora-user-images\image-20200823001755719.png)

在这些包中， “RPM Bundle ” 是全部安装包的集合，其余则是各个独立的子安装包。通常选择 “RPM Package，MySQL Server” 和 “RPM Package,Client Utilities” 及其依赖包即可满足基本使用 从上图中的框选提示符也可以看出，MySQL 官方给出了 Yum Repository 的下载链接地址，推荐大家使用这种更简便的方式进行安装。

（2）以下载Server 包为例 ，单击 “RPM Package，MySQL Server” 一栏对应的 Downloads 按钮，进入如图所示的页面。

![image-20200823001924776](..\..\typora-user-images\image-20200823001924776.png)

​		忽略上面的登陆界面，只需要单击下面框选标识的链接，即可直接下载文件到浏览器默认下载目录中。

（3）将下载后的文件用FTP等工具传送到Linux服务器上。



#### 2.通过命令行方式下载

（1）得到下载地址的URL（用鼠标右键单击上图的 "No thanks, just start my download." 链接，在弹出的菜单中单击 “复制链接地址”）。

（2）使用wget 命令以及复制的链接地址在Linux服务器上直接下载Server 和 Client 及其依赖软件包



## MySQL 的安装

​		MySQL 的安装分很多种不同情况。下面将以Windows 平台和Linux平台为例，介绍MySQL在不同操作系统平台上的安装方法。

#### 在Windows 平台下安装MySQL

​		Window 平台下的安装包主要有两种，一种是noinstall 包，顾名思义，不需要安装就可以直接使用；另一种是 MySQL install 进行图形化安装，这种方式更加简单，可以灵活的安装和配置MySQL 提供的所有组件，也是官方推荐的安装方式，本节将主要介绍这种安装方式。

（1）双击安装文件 mysql-installer-community-8.0.21.0.msi，进入 MySQL 安装界面

（2）进入 Choosing a Setup Type  界面，选择 MySQL 安装类型。

> mysql-installer-community-8.0.21.0.msi 没有这一步：选中条款左下角 “i accept the license terms”，接受条款，单击Next按钮，

![image-20200823095135301](..\..\typora-user-images\image-20200823095135301.png)

​		左边以单选按钮的形式列出了5种按钮类型，右上部的文本框描述了选定类型所对应的安装组件

> 最新的也没有这一 部： 右下部份的单选框按钮可以选择安装所有组件还是只安装已经正式发行（GA）的组件，这里保留照默认选项，即满足开发者需求的所有组件。



（3）单击 Next按钮，进入Installation 界面

![image-20200823100553440](..\..\typora-user-images\image-20200823100553440.png)

​		此界面列出了MySQL 即将安装或者升级的组件列表，如果安装包中的组件是最新版本，则执行安装；否则将先升级到最新版本后再安装。

（4）单击 Execute 按钮，开始安装升级过程，界面显示安装进度，全部安装成功后，显示下图界面

![image-20200823165809951](..\..\typora-user-images\image-20200823165809951.png)

![image-20200823170659809](..\..\typora-user-images\image-20200823170659809.png)



（5）单击Next 按钮，进入配置MySQL 组件界面，如图：

![image-20200823113336425](..\..\typora-user-images\image-20200823113336425.png)

（6）单击Next按钮，开始配置MySQL Server，由于配置内容较多，这里不一一演示，配置内容包括组复制、网络配置、授权方式、账户和角色、服务的自动启停等多个选项，按照默认选项全部单击Next 按钮，最后到达 Apply Configuration 界面后单击 Execute 按钮完成配置，配置完成后界面如图：

![image-20200823113826425](..\..\typora-user-images\image-20200823113826425.png)

（7）单击 Finish 按钮，返回如图所示的主配置界面。

![image-20200823113855855](..\..\typora-user-images\image-20200823113855855.png)

（8）单击 Next 按钮， 进入 MySQL Router Confiuration 界面，如下图所示

![image-20200823113957206](..\..\typora-user-images\image-20200823113957206.png)

MySQL Router 是 MySQL 提供的一个轻量级中间件，可以实现读写分离等路由功能。这里不用配置，直接跳过即可。



（9）单击 Finish 按钮，回到著配置界面， 单击 Next 按钮，进行最后一项样例数据库配置。在8.0 版本中，会创建两个样例数据库，分别是 world 和 sakila,其中world 数据模型较简单，只有3张表；sakila 数据模型较复杂，有 22 张表，并且有视图触发器等对象，方便做各种测试。要创建样例库，首先需要和 MySQL 实例进行连接，如图所示为测试数据库连接的界面。

![image-20200823114237239](..\..\typora-user-images\image-20200823114237239.png)

root 下面输入实例配置时二的密码，单击 Check 按钮，绿色的状态栏显示测试成功。

（10）单击Next 按钮，显示样例库安装脚本执行界面；单击 Execute 按钮，完成所有配置过程。再次单击 Next 按钮，显示安装完成，如图所示，

![image-20200823114535615](..\..\typora-user-images\image-20200823114535615.png)

界面中可以选择启动 Workbench 或者是MySQL Shell 来连接实例。 Workbench 是一个官方提供的免费图形化管理工具，使用很简单，这里不做详细介绍；MySQL shell 是8.0 版本新提供的一个客户端工具。

（11）这里使用传统的MySQL 客户端尝试连接，客户端工具在 "开始" 菜单中可以找到如图：

![image-20200823114647486](..\..\typora-user-images\image-20200823114647486.png)

​		单击上图种的 MySQL 8.0 CommandLine Client 菜单，启动 MySQL 客户端工具，然后输入root 密码，如图所示：

![image-20200823114736037](..\..\typora-user-images\image-20200823114736037.png)

​		输入之前设置的密码后，登陆成功



#### 在Linux 平台下安装 MySQL

​		在Linux 平台下安装MySQL 和 windows 平台下安装有所不同。在Linux平台下不用图形化方式安装，并且 Linux 支持 RPM 包、通用二进制包、源码包 3种安装方式。下面以 RPM包为例来介绍如何在Linux平台下安装 MySQL。

​		RPM 是 Redhat Package Manage 的缩写。通过 RPM 的管理，用户可以把源代码安装成一种以 rpm 为后缀的文件形式，更易于安装。对于 RPM 包的下载和安装，MySQL 官方提供了两种方式：一种是直接在页面上按需下载对应的包；另一种是使用版本仓库（Repository）的方式进行安装，这种方式最大的特点是安装简单方便，对于不同版本的软件安装和升级尤为方便，本节将分别介绍这刘两种安装方式。

##### 		1. 直接安装RPM 包

​		MySQL 的 RPM 包包括很多套件，老一点的版本一般只安装 Server 和 Client 就可以了。其中 Server 包是 MySQL 服务端套件，为用户提供核心的 MySQL 服务；Client 包是连接 MySQL 服务的客户端工具，方便管理员和开发人员在服务器上进行各种管理工作。较新的版本由于包之间存在更多地依赖关系，通常需要下载以下几个包才可以完成标准的安装。

+ mysql-community-server
+ mysql-community-client
+ mysql-community-libs
+ mysql-community-common
+ mysql-community-libs-compat

在本例中，安装 RPM 包的具体操作步骤如下。

（1）切换到 root 下 （只有 root 才可以执行 RPM 包）

（2）按照以下顺序安装MySQL 相关包（顺序不对可能提示包依赖）。

```bash
rmp -ivh mysql-community-commom-.*
rmp -ivh mysql-community-libs
rmp -ivh mysql-community-libs-compat.*
rmp -ivh mysql-community-client.*
rmp -ivh mysql-community-server.*
```

（3）启动MySQL

```bash
service mysqld start
```

​		如果OS是 redhadt7 ，则命令为 systemctl start mysqld.service。通过下面的chkconfig 命令，可以让操作系统重启时自动启动 MySQL

```bash
chkconfig --level 2345 mysqld on
```

​		执行下面的命令，可以检查自启动状态，确认生效。

```bash
chkconfig --list | grep mysql
```

（4）登录 MySQL

```bash
mysql -uroot
```

​		MySQL 5.7 之后的版本在默认安装时去掉了root用户的空密码，初次启动 MySQL  时系统会生成一个临时 root 密码，可以通过查看/var/log/mysqld.log 来查看。

​	用临时密码登录，但临时密码登陆无法进行大多数操作，比如想看当前数据库列表，会提示错误，提示必须要更改初始密码，才可以执行此命令，可以用以下命令进行更改：

```mysql
alter user 'root'@'localhost' identified by 'Test@123';
```

​		新密码必须要满足密码强度规则，默认规则如下：

1. 新密码必须至少8位，其中至少要包含一个数字、一个特殊字符以及大小写字符至少各包含一个。如果不满足这些条件，则提示错误。

##### 2. 通过 Yum Repository 安装

​		通过上面的介绍，我们发现 RPM 安装方式虽然简单，但也有一些缺陷，比如需要下载的包比较多，且包之间安装有先后依赖关系，最重要的是升级不方便。如果有新版本，则需要重新下载所有包进行替换。

​		为了解这些不便之处， MySQL 官方提供了一种新的安装方式——Yum Repository 。Yum（全称位Yellow dog Updater Modified）是一个在 Fedoras和 RedHat 以及Centos 中的 Shell 前端软件包管理器。基于RPM 包管理，能够从指定的服务器自动下载RPM包并且安装，可以自动处理依赖性关系，并且一次安装所有依赖的软件包，无需繁琐地一次次下载和安装。

​		Yum Repository 地安装包非常小，8.0 版本只有25kb，下载方式和其他RPM包类似，这里不再赘述。下面详细介绍Yum Repository 地安装和使用方法

（1）安装 Yum Repository

```bash
[root@localhost ~]# rpm -ivh mysql80-community-release-el7-3.noarch.rpm 
警告：mysql80-community-release-el7-3.noarch.rpm: 头V3 DSA/SHA1 Signature, 密钥 ID 5072e1f5: NOKEY
准备中...                          ################################# [100%]
正在升级/安装...
   1:mysql80-community-release-el7-3  ################################# [100%]
```

​		安装完成后，在 /etc/yum.repos.d 下多了 mysql-community.repo 和 mysql-community-source.repo 这两个文件，它们分别是 MySQL社区版RPM 包和源码包地Yum 源文件，里面记录了支持的软件版本和下载相关的一些参数。

（2）使用Yum Repository 来安装Mysql8.0

用cat 命令截取mysql-community.repo 的部分内容，如下：

```bash
# Enable to use MySQL 5.7
[mysql57-community]
name=MySQL 5.7 Community Server
baseurl=http://repo.mysql.com/yum/mysql-5.7-community/el/7/$basearch/
enabled=0
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-mysql

[mysql80-community]
name=MySQL 8.0 Community Server
baseurl=http://repo.mysql.com/yum/mysql-8.0-community/el/7/$basearch/
enabled=1
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-mysql

```

​		可以看出，最新GA 版本8.0的 enabled = 1 ，其他版本均为 0.如果安装最新 GA版本的 MySQL，则不用做任何设置，直接执行如下命令：

```bash
[root@localhost ~]# yum install mysql-community-server
已加载插件：fastestmirror, langpacks
Loading mirror speeds from cached hostfile
 * base: mirrors.aliyun.com
 * extras: mirrors.cn99.com
 * updates: mirrors.aliyun.com
base                                                                               | 3.6 kB  00:00:00     
extras                                                                             | 2.9 kB  00:00:00     
mysql-connectors-community                                                         | 2.5 kB  00:00:00     
mysql-tools-community                                                              | 2.5 kB  00:00:00     
mysql80-community                                                                  | 2.5 kB  00:00:00     
updates                                                                            | 2.9 kB  00:00:00     
(1/7): base/7/x86_64/group_gz                                                      | 153 kB  00:00:00     
(2/7): extras/7/x86_64/primary_db                                                  | 206 kB  00:00:00     
(3/7): mysql80-community/x86_64/primary_db                                         | 115 kB  00:00:01     
(4/7): mysql-connectors-community/x86_64/primary_db                                |  62 kB  00:00:02     
(5/7): mysql-tools-community/x86_64/primary_db                                     |  76 kB  00:00:03     
(6/7): updates/7/x86_64/primary_db                                                 | 3.8 MB  00:00:03     
(7/7): base/7/x86_64/primary_db                                                    | 6.1 MB  00:00:06     
正在解决依赖关系
--> 正在检查事务
---> 软件包 mysql-community-server.x86_64.0.8.0.21-1.el7 将被 安装
--> 正在处理依赖关系 mysql-community-common(x86-64) = 8.0.21-1.el7，它被软件包 mysql-community-server-8.0.21-1.el7.x86_64 需要
--> 正在处理依赖关系 mysql-community-client(x86-64) >= 8.0.11，它被软件包 mysql-community-server-8.0.21-1.el7.x86_64 需要
--> 正在检查事务
---> 软件包 mysql-community-client.x86_64.0.8.0.21-1.el7 将被 安装
--> 正在处理依赖关系 mysql-community-libs(x86-64) >= 8.0.11，它被软件包 mysql-community-client-8.0.21-1.el7.x86_64 需要
---> 软件包 mysql-community-common.x86_64.0.8.0.21-1.el7 将被 安装
--> 正在检查事务
---> 软件包 mariadb-libs.x86_64.1.5.5.60-1.el7_5 将被 取代
--> 正在处理依赖关系 libmysqlclient.so.18()(64bit)，它被软件包 2:postfix-2.10.1-7.el7.x86_64 需要
--> 正在处理依赖关系 libmysqlclient.so.18(libmysqlclient_18)(64bit)，它被软件包 2:postfix-2.10.1-7.el7.x86_64 需要
---> 软件包 mysql-community-libs.x86_64.0.8.0.21-1.el7 将被 舍弃
--> 正在检查事务
---> 软件包 mysql-community-libs-compat.x86_64.0.8.0.21-1.el7 将被 舍弃
---> 软件包 postfix.x86_64.2.2.10.1-7.el7 将被 升级
---> 软件包 postfix.x86_64.2.2.10.1-9.el7 将被 更新
--> 解决依赖关系完成

依赖关系解决

==========================================================================================================
 Package                            架构          版本                     源                        大小
==========================================================================================================
正在安装:
 mysql-community-libs               x86_64        8.0.21-1.el7             mysql80-community        4.5 M
      替换  mariadb-libs.x86_64 1:5.5.60-1.el7_5
 mysql-community-libs-compat        x86_64        8.0.21-1.el7             mysql80-community        1.2 M
      替换  mariadb-libs.x86_64 1:5.5.60-1.el7_5
 mysql-community-server             x86_64        8.0.21-1.el7             mysql80-community        499 M
为依赖而安装:
 mysql-community-client             x86_64        8.0.21-1.el7             mysql80-community         48 M
 mysql-community-common             x86_64        8.0.21-1.el7             mysql80-community        617 k
为依赖而更新:
 postfix                            x86_64        2:2.10.1-9.el7           base                     2.4 M

事务概要
==========================================================================================================
安装  3 软件包 (+2 依赖软件包)
升级           ( 1 依赖软件包)

总下载量：555 M
Is this ok [y/d/N]: 
```

​		从安装过程可以看出，一些包被废弃，一些被更新，包之间的依赖被自动处理整个过程几乎不需要人工介入，非常方便。

​		如果特殊需求，要安装其他低版本的 MySQL， 比如要安装5.7 版本，那么可以将源文件中 [mysql80-community]下的配置项 enabled改为 0，同时将 [mysql57-community]下对应参数改为1即可。或者使用如下命令更改更方便：

```bash
yum-config-manager  --disable mysql80-community
yum-config-manager  --emable mysql57-community
```



## MySQL 的配置

​		MySQL安装完毕后，大多数情况下都可以直接启动 MySQL 服务，而不需要设置参数，因为喜用中所有的参数都有一个默认值，如果需要修改默认值，则必须要配置参数文件。下面就Windows 和 Linux 两种平台下的配置方法进行介绍。

##### 1. Windwos 平台下配置 MySQL

​		对于图形的安装方式，MySQL提供了一个图形化的实例配置向导，可以引导用户逐步进行实例参数的设置，具体操作步骤如下。

​		（1） 单击 ”开始“ -> MySQL Install Community 菜单，进入安装界面

![image-20200823171723079](..\..\typora-user-images\image-20200823171723079.png)

​		（2）选择产品下面的 MySQL Server, 单击 Reconfigure, 进入选择配置类型界面，可以发现，与安装 MySQL  过程中配置的产品界面和内容一致，这里根据自己需求来设置即可。

![image-20200823173047515](..\..\typora-user-images\image-20200823173047515.png)

​		（3）通过  MySQL  Installer 提供的图形化功能，可以对一些重要的实例参数进行设置，但对于更详细的参数则无能为力。此时，常用的方法是通过修改配置文件进行设置。Windows 中配置文件命名为 my.ini,通常位于 MySQL安装目录下,下面是过滤部分注释后的内容 ：

```ini
[mysql]
#设置mysql客户端默认字符集
default-character-set=utf8
[mysqld]
#设置3306端口
port = 3306
#设置mysql安装目录
basedir = D:\work\databases\mysql8.0.16\mysql-8.0.16-winx64
#设置mysql数据库的数据的存放目录
datadir = D:\work\databases\mysql8.0.16\mysql-8.0.16-winx64\data
#允许最大连接数
max_connections = 200
#服务端使用的字符牧人为8bit编码的latinl字符集
character-set-server = utf8
#创建新表时将使用的默认存储引擎
default-storage-engine = INNODB

```



​		上面示例中的粗体代码代表了不同模块的参数，通常配置最多的参数模块是 【mysqld】,也就是 MySQL 实例相关参数，新增或修改该参数只需要增加或者修改该此模块下的条目即可。



##### 2. Linux 平台下配置 MySQL

​		在Linux 下配置MySQL 和 Windows下通过编辑参数文件配置非常类似，区别在于参数文件的位置和文件名不同。在 Linux 下也可以在多个位置部署配置文件，通过 RPM 包安装的放在 /etc 下，文件名称只能是 my.cnf (在 windows 下文件名默认是 my.ini)。

​		在 MySQL 5.6 之前，MySQL 提供了多个参数模块，用来适应不同环境的需求，它们的名称类似于 my-\*\*\*.cnf, 其中\*\*\* 代表了环境对资源大小的需求，比如 my-huge.cnf  my-large.cnf  my-medium.cnf  my-small.cnf 等， 在 8.0 版本终于i京取消了这些模块们需要直接编辑my.cnf或 my.ini



#### 启动和关闭 MySQL 服务

​		安装配置完毕MySQL后，接下来就该启动MySQL服务了。这里强调一下，MySQL 服务和MySQL数据库不同，MySQL服务是一系列后台进程， 而MySQL数据库则是一系列的数据目录和数据文件: MySQL 数据库必须在MySQL服务启动之后才可以进行访问。下面就针对Windows和Linux两种平台，介绍MySQL服务的启动和关闭方法。



#####  在 Windows 平台下启动和关闭 MySQL 服务

​		对于采用图形化方式安装的MySQL,可以直接通过Windows的“开始” 菜单(单击“开始”→“控制面板”，“管理工具”→ “服务”菜单)中找到MySQL80服务，双击后进人，单击“启动” 或“停止”按钮来启动或关闭MySQL服务。用户也可以在命令行中手工启动和关闭MySQL 服务，如下所示。

​		（1）启动服务

```bat
C:\Users\msi>net start mysql80
mysql80 服务正在启动
MySQL80 服务已经启动成功
```

​		（2）关闭服务

```bat
C:\Users\msi>net stop mysql80
mysql80 服务正在停止
MySQL80 服务已经成功停止
```



#####  在 Linux 平台下启动和关闭 MySQl 服务

​		在Linux 平台下，可以采用如下命令查看 MySQL服务状态

```bash
[root@localhost ~]# systemctl start mysqld.service 
[root@localhost ~]# netstat -nlp | grep mysqld
tcp6       0      0 :::3306                 :::*                    LISTEN      57913/mysqld        
tcp6       0      0 :::33060                :::*                    LISTEN      57913/mysqld        
unix  2      [ ACC ]     STREAM     LISTENING     137341   57913/mysqld         /var/run/mysqld/mysqlx.sock
unix  2      [ ACC ]     STREAM     LISTENING     137345   57913/mysqld         /var/lib/mysql/mysql.sock

```

​		其中 3306 端口是 MySQL 服务器默认监听端口。

​		与在 Windows 平台上类似，在 Linux 平台上启动和关闭 MySQL 也有两种方法，一种是通过命令行方式启动和关闭，另一种是通过服务的方式启动和关闭（适用于 RPM 包安装方式），下面分别对这两种方式进行介绍。

##### 1、命令行方式：

在命令行方式下，启动和关闭 MySQL 服务命令如下所示：

（1）启动服务

```bash
[root@localhost bin]# cd /usr/bin
[root@localhost bin]# ./mysqld_safe &
```

（2）关闭服务：

```bash
[root@localhost bin]# mysqladmin -uroot -shutdown
```



##### 2、服务的方式

如果 MySQL 是用 RPM 包安装的，则启动和关闭 MySQL 服务过程如下所示。

（1）启动服务：

```bash
[root@localhost ~]# systemctl start mysqld.service
```

​	如果再启动状态，需要重启服务，可以用以下命令直接重启，而不需要先关闭再启动；

（2）关闭服务

  ```bash
[root@localhost ~]# systemctl stop mysqld.service
  ```

> 注意：在命令行启动 MySQL 时，如果不加 ”--console“，启动关闭信息将不会在界面中显示，而是记录在安装目录下的data 目录里面，文件名字一般是 hostname.err ,可以通过此文件查看MySQL 的控制台信息。



## 小结

​		本章以 Windows 平台和 Linux 平台为例，讲述了 MySQL 8.0 在不同操作系统品改下的下载、安装、配置、启动和关闭的过程。其中在 Windows平台下介绍了MySQL Install 图形化安装包；在 Linux 平台下只介绍了 RPM 包的两种安装方法，而没有介绍二进制包和源码包。之所以选择这几种包进行介绍，主要是因为他们比较简单，适合初学者快速入门。