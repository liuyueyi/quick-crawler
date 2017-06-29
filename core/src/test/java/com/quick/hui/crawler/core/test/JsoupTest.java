package com.quick.hui.crawler.core.test;

import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by yihui on 2017/6/29.
 */
public class JsoupTest {


    // 获取网页中的所有链接
    @Test
    public void testGetLink() throws IOException {
        String url = "http://chengyu.911cha.com/zishu_3_p1.html";

        Connection httpConnection = HttpConnection.connect(url)
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("connection", "Keep-Alive")
                .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

        Document doc = httpConnection.get();
        Elements links = doc.select("a[href]");

        String str = links.get(181).attr("abs:href");
        System.out.println(str);
    }
}
