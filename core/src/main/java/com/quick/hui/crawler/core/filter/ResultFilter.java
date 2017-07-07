package com.quick.hui.crawler.core.filter;

import com.quick.hui.crawler.core.entity.CrawlMeta;
import com.quick.hui.crawler.core.entity.CrawlResult;
import com.quick.hui.crawler.core.fetcher.FetchQueue;
import com.quick.hui.crawler.core.fetcher.JobCount;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yihui on 2017/7/6.
 */
public class ResultFilter {


    public static void filter(CrawlMeta crawlMeta,
                              CrawlResult crawlResult,
                              FetchQueue fetchQueue,
                              int maxDepth) {
        int count = 0;
        try {
            // 解析返回的网页中的链接，将满足条件的扔到爬取队列中
            int currentDepth = crawlMeta.getCurrentDepth();
            if (currentDepth >= maxDepth) {
                return;
            }


            // 当前的网址中可以继续爬的链接数

            Elements elements = crawlResult.getHtmlDoc().select("a[href]");
            String src;
            for (Element element : elements) {
                // 确保将相对地址转为绝对地址
                src = element.attr("abs:href");
                if (!matchRegex(crawlMeta, src)) {
                    continue;
                }

                CrawlMeta meta = new CrawlMeta(
                        JobCount.genId(),
                        crawlMeta.getJobId(),
                        currentDepth + 1,
                        src,
                        crawlMeta.getSelectorRules(),
                        crawlMeta.getPositiveRegex(),
                        crawlMeta.getNegativeRegex());
                if (fetchQueue.addSeed(meta)) {
                    count++;
                }
            }

        } finally { // 上一层爬完计数+1
            fetchQueue.finishJob(crawlMeta, count, maxDepth);
        }

    }


    private static boolean matchRegex(CrawlMeta crawlMeta, String url) {
        Matcher matcher;
        for (Pattern pattern : crawlMeta.getPositiveRegex()) {
            matcher = pattern.matcher(url);
            if (matcher.find()) {
                return true;
            }
        }


        for (Pattern pattern : crawlMeta.getNegativeRegex()) {
            matcher = pattern.matcher(url);
            if (matcher.find()) {
                return false;
            }
        }


        return crawlMeta.getPositiveRegex().size() == 0;
    }

}
