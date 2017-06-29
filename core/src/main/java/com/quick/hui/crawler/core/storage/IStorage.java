package com.quick.hui.crawler.core.storage;

import com.quick.hui.crawler.core.entity.CrawlResult;

/**
 * Created by yihui on 2017/6/29.
 */
public interface IStorage {

    /**
     * 若爬取的URL不在storage中， 则写入； 否则忽略
     *
     * @param url 爬取的网址
     * @return true 表示写入成功， 即之前没有这条记录； false 则表示之前已经有记录了
     */
    boolean putIfNotExist(String url, CrawlResult result);


    /**
     * 判断是否存在
     * @param url
     * @return
     */
    boolean contains(String url);

}
