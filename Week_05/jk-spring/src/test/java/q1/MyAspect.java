package q1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyAspect implements InvocationHandler {

    private Object o;

    public MyAspect(Object o) {
        this.o = o;
    }

    /**
     * 执行代理
     *
     * @param proxy  代理的对象
     * @param method 代理要执行的方法
     * @param args   入参
     * @return
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 前置增强
        before();

        // 执行代理类proxy对象的方法(可能会因为没有方法而抛出异常)
        Object result = method.invoke(o, args);

        // 后置增强
        after();

        return result;
    }

    public void before() {
        System.out.println("前置增强");
    }

    public void after() {
        System.out.println("后置增强");
    }
}
