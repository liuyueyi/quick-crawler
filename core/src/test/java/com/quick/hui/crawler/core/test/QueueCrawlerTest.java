package com.quick.hui.crawler.core.test;

import com.quick.hui.crawler.core.entity.CrawlMeta;
import com.quick.hui.crawler.core.entity.CrawlResult;
import com.quick.hui.crawler.core.fetcher.Fetcher;
import com.quick.hui.crawler.core.job.DefaultAbstractCrawlJob;
import org.junit.Test;

/**
 * 将待爬去任务放在队列中进行一次爬取
 * <p>
 * Created by yihui on 2017/7/6.
 */
public class QueueCrawlerTest {

    public static class QueueCrawlerJob extends DefaultAbstractCrawlJob {

        public void beforeRun() {
            // 设置返回的网页编码
            super.setResponseCode("gbk");
        }

        @Override
        protected void visit(CrawlResult crawlResult) {
            System.out.println(Thread.currentThread().getName() + " ___ " + crawlResult.getUrl());
        }
    }


    @Test
    public void testCrawel() throws Exception {
        Fetcher fetcher = new Fetcher(1);

        String url = "http://chengyu.t086.com/gushi/1.htm";
        CrawlMeta crawlMeta = new CrawlMeta();
        crawlMeta.setUrl(url);
        crawlMeta.addPositiveRegex("http://chengyu.t086.com/gushi/[0-9]+\\.htm$");

        fetcher.addFeed(crawlMeta);


        fetcher.start(QueueCrawlerJob.class);
    }
}
