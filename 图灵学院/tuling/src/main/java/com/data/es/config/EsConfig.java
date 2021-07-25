package com.data.es.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-07-24 10:28
 * @Version 1.0
 */
@Configuration
public class EsConfig {

    @Value("${es.host}")
    private String host;

    @Bean
    public RestHighLevelClient setRestHighLevelClient () {
        System.out.println("host => " + host);
        String[] split = host.split(":");
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(split[0], Integer.parseInt(split[1])))
        );

        return client;
    }
}
