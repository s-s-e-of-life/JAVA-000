package q1;

import org.junit.Test;

import java.lang.reflect.Proxy;

public class test {

    @Test
    public void proxy() {
        LogInfo s = new LogInfoImpl();
        ClassLoader classLoader = s.getClass().getClassLoader();
        Class<?>[] interfaces = s.getClass().getInterfaces();

        LogInfo o = (LogInfo)Proxy.newProxyInstance(classLoader, interfaces, new MyAspect(s));

        o.log("Are you ok?");
    }

}
