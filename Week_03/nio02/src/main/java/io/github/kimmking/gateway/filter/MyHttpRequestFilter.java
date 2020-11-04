package io.github.kimmking.gateway.filter;

import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class MyHttpRequestFilter extends ChannelInboundHandlerAdapter implements HttpRequestFilter {
    private final static String JSON_CONTENT_TYPE = "application/json";
    private final static String ENCODE_UTF8 = "UTF-8";

    private final Log log = LogFactory.getLog(getClass());

    private static MyHttpRequestFilter instance  = new MyHttpRequestFilter();

    public static MyHttpRequestFilter getInstance() {
        return instance;
    }

    public MyHttpRequestFilter() {

    }

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {

        DecoderResult decoderResult = fullRequest.getDecoderResult();
        // 这个过滤器没有想好要干什么,打个日志,加个头吧.

        log.info(">>>> Filter Invoked .");
        fullRequest.headers().add("author", "xxxx_nan_xxxx");
    }
}
