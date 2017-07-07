package com.quick.hui.crawler.core.fetcher;

import com.quick.hui.crawler.core.entity.CrawlMeta;
import com.quick.hui.crawler.core.job.DefaultAbstractCrawlJob;
import lombok.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yihui on 2017/6/27.
 */
public class Fetcher {

    private int maxDepth;

    private FetchQueue fetchQueue;


    private Executor executor;


    @Setter
    private ThreadConf threadConf;


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
        threadConf = ThreadConf.DEFAULT_CONF;
        initExecutor();
    }


    /**
     * 初始化线程池
     */
    private void initExecutor() {
        executor = new ThreadPoolExecutor(threadConf.getCoreNum(),
                threadConf.getMaxNum(),
                threadConf.getAliveTime(),
                threadConf.getTimeUnit(),
                new LinkedBlockingQueue<>(threadConf.getQueueSize()),
                new CustomThreadFactory(threadConf.getThreadName()),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }


    public <T extends DefaultAbstractCrawlJob> void start(Class<T> clz) throws Exception {
        CrawlMeta crawlMeta;

        while (!fetchQueue.isOver) {
            crawlMeta = fetchQueue.pollSeed();
            if (crawlMeta == null) {
                Thread.sleep(200);
                continue;
            }


            DefaultAbstractCrawlJob job = clz.newInstance();
            job.setDepth(this.maxDepth);
            job.setCrawlMeta(crawlMeta);
            job.setFetchQueue(fetchQueue);

            executor.execute(job);
        }
    }


    private static class CustomThreadFactory implements ThreadFactory {

        private String name;

        private AtomicInteger count = new AtomicInteger(0);

        public CustomThreadFactory(String name) {
            this.name = name;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, name + "-" + count.addAndGet(1));
        }
    }


    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class ThreadConf {
        private int coreNum = 6;
        private int maxNum = 10;
        private int queueSize = 10;
        private int aliveTime = 1;
        private TimeUnit timeUnit = TimeUnit.MINUTES;
        private String threadName = "crawl-fetch";


        public final static ThreadConf DEFAULT_CONF = new ThreadConf();
    }

}
