package io.github.kimmking.gateway.outbound.httpclient;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static org.apache.http.HttpHeaders.CONNECTION;

public class HttpClientOutBoundHandler {

    private Log logger = LogFactory.getLog(getClass());
    private HttpClient client;
    private final static String ENCODE_UTF8 = "UTF-8";

    private String proxyServer;

    public HttpClientOutBoundHandler(String proxyServer) {
        this.proxyServer = proxyServer;
        client = HttpClientBuilder.create().build();
        logger.info("My HttpClient OutBoundHandler Init ....");
    }

    /**
     * 提供给inboundhandler调用,用于响应客户端请求
     *
     * @param fullRequest
     * @param ctx
     */
    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {

        FullHttpResponse response = null;
        try {
            final String value = post(proxyServer + fullRequest.uri(), null, null);

            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(value.getBytes(ENCODE_UTF8)));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", response.content().readableBytes());

        } catch (IOException e) {
            logger.error("处理测试接口出错", e);
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
        }finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    logger.info("send data to client");
                    response.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
            }

            ctx.flush();
        }

    }

    public String post(String url, HttpEntity reqEntity, RequestConfig config) throws IOException {
        HttpPost post = new HttpPost(url);
        post.setConfig(config);
        post.setEntity(reqEntity);
        HttpResponse response = client.execute(post);
        return responseMessage(response);
    }

    public String get(String url, RequestConfig config) throws IOException {
        HttpGet get = new HttpGet(url);
        get.setConfig(config);
        HttpResponse response = client.execute(get);
        return responseMessage(response);
    }

    private String responseMessage(HttpResponse response) throws IOException {
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity resEntity = response.getEntity();
            return EntityUtils.toString(resEntity, ENCODE_UTF8);
        }
        return null;
    }

}
