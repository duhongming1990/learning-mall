package com.mmall.controller.common;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/10/23 17:20
 */
@Slf4j
//@WebFilter(urlPatterns = "/*")
public class XSSAndDesensitizedFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("XSSAndDesensitizedFilter start");
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
//        DesensitizedHttpServletResponseWrapper httpServletResponseWrapper= new DesensitizedHttpServletResponseWrapper(response);
//        String result = httpServletResponseWrapper.getResult();
//
//        PrintWriter out = response.getWriter();
//        out.write(result);

        filterChain.doFilter(request,response);
        log.info("XSSAndDesensitizedFilter end");
    }

    @Override
    public void destroy() {

    }
}