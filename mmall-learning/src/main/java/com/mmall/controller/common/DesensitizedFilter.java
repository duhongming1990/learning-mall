package com.mmall.controller.common;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/11/5 10:39
 * 脱敏过滤器
 */
@Slf4j
public class DesensitizedFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //TODO 脱敏放到过滤器里面呢还是ResultBean的AOP中呢？？？
        log.info("DesensitizedFilter start");
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        ResponseWrapper responseWrapper= new ResponseWrapper(response);

        filterChain.doFilter(new XSSHttpServletRequestWrapper(request),servletResponse);
        byte[] bytes = responseWrapper.getBytes(); // 获取缓存的响应数据
        log.info("压缩前大小：" + bytes.length);
        log.info("压缩前数据：" + new String(bytes,"utf-8"));
        response.getOutputStream().write(bytes); // 将压缩数据响应给客户端
        log.info("DesensitizedFilter end");
    }

    @Override
    public void destroy() {

    }
}