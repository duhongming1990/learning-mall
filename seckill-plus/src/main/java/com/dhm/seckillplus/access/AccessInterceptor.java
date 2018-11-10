package com.dhm.seckillplus.access;

import com.alibaba.fastjson.JSON;
import com.dhm.seckillplus.common.exception.CommonException;
import com.dhm.seckillplus.common.exception.CommonExceptions;
import com.dhm.seckillplus.dao.redis.RedisDao;
import com.dhm.seckillplus.domain.SeckillUser;
import com.dhm.seckillplus.service.SeckillUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;


@Service
public class AccessInterceptor  extends HandlerInterceptorAdapter{
	
	@Autowired
	SeckillUserService seckillUserService;
	
	@Autowired
	RedisDao redisDao;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(handler instanceof HandlerMethod) {
			SeckillUser user = getUser(request, response);
			UserContext.setUser(user);
			HandlerMethod hm = (HandlerMethod)handler;
			AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
			if(accessLimit == null) {
				return true;
			}
			int seconds = accessLimit.seconds();
			int maxCount = accessLimit.maxCount();
			boolean needLogin = accessLimit.needLogin();
			String key = request.getRequestURI();
			if(needLogin) {
				if(user == null) {
					render(response, CommonExceptions.UserCommonException.SESSION_ERROR.getCommonException());
					return false;
				}
				key += "_" + user.getId();
			}else {
				//do nothing
			}
//			AccessKey ak = AccessKey.withExpire(seconds);
//			Integer count = redisService.get(ak, key, Integer.class);
//			if(count  == null) {
//				redisDao.set(ak, key, 1);
//			}else if(count < maxCount) {
//				redisDao.incr(ak, key);
//			}else {
//				render(response, CodeMsg.ACCESS_LIMIT_REACHED);
//				return false;
//			}
		}
		return true;
	}
	
	private void render(HttpServletResponse response, CommonException CommonException)throws Exception {
		response.setContentType("application/json;charset=UTF-8");
		OutputStream out = response.getOutputStream();
		String str  = JSON.toJSONString(CommonException);
		out.write(str.getBytes("UTF-8"));
		out.flush();
		out.close();
	}

	private SeckillUser getUser(HttpServletRequest request, HttpServletResponse response) {
		String paramToken = request.getParameter(SeckillUserService.COOKI_NAME_TOKEN);
		String cookieToken = getCookieValue(request, SeckillUserService.COOKI_NAME_TOKEN);
		if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
			return null;
		}
		String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
		return seckillUserService.getByToken(response, token);
	}
	
	private String getCookieValue(HttpServletRequest request, String cookiName) {
		Cookie[]  cookies = request.getCookies();
		if(cookies == null || cookies.length <= 0){
			return null;
		}
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals(cookiName)) {
				return cookie.getValue();
			}
		}
		return null;
	}
	
}
