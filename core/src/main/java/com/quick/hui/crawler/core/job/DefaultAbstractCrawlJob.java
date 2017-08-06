package com.quick.hui.crawler.core.job;

import com.quick.hui.crawler.core.entity.CrawlHttpConf;
import com.quick.hui.crawler.core.entity.CrawlMeta;
import com.quick.hui.crawler.core.entity.CrawlResult;
import com.quick.hui.crawler.core.fetcher.FetchQueue;
import com.quick.hui.crawler.core.filter.ResultFilter;
import com.quick.hui.crawler.core.utils.HttpUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yihui on 2017/6/29.
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
public abstract class DefaultAbstractCrawlJob extends AbstractJob {

    /**
     * 待爬取的任务队列
     */
    private FetchQueue fetchQueue;


    /**
     * 解析的结果
     */
    private CrawlResult crawlResult;

    /**
     * 配置项信息
     */
    protected CrawlMeta crawlMeta;


    /**
     * http配置信息
     */
    private CrawlHttpConf httpConf = new CrawlHttpConf();


    /**
     * 爬网页的深度, 默认为0， 即只爬取当前网页
     */
    protected int depth = 0;


    public DefaultAbstractCrawlJob(int depth) {
        this.depth = depth;
    }


    /**
     * 执行抓取网页
     */
    void doFetchPage() throws Exception {
        long start = System.currentTimeMillis();
        log.info("start crawl url: {}", crawlMeta.getUrl());

        HttpResponse response = HttpUtils.request(this.crawlMeta, httpConf);
        String res = EntityUtils.toString(response.getEntity(), httpConf.getCode());
        long end = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("crawl url:{} response code: {} cost time: {} ms\n",
                    this.crawlMeta.getUrl(),
                    response.getStatusLine().getStatusCode(),
                    end - start);
        }


        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) { // 请求成功
            this.crawlResult = new CrawlResult();
            this.crawlResult.setStatus(response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
            this.crawlResult.setUrl(crawlMeta.getUrl());
            this.visit(this.crawlResult);
            return;
        }


        // 网页解析
        this.crawlResult = doParse(res, this.crawlMeta);

        // 回调用户的网页内容解析方法
        this.visit(this.crawlResult);

        // 结果过滤
        ResultFilter.filter(crawlMeta, crawlResult, fetchQueue, depth);


        log.info("end crawl url: {}, cost: {}ms", crawlMeta.getUrl(), System.currentTimeMillis() - start);
    }


    private CrawlResult doParse(String html, CrawlMeta meta) {
        Document doc = Jsoup.parse(html, meta.getUrl());

        Map<String, List<String>> map = new HashMap<>(meta.getSelectorRules().size());
        for (String rule : crawlMeta.getSelectorRules()) {
            List<String> list = new ArrayList<>();
            for (Element element : doc.select(rule)) {
                list.add(element.text());
            }

            map.put(rule, list);
        }


        CrawlResult result = new CrawlResult();
        result.setHtmlDoc(doc);
        result.setUrl(meta.getUrl());
        result.setResult(map);
        result.setStatus(CrawlResult.SUCCESS);
        return result;
    }


    protected void setResponseCode(String code) {
        httpConf.setCode(code);
    }


    @Override
    public void clear() {
        this.depth = 0;
        this.crawlMeta = null;
        this.fetchQueue = null;
        this.crawlResult = null;
    }
}
