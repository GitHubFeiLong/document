package com.mianshi.basic;

/**
 * 类描述：
 * 参数方法参数传递
 * @Author e-Feilong.Chen
 * @Date 2022/4/1 11:07
 */
public class MethodParameter {
    //~fields
    //==================================================================================================================

    //~construct methods
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String[] args) {
        Integer integer = new Integer(100000);
        changeVariable(integer);

        System.out.println("integer = " + integer); // 100000

        System.out.println("returnResult() = " + returnResult());
    }


    public static void changeVariable(Integer integer) {
        integer = 100;
    }

    public static Integer returnResult() {
        Integer i = 10;
        try {
            // 异常
            return i;
        } catch (Exception e){
            return i;
        } finally {
            i += 10;
            return i;
        }
    }

}
