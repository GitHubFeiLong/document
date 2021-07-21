### IGNORE 关键字

```mys
更新多行时，如果其中一行或多行出现错误，那么整个操作都被取消

UPDATE IGNORE table_name...
```

设置主键默认值为UUID

```sql
alter table table_name alter column uuid set default (uuid());
```

> 注意是 `(uuid())`

## 删除表

删除表时的SQL语句格式：

```sql
DROP TABLE IF EXISTS `db`.`table`;
```

> 存在时才删除

如果删除表时，有外键约束，会导致删除失败，此时可以先关闭外键约束，然后再删，最后再开启约束

```sql
-- 查看外键约束
SELECT @@FOREIGN_KEY_CHECKS;
-- 关闭外键约束
SET FOREIGN_KEY_CHECKS=0;
-- 开启外键约束
SET FOREIGN_KEY_CHECKS=1;
```

## UPSERT

当数据不存在时，进行插入操作，当数据存在时，进行更新搞作

```sql
INSERT INTO table_name (station_id, user_id, percentage, create_user_id)
VALUES ( #{stationId}, #{userId}, #{percentage}, #{createUserId})
ON DUPLICATE KEY UPDATE percentage=#{percentage}, deleted=0,update_user_id=#{createUserId},update_time=now()
```

> 备注：现用来在出现唯一约束时，会更新数据