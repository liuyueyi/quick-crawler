package com.quick.hui.crawler.core.conf;

/**
 * 读取配置文件的接口
 * <p>
 * Created by yihui on 2017/7/8.
 */
public interface IConfRead {

    /**
     * 初始化配置信息
     *
     * @param var
     * @return
     */
    Config initConf(String var);


    /**
     * 注册配置信息更新检测任务
     *
     * @param path
     */
    void registerCheckTask(final String path);
}
