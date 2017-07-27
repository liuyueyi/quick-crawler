package com.quick.hui.crawler.core.conf;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.quick.hui.crawler.core.conf.file.FileConfRead;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


/**
 * Created by yihui on 2017/7/8.
 */
@Slf4j
public class ConfigWrapper {
    private static final String CONFIG_PATH = "conf/crawler.properties";

    private EventBus eventBus;


    private IConfRead confRead;

    private Config config;

    private static volatile ConfigWrapper instance;

    private ConfigWrapper() {
        confRead = new FileConfRead();
        confRead.registerCheckTask(CONFIG_PATH);
        config = confRead.initConf(CONFIG_PATH);


        // 注册监听器
        eventBus = new EventBus();
        eventBus.register(this);
    }


    public static ConfigWrapper getInstance() {
        if (instance == null) {
            synchronized (ConfigWrapper.class) {
                if (instance == null) {
                    instance = new ConfigWrapper();
                }
            }
        }

        return instance;
    }


    @Subscribe
    public void init(UpdateConfEvent event) {
        config = confRead.initConf(event.conf);

        if (log.isDebugEnabled()) {
            log.debug("time:{} processor:{} update config! new config is: {}",
                    event.now, event.operator, config);
        }
    }


    public Config getConfig() {
        return config;
    }


    public void post(Object event) {
        eventBus.post(event);
    }

    @Getter
    @Setter
    public static class UpdateConfEvent {
        private long now = System.currentTimeMillis();

        private String operator = "System";

        private String conf = CONFIG_PATH;
    }
}
