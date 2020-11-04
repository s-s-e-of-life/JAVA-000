package io.github.kimmking.gateway;


import io.github.kimmking.gateway.inbound.HttpInboundServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NettyServerApplication {

    public final static String GATEWAY_NAME = "NIOGateway";
    public final static String GATEWAY_VERSION = "1.0.0";

    private static final Log log = LogFactory.getLog(NettyServerApplication.class);

    public static void main(String[] args) {
        String proxyServer = System.getProperty("proxyServer","http://localhost:8808");
        String proxyPort = System.getProperty("proxyPort","8888");

          //  http://localhost:8888/api/hello  ==> gateway API
          //  http://localhost:8088/api/hello  ==> backend service

        int port = Integer.parseInt(proxyPort);
        log.info(GATEWAY_NAME + " " + GATEWAY_VERSION +" starting...");
        HttpInboundServer server = new HttpInboundServer(port, proxyServer);
        log.info(GATEWAY_NAME + " " + GATEWAY_VERSION +" started at http://localhost:" + port + " for server:" + proxyServer);
        try {
            server.run();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
