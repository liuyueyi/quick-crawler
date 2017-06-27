package com.quick.hui.crawler.berkely.test;

import com.quick.hui.crawler.berkely.BerkelyHelper;
import com.sleepycat.je.OperationStatus;
import org.junit.Test;

/**
 * Created by yihui on 2017/6/21.
 */
public class BerkelyHelperTest {

    @Test
    public void testBerkelyHelper() {
        String key = "test" + System.currentTimeMillis();
        String value = "第一个测试";

        BerkelyHelper berkelyHelper = BerkelyHelper.newInstance("logs", "myDB");
        OperationStatus status = berkelyHelper.put(key, value);
        System.out.println("首次塞入返回: " + status);


        // 读取数据
        String result = berkelyHelper.get(key, String.class);
        System.out.println("the result: " + result);
        System.out.println("-------------------------------------");


        // 再次塞入
        value = "更新后的测试";
        status = berkelyHelper.put(key, value);
        System.out.println("再次塞入返回: " + status);


        result = berkelyHelper.get(key, String.class);
        System.out.println("the result: " + result);
        System.out.println("-------------------------------------");

        // 更新塞入的数据类型
        int value2 = 100;
        status = berkelyHelper.put(key, value2);
        System.out.println("更新塞入数据类型返回: " + status);


        int result2 = berkelyHelper.get(key, int.class);
        System.out.println("the result: " + result2);
        System.out.println("-------------------------------------");


        // 获取一个不存在的数据
        result = berkelyHelper.get("no exists!", String.class);
        System.out.println("the result: " + result);
        System.out.println("-------------------------------------");


        try {
            // 输入错误的返回类型
            result = berkelyHelper.get(key, String.class);
            System.out.println("the result: " + result);
            System.out.println("-------------------------------------");
        } catch (Exception e) {
            System.out.println("error! e" + e);
        }

        berkelyHelper.close();
    }

}
