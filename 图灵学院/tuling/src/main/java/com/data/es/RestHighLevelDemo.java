package com.data.es;

import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * 类描述：
 * es 高级 Rest Api
 * @Author msi
 * @Date 2021-07-24 10:14
 * @Version 1.0
 */
public class RestHighLevelDemo {
    public static void main(String[] args) throws IOException {
        // 获取连接客户端
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
        // 构建请求
        GetRequest getRequest = new GetRequest("book", "2");

        // 执行
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);

        // 获取结果
        if (getResponse.isExists()) {
            System.out.println(getResponse);
        }
    }
}
