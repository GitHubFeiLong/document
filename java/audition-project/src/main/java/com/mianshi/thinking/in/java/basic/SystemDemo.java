package com.mianshi.thinking.in.java.basic;


import java.util.Random;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/5/21 21:30
 */
public class SystemDemo {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String[] args) {
        // System.getProperties().list(System.out);
        // System.out.println(System.getProperty("user.name"));
        // System.out.println(System.getProperty("java.lib.path"));

        /*
            一元减号和一元加号
            一元减号变为相反数，一元加号提生类型为int
         */
        // int i = 10;
        // System.out.println(-i);
        // byte b = 0;
        // System.out.println(+b);

        /*
            随机种子相同，生成得数是一样得
            默认值是时间戳
         */
        // Random random = new Random(47);
        // System.out.println(random.nextInt(100));
        // System.out.println(random.nextInt(100));
        // System.out.println(random.nextInt(100));

        /*
         * 此时的e=10 下面的例子表示 10 * 10 * 10
         */
        // System.out.println(10e2);

        // System.out.println(Float.MAX_VALUE);
        // System.out.println(3.4028235e38);

        // System.out.println(2 & 3); // 2
        // System.out.println(2 | 3); // 3
        // System.out.println(2 ^ 3); // 1

        // int i = -1;
        // System.out.println(Integer.toBinaryString(i));
        // i >>>= 10;
        // System.out.println(Integer.toBinaryString(i));
        // long l = -1;
        // System.out.println(Long.toBinaryString(l));
        // l >>>= 10;
        // System.out.println(Long.toBinaryString(l));
        // short s = -1;
        // System.out.println(Integer.toBinaryString(s));
        // s >>>= 10;
        // System.out.println(Integer.toBinaryString(s));

        // System.out.println(true | true);
        // System.out.println(true & true);
        // System.out.println(true ^ true);

        /*Random r = new Random(47);
        int cur;
        int pre = 0;
        while (true) {

            cur = r.nextInt();
            if (cur > pre) {
                System.out.println("cur = " + cur + ", pre = " + pre + "本次比上次大");
            }else if (cur < pre) {
                System.out.println("cur = " + cur + ", pre = " + pre + "本次比上次小");
            } else {
                System.out.println("cur = " + cur + ", pre = " + pre + "相等");
            }
            pre = cur;
        }*/

        /*for (int i = 1; i <= 100; i++) {
            int count = 0;
            for (int j = 2; j < i; j++) {
                if (i % j == 0) {
                    count ++;
                    break;
                }
            }
            if (count == 0) {
                System.out.println(i + " 是素数");
            }

        }*/

        /*int n = 5;
        int count = 0;
        int one = 1;
        int two = 1;
        int three;
        System.out.print("1 1 ");
        while (count < n - 2) {
            three = one + two;
            one = two;
            two = three;
            System.out.print(three + " ");
            count ++;
        }*/

        /*
            吸血鬼数字
         */
        /*long t1 = System.currentTimeMillis();
        System.out.println();
        for (int i = 1001; i < 10000; i++) {
            int ge = i % 10;
            int shi = (i / 10) % 10;
            int bai = (i / 100) % 10;
            int qian = i / 1000;
            int[] arr = {ge, shi, bai, qian};
            A:
            for (int j = 0; j < arr.length; j++) {

                for (int k = 0; k < arr.length; k++) {
                    if (k == j) {
                        continue;
                    }
                    for (int l = 0; l < arr.length; l++) {
                        if (l == j || l == k) {
                            continue;
                        }
                        for (int m = 0; m < arr.length; m++) {
                            if (m == j || m == k || m == l) {
                                continue;
                            }
                            if (i == (arr[j] * 10 + arr[k]) * (arr[l] * 10 + arr[m])) {
                                // 此行执行，表明是一个 吸血鬼数字。跳过
                                System.out.println("i = " + i);
                                break A;
                            }
                        }
                    }
                }
            }
        }

        System.out.println(System.currentTimeMillis() - t1);*/

        f(1, 'a');
        f('a', 'b');
    }

    static void f(Character... characters) {

    }
    static void f(float i, Character...characters) {

    }
}