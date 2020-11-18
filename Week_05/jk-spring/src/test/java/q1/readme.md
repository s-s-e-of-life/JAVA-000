# 代理实现aop

- 接口代理的实现

(1) 定义个一个切面
```java
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
```

(2) 使用Proxy来生成代理类执行代理方法.

```java
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
```

(3) 测试运行
```text
/home/nan/env/jdk1.8.0_144/bin/java -ea -Didea.test.cyclic.buffer.size=1048576 -javaagent:/home/nan/env/idea-IU-182.5262.2/lib/idea_rt.jar=42219:/home/nan/env/idea-IU-182.5262.2/bin -Dfile.encoding=UTF-8 -classpath /home/nan/env/idea-IU-182.5262.2/lib/idea_rt.jar:/home/nan/env/idea-IU-182.5262.2/plugins/junit/lib/junit-rt.jar:/home/nan/env/idea-IU-182.5262.2/plugins/junit/lib/junit5-rt.jar:/home/nan/env/jdk1.8.0_144/jre/lib/charsets.jar:/home/nan/env/jdk1.8.0_144/jre/lib/deploy.jar:/home/nan/env/jdk1.8.0_144/jre/lib/ext/cldrdata.jar:/home/nan/env/jdk1.8.0_144/jre/lib/ext/dnsns.jar:/home/nan/env/jdk1.8.0_144/jre/lib/ext/jaccess.jar:/home/nan/env/jdk1.8.0_144/jre/lib/ext/jfxrt.jar:/home/nan/env/jdk1.8.0_144/jre/lib/ext/localedata.jar:/home/nan/env/jdk1.8.0_144/jre/lib/ext/nashorn.jar:/home/nan/env/jdk1.8.0_144/jre/lib/ext/sunec.jar:/home/nan/env/jdk1.8.0_144/jre/lib/ext/sunjce_provider.jar:/home/nan/env/jdk1.8.0_144/jre/lib/ext/sunpkcs11.jar:/home/nan/env/jdk1.8.0_144/jre/lib/ext/zipfs.jar:/home/nan/env/jdk1.8.0_144/jre/lib/javaws.jar:/home/nan/env/jdk1.8.0_144/jre/lib/jce.jar:/home/nan/env/jdk1.8.0_144/jre/lib/jfr.jar:/home/nan/env/jdk1.8.0_144/jre/lib/jfxswt.jar:/home/nan/env/jdk1.8.0_144/jre/lib/jsse.jar:/home/nan/env/jdk1.8.0_144/jre/lib/management-agent.jar:/home/nan/env/jdk1.8.0_144/jre/lib/plugin.jar:/home/nan/env/jdk1.8.0_144/jre/lib/resources.jar:/home/nan/env/jdk1.8.0_144/jre/lib/rt.jar:/home/nan/idea-workspace/jk-spring/target/test-classes:/home/nan/idea-workspace/jk-spring/target/classes:/opt/repo/junit/junit/4.12/junit-4.12.jar:/opt/repo/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:/opt/repo/org/slf4j/slf4j-api/1.7.25/slf4j-api-1.7.25.jar:/opt/repo/org/slf4j/slf4j-log4j12/1.7.25/slf4j-log4j12-1.7.25.jar:/opt/repo/log4j/log4j/1.2.17/log4j-1.2.17.jar:/opt/repo/org/springframework/spring-beans/5.1.3.RELEASE/spring-beans-5.1.3.RELEASE.jar:/opt/repo/org/springframework/spring-core/5.1.3.RELEASE/spring-core-5.1.3.RELEASE.jar:/opt/repo/org/springframework/spring-context/5.1.3.RELEASE/spring-context-5.1.3.RELEASE.jar:/opt/repo/org/springframework/spring-jcl/5.1.3.RELEASE/spring-jcl-5.1.3.RELEASE.jar:/opt/repo/org/springframework/spring-expression/5.1.3.RELEASE/spring-expression-5.1.3.RELEASE.jar:/opt/repo/org/springframework/spring-aop/5.1.3.RELEASE/spring-aop-5.1.3.RELEASE.jar:/opt/repo/org/springframework/spring-tx/5.1.3.RELEASE/spring-tx-5.1.3.RELEASE.jar:/opt/repo/org/springframework/spring-test/4.3.10.RELEASE/spring-test-4.3.10.RELEASE.jar:/opt/repo/org/projectlombok/lombok/1.18.10/lombok-1.18.10.jar com.intellij.rt.execution.junit.JUnitStarter -ideVersion5 -junit4 q1.test,proxy
前置增强
Are you ok?
后置增强
```

- 此方式,只是实现了简单的对一个服务接口的方式的增强.并未实现切点及表达式.
