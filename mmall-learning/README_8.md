# 8 Spring Session框架集成零侵入实现单点登录

## 8.1 新增spring-session依赖
```xml
   <dependency>
     <groupId>org.springframework.session</groupId>
     <artifactId>spring-session-data-redis</artifactId>
     <version>1.2.1.RELEASE</version>
   </dependency>
```

## 8.2 web.xml配置
```xml
    <!-- Spring Session -->
    <filter>
        <filter-name>springSessionRepositoryFilter</filter-name>
        <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>springSessionRepositoryFilter</filter-name>
        <url-pattern>*.do</url-pattern>
    </filter-mapping>
```

## 8.3 spring-web.xml配置
```xml
    <bean id="redisHttpSessionConfiguration" class="org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration">
        <property name="maxInactiveIntervalInSeconds" value="1800" />
    </bean>
    
    <bean id="defaultCookieSerializer" class="org.springframework.session.web.http.DefaultCookieSerializer">
        <property name="cookieName" value="SESSION_NAME"/>
        <property name="domainName" value=".happymmall.com"/>
        <property name="useHttpOnlyCookie" value="true"/>
        <property name="cookiePath" value="/"/>
        <property name="cookieMaxAge" value="31536000"/>
    </bean>

    <bean id="jedisConnectionFactory" class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory">
        <property name="hostName" value="${redis.ip}" />
        <property name="port" value="${redis.port}" />
        <property name="password" value="${redis.password}"/>
        <property name="poolConfig" ref="jedisPoolConfig" />
    </bean>
```

