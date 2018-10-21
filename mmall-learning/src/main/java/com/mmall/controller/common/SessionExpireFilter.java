package com.mmall.controller.common;


import com.alibaba.fastjson.JSON;
import com.mmall.bean.pojo.User;
import com.mmall.common.Const;
import com.mmall.util.CookieUtil;
import com.mmall.util.RedisPoolUtil;
import com.mmall.util.SpringContextHolder;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/10/21 12:49
 */
@Deprecated
public class SessionExpireFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        RedisPoolUtil redisPoolUtil = SpringContextHolder.getBean(RedisPoolUtil.class);
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String loginToken = CookieUtil.readLoginToken(httpServletRequest);

        if (StringUtils.isNotEmpty(loginToken)) {
            //判断logintoken是否为空或者""；
            //如果不为空的话，符合条件，继续拿user信息

            String userJsonStr = redisPoolUtil.get(loginToken);
            User user = JSON.parseObject(userJsonStr,User.class);
            if (user != null) {
                //如果user不为空，则重置session的时间，即调用expire命令
                redisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }


    @Override
    public void destroy() {

    }
}
