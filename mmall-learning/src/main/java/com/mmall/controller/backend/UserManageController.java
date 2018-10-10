package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.exception.CommonExceptions;
import com.mmall.common.response.ResultBean;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by geely
 */

@Controller
@RequestMapping("/manage/user")
public class UserManageController {

    @Autowired
    private IUserService iUserService;

    @PostMapping(value="login.do")
    @ResponseBody
    public ResultBean<User> login(String username, String password, HttpSession session){
        User user = iUserService.login(username,password);
        iUserService.checkAdminRole(user);
        session.setAttribute(Const.CURRENT_USER,user);
        return new ResultBean<>(user);
    }

}
