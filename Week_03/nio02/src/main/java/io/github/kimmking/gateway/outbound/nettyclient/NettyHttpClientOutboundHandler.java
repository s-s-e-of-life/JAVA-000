package io.github.kimmking.gateway.outbound.nettyclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.util.EntityUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static java.util.regex.Pattern.compile;
import static org.apache.http.HttpHeaders.CONNECTION;

public class NettyHttpClientOutboundHandler {

    private Log logger = LogFactory.getLog(getClass());
    private String proxyHost;
    private int proxyPort;

    public NettyHttpClientOutboundHandler(String proxyServer) {
        try {
            URL url = new URL(proxyServer);
            proxyHost = url.getHost();
            proxyPort = url.getPort();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public void handle(FullHttpRequest fullRequest, ChannelHandlerContext ctx) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                    ch.pipeline().addLast(new HttpResponseDecoder());
                    // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
                    ch.pipeline().addLast(new HttpRequestEncoder());

                    // 添加一个用于响应的
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext innerctx, Object msg) throws Exception {
                            FullHttpResponse response = null;

                            // TODO 解析我们的数据

                            try {
                                response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer("Good".getBytes("UTF-8")));
                                response.headers().set("Content-Type", "application/json");
                                response.headers().setInt("Content-Length", response.content().readableBytes());
                            } catch (Exception e) {
                                logger.error("处理测试接口出错", e);
                                response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
                            } finally {
                                if (fullRequest != null) {
                                    if (!HttpUtil.isKeepAlive(fullRequest)) {
                                        ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                                    } else {
                                        response.headers().set(CONNECTION, KEEP_ALIVE);
                                        ctx.write(response);
                                    }
                                }
                                ctx.flush();
                            }
                        }
                    });
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(proxyHost, proxyPort).sync();

            f.channel().write(fullRequest);
            f.channel().flush();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
