# mmall-learning

## 项目优化

1.数据库连接池用Druid

    不解释
2.Spring分模块配置

    spring-context-dao.xml
    spring-context-service.xml
    spring-context-web.xml

在web.xml配置如下：
```xml
    <!-- Spring DispatcherServlet -->
    <servlet>
        <servlet-name>mmall-learning-dispatcher</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:spring/spring-context*.xml</param-value>
        </init-param>
    </servlet>
    <servlet-mapping>
        <servlet-name>mmall-learning-dispatcher</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
```

3.全局响应侵入太严重，用切面

4.异常定义侵入太严重，用枚举
    
    