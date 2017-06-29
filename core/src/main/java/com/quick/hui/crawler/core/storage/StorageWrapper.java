package com.quick.hui.crawler.core.storage;

import com.quick.hui.crawler.core.entity.CrawlResult;
import com.quick.hui.crawler.core.storage.ram.RamStorage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yihui on 2017/6/29.
 */
public class StorageWrapper {

    private static StorageWrapper instance = new StorageWrapper();


    private IStorage storage;

    private Map<String, Lock> lockMap = new ConcurrentHashMap<>();

    public static StorageWrapper getInstance() {
        return instance;
    }


    private StorageWrapper() {
        storage = new RamStorage();
    }


    /**
     * 判断url是否被爬取过; 是则返回true； 否这返回false， 并上锁
     *
     * @param url
     * @return
     */
    public boolean ifUrlFetched(String url) {
        if(storage.contains(url)) {
            return true;
        }

        synchronized (this) {
            if (!lockMap.containsKey(url)) {
                // 不存在时，加一个锁
                lockMap.put(url, new ReentrantLock());
            }

            this.lock(url);


            if (storage.contains(url)) {
                return true;
            }
//            System.out.println(Thread.currentThread() + " lock url: " + url);
            return false;
        }
    }


    /**
     * 爬完之后， 新增一条爬取记录
     * @param url
     * @param crawlResult
     */
    public void addFetchRecord(String url, CrawlResult crawlResult) {
        try {
            if (crawlResult != null) {
                storage.putIfNotExist(url, crawlResult);
                this.unlock(url);
            }
        } catch (Exception e) {
            System.out.println(Thread.currentThread().getName() + " result: " + url + " e: " + e);
        }
    }



    private void lock(String url) {
        lockMap.get(url).lock();
    }


    private void unlock(String url) {
        lockMap.get(url).unlock();
    }
}
