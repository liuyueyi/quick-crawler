package com.quick.hui.crawler.core.conf;

import com.quick.hui.crawler.core.utils.NumUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by yihui on 2017/7/8.
 */
@Getter
@Setter
@ToString
public class Config {

    /**
     * 爬取任务的间隔时间
     */
    private long sleep;


    /**
     * 从队列中获取任务，返回空时，等待时间之后再进行重试
     */
    private long emptyQueueWaitTime;


    /**
     * 对象池大小
     */
    private int fetchQueueSize;


    public void setSleep(String str, long sleep) {
        this.sleep = NumUtils.str2long(str, sleep);
    }

    public void setEmptyQueueWaitTime(String str, long emptyQueueWaitTime) {
        this.emptyQueueWaitTime = NumUtils.str2long(str, emptyQueueWaitTime);
    }


    public void setFetchQueueSize(String str, int size) {
        this.fetchQueueSize = NumUtils.str2int(str, size);
    }
}
