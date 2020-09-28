## 序列

1. 创建序列名`exam_basic_id` 从1开始自增

```sql
CREATE SEQUENCE exam_basic_id INCREMENT 1 START 1
MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;
```

2. 字段默认值使用

```sql
nextval('exam_basic_id')
```

3. 序列重置

```sql
alter sequence exam_basic_id restart with 1
```

4. 查询序列

```sql
SELECT nextval('exam_basic_id');
```



## 修改字段数据类型

```sql
alter table test alter column filed type varchar using filed:: varchar
```



