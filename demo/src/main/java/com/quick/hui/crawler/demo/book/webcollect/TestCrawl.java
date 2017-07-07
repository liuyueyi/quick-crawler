package com.quick.hui.crawler.demo.book.webcollect;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

/**
 * Created by yihui on 2017/7/6.
 */
public class TestCrawl extends BreadthCrawler {
    /**
     * 构造一个基于伯克利DB的爬虫
     * 伯克利DB文件夹为crawlPath，crawlPath中维护了历史URL等信息
     * 不同任务不要使用相同的crawlPath
     * 两个使用相同crawlPath的爬虫并行爬取会产生错误
     *
     * @param crawlPath 伯克利DB使用的文件夹
     * @param autoParse 是否根据设置的正则自动探测新URL
     */
    public TestCrawl(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
    }

    @Override
    public void visit(Page page, CrawlDatums next) {
        System.out.println(page.getUrl());
    }


    public static void main(String[] args) throws Exception {
        String url = "http://chengyu.t086.com/gushi/1.htm";
        String regix = "http://chengyu.t086.com/gushi/[0-9]+\\.html$";


        BookSearch bookSearch = new BookSearch("book", true);
        bookSearch.addSeed(url);
        bookSearch.addRegex(regix);


        bookSearch.setThreads(1);
        bookSearch.setTopN(5000);
        bookSearch.start(3);
    }
}
