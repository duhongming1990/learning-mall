package com.dhm.seckillplus.service;

import com.dhm.seckillplus.common.exception.CommonExceptions;
import com.dhm.seckillplus.common.prefix.KeyPrefixs;
import com.dhm.seckillplus.dao.mysql.SeckillUserDao;
import com.dhm.seckillplus.dao.redis.RedisDao;
import com.dhm.seckillplus.domain.SeckillUser;
import com.dhm.seckillplus.util.MD5Util;
import com.dhm.seckillplus.util.UUIDUtil;
import com.dhm.seckillplus.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class SeckillUserService {
	
	
	public static final String COOKI_NAME_TOKEN = "token";
	
	@Autowired
	SeckillUserDao seckillUserDao;
	
	@Autowired
	RedisDao redisDao;
	
	public SeckillUser getById(long id) {
		//取缓存
		SeckillUser user = (SeckillUser) redisDao.get(KeyPrefixs.UserKey.ID.getBasePrefix(), ""+id, SeckillUser.class);
		if(user != null) {
			return user;
		}
		//取数据库
		user = seckillUserDao.getById(id);
		if(user != null) {
			redisDao.set(KeyPrefixs.UserKey.ID.getBasePrefix(), ""+id, user);
		}
		return user;
	}
	// http://blog.csdn.net/tTU1EvLDeLFq5btqiK/article/details/78693323
	public boolean updatePassword(String token, long id, String formPass) {
		//取user
		SeckillUser user = getById(id);
		if(user == null) {
			throw CommonExceptions.UserCommonException.MOBILE_NOT_EXIST.getCommonException();
		}
		//更新数据库
		SeckillUser toBeUpdate = new SeckillUser();
		toBeUpdate.setId(id);
		toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
		seckillUserDao.update(toBeUpdate);
		//处理缓存
		redisDao.delete(KeyPrefixs.UserKey.ID.getBasePrefix(), ""+id);
		user.setPassword(toBeUpdate.getPassword());
		redisDao.set(KeyPrefixs.UserKey.TOKEN.getBasePrefix(), token, user);
		return true;
	}


	public SeckillUser getByToken(HttpServletResponse response, String token) {
		if(StringUtils.isEmpty(token)) {
			return null;
		}
		SeckillUser user = (SeckillUser) redisDao.get(KeyPrefixs.UserKey.TOKEN.getBasePrefix(), token, SeckillUser.class);
		//延长有效期
		if(user != null) {
			addCookie(response, token, user);
		}
		return user;
	}
	

	public String login(HttpServletResponse response, LoginVo loginVo) {
		if(loginVo == null) {
			throw CommonExceptions.GlobalCommonException.SERVER_ERROR.getCommonException();
		}
		String mobile = loginVo.getMobile();
		String formPass = loginVo.getPassword();
		//判断手机号是否存在
		SeckillUser user = getById(Long.parseLong(mobile));
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
		String token	 = UUIDUtil.uuid();
		addCookie(response, token, user);
		return token;
	}
	
	private void addCookie(HttpServletResponse response, String token, SeckillUser user) {
		redisDao.set(KeyPrefixs.UserKey.TOKEN.getBasePrefix(), token, user);
		Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
		cookie.setMaxAge(KeyPrefixs.UserKey.TOKEN.getBasePrefix().expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}

}
