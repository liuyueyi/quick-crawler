package com.quick.hui.crawler.core.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by yihui on 2017/7/12.
 */
@Slf4j
public class NumUtils {

    public static long str2long(String str, long defaultValue) {
        if (str == null) {
            return defaultValue;
        }


        try {
            return Long.parseLong(str.trim());
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("parse str{} to long error! return defaultValue: {}", str, defaultValue);
            }
            return defaultValue;
        }
    }



    public static int str2int(String str, int defaultValue) {
        if (str == null) {
            return defaultValue;
        }


        try {
            return Integer.parseInt(str.trim());
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("parse str{} to int error! return defaultValue: {}", str, defaultValue);
            }
            return defaultValue;
        }
    }

}
