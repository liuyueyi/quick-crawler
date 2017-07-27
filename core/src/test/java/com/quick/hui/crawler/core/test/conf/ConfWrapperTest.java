package com.quick.hui.crawler.core.test.conf;

import com.quick.hui.crawler.core.conf.Config;
import com.quick.hui.crawler.core.conf.ConfigWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Created by yihui on 2017/7/8.
 */
@Slf4j
public class ConfWrapperTest {


    @Test
    public void testConfLoad() {
        Config config = ConfigWrapper.getInstance().getConfig();
        log.info("the config : {}", config);
    }

}
