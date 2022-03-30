package com.mianshi.thread.callable;

import java.util.concurrent.Callable;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-06-07 21:31
 * @Version 1.0
 */
public class TaskWithResult implements Callable<String> {

    private int id;

    public TaskWithResult(int id) {
        this.id = id;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    public String call() throws Exception {
        return "resul of TaskWithResult " + id;
    }
}
