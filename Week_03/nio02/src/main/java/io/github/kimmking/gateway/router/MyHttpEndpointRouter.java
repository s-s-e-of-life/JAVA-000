package io.github.kimmking.gateway.router;

import java.util.List;
import java.util.Random;

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
