---
tags: [mongodb]
title: MongoDB副本集（Replica Sets）安装教程
created: '2022-04-16T04:10:41.061Z'
modified: '2022-04-17T03:09:03.979Z'
---

# MongoDB副本集（Replica Sets）安装教程

最近在生产环境下部署公司项目。由于项目中有使用到MongoDB，所以参考网上的博客，在服务器上进行搭建MongoDB副本集。今天打算将其记录成笔记，其目的是巩固记忆，方便后续二次搭建。

## 为什么使用副本集
MongoDB中的副本集（Replica Set）是一组维护相同数据集的mongod服务。 副本集可提供冗余和高可用性，是所有生产部署的基础。也可以说，副本集类似于有自动故障恢复功能的主从集群。通俗的讲就是用多台机器进行同一数据的异步同步，从而使多台机器拥有同一数据的多个副本，并且当主库当掉时在不需要用户干预的情况下自动切换其他备份服务器做主库。而且还可以利用副本服务器做只读服务器，实现读写分离，提高负载。

关于MongoDB副本集的更多描述请查看官方[副本集文档](https://www.mongodb.com/docs/manual/replication/)

## 准备工作
准备3台服务器（自己电脑上装了3台虚拟机进行模拟）
1. 192.168.31.128:27017 主要成员（Primary）
2. 192.168.31.129:27017 仲裁（Arbiter）
3. 192.168.31.130:27017 副本成员（Secondaries）

>副本集有两种类型，三种角色。
>**两种类型：**
>1. 主节点（Primary）类型：数据操作的主要连接点，可读写
>2. 次、辅助、从节点（Secondaries）类型：数据冗余备份节点，可以读（需要设置）或选举
>
>**三种角色：**
>1. 主要成员（Primary）：主要接收所有写操作。就是主节点。
>2. 副本成员（Replicate）：从主节点通过复制操作以维护相同的数据集，即备份数据，不可写操作，但可以读操作（但需要配置）。是默认的一种从节点类型。
>3. 仲裁者（Arbiter）：不保留任何数据的副本，只具有投票选举作用。当然也可以将仲裁服务器维护为副本集的一部分，即副本成员同时也可以是仲裁者。也是一种从节点类型。

## 安装主节点
### 下载安装包
打开[MongoDB官网](https://www.mongodb.com/) 首页，选择头部的Products（产品）中的二级标题Community Edition（社区版）下面的Community Server（社区服务）。
选择合适的Community Server版本下载即可。
> MongoDB的版本命名规范如：x.y.z；
y为奇数时表示当前版本为开发版，如：1.5.2、4.1.13；
y为偶数时表示当前版本为稳定版，如：1.6.3、4.0.10；
z是修正版本号，数字越大越好。 

这里我下载的版本是5.0.7，平台是Redhat/Centos7,包是tgz格式压缩包。[5.0.7 Centos7 tgz 快速下载连接](https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-rhel70-5.0.7.tgz)

```bash
# 下载软件包
wget https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-rhel70-5.0.7.tgz
```
### 解压并创建重要目录
将上一步下载好的压缩包，移动到`/usr/local`目录下，并且将其解压重命名为`mongodb`，最后在`mongodb`下创建一些文件目录和配置文件。
```bash
# 移动安装包到/usr/local目录
mv mongodb-linux-x86_64-rhel70-5.0.7.tgz /usr/local/
# 进行解压
tar zxvf mongodb-linux-x86_64-rhel70-5.0.7.tgz 
# 重命名文件
mv mongodb-linux-x86_64-rhel70-5.0.7 mongodb
# 创建目录
cd mongodb && mkdir {data,log,conf,pid}
# 创建配置文件
touch /usr/local/mongodb/conf/mongo.conf
```
各目录介绍：
+ data目录用来存放数据库数据文件
+ log目录存放mongo的日志文件
+ conf目录存放mongodb的配置文件
+ pid目录存放mongodb运行时的pid文件


### 创建keyFile
keyfile是用于mongodb集群内部成员认证用的。keyfile主要是用于集群内部认证用，唯一的要求是6-1024长度内容，集群内部成员该文件值必须一样才可以。
```bash
openssl rand -base64 20 > keyfile
chmod 400 keyfile
```

### 编写配置文件
编辑配置文件（`/usr/local/mongodb/conf/mongo.conf`）,编辑完成后保存退出。
```yaml
systemLog:
  #MongoDB发送所有日志输出的目标指定为文件  
  destination: file
  #mongod或mongos应向其发送所有诊断日志记录信息的日志文件的路径  
  path: "/usr/local/mongodb/log/mongod.log"
  #当mongos或mongod实例重新启动时，mongos或mongod会将新条目附加到现有日志文件的末尾  
  logAppend: true
storage:
  #mongod实例存储其数据的目录。storage.dbPath设置仅适用于mongod
  dbPath: "/usr/local/mongodb/data"
  journal:
    #启用或禁用持久性日志以确保数据文件保持有效和可恢复。     
    enabled: true
processManagement:
  #启用在后台运行mongos或mongod进程的守护进程模式。  
  fork: true
  #指定用于保存mongos或mongod进程的进程ID的文件位置，其中mongos或mongod将写入其PID
  pidFilePath: "/usr/local/mongodb/pid/mongod.pid"
  #从中加载时区数据库的完整路径
  timeZoneInfo: /usr/share/zoneinfo
net:
  #服务实例绑定所有IP，有副作用，副本集初始化的时候，节点名字会自动设置为本地域名，而不是ip
  #bindIpAll: true
  #服务实例绑定的IP,0.0.0.0让所有机器都能连接。
  bindIp: 0.0.0.0
  #bindIp
  #绑定的端口  
  port: 27017
# 安全配置  
security:
  # 配置密码文件
  keyFile: /usr/local/mongodb/keyfile
replication:
  #副本集的名称  
  replSetName: "myrs"

```
### 设置环境变量
设置好环境变量，方便后续直接执行mongo脚本。
```bash
# 将mongodb的bin目录添加环境变量PATH中去
[root@localhost mongodb]# echo "export PATH=$PATH:/usr/local/mongodb/bin" >> /etc/profile
# 刷新环境变量
[root@localhost mongodb]# source /etc/profile
# 测试环境变量设置是否成功
[root@localhost mongodb]# mongo -version
MongoDB shell version v5.0.7
Build Info: {
    "version": "5.0.7",
    "gitVersion": "b977129dc70eed766cbee7e412d901ee213acbda",
    "openSSLVersion": "OpenSSL 1.0.1e-fips 11 Feb 2013",
    "modules": [],
    "allocator": "tcmalloc",
    "environment": {
        "distmod": "rhel70",
        "distarch": "x86_64",
        "target_arch": "x86_64"
    }
}
```

### 启动mongod服务
启动测试下，mongo是否启动成功。
```bash
[root@localhost mongodb]# mongod -f conf/mongo.conf 
about to fork child process, waiting until server is ready for connections.
forked process: 18064
child process started successfully, parent exiting
```
### 关闭mongod服务
```bash
[root@localhost mongodb]#  ps -ef | grep mongo
root      80280      1 29 23:44 ?        00:00:01 mongod -f conf/mongo.conf
root      80550  36922  0 23:44 pts/0    00:00:00 grep --color=auto mongo

[root@localhost mongodb]# kill -9 80280
```

### 删除数据
清空 data，log, pid 三个目录，后面再复制mongodb目录后，其它两台机器上的数据目录是全新的。
```bash
[root@localhost mongodb]# cd /usr/local/mongodb/
[root@localhost mongodb]# rm -rf {data/*,log/*,pid/*}
```

### 做成系统服务

1. 在/usr/lib/systemd/system 下创建一个mongod.service 文件，内容如下：
```service
[Unit]
Description=mongodb service
Documentation=https://docs.mongodb.com/manual/
After=network.target remote-fs.target nss-lookup.target

[Service]
Type=forking
PIDFile=/usr/local/mongodb/pid/mongod.pid
ExecStart=/usr/local/mongodb/bin/mongod -f /usr/local/mongodb/conf/mongo.conf
ExecReload=/bin/kill -s HUP $MAINPID
ExecStop=/usr/local/mongodb/bin/mongod --shutdown --config /usr/local/mongodb/conf/mongo.conf
PrivateTmp=true
Restart=always
RestartSec=1
[Install]
WantedBy=multi-user.target
```
2. 服务使用命令
+ 启动服务
  ```bash
  systemctl start mongod.service
  ```
+ 重启服务
  ```bash
  systemctl restart mongod.service
  ```
+ 关闭服务
  ```bash
  systemctl stop mongod.service
  ```
+ 开机启动
  ```bash
  systemctl enable mongodb.service
  ```
> 其它两台服务器可以参考上面的方式进行创建服务。


## 安装剩余两台
### 复制已安装目录
将上面已经安装好的mongodb目录完整的拷贝到其它两台服务器上，下面我将使用scp命令进行拷贝文件。
> + 可以在另外两台机器上进行重复安装。
> + 也可以将文件目录下载后再将其拷贝过去。
> + 也可以使用`scp`命令将其拷贝到另两台服务器去。

使用 scp 的 -r 选项，进行文件夹拷贝，根据提示输入远程机器的root密码即可。
```bash
# 拷贝到 129.168.31.130
[root@localhost local]# scp -r /usr/local/mongodb root@192.168.31.130:/usr/local/
The authenticity of host '192.168.31.130 (192.168.31.130)' can't be established.
ECDSA key fingerprint is SHA256:h72YDYewH4bpsq408yVkdF5S2hPzsbeYO03UqMselWI.
ECDSA key fingerprint is MD5:55:ed:19:03:c0:2e:3b:9d:2e:7e:ef:74:17:c9:e2:59.
Are you sure you want to continue connecting (yes/no)? yes
Warning: Permanently added '192.168.31.130' (ECDSA) to the list of known hosts.
root@192.168.31.130's password: 

# 拷贝到 129.168.31.129
[root@localhost local]# scp -r /usr/local/mongodb root@192.168.31.129:/usr/local/
root@192.168.31.129's password: 

```
### 设置环境变量
在另外两台机器上设置环境变量，具体步骤和上面一致
```bash
# 将mongodb的bin目录添加环境变量PATH中去
[root@localhost mongodb]# echo "export PATH=$PATH:/usr/local/mongodb/bin" >> /etc/profile
# 刷新环境变量
[root@localhost mongodb]# source /etc/profile
```

## 初始化副本集
### 3台机器均启动mongo
```bash
mongod -f /usr/local/mongodb/conf/mongo.conf
```
### 初始化
连接主节点，进行副本集初始化
```bash
# 如果是在 128机器上连接自己，可以直接使用 mongo命令不加参数
mongo --host 192.168.31.128 --port 27017
```
当连接mongodb后，需要进行初始化副本集。

```bash
>rs.initiate(
   {
      _id: "myrs",
      version: 1,
      members: [
         { _id: 0, host : "192.168.31.128:27017" },
         { _id: 1, host : "192.168.31.129:27017", arbiterOnly: true },
         { _id: 2, host : "192.168.31.130:27017" }
      ]
   }
)
{ "ok" : 1 }
myrs:OTHER> 
myrs:SECONDARY> 
myrs:PRIMARY>

# 查看副本集状态
myrs:PRIMARY> rs.status()

```

>使用默认的方式初始后，再新增次节点和仲裁节点时，其状态都不正确。目前未找到原因。
> ```bash
> rs.initiate()
>{
>        "info2" : "no configuration specified. Using a default configuration for the set",
>        "me" : "localhost.localdomain:27017",
>        "ok" : 1
>}
># 初始化后首先显示是次节点，过一会回车后变成主节点
>myrs:SECONDARY> 
>myrs:PRIMARY> 
> #添加次节点
> rs.add(config)
> #添加仲裁节点
> rs.addArb(config)
>```
>config的内容是一个json格式如下:
>```json
>{
>_id: <int>,
>host: <string>, // required 主机ip地址，可以只输入ip此时端口默认是27017，也可以指定端口："192.168.31.129:27017"
>arbiterOnly: <boolean>, // 是否是仲裁节点，默认false
>buildIndexes: <boolean>,
>hidden: <boolean>, 
>priority: <number>, // 优先级0-1000，相当于可额外增加0-1000的票数，优先级的值越大，就越可能获得多数成员的投票（votes）数，次节点默认值是1，仲裁节点默认是0
>tags: <document>,
>slaveDelay: <int>,
>votes: <number>
>}
>```


说明：

1. 当执行完初始化成功后，我们使用mongo终端连接到192.168.31.129（仲裁节点），192.168.31.130（次节点）。可以发现命令提示符分别变成了`myrs:ARBITER>`和`myrs:SECONDARY> `
2. 默认次节点是不可以读写操作的，但是我们可以设置拥有读权限（认证过后，执行：rs.secondaryOk() 或者 rs.secondaryOk(true)即可）
3. 我们可以取消次节点的读权限（rs.secondaryOk(false)）

## 副本集的一些操作记录
### 副本集的shell命令
| 名称 | 描述 | 示例 |
|:------------|:-------------:|:-------------:|
| rs.add() | 将成员添加到副本集。 | rs.add('192.168.31.130:27017')|
| rs.addArb() | 将仲裁者添加到副本集。 |rs.addArb('192.168.31.129:27017')|
| rs.conf() | 返回副本集 configuration 文档。 |
| rs.freeze() | 阻止当前成员在 time 期间寻求选举。 |
| rs.help() | 返回副本集函数的基本帮助文本。 |
| rs.initiate() | 初始化新的副本集。 |
| rs.printReplicationInfo() | 从主数据库的角度打印副本集状态的报告。 |
| rs.printSlaveReplicationInfo() | 从辅助节点的角度打印副本集状态的报告。 |
| rs.reconfig() | Re-configures 通过应用新副本集 configuration object 设置副本。 |
| rs.remove() | 从副本集中删除成员。 |
| rs.slaveOk() | 已过时，建议使用 rs.secondaryOk()。 |
| rs.secondaryOk() | 在次节点中，设置次节点可以读数据，rs.secondaryOk()等同于rs.secondaryOk(true)。想要关闭时，执行rs.secondaryOk(false)|
| rs.status() | 返回包含有关副本集的 state 的信息的文档。 |
| rs.stepDown() | 导致当前主成为强制选举的辅助。 |
| rs.syncFrom() | 设置此副本集成员将同步的成员，覆盖默认同步目标选择逻辑。 |

### 副本集的shell命令
|名称	| 描述 |
|:---|:---:|
|applyOps |	将OPLOG条目应用于当前数据集的内部命令。|
|isMaster	| 显示有关此成员在副本集中的角色的信息，包括它是否为 master。|
|replSetAbortPrimaryCatchUp	| 强制选举的主中止同步(赶上)然后完成向主要的过渡。|
|replSetFreeze |	阻止当前成员在 time 期间选举主。|
|replSetGetConfig	| 返回副本集的 configuration object。|
|replSetGetStatus	| 返回报告副本集状态的文档。|
|replSetInitiate	| 初始化新的副本集。|
|replSetMaintenance|	启用或禁用维护模式，该模式将次要节点放在RECOVERING state 中。|
|replSetReconfig	| 将新的 configuration 应用于现有副本集。|
|replSetResizeOplog	| 动态调整副本集成员的 oplog 大小。仅适用于 WiredTiger 存储引擎。|
|replSetStepDown	| 强制当前主到 step 下降并成为次要，迫使选举。|
|replSetSyncFrom	| 显式覆盖用于选择要复制的成员的默认逻辑。|

重新同步	从主强制mongod到 re-synchronize。仅适用于 master-slave 复制。


### rs.conf() 
```bash
myrs:PRIMARY> rs.conf()
{
        "_id" : "myrs", // 副本集的配置数据存储的主键值，默认就是副本集的名字
        "version" : 1,
        "term" : 8,
        "members" : [ // 副本集成员数组
                {
                        "_id" : 0,
                        "host" : "192.168.31.128:27017",
                        "arbiterOnly" : false, // 是否是仲裁节点
                        "buildIndexes" : true,
                        "hidden" : false, // 是否是隐藏节点
                        "priority" : 1, // 优先级（权重值）
                        "tags" : {
                                
                        },
                        "secondaryDelaySecs" : NumberLong(0),
                        "votes" : 1
                },
                {
                        "_id" : 1,
                        "host" : "192.168.31.129:27017",
                        "arbiterOnly" : true,
                        "buildIndexes" : true,
                        "hidden" : false,
                        "priority" : 0,
                        "tags" : {
                                
                        },
                        "secondaryDelaySecs" : NumberLong(0),
                        "votes" : 1
                },
                {
                        "_id" : 2,
                        "host" : "192.168.31.130:27017",
                        "arbiterOnly" : false,
                        "buildIndexes" : true,
                        "hidden" : false,
                        "priority" : 1,
                        "tags" : {
                                
                        },
                        "secondaryDelaySecs" : NumberLong(0),
                        "votes" : 1
                }
        ],
        "protocolVersion" : NumberLong(1),
        "writeConcernMajorityJournalDefault" : true,
        "settings" : {  // 副本集的参数配置。
                "chainingAllowed" : true,
                "heartbeatIntervalMillis" : 2000,
                "heartbeatTimeoutSecs" : 10,
                "electionTimeoutMillis" : 10000,
                "catchUpTimeoutMillis" : -1,
                "catchUpTakeoverDelayMillis" : 30000,
                "getLastErrorModes" : {
                        
                },
                "getLastErrorDefaults" : {
                        "w" : 1,
                        "wtimeout" : 0
                },
                "replicaSetId" : ObjectId("625a7bee81773d2b614e1610")
        }
}

```

### rs.status()
```shell
myrs:PRIMARY> rs.status()
{
        "set" : "myrs", // 副本集的名字
        "date" : ISODate("2022-04-17T01:45:08.287Z"),
        "myState" : 1, //说明状态正常
        "term" : NumberLong(8),
        "syncSourceHost" : "",
        "syncSourceId" : -1,
        "heartbeatIntervalMillis" : NumberLong(2000),
        "majorityVoteCount" : 2,
        "writeMajorityCount" : 2,
        "votingMembersCount" : 3,
        "writableVotingMembersCount" : 2,
        "optimes" : {
                "lastCommittedOpTime" : {
                        "ts" : Timestamp(1650159901, 1),
                        "t" : NumberLong(8)
                },
                "lastCommittedWallTime" : ISODate("2022-04-17T01:45:01.392Z"),
                "readConcernMajorityOpTime" : {
                        "ts" : Timestamp(1650159901, 1),
                        "t" : NumberLong(8)
                },
                "appliedOpTime" : {
                        "ts" : Timestamp(1650159901, 1),
                        "t" : NumberLong(8)
                },
                "durableOpTime" : {
                        "ts" : Timestamp(1650159901, 1),
                        "t" : NumberLong(8)
                },
                "lastAppliedWallTime" : ISODate("2022-04-17T01:45:01.392Z"),
                "lastDurableWallTime" : ISODate("2022-04-17T01:45:01.392Z")
        },
        "lastStableRecoveryTimestamp" : Timestamp(1650159841, 1),
        "electionCandidateMetrics" : {
                "lastElectionReason" : "electionTimeout",
                "lastElectionDate" : ISODate("2022-04-17T01:40:21.198Z"),
                "electionTerm" : NumberLong(8),
                "lastCommittedOpTimeAtElection" : {
                        "ts" : Timestamp(0, 0),
                        "t" : NumberLong(-1)
                },
                "lastSeenOpTimeAtElection" : {
                        "ts" : Timestamp(1650116073, 1),
                        "t" : NumberLong(7)
                },
                "numVotesNeeded" : 2,
                "priorityAtElection" : 1,
                "electionTimeoutMillis" : NumberLong(10000),
                "numCatchUpOps" : NumberLong(0),
                "newTermStartDate" : ISODate("2022-04-17T01:40:21.318Z"),
                "wMajorityWriteAvailabilityDate" : ISODate("2022-04-17T01:40:26.886Z")
        },
        "members" : [
                {
                        "_id" : 0,
                        "name" : "192.168.31.128:27017",
                        "health" : 1, // 节点是健康的
                        "state" : 1,
                        "stateStr" : "PRIMARY", // 角色是主节点
                        "uptime" : 304,
                        "optime" : {
                                "ts" : Timestamp(1650159901, 1),
                                "t" : NumberLong(8)
                        },
                        "optimeDate" : ISODate("2022-04-17T01:45:01Z"),
                        "lastAppliedWallTime" : ISODate("2022-04-17T01:45:01.392Z"),
                        "lastDurableWallTime" : ISODate("2022-04-17T01:45:01.392Z"),
                        "syncSourceHost" : "",
                        "syncSourceId" : -1,
                        "infoMessage" : "",
                        "electionTime" : Timestamp(1650159621, 1),
                        "electionDate" : ISODate("2022-04-17T01:40:21Z"),
                        "configVersion" : 1,
                        "configTerm" : 8,
                        "self" : true,
                        "lastHeartbeatMessage" : ""
                },
                {
                        "_id" : 1,
                        "name" : "192.168.31.129:27017",
                        "health" : 1,
                        "state" : 7,
                        "stateStr" : "ARBITER", // 仲裁节点
                        "uptime" : 297,
                        "lastHeartbeat" : ISODate("2022-04-17T01:45:06.556Z"),
                        "lastHeartbeatRecv" : ISODate("2022-04-17T01:45:06.574Z"),
                        "pingMs" : NumberLong(0),
                        "lastHeartbeatMessage" : "",
                        "syncSourceHost" : "",
                        "syncSourceId" : -1,
                        "infoMessage" : "",
                        "configVersion" : 1,
                        "configTerm" : 8
                },
                {
                        "_id" : 2,
                        "name" : "192.168.31.130:27017",
                        "health" : 1,
                        "state" : 2,
                        "stateStr" : "SECONDARY", // 次节点
                        "uptime" : 280,
                        "optime" : {
                                "ts" : Timestamp(1650159901, 1),
                                "t" : NumberLong(8)
                        },
                        "optimeDurable" : {
                                "ts" : Timestamp(1650159901, 1),
                                "t" : NumberLong(8)
                        },
                        "optimeDate" : ISODate("2022-04-17T01:45:01Z"),
                        "optimeDurableDate" : ISODate("2022-04-17T01:45:01Z"),
                        "lastAppliedWallTime" : ISODate("2022-04-17T01:45:01.392Z"),
                        "lastDurableWallTime" : ISODate("2022-04-17T01:45:01.392Z"),
                        "lastHeartbeat" : ISODate("2022-04-17T01:45:06.798Z"),
                        "lastHeartbeatRecv" : ISODate("2022-04-17T01:45:08.217Z"),
                        "pingMs" : NumberLong(0),
                        "lastHeartbeatMessage" : "",
                        "syncSourceHost" : "192.168.31.128:27017",
                        "syncSourceId" : 0,
                        "infoMessage" : "",
                        "configVersion" : 1,
                        "configTerm" : 8
                }
        ],
        "ok" : 1,
        "$clusterTime" : {
                "clusterTime" : Timestamp(1650159901, 1),
                "signature" : {
                        "hash" : BinData(0,"rTJm51/Wo0vX7A3Lbgaz9Rthr8c="),
                        "keyId" : NumberLong("7087113268703002629")
                }
        },
        "operationTime" : Timestamp(1650159901, 1)
}

```

## 修改优先级
比如，下面提升从节点的优先级：
1. 先将配置导入cfg变量
  ```bash
  myrs:PRIMARY> cfg=rs.conf()
  ```
2. 然后修改值（ID号默认从0开始）：
  ```bash
  myrs:PRIMARY> cfg.members[2].priority=2
  2
  ```
3. 重新加载配置
```bash
myrs:PRIMARY> rs.reconfig(cfg)
{
        "ok" : 1,
        "$clusterTime" : {
                "clusterTime" : Timestamp(1650162053, 1),
                "signature" : {
                        "hash" : BinData(0,"Wap6hq5wxmpqQkQ/vHrl1v8/bAY="),
                        "keyId" : NumberLong("7087113268703002629")
                }
        },
        "operationTime" : Timestamp(1650162053, 1)
}

```
稍等片刻会重新开始选举。



## spring boot 集成副本集
1. pom.xml中引入依赖
```xml
 <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```
2. 在application.yml中配置副本集
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://zs:123456@192.168.31.128:27017,192.168.31.129:27017,192.168.31.130:27017/db1?replicaSet=myrs&readPreference=secondaryPreferred&connectTimeoutMS=300000&slaveOk=true

```

3. mongodb客户端连接语法
```txt
mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]
```
+ mongodb:// 这是固定的格式，必须要指定。
+ username:password@ 可选项，如果设置，在连接数据库服务器之后，驱动都会尝试登陆这个数据库
+ host1 必须的指定至少一个host, host1 是这个URI唯一要填写的。它指定了要连接服务器的地址。如果要连接复制集，请指定多个主机地址。
+ portX 可选的指定端口，如果不填，默认为27017
+ /database 如果指定username:password@，连接并验证登陆指定数据库。若不指定，默认打开test 数据库。
+ ?options 是连接选项。如果不使用/database，则前面需要加上/。所有连接选项都是键值对name=value，键值对之间通过&或;（分号）隔开

options有如下选择：

| 选项 | 描述 |
|:------|:------|
|replicaSet=name | 验证replica set的名称。 Impliesconnect=replicaSet.|
|slaveOk=true\|false | true:在connect=direct模式下，驱动会连接第一台机器，即使这台服务器不是主。在connect=replicaSet模式下，驱动会发送所有的写请求到主并且把读取操作分布在其他从服务器。false: 在connect=direct模式下，驱动会自动找寻主服务器. 在connect=replicaSet 模式下，驱动仅仅连接主服务器，并且所有的读写命令都连接到主服务器。|
|safe=true\|false | true: 在执行更新操作之后，驱动都会发送getLastError命令来确保更新成功。(还要参考 wtimeoutMS).false: 在每次更新之后，驱动不会发送getLastError来确保更新成功。|
|w=n | 驱动添加 { w : n } 到getLastError命令. 应用于safe=true。|
|wtimeoutMS=ms |驱动添加 { wtimeout : ms } 到 getlasterror 命令. 应用于 safe=true.|
|fsync=true\|false | true: 驱动添加 { fsync : true } 到 getlasterror 命令.应用于safe=true.false: 驱动不会添加到getLastError命令中。|
|journal=true\|false | 如果设置为 true, 同步到 journal (在提交到数据库前写入到实体中).应用于 safe=true|
|connectTimeoutMS=ms | 可以打开连接的时间。|
|socketTimeoutMS=ms | 发送和接受sockets的时间。|


