package com.quick.hui.crawler.core.fetcher;

import com.quick.hui.crawler.core.entity.CrawlMeta;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 待爬的网页队列
 * <p>
 * Created by yihui on 2017/7/6.
 */
public class FetchQueue {

    public static FetchQueue DEFAULT_INSTANCE = newInstance("default");

    /**
     * 表示爬取队列的标识
     */
    private String tag;


    /**
     * 待爬取的网页队列
     */
    private Queue<CrawlMeta> toFetchQueue = new ArrayBlockingQueue<>(200);

//
//    /**
//     * 爬取的结果队列，用于分析内部链接，并产出下一个可爬取的链接塞入 {@link FetchQueue#toFetchQueue}
//     */
//    private Queue<CrawlResult> fetchResultQueue = new ArrayBlockingQueue<>(200);


    /**
     * 所有爬取过的url集合， 用于去重
     */
    private Set<String> urls = ConcurrentHashMap.newKeySet();


    private FetchQueue(String tag) {
        this.tag = tag;
    }


    public static FetchQueue newInstance(String tag) {
        return new FetchQueue(tag);
    }


    /**
     * 当没有爬取过时， 才丢入队列； 主要是避免重复爬取的问题
     *
     * @param crawlMeta
     */
    public void addSeed(CrawlMeta crawlMeta) {
        if (urls.contains(crawlMeta.getUrl())) {
            return;
        }

        synchronized (this) {
            if (urls.contains(crawlMeta.getUrl())) {
                return;
            }


            urls.add(crawlMeta.getUrl());
            toFetchQueue.add(crawlMeta);
        }
    }


    public CrawlMeta pollSeed() {
        return toFetchQueue.poll();
    }

//
//    public void addResult(CrawlResult crawlResult)  {
//        this.fetchResultQueue.add(crawlResult);
//    }
//
//
//    public CrawlResult pollResult() {
//        return fetchResultQueue.poll();
//    }
}
