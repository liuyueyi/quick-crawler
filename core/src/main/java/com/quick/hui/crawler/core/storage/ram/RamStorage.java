package com.quick.hui.crawler.core.storage.ram;

import com.quick.hui.crawler.core.entity.CrawlResult;
import com.quick.hui.crawler.core.storage.IStorage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yihui on 2017/6/29.
 */
public class RamStorage implements IStorage {

    private Map<String, CrawlResult> map = new ConcurrentHashMap<>();


    @Override
    public boolean putIfNotExist(String url, CrawlResult result) {
        if(map.containsKey(url)) {
            return false;
        }

        map.put(url, result);
        return true;
    }

    @Override
    public boolean contains(String url) {
        return map.containsKey(url);
    }
}
