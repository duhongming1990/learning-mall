# 1.SpringBoot配置Filter

## 1.1 FilterRegistrationBean方式

```java
package com.mooc.house.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

@Slf4j
public class LogFilter implements Filter {


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		log.info("log filter!!!");
		filterChain.doFilter(servletRequest,servletResponse);
	}

	@Override
	public void destroy() {

	}
}
```

```java
package com.mooc.house.web.filter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterBeanConfig {

	/**
	 * 1.构造filter
	 * 2.配置拦截urlPattern
	 * 3.利用FilterRegistrationBean进行包装
	 * @return
	 */
	@Bean
	public FilterRegistrationBean logFilter(){
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new LogFilter());
		List<String> urList = new ArrayList<>();
		urList.add("*");
		filterRegistrationBean.setUrlPatterns(urList);
		return filterRegistrationBean;
	}
}
```
## 1.2 @WebFilter方式

新增注解：

    配置urlPatterns：@WebFilter(urlPatterns = "/*")
    配置filter执行顺序：@Order(9999)
```java
package com.mooc.house.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

@Slf4j
@WebFilter(urlPatterns = "/*")
@Order(9999)
public class LogFilter implements Filter {


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		log.info("log filter!!!");
		filterChain.doFilter(servletRequest,servletResponse);
	}

	@Override
	public void destroy() {

	}
}
```

新增注解：
    
    扫描filter包：@ServletComponentScan(basePackages = {"com.mooc.house.web.filter"})

```java

package com.mooc.house;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import com.mooc1.house.autoconfig.EnableHttpClient;

@SpringBootApplication
@ServletComponentScan(basePackages = {"com.mooc.house.web.filter"})
public class HouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(HouseApplication.class, args);
	}
}
```

# 2 移除tomcat，添加jetty
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
``` 

# 3 SpringBoot起步依赖stater
略