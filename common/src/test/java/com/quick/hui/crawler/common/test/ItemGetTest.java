package com.quick.hui.crawler.common.test;

import com.quick.hui.crawler.common.http.HttpWrapper;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yihui on 2018/3/21.
 */
public class ItemGetTest {


    private void curl() {
        try {
            okhttp3.Response res = HttpWrapper.of("http://10.50.40.44/invoke?access_token=FD2DD0C6298A82C52A1E8B504BBB0848&itemId=1ltdroo&app_key=100602&method=xiaodian.item.get&format=json&timestamp=1521602646195&sign=B690B2CAE4A43A7FD1D496855051004A").get();
            if (res.isSuccessful()) {
                String ans = res.body().string();
                System.out.println(Thread.currentThread().getName() + " >>> ans : " + ans);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGet() throws InterruptedException {
        for(int i = 0; i < 20; i++) {
            new Thread(this::curl).start();
        }

        Thread.sleep(4000);
    }


    @Test
    public void testRef() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<String> list = new ArrayList<>();
        list.add("hello World!");

        ((ArrayList)list).add(10);
        System.out.println(list);


        Method method = list.getClass().getMethod("add", Object.class);
        method.invoke(list, 20);

        System.out.println(list);


    }
}
