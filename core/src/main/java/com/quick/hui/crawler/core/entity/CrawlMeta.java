package com.quick.hui.crawler.core.entity;


import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by yihui on 2017/6/27.
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CrawlMeta {
    /**
     * 当前爬取的深度
     */
    @Getter
    @Setter
    private int currentDepth = 0;

    /**
     * 待爬去的网址
     */
    @Getter
    @Setter
    private String url;


    /**
     * 获取指定内容的规则, 因为一个网页中，你可能获取多个不同的内容， 所以放在集合中
     */
    @Setter
    @Getter
    private Set<String> selectorRules = new HashSet<>();


    /**
     * 正向的过滤规则
     */
    @Setter
    @Getter
    private Set<Pattern> positiveRegex = new HashSet<>();


    /**
     * 逆向的过滤规则
     */
    @Setter
    @Getter
    private Set<Pattern> negativeRegex = new HashSet<>();


    public Set<String> addSelectorRule(String rule) {
        this.selectorRules.add(rule);
        return selectorRules;
    }


    public Set<Pattern> addPositiveRegex(String regex) {
        this.positiveRegex.add(Pattern.compile(regex));
        return this.positiveRegex;
    }


    public Set<Pattern> addNegativeRegex(String regex) {
        this.negativeRegex.add(Pattern.compile(regex));
        return this.negativeRegex;
    }
}
