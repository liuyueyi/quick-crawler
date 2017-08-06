package com.quick.hui.crawler.core.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yihui on 2017/8/6.
 */
@Slf4j
public class SimplePool<T extends IPoolCell> {

    private static SimplePool instance;


    public static void initInstance(SimplePool simplePool) {
        instance = simplePool;
    }

    public static  SimplePool getInstance() {
        return instance;
    }


    private int size;

    private BlockingQueue<T> queue;

    private String name;

    private ObjectFactory objectFactory;


    private AtomicInteger objCreateCount = new AtomicInteger(0);

    public SimplePool(int size, ObjectFactory objectFactory) {
        this(size, "default-pool", objectFactory);
    }

    public SimplePool(int size, String name, ObjectFactory objectFactory) {
        this.size = size;
        this.name = name;
        this.objectFactory = objectFactory;

        queue = new LinkedBlockingQueue<>(size);
    }


    /**
     * 获取对象，若队列中存在， 则直接返回；若不存在，则新创建一个返回
     * @return
     */
    public T get() {
        T obj = queue.poll();

        if (obj != null) {
            return obj;
        }

        obj = (T) objectFactory.create();
        int num = objCreateCount.addAndGet(1);


        if (log.isDebugEnabled()) {
            if (objCreateCount.get() >= size) {
                log.debug("objectPoll fulled! create a new object! total create num: {}, poll size: {}", num, queue.size());
            } else {
                // fixme 由于并发问题，这个队列的大小实际上与添加对象时的大小不一定相同
                log.debug("objectPoll not fulled!, init object, now poll size: {}", queue.size());
            }
        }

        return obj;
    }


    /**
     * 将对象扔回到队列中
     *
     * @param obj
     */
    public void release(T obj) {
        obj.clear();

        // 非阻塞方式的扔进队列
        boolean ans = queue.offer(obj);

        if (log.isDebugEnabled()) {
            log.debug("return obj to pool status: {}, now size: {}", ans, queue.size());
        }
    }


    public void clear() {
        queue.clear();
    }
}
