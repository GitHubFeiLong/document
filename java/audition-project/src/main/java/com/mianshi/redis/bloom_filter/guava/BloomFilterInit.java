package com.mianshi.redis.bloom_filter.guava;

import com.google.common.collect.Lists;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2022/4/26 16:42
 */
public class BloomFilterInit {
    //~fields
    //==================================================================================================================
    /**
     * 布隆过滤器实例，这里的泛型我是使用 数据库Id
     */
    private BloomFilter<Long> bloomFilter;
    //~construct methods
    //==================================================================================================================

    public BloomFilterInit() {
        initBloomFilter();
    }

    //~methods
    //==================================================================================================================
    @PostConstruct
    public void initBloomFilter() {
        // 使用硬编码的方式（实际开发过程中是从db查询数据）
        final int size = 100000;
        List<Long> userIds = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            userIds.add((long)i);
        }

        /*
            创建布隆过滤器:
            1. 元素是Long类型 Funnels.longFunnel()
            2. 预估存储元素的个数
            3. 设置误判率（默认值是0.03D）
         */
        //
        bloomFilter = BloomFilter.create(Funnels.longFunnel(), userIds.size(), 0);
        // 将用户id插入到布隆过滤器中
        for (Long userId: userIds) {
            /*
                使用put方法添加元素到bloomFilter
                使用 mightContain 方法判断元素是否存在
             */
            bloomFilter.put(userId);
        }
    }

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        // 检查是否存在
        BloomFilterInit bloomFilterInit = new BloomFilterInit();
        ArrayList<Long> list = Lists.newArrayList(100L, 100001L, 21000L, 1000001L);
        for (Long l : list) {
            boolean b = bloomFilterInit.bloomFilter.mightContain(l);
            System.out.printf("元素:%s 布隆过滤器%s命中\n", l, b ? "" : "未");
        }

    }
}
