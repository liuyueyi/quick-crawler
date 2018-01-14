package com.quick.hui.crawler.common.test;

import com.quick.hui.crawler.common.http.HttpWrapper;
import okhttp3.Response;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by yihui on 2018/1/12.
 */
public class HttpWrapperTest {

    @Test
    public void testGet() {
        String url = "https://zbang.online/wx/list";
        try {
            okhttp3.Response res = HttpWrapper.of(url).get();
            if (res.isSuccessful()) {
                String ans = res.body().string();
                System.out.println("ans : " + ans);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testPost() {
        String url = "https://zbang.online/wx/md2img";

        String content = "h1 header\n" +
                "============\n" +
                "\n" +
                "Paragraphs are separated by a blank line.\n" +
                "\n" +
                "2nd paragraph. *Italic*, **bold**, and `monospace`. Itemized lists\n" +
                "look like:\n" +
                "\n" +
                "  * this one\n" +
                "  * that one\n" +
                "  * the other one";
        String token = "0xdahdljk3u8eqhrjqwer90e";
        String noborder = "true";


        try {
            Response res = HttpWrapper.of(url)
                    .addParam("content", content)
                    .addParam("token", token)
                    .addParam("noborder", noborder)
                    .post();
            if (res.isSuccessful()) {
                String str = res.body().string();
                System.out.println("ans: " + str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testUpload() {
        String url = "https://zbang.online/wx/qrcode/encode";

        String path = "/Users/yihui/Desktop/img/test.jpg";
        File file = new File(path);

        try {
            Response res = HttpWrapper.of(url)
                    .file("image", file.getName(), "image/jpeg", file)
                    .addParam("content", "http://www.baidu.com")
                    .addParam("size", "400")

                    .upload();
            if (res.isSuccessful()) {
                String str = res.body().string();
                System.out.println("ans: " + str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
