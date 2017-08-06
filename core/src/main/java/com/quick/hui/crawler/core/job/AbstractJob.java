package com.quick.hui.crawler.core.job;

import com.quick.hui.crawler.core.entity.CrawlResult;
import com.quick.hui.crawler.core.pool.SimplePool;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by yihui on 2017/6/27.
 */
@Slf4j
public abstract class AbstractJob implements IJob {

    public void beforeRun() {
    }

    public void afterRun() {
    }


    /**
     * 解析完网页后的回调方法
     *
     * @param crawlResult
     */
    protected abstract void visit(CrawlResult crawlResult);


    @Override
    public void run() {
        this.beforeRun();


        try {
            this.doFetchPage();
        } catch (Exception e) {
            log.error("fetch page error! e: {}", e);
        }


        this.afterRun();

//
//        // 将job扔回队列
        SimplePool.getInstance().release(this);
    }


    /**
     * 具体的抓去网页的方法， 需要子类来补全实现逻辑
     *
     * @throws Exception
     */
    abstract void doFetchPage() throws Exception;
}
