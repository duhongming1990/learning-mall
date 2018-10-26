package com.mmall.controller.common;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/10/23 17:26
 */
@Slf4j
public class DesensitizedHttpServletResponseWrapper extends HttpServletResponseWrapper {

    public DesensitizedHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void setResponse(ServletResponse response) {
        log.info("DesensitizedHttpServletResponseWrapper start");
        super.setResponse(response);
    }
}