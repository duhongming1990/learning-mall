package com.dhm.seckillplus.controller;

import com.dhm.seckillplus.common.response.ResultBean;
import com.dhm.seckillplus.domain.User;
import com.dhm.seckillplus.service.UserService;
import com.dhm.seckillplus.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
    @Author duhongming
    @Email 935720334@qq.com
    @Date 2018/10/31 21:01
    用户登录
*/
@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {

	@Autowired
    UserService userService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }
    
    @RequestMapping("/do_login")
    @ResponseBody
    public ResultBean<User> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
    	//登录
    	User user = userService.login(response, loginVo);
    	return new ResultBean<>(user);
    }
}
