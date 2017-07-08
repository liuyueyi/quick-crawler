package com.quick.hui.crawler.core.fetcher;

import com.quick.hui.crawler.core.entity.CrawlMeta;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 待爬的网页队列
 * <p>
 * Created by yihui on 2017/7/6.
 */
@Slf4j
public class FetchQueue {

    public static FetchQueue DEFAULT_INSTANCE = newInstance("default");

    /**
     * 表示爬取队列的标识
     */
    private String tag;


    /**
     * 待爬取的网页队列
     */
    private Queue<CrawlMeta> toFetchQueue = new ArrayBlockingQueue<>(2000);


    /**
     * JobCount 映射表， key为 {@link JobCount#id}, value 为对应的JobCount
     */
    public Map<Integer, JobCount> jobCountMap = new ConcurrentHashMap<>();


    /**
     * 爬取是否完成的标识
     */
    public volatile boolean isOver = false;


    /**
     * 所有爬取过的url集合， 用于去重
     */
    public Set<String> urls = ConcurrentHashMap.newKeySet();


    private FetchQueue(String tag) {
        this.tag = tag;
    }


    public static FetchQueue newInstance(String tag) {
        return new FetchQueue(tag);
    }


    public int size() {
        return toFetchQueue.size();
    }


    /**
     * 当没有爬取过时， 才丢入队列； 主要是避免重复爬取的问题
     *
     * @param crawlMeta
     * @return true 表示丢入队列成功; false 表示已经爬取过了
     */
    public boolean addSeed(CrawlMeta crawlMeta) {
        if (urls.contains(crawlMeta.getUrl())) {
            return false;
        }

        synchronized (this) {
            if (urls.contains(crawlMeta.getUrl())) {
                return false;
            }


            urls.add(crawlMeta.getUrl());
            toFetchQueue.add(crawlMeta);
            return true;
        }
    }


    public CrawlMeta pollSeed() {
        return toFetchQueue.poll();
    }


    public void finishJob(CrawlMeta crawlMeta, int count, int maxDepth) {
        if (finishOneJob(crawlMeta, count, maxDepth)) {
            isOver = true;
        }
    }


    /**
     * 完成一个爬取任务
     *
     * @param crawlMeta 爬取的任务
     * @param count     爬取的网页上满足继续爬取的链接数
     * @return 如果所有的都爬取完了， 则返回true
     */
    private boolean finishOneJob(CrawlMeta crawlMeta, int count, int maxDepth) {
        JobCount jobCount = new JobCount(crawlMeta.getJobId(),
                crawlMeta.getParentJobId(),
                crawlMeta.getCurrentDepth(),
                count, 0);
        jobCountMap.put(crawlMeta.getJobId(), jobCount);


        if (crawlMeta.getCurrentDepth() == 0) { // 爬取种子页时，特判一下
            return count == 0; // 若没有子链接可以爬取， 则直接结束
        }


        if (count == 0 || crawlMeta.getCurrentDepth() == maxDepth) {
            // 当前的为最后一层的job时， 上一层计数+1
            return finishOneJob(jobCountMap.get(crawlMeta.getParentJobId()));
        }


        return false;
    }


    /**
     * 递归向上进行任务完成 +1
     *
     * @param jobCount
     * @return true 表示所有的任务都爬取完成
     */
    private boolean finishOneJob(JobCount jobCount) {
        if (jobCount.finishJob()) {
            if (jobCount.getCurrentDepth() == 0) {
                return true; //  结束
            }

            return finishOneJob(jobCountMap.get(jobCount.getParentId()));
        }

        return false;
    }
}
