package com.quick.hui.crawler.core.job;

import com.quick.hui.crawler.core.entity.CrawlMeta;
import com.quick.hui.crawler.core.entity.CrawlResult;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 最简单的一个爬虫任务
 * <p>
 * Created by yihui on 2017/6/27.
 */
@Getter
@Setter
public class SimpleCrawlJob extends AbstractJob {

    /**
     * 配置项信息
     */
    private CrawlMeta crawlMeta;


    /**
     * 存储爬取的结果
     */
    private CrawlResult crawlResult;


    /**
     * 执行抓取网页
     */
    public void doFetchPage() throws Exception {

        URL url = new URL(crawlMeta.getUrl());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader in = null;

        StringBuilder result = new StringBuilder();

        try {
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();


            Map<String, List<String>> map = connection.getHeaderFields();
            //遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } finally {        // 使用finally块来关闭输入流
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }


        doParse(result.toString());
    }



    private void doParse(String html) {
        Document doc = Jsoup.parse(html);

        Map<String, List<String>> map = new HashMap<>(crawlMeta.getSelectorRules().size());
        for (String rule: crawlMeta.getSelectorRules()) {
            List<String> list = new ArrayList<>();
            for (Element element: doc.select(rule)) {
                list.add(element.text());
            }

            map.put(rule, list);
        }


        this.crawlResult = new CrawlResult();
        this.crawlResult.setHtmlDoc(doc);
        this.crawlResult.setUrl(crawlMeta.getUrl());
        this.crawlResult.setResult(map);
    }
}
