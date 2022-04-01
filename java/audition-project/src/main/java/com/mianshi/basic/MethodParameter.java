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
        System.out.println("integer = " + integer);

        System.out.println("returnResult() = " + returnResult());
    }


    public static void changeVariable(Integer integer) {
        integer = 100;
    }

    public static boolean returnResult() {
        try {
            return true;
        } catch (Exception e){

        } finally {
            return false;
        }
    }

}
