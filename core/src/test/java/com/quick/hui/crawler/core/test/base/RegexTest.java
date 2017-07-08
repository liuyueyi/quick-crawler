package com.quick.hui.crawler.core.test.base;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yihui on 2017/6/29.
 */
@Slf4j
public class RegexTest {

    @Test
    public void testRegex() {

        String regex = "https://my.oschina.net/u/566591/blog(.*)";
        String[] urls = new String[] {
                "https://my.oschina.net/u/566591/blog?search=java",
                "https://my.oschina.net/u/566592/blog?search=java",
                "https://my.oschina.net/u/566591/blog/12342"
        };


        Pattern patter = Pattern.compile(regex);
        for(String url: urls) {
            Matcher matcher = patter.matcher(url);
//            System.out.println(url + " " + matcher.find());
            log.debug("url:{} match result:{}", url, matcher.find());
        }
    }

}
