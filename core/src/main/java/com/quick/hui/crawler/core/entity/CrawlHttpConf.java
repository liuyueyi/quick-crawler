package com.quick.hui.crawler.core.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * http的相关配置
 *
 * 1. 请求参数头
 * 2. 返回的各项设置
 *
 * Created by yihui on 2017/6/27.
 */
@ToString
public class CrawlHttpConf {

    private static Map<String, String> DEFAULT_HEADERS;

    static  {
        DEFAULT_HEADERS = new HashMap<>();
        DEFAULT_HEADERS.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        DEFAULT_HEADERS.put("connection", "Keep-Alive");
        DEFAULT_HEADERS.put("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
    }


    public enum HttpMethod {
        GET,
        POST,
        OPTIONS,
        PUT;
    }


    @Getter
    @Setter
    private HttpMethod method = HttpMethod.GET;


    /**
     * 请求头
     */
    @Setter
    private Map<String, String> requestHeaders;


    /**
     * 请求参数
     */
    @Setter
    private Map<String, Object> requestParams;


    /**
     * 返回的编码格式
     */
    @Getter
    @Setter
    private String code = "UTF-8";


    public Map<String, String> getRequestHeaders() {
        return requestHeaders == null ? DEFAULT_HEADERS : requestHeaders;
    }

    public Map<String, Object> getRequestParams() {
        return requestParams == null ? Collections.emptyMap() : requestParams;
    }
}
