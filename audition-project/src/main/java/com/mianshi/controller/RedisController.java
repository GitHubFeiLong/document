package com.mianshi.controller;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-06-08 10:42
 * @Version 1.0
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    private static int count = 10;

    /**
     * Redis实现分布式锁
     */
    @GetMapping("/demo1")
    public void demo1 () {
        String key = "key";
        String value = UUID.randomUUID().toString();
        if (count < 0) {
//            System.out.println("库存为空");
            return;
        }
        Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(key, value, 30, TimeUnit.SECONDS);
        if (!aBoolean) {
//            System.out.println("未获取到锁:" + count);
            return;
        }

        // 减库存
        try {
            System.out.println("count = " + count);
            count--;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (value.equals(stringRedisTemplate.opsForValue().get(key))) {
                stringRedisTemplate.delete(key);
            }
        }
    }

    /**
     * redisson 分布式锁
     */
    @GetMapping("/demo2")
    public void demo2 () {

        String key = "redisSonKey";
        RLock lock = redissonClient.getLock(key);
        lock.lock(10, TimeUnit.SECONDS);

        if (count >= 0) {
            count--;
            System.out.println("count=" + count);
        }

        lock.unlock();
    }

    private static int insertions = 10000000;
    /**
     * 布隆过滤器
     */
    public static void main(String[] args) {
        BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), insertions, 0.001);

        Set<String> sets = new HashSet<String>(insertions);

        List<String> lists = new ArrayList<String>(insertions);

        for (int i = 0; i < insertions; i++) {
            String uid = UUID.randomUUID().toString();
            bloomFilter.put(uid);
            sets.add(uid);
            lists.add(uid);
        }

        int right = 0;
        int wrong = 0;

        for (int i = 0; i < 10000; i++) {
            String data = i % 100 == 0 ? lists.get(i / 100) : UUID.randomUUID().toString();
            if (bloomFilter.mightContain(data)) {
                if (sets.contains(data)) {
                    right++;
                    continue;
                }
                wrong++;
            }
        }

        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMaximumFractionDigits(2);
        float percent = (float) wrong / 9900;
        float bingo = (float) (9900 - wrong) / 9900;

        System.out.println("在 " + insertions + " 条数据中，判断 100 实际存在的元素，布隆过滤器认为存在的数量为：" + right);
        System.out.println("在 " + insertions + " 条数据中，判断 9900 实际不存在的元素，布隆过滤器误认为存在的数量为：" + wrong);
        System.out.println("命中率为：" + percentFormat.format(bingo) + "，误判率为：" + percentFormat.format(percent));
    }

}
