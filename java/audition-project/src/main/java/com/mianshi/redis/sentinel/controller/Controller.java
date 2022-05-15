package com.mianshi.redis.sentinel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/5/15 10:12
 */
@RequestMapping("/redis/sentinel")
@RestController
public class Controller {

    //~fields
    //==================================================================================================================
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //~methods
    //==================================================================================================================
    @GetMapping("/key/{key}")
    public String getKey(@PathVariable String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @PostMapping("/key")
    public String getKey(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
        return "设置成功";
    }

}