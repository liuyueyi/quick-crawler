package com.quick.hui.crawler.berkely;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yihui on 2017/6/21.
 */
public class BasicTypeHelper {

    public static Set<Class> basicType = new HashSet<>();

    static {
        basicType.addAll(Arrays.asList(String.class,
                Integer.class, int.class, Long.class, long.class,
                Double.class, double.class, Float.class, float.class,
                Character.class, char.class, Byte.class, byte.class,
                Boolean.class, boolean.class, Short.class, short.class));
    }


    public static boolean isBasicType(Class clz) {
        return basicType.contains(clz);
    }
}
