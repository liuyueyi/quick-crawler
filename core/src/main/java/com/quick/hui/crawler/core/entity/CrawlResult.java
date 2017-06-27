package com.quick.hui.crawler.core.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Map;

/**
 * Created by yihui on 2017/6/27.
 */
@Getter
@Setter
@ToString
public class CrawlResult {

    /**
     * 爬取的网址
     */
    private String url;


    /**
     * 爬取的网址对应的 DOC 结构
     */
    private Document htmlDoc;


    /**
     * 选择的结果，key为选择规则，value为根据规则匹配的结果
     */
    private Map<String, List<String>> result;

}
