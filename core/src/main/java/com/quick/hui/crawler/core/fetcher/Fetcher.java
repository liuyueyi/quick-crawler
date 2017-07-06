package com.quick.hui.crawler.core.fetcher;

import com.quick.hui.crawler.core.entity.CrawlMeta;
import com.quick.hui.crawler.core.job.DefaultAbstractCrawlJob;

/**
 * Created by yihui on 2017/6/27.
 */
public class Fetcher {

    private int maxDepth;

    private FetchQueue fetchQueue;


    public FetchQueue addFeed(CrawlMeta feed) {
        fetchQueue.addSeed(feed);
        return fetchQueue;
    }


    public Fetcher() {
        this(0);
    }


    public Fetcher(int maxDepth) {
        this.maxDepth = maxDepth;
        fetchQueue = FetchQueue.DEFAULT_INSTANCE;
    }


    public <T extends DefaultAbstractCrawlJob> void start(Class<T> clz) throws Exception {
        CrawlMeta crawlMeta;
        int i = 0;
        while (true) {
            crawlMeta = fetchQueue.pollSeed();
            if (crawlMeta == null) {
                Thread.sleep(200);
                if (++i > 300) { // 连续一分钟内没有数据时，退出
                    break;
                }

                continue;
            }

            i = 0;

            DefaultAbstractCrawlJob job = clz.newInstance();
            job.setDepth(this.maxDepth);
            job.setCrawlMeta(crawlMeta);
            job.setFetchQueue(fetchQueue);

            new Thread(job, "crawl-thread-" + System.currentTimeMillis()).start();
        }
    }

}
