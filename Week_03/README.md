# 学习笔记

## 作业:


作业源码参见nio02下面

### 测试替换自己写的handler:

首先是 HttpInboundHandler 的构造器里,替换自己的handler.
```java
    public HttpInboundHandler(String proxyServer) {
        this.proxyServer = proxyServer;

//        handler = new HttpClientOutBoundHandler(this.proxyServer);
//        handler = new HttpOutboundHandler(this.proxyServer);
        handler = new NettyHttpClientOutboundHandler(this.proxyServer);
    }
```

然后是在HttpInboundHandler的channelRead方法里面调用即可.
```java
 @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            //logger.info("channelRead流量接口请求开始，时间为{}", startTime);
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
//            String uri = fullRequest.uri();
//            //logger.info("接收到的请求url为{}", uri);
//            if (uri.contains("/test")) {
//                handlerTest(fullRequest, ctx);
//            }


            MyHttpRequestFilter
                    .getInstance()
                    .filter(fullRequest, ctx);

            log.info("Get Filter Add Header Value = " + fullRequest.headers().get("author"));

            handler.handle(fullRequest, ctx);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
```

### 作业盘点:


1) 将自己写的httpcilent整合到gateway中

[HttpClientOutBoundHandler](nio02/src/main/java/io/github/kimmking/gateway/outbound/httpclient/HttpClientOutBoundHandler.java)

2) 写filter

[MyHttpRequestFilter](nio02/src/main/java/io/github/kimmking/gateway/filter/MyHttpRequestFilter.java)

3) 用netty的客户端模式实现请求服务

- 没能实现. ...

对netty不熟悉,对于其客户端代替okhttp或者httpclient没有明确的思路.

4) 路由
[MyHttpEndpointRouter](nio02/src/main/java/io/github/kimmking/gateway/router/MyHttpEndpointRouter.java)

简单的实现了随机一个.
```java
public class MyHttpEndpointRouter implements HttpEndpointRouter {
    @Override
    public String route(List<String> endpoints) {

        if (null != endpoints && endpoints.size() > 0) {
            // 随机选择一个作为请求的路由进行调用
            final int choose = new Random().nextInt(endpoints.size());
            return endpoints.get(choose);
        }

        return null;
    }
}
```



## **出现的问题**:

1) 自己实现的httpcilent的请求后,outboundhandler存在连续点几次出现假死的现象.

> 具体的表现是: 等待请求完全完成,再请求就没有问题.如果取消了,再请求出现问题.
应该是页面请求取消,导致后台部分不知道,导致最后没有关闭.

经过查资料,了解到ChannelOption.SO_KEEPALIVE设置为true,表示进行请求复用,同一个连接上串行方式传递请求-响应数据.
- HTTP协议的KeepAlive意图在于连接复用，同一个连接上串行方式传递请求-响应数据

```text
KeepAlive并不是TCP协议的一部分，但是大多数操作系统都实现了这个机制（所以需要在操作系统层面设置KeepAlive的相关参数）。
KeepAlive机制开启后，在一定时间内（一般时间为7200s，参数tcp_keepalive_time）在链路上没有数据传送的情况下，
TCP层将发送相应的KeepAlive探针以确定连接可用性，探测失败后重试10（参数 tcp_keepalive_probes）次，每次间隔时间75s（参数 tcp_keepalive_intvl），
所有探测失败后，才认为当前连接已经不可用。
```


**存在请求方主动取消了请求,导致连接不可用.**

后尝试将KeepAlive关闭后,就没有出现取消连接失败的问题.这个问题的解决方案还是采用心跳机制或结合心跳机制比较好.

