学习笔记


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


# 2 关于bean的装配方案

- 非springboot,可以采用xml中来实现装配

(1) 简单的bean

```java
<bean id="storeMoneyService" class="io.app.StoreMoneyServiceImpl"></bean>
```

(2) 带构造参数bean

```java
    <bean id="ccbc" class="io.app.Bank">
        <constructor-arg index="0" value="ICBC"></constructor-arg>
    </bean>
```

(3) 需要引用其他的bean

```java
<bean class="io.app.Bank">
<constructor-arg index="0" ref="ccbc"></constructor-arg>
</bean>
```

- springboot项目中

(1) 在启动类中直接定义

```java

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BootDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootDemoApplication.class, args);
    }

    @Bean
    public Bank createBank() {
        return new Bank("ICBC");
    }
}

```

(2) 将一个类配置成配置类, 然后,放在启动类同级或子级即可

```java

@Configuration
public class AppConfiguration {
    
    @Bean
    public Bank createBank() {
        return new Bank("ICBC");
    }
    
}

```

(3) 使用@Component注解标记一个bean

```java
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Money {
    private int total;

    public Money(int how){
        total = how;
    }
}
```


# 3 xml 自定义配置 bean

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">


    <!--
    the frist, by label bean
    -->
    <bean class="io.app.Money">
        <constructor-arg index="0" value="100"></constructor-arg>
    </bean>

    <bean class="io.app.Bank">
        <constructor-arg index="0" value="ICBC"></constructor-arg>
    </bean>

    <bean id="storeMoneyService" class="io.app.StoreMoneyServiceImpl"></bean>

</beans>

```

### Test
```java

import io.app.Money;
import io.app.StoreMonyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Proxy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring_*.xml"})
public class MoneyTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    StoreMonyService monyService;

    @Autowired
    Money money;

    @Test
    public void get() {
        System.out.println(money.getTotal());
    }

    @Test
    public void fetch() {
        monyService.regist(1);

        monyService.store(1,1000);

        System.out.println("Fetched : "+monyService.fetch(1));
    }

}
```



# 4 实现starter并进行测试



# Starter 是如何定义的



### 基本结构

首先,我们开始对结构进行解析

- resources
   - META-INF
      - spring.factories
          - 配置的类的位置,需要自动化注解的东西
      - spring.provides
          - 这个是这个包下面有这样一个starter,叫什么名字的.
      - additional-spring-configuration-metadata.json
          - 我们的配置属性中的选项,可配置的参数及默认值的配置



## 自动化配置的配置类

定义配置类 SchoolAutoConfiguration > 在spring.factories里面去

- 添加注解@Configuration表示这个是一个配置类
- 初始化条件:通过配置@ConditionalOnProperty(prefix = "school", value = "enabled", havingValue = "true", matchIfMissing = true)
  - prefix = "school" 配置文件中有以此打头的
  - value = "enabled" 参数的名称是enable
  - havingValue = "true" 当设置true生效
  - matchIfMissing = true 如果有school打头的配置,则没有设置enable属性,也是默认的enable启用.



- 开关 @EnableConfigurationProperties(SpringBootPropertiesConfiguration.class)
  - 使用 ${xxx.xx} 的方式取值配置文件中的值等处理properties的一些支持.



### 最后

这个starter,就是自动配置.

- 通过配置属性中简单的配置,及默认值来实现最小化的配置,正常的启动一个服务.
- 其核心就是根据配置,自动根据配置的配置参数值进行初始化
- 那么基本可以理解为: `用户配置 > 默认配置` >>>> `转换` >>> `注册成bean (自动配置类里)` 的过程.
- 写一个starter,就是需要完成这个过程. 



代码之一: 

- schoolStart 项目
- demo (引用了start的jar包),并起一个服务,测试配置数据及自动配置的MySchool信息查看.



关于starter的打包:

- 进入schoolStart目录
- 执行 `mvn clean`
- 执行 `mvn package`

