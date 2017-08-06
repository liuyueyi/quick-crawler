package com.quick.hui.crawler.core.conf.file;

import com.quick.hui.crawler.common.FileReadUtil;
import com.quick.hui.crawler.core.conf.Config;
import com.quick.hui.crawler.core.conf.ConfigWrapper;
import com.quick.hui.crawler.core.conf.IConfRead;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 从配置文件中获取配置信息
 * <p>
 * Created by yihui on 2017/7/8.
 */
@Slf4j
public class FileConfRead implements IConfRead {


    public Config initConf(String path) {
        try {
            Properties properties = read(path);

            Config config = new Config();
            config.setSleep(properties.getProperty("sleep"), 0);
            config.setEmptyQueueWaitTime(properties.getProperty("emptyQueueWaitTime"), 200);
            config.setFetchQueueSize(properties.getProperty("fetchQueueSize"), 100);

            return config;
        } catch (Exception e) {
            log.error("init config from file: {} error! e: {}", path, e);
            return new Config();
        }
    }


    private Properties read(String fileName) throws IOException {
        try (InputStream inputStream = FileReadUtil.getStreamByFileName(fileName)) {
            Properties pro = new Properties();
            pro.load(inputStream);
            return pro;
        }
    }


    private File file;
    private long lastTime;

    public void registerCheckTask(final String path) {
        try {
            file = FileReadUtil.getFile(path);
            lastTime = file.lastModified();


            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                        if (file.lastModified() > lastTime) {
                            lastTime = file.lastModified();
                            ConfigWrapper.getInstance().post(new ConfigWrapper.UpdateConfEvent());
                        }
                    },
                    1,
                    1,
                    TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
