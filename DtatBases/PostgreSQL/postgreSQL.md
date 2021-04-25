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



## 函数

​		非空，等同于isnull()

```sql
-- col_num 是null，使用 value
COALESCE(col_num, value)
```

## 查询列的数据类型转换

​		例如，将 4.0/3 转换成两位小数（cast）

```sql
select 4.0/3 -- 结果是1.3333333333333333
select cast(4.0/3 as decimal(10,2)) -- 结果是1.33
```



## 新增或修改(UPSERT)

```sql
INSERT INTO question_type_introduction (uuid, question_type_introduction)
        VALUES(#{uuid}, #{questionTypeIntroduction})
        ON conflict(uuid) DO UPDATE SET question_type_introduction = #{questionTypeIntroduction}
```

```sql
-- 批量新增时重复进行更新
INSERT INTO PUBLIC.zp_course ( uuid, close_major_uuid, code, NAME, credit, textbook_name, textbook_version, textbook_editor, remark, is_delete, create_time, update_time )
VALUES
	( 1,'79be0d3c-a31b-11eb-9630-1aa7218a11c3', '课程11', '课程代11', 11, '代码11', '课代码1', '试听课程', '课程代码11', NULL, NULL, NULL ),
	( 2,'79be0d3c-a31b-11eb-9630-1aa7218a11c3', '课程22', '课程代22', 22, '代码22', '课代码2', '课程代码2', '课程代码22', NULL, NULL, NULL ) 
	ON CONFLICT ( uuid ) DO
UPDATE 
	SET close_major_uuid = EXCLUDED.close_major_uuid,
	code = EXCLUDED.code,
	NAME = EXCLUDED.NAME,
	credit = EXCLUDED.credit,
	textbook_name = EXCLUDED.textbook_name,
	textbook_version = EXCLUDED.textbook_version,
	textbook_editor = EXCLUDED.textbook_editor,
	remark = EXCLUDED.remark,
	update_time = now( );
------------------------------------------------------------------------------------------------------------------------
```

