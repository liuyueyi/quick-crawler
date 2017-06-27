package com.quick.hui.crawler.core.job;

import com.quick.hui.crawler.core.entity.CrawlHttpConf;
import com.quick.hui.crawler.core.entity.CrawlMeta;
import com.quick.hui.crawler.core.entity.CrawlResult;
import com.quick.hui.crawler.core.utils.HttpUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
     * http配置信息
     */
    private CrawlHttpConf httpConf = new CrawlHttpConf();


    /**
     * 存储爬取的结果
     */
    private CrawlResult crawlResult = new CrawlResult();


    /**
     * 执行抓取网页
     */
    public void doFetchPage() throws Exception {
        HttpResponse response = HttpUtils.request(crawlMeta, httpConf);
        String res = EntityUtils.toString(response.getEntity());
        if (response.getStatusLine().getStatusCode() == 200) { // 请求成功
            doParse(res);
        } else {
            this.crawlResult = new CrawlResult();
            this.crawlResult.setStatus(response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
            this.crawlResult.setUrl(crawlMeta.getUrl());
        }
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
        this.crawlResult.setStatus(CrawlResult.SUCCESS);
    }
}
