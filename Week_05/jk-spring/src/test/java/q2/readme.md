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

