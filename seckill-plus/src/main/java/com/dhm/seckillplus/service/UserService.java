package com.dhm.seckillplus.service;

import com.dhm.seckillplus.common.exception.CommonExceptions;
import com.dhm.seckillplus.dao.mysql.UserDao;
import com.dhm.seckillplus.dao.redis.RedisDao;
import com.dhm.seckillplus.domain.User;
import com.dhm.seckillplus.util.MD5Util;
import com.dhm.seckillplus.util.UUIDUtil;
import com.dhm.seckillplus.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {

    public static final String COOKI_NAME_TOKEN = "token";
    private static final String USER_KEY="session:";
    private static final Integer CACHE_SECONDS=3600*24*2;
    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisDao redisDao;

    public String login(HttpServletResponse response, LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        User user = userDao.getById(Long.parseLong(mobile));
        if(user == null) {
            throw CommonExceptions.UserCommonException.MOBILE_NOT_EXIST.getCommonException();
        }
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if(!calcPass.equals(dbPass)) {
            throw CommonExceptions.UserCommonException.PASSWORD_ERROR.getCommonException();
        }
        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return token;
    }

    private void addCookie(HttpServletResponse response, String token, User user) {
        redisDao.setObject(USER_KEY+token,user,CACHE_SECONDS);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        cookie.setMaxAge(CACHE_SECONDS);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public User getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        User user = (User) redisDao.getObject(USER_KEY+token, User.class);
        //延长有效期
        if(user != null) {
            addCookie(response, token, user);
        }
        return user;
    }
}
