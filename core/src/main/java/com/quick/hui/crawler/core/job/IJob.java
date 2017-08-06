package com.quick.hui.crawler.core.job;

import com.quick.hui.crawler.core.pool.IPoolCell;

/**
 * Created by yihui on 2017/6/27.
 */
public interface IJob extends Runnable, IPoolCell {

    /**
     * 在job执行之前回调的方法
     */
    void beforeRun();


    /**
     * 在job执行完毕之后回调的方法
     */
    void afterRun();
}
