package com.quick.hui.crawler.core.test;

import com.quick.hui.crawler.core.entity.CrawlMeta;
import com.quick.hui.crawler.core.entity.CrawlResult;
import com.quick.hui.crawler.core.job.SimpleCrawlJob;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by yihui on 2017/6/27.
 */
public class BaseCrawlerTest {


    /**
     * 测试我们写的最简单的一个爬虫,
     *
     * 目标是爬取一篇博客
     */
    @Test
    public void testFetch() throws InterruptedException {
        String url = "https://my.oschina.net/u/566591/blog/1031575";
        Set<String> selectRule = new HashSet<>();
        selectRule.add("div[class=title]"); // 博客标题
        selectRule.add("div[class=blog-body]"); // 博客正文

        CrawlMeta crawlMeta = new CrawlMeta();
        crawlMeta.setUrl(url);
        crawlMeta.setSelectorRules(selectRule);


        SimpleCrawlJob job = new SimpleCrawlJob();
        job.setCrawlMeta(crawlMeta);
        Thread thread = new Thread(job, "crawler-test");
        thread.start();

        thread.join();


        CrawlResult result = job.getCrawlResult();
        System.out.println(result);
    }

}
