package com.quick.hui.crawler.common.http;

import com.google.common.base.Joiner;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yihui on 2018/1/12.
 */
public class HttpWrapper {
    private static OkHttpClient client = new OkHttpClient();

    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";

    public static Builder of(String url) {
        return new Builder(url);
    }


    public static class Builder {

        private String url;

        private Map<String, String> params;

        private List<MultipartBody.Part> uploadParts;

        Request.Builder reqBuilder;

        Builder(String url) {
            this.url = url;
            params = new HashMap<>();
            uploadParts = new ArrayList<>();

            reqBuilder = new Request.Builder();


            // 默认添加上user-agent
            addHeader("User-Agent", DEFAULT_USER_AGENT);
        }


        // 添加参数
        public Builder addParam(String key, String value) {
            params.put(key, value);
            return this;
        }


        // 添加头
        public Builder addHeader(String key, String value) {
            reqBuilder.addHeader(key, value);
            return this;
        }


        public Builder file(String key, String fileName, String fileMime, byte[] bytes) {
            MultipartBody.Part part = MultipartBody.Part.createFormData(
                    key,
                    fileName,
                    RequestBody.create(MediaType.parse(fileMime), bytes));
            uploadParts.add(part);
            return this;
        }

        public Builder file(String key, String fileName, String fileMime, File file) {
            MultipartBody.Part part = MultipartBody.Part.createFormData(
                    key,
                    fileName,
                    RequestBody.create(MediaType.parse(fileMime), file));
            uploadParts.add(part);
            return this;
        }

        public Builder file(String key, String fileName, String fileMime, InputStream stream) throws IOException {
            int size = stream.available();
            byte[] bytes = new byte[size];
            stream.read(bytes);
            return file(key, fileName, fileMime, bytes);
        }


        /**
         * 发送get请求
         *
         * @return
         * @throws IOException
         */
        public Response get() throws IOException {
            StringBuilder urlBuilder = new StringBuilder(url);
            if (!params.isEmpty()) {
                urlBuilder.append("?").append(Joiner.on('&').withKeyValueSeparator('=').join(params));
            }

            return client.newCall(reqBuilder.url(urlBuilder.toString()).build()).execute();
        }


        /**
         * post表单数据
         *
         * @return
         */
        public Response post() throws IOException {
            // 创建表单
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            if (!params.isEmpty()) {
                params.forEach(formBodyBuilder::add);
            }

            return client.newCall(reqBuilder.url(url)
                    .post(formBodyBuilder.build())
                    .build())
                    .execute();
        }


        /**
         * 文件上传
         *
         * @return
         * @throws IOException
         */
        public Response upload() throws IOException {
            MultipartBody.Builder bodyBuilder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            uploadParts.forEach(bodyBuilder::addPart);

            // 添加参数
            params.forEach(bodyBuilder::addFormDataPart);

            return client.newCall(reqBuilder.url(url)
                    .post(bodyBuilder.build())
                    .build())
                    .execute();
        }
    }


}
