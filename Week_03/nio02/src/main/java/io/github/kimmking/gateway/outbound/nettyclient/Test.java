package io.github.kimmking.gateway.outbound.nettyclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static org.apache.http.HttpHeaders.CONNECTION;

public class Test {
    public static void main(String[] args) {
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
                            System.out.println(msg);
//                        FullHttpResponse response = null;
//
//                        // TODO 解析我们的数据
//
//                        try {
//                            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer("Good".getBytes("UTF-8")));
//                            response.headers().set("Content-Type", "application/json");
//                            response.headers().setInt("Content-Length", response.content().readableBytes());
//                        } catch (Exception e) {
//                            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
//                        } finally {
//                            if (fullRequest != null) {
//                                if (!HttpUtil.isKeepAlive(fullRequest)) {
//                                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
//                                } else {
//                                    response.headers().set(CONNECTION, KEEP_ALIVE);
//                                    ctx.write(response);
//                                }
//                            }
//                            ctx.flush();
//                        }
                        }
                    });
                }
            });

            while (true) {
                // Start the client.
                ChannelFuture f = b.connect("localhost", 8808).sync();

                f.channel().write("/test");
                f.channel().flush();
                f.channel().closeFuture().sync();
            }
        } catch(Exception e) {

        }
        finally {
            workerGroup.shutdownGracefully();
        }
    }
}
