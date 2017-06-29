package com.quick.hui.crawler.core.test;

import com.quick.hui.crawler.core.entity.CrawlMeta;
import com.quick.hui.crawler.core.entity.CrawlResult;
import com.quick.hui.crawler.core.job.DefaultAbstractCrawlJob;
import com.quick.hui.crawler.core.job.SimpleCrawlJob;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
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


    /**
     * 深度爬
     * @throws InterruptedException
     */
    @Test
    public void testDepthFetch() throws InterruptedException {
        String url = "http://chengyu.911cha.com/zishu_3_p1.html";
        CrawlMeta crawlMeta = new CrawlMeta();
        crawlMeta.setUrl(url);
        crawlMeta.addPositiveRegex("http://chengyu.911cha.com/zishu_3_p([0-9]+).html");


        SimpleCrawlJob job = new SimpleCrawlJob(1);
        job.setCrawlMeta(crawlMeta);
        Thread thread = new Thread(job, "crawlerDepth-test");
        thread.start();


        thread.join();
        List<CrawlResult> result = job.getCrawlResults();
        System.out.println(result);
    }



    @Test
    public void testSelfCwralFetch() throws InterruptedException {
        String url = "http://chengyu.t086.com/gushi/1.htm";
        CrawlMeta crawlMeta = new CrawlMeta();
        crawlMeta.setUrl(url);
        crawlMeta.addPositiveRegex("http://chengyu.t086.com/gushi/[0-9]+\\.htm$");


        DefaultAbstractCrawlJob job = new DefaultAbstractCrawlJob(1) {
            @Override
            protected void visit(CrawlResult crawlResult) {
                System.out.println("job1 >>> " + crawlResult.getUrl());
            }
        };
        job.setCrawlMeta(crawlMeta);



        String url2 = "http://chengyu.t086.com/gushi/2.htm";
        CrawlMeta crawlMeta2 = new CrawlMeta();
        crawlMeta2.setUrl(url2);
        crawlMeta2.addPositiveRegex("http://chengyu.t086.com/gushi/[0-9]+\\.htm$");
        DefaultAbstractCrawlJob job2 = new DefaultAbstractCrawlJob(1) {
            @Override
            protected void visit(CrawlResult crawlResult) {
                System.out.println("job2 >>> " + crawlResult.getUrl());
            }
        };
        job2.setCrawlMeta(crawlMeta2);



        Thread thread = new Thread(job, "crawlerDepth-test");
        Thread thread2 = new Thread(job2, "crawlerDepth-test2");
        thread.start();
        thread2.start();


        thread.join();
        thread2.join();
    }


}
