package com.nn.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @最后修改人 杨南
 * @最后修改时间 2020/10/28-10:06
 */
public class HttpClientUtil {

    private static HttpClient client = HttpClientBuilder.create().build();

    private HttpClientUtil() {
    }

    public static HttpClient getClient() {
        return client;
    }

    public static void main(String[] args) throws IOException {
        String url = "http://www.baidu.com";

        // 通过名称对传输数据
//        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
//        formparams.add(new BasicNameValuePair("account", ""));
//        formparams.add(new BasicNameValuePair("password", ""));
//        HttpEntity reqEntity = new UrlEncodedFormEntity(formparams, "utf-8");

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();

        String post = post(url, null, requestConfig);
        System.out.println(post);
    }

    public static String post(String url, HttpEntity reqEntity, RequestConfig config) throws IOException {
        HttpPost post = new HttpPost(url);
        post.setConfig(config);
        post.setEntity(reqEntity);
        HttpResponse response = client.execute(post);
        return responseMessage(response);
    }

    public static String get(String url, RequestConfig config) throws IOException {
        HttpGet get = new HttpGet(url);
        get.setConfig(config);
        HttpResponse response = client.execute(get);
        return responseMessage(response);
    }

    private static String responseMessage(HttpResponse response) throws IOException {
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity resEntity = response.getEntity();
            return EntityUtils.toString(resEntity, "utf-8");
        }
        return "Fail Request (500)";
    }

}
