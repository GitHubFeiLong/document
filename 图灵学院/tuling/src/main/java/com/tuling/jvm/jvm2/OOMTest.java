package com.tuling.jvm.jvm2;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-07-01 21:26
 * @Version 1.0
 */
public class OOMTest {
    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (true) {
            list.add(new User(i++, UUID.randomUUID().toString()));
            new User(j--, UUID.randomUUID().toString());
        }
    }
}
