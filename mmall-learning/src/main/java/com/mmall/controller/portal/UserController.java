package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.exception.CommonExceptions;
import com.mmall.common.response.ResultBean;
import com.mmall.bean.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @PostMapping("/login")
    public ResultBean<User> login(String username, String password, HttpSession session){
        User user = iUserService.login(username,password);
        session.setAttribute(Const.CURRENT_USER,user);
        return new ResultBean<>(user);
    }

    /**
     * 用户登出
     * @param session
     * @return
     */
    @PostMapping("/logout")
    public ResultBean<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return new ResultBean<>();
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public ResultBean<String> register(User user){
        iUserService.register(user);
        return new ResultBean<>();
    }

    /**
     * 参数校验
     * @param str
     * @param type
     * @return
     */
    @PostMapping("/check_valid")
    public ResultBean<Boolean> checkValid(String str,String type){
        Boolean isCheckValid = iUserService.checkExistValid(str,type);
        return new ResultBean<>(isCheckValid);
    }

    /**
     * 获取用户信息
     * @param session
     * @return
     */
    @PostMapping("/get_user_info")
    public ResultBean<User> getUserInfo(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user != null){
            return new ResultBean<>(user);
        }else{
            throw CommonExceptions.UserCommonException.USER_NOT_LOGIN.getCommonException();
        }
    }

    /**
     * 忘记密码
     * @param username
     * @return
     */
    @PostMapping("/forget_get_question")
    public ResultBean<String> forgetGetQuestion(String username){
        String question = iUserService.selectQuestion(username);
        return new ResultBean<>(question);
    }

    /**
     * 问题校验
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @PostMapping("/forget_check_answer")
    public ResultBean<String> forgetCheckAnswer(String username,String question,String answer){
        String forgetToken = iUserService.checkAnswer(username,question,answer);
        return new ResultBean<>(forgetToken);
    }

    /**
     * 忘记密码重置
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @PostMapping("/forget_rest_password")
    public ResultBean<String> forgetRestPassword(String username,String passwordNew,String forgetToken){
        iUserService.forgetResetPassword(username, passwordNew, forgetToken);
        return new ResultBean<>();
    }

    /**
     * 重置密码
     * @param session
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @PostMapping("/reset_password")
    public ResultBean<String> resetPassword(HttpSession session,String passwordOld,String passwordNew){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            throw CommonExceptions.UserCommonException.USER_NOT_LOGIN.getCommonException();
        }
        iUserService.resetPassword(passwordOld,passwordNew,user);
        return new ResultBean<>();
    }

    /**
     * 更新用户信息
     * @param session
     * @param user
     * @return
     */
    @PostMapping("/update_information")
    public ResultBean<User> update_information(HttpSession session,User user){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            throw CommonExceptions.UserCommonException.USER_NOT_LOGIN.getCommonException();
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        iUserService.updateInformation(user);
        session.setAttribute(Const.CURRENT_USER,user);
        return new ResultBean<>();
    }

    /**
     * 得到用户信息
     * @param session
     * @return
     */
    @PostMapping("/get_information")
    public ResultBean<User> get_information(HttpSession session){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            throw CommonExceptions.UserCommonException.USER_NOT_LOGIN.getCommonException();
        }
        return new  ResultBean<>(iUserService.getInformation(currentUser.getId()));
    }

}
