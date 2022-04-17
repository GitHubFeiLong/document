---
tags: [mongodb]
title: MongoDB用户管理命令
created: '2022-04-16T08:22:34.289Z'
modified: '2022-04-17T03:44:41.811Z'
---

# MongoDB用户管理命令

## 1. 创建用户

### 1.1. 创建超级管理员
1. 连接mongodb,进入MongoDB的shell
```bash
mongo
```

2. 切换数据库到admin数据库
```bash
use admin
```

3. 创建超级管理员
```bash
db.createUser( 
 { user: "admin", 
  customData：{description:"superuser"},
  pwd: "admin", 
  roles: [ { role: "userAdminAnyDatabase", db: "admin" } ] 
 } 
)
```
> 其中各个字段解释如下:
user字段： 新用户的名字。
pwd 字段 用户的密码
cusomData 字段，任意内容，可以是用户名的介绍。
roles 字段 指定用户的角色，用于给一个空数组，给新用户设定空的角色，在roles字段，可以指定设置相关的角色。
db 数据库的名字，用于管理数据库。


4. 创建超级用户
这里创建一个超级用户，用于管理全部用户的权限
```bash
db.createUser(
  {
    user:"root",
    pwd:"root",
    roles:["root"]
  }
)
```

### 1.2. 创建普通库管理员
创建一个业务数据库的管理员的用户，这个用户专门负责一个或者几个数据库的增删查改。
```bash
# 未进行认证时，不允许创建用户。需要先使用管理员认证
use admin
db.auth('root','root');
# 切换到需要添加库管理员的数据库
use db1
# 一般情况，我们会针对一个库创建一个用户，供java客户端连接
db.createUser({
  user:"zs",
  pwd:"123456",
  customData:{
    name:'jim',
    email:'jim@qq.com',
    age:18,
  },
  roles:[
    {role:"readWrite",db:"db1"}
  ]
})
```
>其中
数据库用户角色，read，readWrite。
数据库管理角色： dbAdmin，dbOwner，userAdmin
集群管理角色： clusterAdmin，clusterManager，clusterMonitor，hostManage。
备份恢复角色： backup，restore。
所有数据库角色： readAnyDatabase。readWriteAnyDatabase，userAdminAnyDatabase，dbAdminAnyDatabase。
超级用户角色： root
内部角色 _system
这样就完成了一个最基本的数据库管理角色的创建。



## 2. 查看用户

使用如下的命令，进行查看用户
```bash
show users
```

## 3. 修改用户信息
前面的步骤一致，后续的用户增查改删操作不同。
```bash
# 切换到admin数据库
use admin
# 进行认证
db.auth('管理员用户名', '管理员密码')
# 切换到用户所在库
use db1
# 进行 増查改删该数据库专有用户
....
```

**注意**：在修改用户的信息时，首先需要切换到admin数据库，其次需要进行**管理员认证**，如果不进行认证或使用普通用户认证。再进行修改用户信息时，会提示**需要认证**或**认证失败**


### 3.1. 修改密码
修改用户的密码，需要先切换到用户的库中去,再执行下面的命令进行修改密码
```bash
#使用该命令，可以实现对密码的修改。
db.changeUserPassword("username", "xxx")
```

### 3.2. 修改用户其他信息
```bash
db.runCommand(
  {
    updateUser:"username",
    pwd:"xxx",
    customData:{title:"xxx"}
  }
)

```

## 4. 删除数据库用户
```bash
# 切换到用户所在的库
use db1
# 删除
db.dropUser('zs')
# 检查
show users
```

