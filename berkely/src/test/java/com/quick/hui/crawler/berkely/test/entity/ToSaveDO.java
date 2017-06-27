package com.quick.hui.crawler.berkely.test.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by yihui on 2017/6/21.
 */
@Getter
@Setter
@ToString
public class ToSaveDO implements Serializable {
    private static final long serialVersionUID = 968345657199606335L;

    private List<String> names;

    private int id;

    private String title;

    private Boolean unique;

    private Map<String, Object> map;
}
