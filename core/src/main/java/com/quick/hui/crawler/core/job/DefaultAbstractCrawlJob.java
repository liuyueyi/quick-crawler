package com.quick.hui.crawler.core.job;

import com.quick.hui.crawler.core.entity.CrawlHttpConf;
import com.quick.hui.crawler.core.entity.CrawlMeta;
import com.quick.hui.crawler.core.entity.CrawlResult;
import com.quick.hui.crawler.core.storage.StorageWrapper;
import com.quick.hui.crawler.core.utils.HttpUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yihui on 2017/6/29.
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class DefaultAbstractCrawlJob extends AbstractJob {
    /**
     * 配置项信息
     */
    private CrawlMeta crawlMeta;


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
    public void doFetchPage() throws Exception {
        doFetchNextPage(0, this.crawlMeta.getUrl());
    }


    // fixme 非线程安全
    private void doFetchNextPage(int currentDepth, String url) throws Exception {
        CrawlResult result = null;
        try {
            // 判断是否爬过；未爬取，则上锁并继续爬取网页
            if (StorageWrapper.getInstance().ifUrlFetched(url)) {
                return;
            }

            CrawlMeta subMeta = new CrawlMeta(url, this.crawlMeta.getSelectorRules(), this.crawlMeta.getPositiveRegex(), this.crawlMeta.getNegativeRegex());
            HttpResponse response = HttpUtils.request(subMeta, httpConf);
            String res = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) { // 请求成功
                result = new CrawlResult();
                result.setStatus(response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
                result.setUrl(crawlMeta.getUrl());
                this.visit(result);
                return;
            }


            // 网页解析
            result = doParse(res, subMeta);
        } finally {
            // 添加一条记录， 并释放锁
            StorageWrapper.getInstance().addFetchRecord(url, result);
        }

        // 回调用户的网页内容解析方法
        this.visit(result);


        // 超过最大深度， 不继续爬
        if (currentDepth > depth) {
            return;
        }


        Elements elements = result.getHtmlDoc().select("a[href]");
        String src;
        for(Element element: elements) {
            // 确保将相对地址转为绝对地址
            src = element.attr("abs:href");
            if (matchRegex(src)) {
                doFetchNextPage(currentDepth + 1, src);
            }
        }
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


    private boolean matchRegex(String url) {
        Matcher matcher;
        for(Pattern pattern: crawlMeta.getPositiveRegex()) {
            matcher = pattern.matcher(url);
            if (matcher.find()) {
                return true;
            }
        }


        for(Pattern pattern: crawlMeta.getNegativeRegex()) {
            matcher = pattern.matcher(url);
            if(matcher.find()) {
                return false;
            }
        }


        return crawlMeta.getPositiveRegex().size() == 0;
    }
}
