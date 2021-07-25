package com.data.es;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：
 * Elasticsearch 文档操作
 * @Author msi
 * @Date 2021-07-24 10:23
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticsearchDocumentTest {

    @Autowired
    private RestHighLevelClient client;

    /**
     * 测试 同步获取、异步获取
     * @throws IOException
     */
    @Test
    public void testGet() throws IOException {
        // 构建请求
        GetRequest getRequest = new GetRequest("book", "2");
        //========================可选参数 start======================
        //为特定字段配置_source_include
        String[] includes = new String[]{"name", "price","description"};
        String[] excludes = Strings.EMPTY_ARRAY;
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
        getRequest.fetchSourceContext(fetchSourceContext);
        // ========================可选参数 end=====================

        // 同步查询
//        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        // 获取结果
//        if (getResponse.isExists()) {
//            System.out.println("getResponse = " + getResponse);
//        }

        // 异步查询
        ActionListener<GetResponse> listener = new ActionListener<GetResponse>() {
            //查询成功时的立马执行的方法
            @Override
            public void onResponse(GetResponse getResponse) {
                long version = getResponse.getVersion();
                String sourceAsString = getResponse.getSourceAsString();//检索文档(String形式)
                System.out.println(sourceAsString);
            }

            //查询失败时的立马执行的方法
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        };
        //执行异步请求
        client.getAsync(getRequest, RequestOptions.DEFAULT, listener);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试新增
     */
    @Test
    public void testAdd() throws IOException {
        // 1. 新建请求
        IndexRequest request = new IndexRequest("test_posts");
        request.id("2");
        //        =======================构建文档============================
//        构建方法1
        String jsonString="{\n" +
                "  \"user\":\"tomas J\",\n" +
                "  \"postDate\":\"2019-07-18\",\n" +
                "  \"message\":\"trying out es3\"\n" +
                "}";
        request.source(jsonString, XContentType.JSON);

//        构建方法2
//        Map<String,Object> jsonMap=new HashMap<>();
//        jsonMap.put("user", "tomas");
//        jsonMap.put("postDate", "2019-07-18");
//        jsonMap.put("message", "trying out es2");
//        request.source(jsonMap);

//        构建方法3
//        XContentBuilder builder= XContentFactory.jsonBuilder();
//        builder.startObject();
//        {
//            builder.field("user", "tomas");
//            builder.timeField("postDate", new Date());
//            builder.field("message", "trying out es2");
//        }
//        builder.endObject();
//        request.source(builder);
//        构建方法4
//        request.source("user","tomas",
//                    "postDate",new Date(),
//                "message","trying out es2");
//
//        ========================可选参数===================================
        // 设置超时时间
        request.timeout(TimeValue.timeValueSeconds(1));
        request.timeout("1s");
        //自己维护版本号
//        request.version(2);
//        request.versionType(VersionType.EXTERNAL);

        // 执行
        // 同步
        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
        System.out.println("indexResponse = " + indexResponse);

        //异步
//        ActionListener<IndexResponse> listener=new ActionListener<IndexResponse>() {
//            @Override
//            public void onResponse(IndexResponse indexResponse) {
//
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//
//            }
//        };
//        client.indexAsync(request,RequestOptions.DEFAULT, listener );
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //获取插入的类型
        if(indexResponse.getResult()== DocWriteResponse.Result.CREATED){
            DocWriteResponse.Result result=indexResponse.getResult();
            System.out.println("CREATED:"+result);
        }else if(indexResponse.getResult()== DocWriteResponse.Result.UPDATED){
            DocWriteResponse.Result result=indexResponse.getResult();
            System.out.println("UPDATED:"+result);
        }

        ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
        if(shardInfo.getTotal()!=shardInfo.getSuccessful()){
            System.out.println("处理成功的分片数少于总分片！");
        }
        if(shardInfo.getFailed()>0){
            for (ReplicationResponse.ShardInfo.Failure failure:shardInfo.getFailures()) {
                String reason = failure.reason();//处理潜在的失败原因
                System.out.println(reason);
            }
        }
    }

    /**
     * 测试修改
     */
    @Test
    public void testUpdate () throws IOException {
        // 构建请求
        UpdateRequest request = new UpdateRequest("test_posts", "2");
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "tomas JJ");
        request.doc(jsonMap);

        request.timeout("1s");
        // 重试次数
        request.retryOnConflict(3);

        //设置在继续更新之前，必须激活的分片数
//        request.waitForActiveShards(2);
        //所有分片都是active状态，才更新
//        request.waitForActiveShards(ActiveShardCount.ALL);

//        2执行
//        同步
        UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
//        异步

//        3获取数据
        updateResponse.getId();
        updateResponse.getIndex();

        //判断结果
        if (updateResponse.getResult() == DocWriteResponse.Result.CREATED) {
            DocWriteResponse.Result result = updateResponse.getResult();
            System.out.println("CREATED:" + result);
        } else if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {
            DocWriteResponse.Result result = updateResponse.getResult();
            System.out.println("UPDATED:" + result);
        }else if(updateResponse.getResult() == DocWriteResponse.Result.DELETED){
            DocWriteResponse.Result result = updateResponse.getResult();
            System.out.println("DELETED:" + result);
        }else if (updateResponse.getResult() == DocWriteResponse.Result.NOOP){
            //没有操作
            DocWriteResponse.Result result = updateResponse.getResult();
            System.out.println("NOOP:" + result);
        }
    }


    /**
     * 测试删除
     */
    @Test
    public void testDelete() throws IOException {
        DeleteRequest request = new DeleteRequest("test_posts", "2");

        DeleteResponse deleteResponse = client.delete(request, RequestOptions.DEFAULT);

        System.out.println("deleteResponse = " + deleteResponse);
    }


    /**
     * 测试bulk
     */
    @Test
    public void testBulk() throws IOException {
        BulkRequest request = new BulkRequest();
        request.add(new IndexRequest("post").id("1").source("field","value1"));
        request.add(new IndexRequest("post").id("2").source(XContentType.JSON, "field", "2"));
        request.add(new UpdateRequest("post","2").doc(XContentType.JSON, "field", "3"));
        request.add(new DeleteRequest("post").id("1"));

        BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
        for (BulkItemResponse itemResponse:bulkResponse
             ) {
            DocWriteResponse itemResponseResponse = itemResponse.getResponse();

            switch (itemResponse.getOpType()) {
                case INDEX:
                case CREATE:
                    IndexResponse indexResponse = (IndexResponse) itemResponseResponse;
                    indexResponse.getId();
                    System.out.println(indexResponse.getResult());
                    break;
                case UPDATE:
                    UpdateResponse updateResponse = (UpdateResponse) itemResponseResponse;
                    updateResponse.getIndex();
                    System.out.println(updateResponse.getResult());
                    break;
                case DELETE:
                    DeleteResponse deleteResponse = (DeleteResponse) itemResponseResponse;
                    System.out.println(deleteResponse.getResult());
                    break;
            }
        }
    }
}

