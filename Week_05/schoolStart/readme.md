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

