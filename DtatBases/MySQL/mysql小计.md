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