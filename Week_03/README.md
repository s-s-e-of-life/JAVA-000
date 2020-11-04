学习笔记

作业:



作业源码参见nio02下面



1) 将自己写的httpcilent整合到gateway中

[HttpClientOutBoundHandler](nio02/src/main/java/io/github/kimmking/gateway/outbound/httpclient/HttpClientOutBoundHandler.java)

2) 写filter

[MyHttpRequestFilter](nio02/src/main/java/io/github/kimmking/gateway/filter/MyHttpRequestFilter.java)

3) 用netty的客户端模式实现请求服务

- 没有实现. ...

对netty不熟悉,对于其客户端代替okhttp或者httpclient没有明确的思路.

**出现的问题**:

1) 自己实现的httpcilent的请求后,outboundhandler存在连续点几次出现假死的现象.

> 具体的表现是: 等待请求完全完成,再请求就没有问题.如果取消了,再请求出现问题.
应该是页面请求取消,导致后台部分不知道,导致最后没有关闭.

经过查资料,了解到ChannelOption.SO_KEEPALIVE设置为true,表示进行请求复用,同一个连接上串行方式传递请求-响应数据.
- HTTP协议的KeepAlive意图在于连接复用，同一个连接上串行方式传递请求-响应数据

KeepAlive并不是TCP协议的一部分，但是大多数操作系统都实现了这个机制（所以需要在操作系统层面设置KeepAlive的相关参数）。
KeepAlive机制开启后，在一定时间内（一般时间为7200s，参数tcp_keepalive_time）在链路上没有数据传送的情况下，
TCP层将发送相应的KeepAlive探针以确定连接可用性，探测失败后重试10（参数 tcp_keepalive_probes）次，每次间隔时间75s（参数 tcp_keepalive_intvl），
所有探测失败后，才认为当前连接已经不可用。

存在请求方主动取消了请求,导致连接不可用.

后尝试将KeepAlive关闭后,就没有出现取消连接失败的问题.这个问题的解决方案还是采用心跳机制或结合心跳机制比较好.

