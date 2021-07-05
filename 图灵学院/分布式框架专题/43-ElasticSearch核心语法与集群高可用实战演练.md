# 43-ElasticSearch核心语法与集群高可用实战演练

# 一、文档批量操作

这里多个文档是指，批量操作多个文档，搜索查询文档将在之后的章节讲解

## 1.批量获取文档数据

批量获取文档数据是通过_mget的API来实现的

(1) 在URL中不指定index和type

+ 请求方式：GET
+ 请求地址：_mget
+ 功能说明 ： 可以通过ID批量获取不同index和type的数据
+ 请求参数：
  + docs : 文档数组参数
  + _index : 指定index
  + _type : 指定type
  + _id : 指定id
  + _source : 指定要查询的字段  

```json
GET _mget
{
 "docs": [
   {
     "_index": "es_db",
     "_type": "_doc",
     "_id": 1
   },
   {
     "_index": "es_db",
     "_type": "_doc",
     "_id": 2
    }
  ]
}
```

响应结果如下 ：

```json
{
2 "docs" : [3 {
4 "_index" : "es_db",
5 "_type" : "_doc",
6 "_id" : "1",
7 "_version" : 3,
8 "_seq_no" : 7,
9 "_primary_term" : 1,
10 "found" : true,
11 "_source" : {
12 "name" : "张三666",
13 "sex" : 1,
14 "age" : 25,
15 "address" : "广州天河公园",
16 "remark" : "java developer"
17 }
18 },
19 {
20 "_index" : "es_db",
21 "_type" : "_doc",
22 "_id" : "2",
23 "_version" : 1,
24 "_seq_no" : 1,
25 "_primary_term" : 1,
26 "found" : true,
27 "_source" : {
28 "name" : "李四",
29 "sex" : 1,
30 "age" : 28,
31 "address" : "广州荔湾大厦",
32 "remark" : "java assistant"
33 }
34 }
35 ]
36 }
```

(2)在URL中指定index

请求方式：GET 请求地址：/{{indexName}}/_mget

功能说明 ： 可以通过ID批量获取不同index和type的数据
请求参数：
docs : 文档数组参数
_index : 指定index
_type : 指定type
_id : 指定id
_source : 指定要查询的字段