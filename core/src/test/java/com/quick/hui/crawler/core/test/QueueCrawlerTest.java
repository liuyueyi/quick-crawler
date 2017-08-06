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
//            System.out.println(crawlResult.getHtmlDoc());
//            System.out.println(Thread.currentThread().getName() + "___" + crawlMeta.getCurrentDepth() + "___" + crawlResult.getUrl());
        }
    }


    @Test
    public void testCrawel() throws Exception {
        Fetcher fetcher = new Fetcher(2, QueueCrawlerJob.class);

        String url = "http://chengyu.911cha.com/zishu_4.html";
        CrawlMeta crawlMeta = new CrawlMeta();
        crawlMeta.setUrl(url);
        crawlMeta.addPositiveRegex("http://chengyu.911cha.com/zishu_4_p[0-9]+\\.html$");

        fetcher.addFeed(crawlMeta);


        fetcher.start();
    }
}
