package com.mmall.controller.common;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Map;

/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/10/23 17:23
 */
@Slf4j
public class XSSHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XSSHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        log.info("XSSHttpServletRequestWrapper getHeader");
        return super.getHeader(name);
    }

    @Override
    public String getQueryString() {
        log.info("XSSHttpServletRequestWrapper getQueryString");
        return super.getQueryString();
    }

    @Override
    public String getParameter(String name) {
        log.info("XSSHttpServletRequestWrapper getParameter");
        return super.getParameter(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        log.info("XSSHttpServletRequestWrapper getParameterMap");
        return super.getParameterMap();
    }
}