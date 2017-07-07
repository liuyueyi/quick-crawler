package com.quick.hui.crawler.core.fetcher;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yihui on 2017/7/6.
 */
@Getter
public class JobCount {

    /**
     * 种子对应的id
     */
    public static int SEED_ID = 1;

    public static AtomicInteger idGen = new AtomicInteger(0);


    public static int genId() {
        return idGen.addAndGet(1);
    }


    /**
     * 该Job对应的唯一ID
     */
    private int id;


    /**
     * 该job对应父job的id
     */
    private int parentId;


    /**
     * 当前的层数
     */
    private int currentDepth;


    /**
     * 该job对应的网页中，子Job的数量
     */
    private AtomicInteger jobCount = new AtomicInteger(0);


    /**
     * 该Job对应的网页中， 子Job完成的数量
     */
    private AtomicInteger finishCount = new AtomicInteger(0);


    public boolean fetchOver() {
        return jobCount.get() == finishCount.get();
    }


    /**
     * 爬取完成一个子任务
     */
    public synchronized boolean finishJob() {
        finishCount.addAndGet(1);
        return fetchOver();
    }


    public JobCount(int id, int parentId, int currentDepth, int jobCount, int finishCount) {
        this.id = id;
        this.parentId = parentId;
        this.currentDepth = currentDepth;
        this.jobCount.set(jobCount);
        this.finishCount.set(finishCount);
    }
}
