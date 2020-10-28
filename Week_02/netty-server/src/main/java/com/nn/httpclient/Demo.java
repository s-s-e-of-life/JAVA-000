package com.nn.httpclient;


import java.io.IOException;

/**
 * @最后修改人 杨南
 * @最后修改时间 2020/10/28-9:51
 */
public class Demo {
    public static void main(String[] args) throws IOException {
        // 请求api并获取结果
        System.out.println(HttpClientUtil.get("http://localhost:8808/test", null));
//        System.out.println(HttpClientUtil.get("http://www.baidu.com", null));
    }
}
