# ELK安装教程

## Windows

### Elasticsearch

#### 下载解压

[官方下载]([Past Releases of Elastic Stack Software | Elastic](https://www.elastic.co/cn/downloads/past-releases#elasticsearch))， 选择指定的产品和版本，将下载好的压缩包进行解压，**解压后的目录，不能出现中文和空格！**

#### 修改配置文件

1. 修改`config\elasticsearch.yml`

```yaml
#配置集群名称
cluster.name: cluster-es
#配置节点名称
node.name: node1
#数据目录
path.data: D:\application\elasticsearch-7.13.2-windows-x86_64\elasticsearch-7.13.2\data
#日志输出目录
path.logs: D:\application\elasticsearch-7.13.2-windows-x86_64\elasticsearch-7.13.2\log
network.host: 0.0.0.0
#端口号
http.port: 9200
discovery.seed_hosts: ["localhost"]
cluster.initial_master_nodes: ["node1"]
bootstrap.system_call_filter: false
bootstrap.memory_lock: false
http.cors.enabled: true
http.cors.allow-origin: "*"
action.destructive_requires_name: true #为了安全起见，防止恶意删除索引，删除时必须指定索引名：
```

2. 修改`config\jvm.options`,指定jvm内存大小

```txt
-Xms500m
-Xmx500m
```

#### 启动

启动`bin\elasticsearch.bat`，访问 http://localhost:9200

```json
{
  "name" : "node1",
  "cluster_name" : "cluster-es",
  "cluster_uuid" : "dvYlP0PgSrWF-n3de8Ab4A",
  "version" : {
    "number" : "7.13.2",
    "build_flavor" : "default",
    "build_type" : "zip",
    "build_hash" : "4d960a0733be83dd2543ca018aa4ddc42e956800",
    "build_date" : "2021-06-10T21:01:55.251515791Z",
    "build_snapshot" : false,
    "lucene_version" : "8.8.2",
    "minimum_wire_compatibility_version" : "6.8.0",
    "minimum_index_compatibility_version" : "6.0.0-beta1"
  },
  "tagline" : "You Know, for Search"
}
```



### 插件安装

##### IK分词器

1. 在plugins下新建文件夹`ik`

2. 将下载的IK分词器压缩包复制到elasticsearch根目录的`plugins\ik`下。

3. 将压缩文件内容，提取到当前文件下即可,然后删除压缩包。

   ![image-20210707213040354](ELK安装教程.assets/image-20210707213040354.png)

4. 重启elasticsearch



### Kibana

#### 下载解压

[下载](https://www.elastic.co/cn/downloads/past-releases#kibana)， 选择指定的产品和版本，将下载好的压缩包进行解压，**解压后的目录，不能出现中文和空格！**

#### 修改配置文件

`config\kibana.yml`

```yaml
server.port: 5601
server.host: "服务器IP"
elasticsearch.hosts: ["http://IP:9200"] #这里是elasticsearch的访问地址
i18n.locale: "zh-CN" #设置界面语言为中文
```

启动 `bin\kibana.bat`，访问 http://localhost:5601

4. 

### 安装 elasticsearch-head

[进入github下载 elasticsearch-head](https://github.com/mobz/elasticsearch-head/releases)

下载完成后，解压进入目录，使用`npm i`命令进行下载包文件，然后使用启动命令`npm run start` 启动即可。当启动完成后，浏览器访问 http://localhost:9100 即可。

> 注意:需要Node环境
