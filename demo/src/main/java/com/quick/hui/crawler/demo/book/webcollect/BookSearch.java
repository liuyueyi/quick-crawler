package com.quick.hui.crawler.demo.book.webcollect;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import org.apache.commons.lang3.StringUtils;

/**
 * 爬取小说的爬虫
 *
 * Created by yihui on 2017/6/21.
 */
public class BookSearch extends BreadthCrawler {

    /**
     * 构造一个基于伯克利DB的爬虫
     * 伯克利DB文件夹为crawlPath，crawlPath中维护了历史URL等信息
     * 不同任务不要使用相同的crawlPath
     * 两个使用相同crawlPath的爬虫并行爬取会产生错误
     *
     * @param crawlPath 伯克利DB使用的文件夹
     * @param autoParse 是否根据设置的正则自动探测新URL
     */
    public BookSearch(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
    }



    public void visit(Page page, CrawlDatums next) {
        if (!page.matchUrl(regix)) {
            return;
        }

        page.charset("utf-8");

        String title = page.select("div[class=bookname]>h1").text();
        String content = page.select("div[id=content]").html();
        content = StringUtils.replace(content, "&nbsp;&nbsp;&nbsp;&nbsp;", "\n\t");

        System.out.println("title: " + title + " content: " + content);
    }


    static String regix = "http://www.xxbiquge.com/8_8947/[0-9]+.html";
    public static void main(String[] args) throws Exception {
        String seed = "http://www.xxbiquge.com/8_8947/";

        BookSearch bookSearch = new BookSearch("book", true);
        bookSearch.addSeed(seed);
        bookSearch.addRegex(regix);


        bookSearch.setThreads(1);
        bookSearch.setTopN(5000);
        bookSearch.start(2);
    }
}
