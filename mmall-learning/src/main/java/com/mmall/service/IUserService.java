package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/10/10 13:51
 */
public interface IUserService {

    User login(String username, String password);

    Integer register(User user);

    Boolean checkExistValid(String str, String type);

    Boolean CheckNotExistValid(String str, String type);

    String selectQuestion(String username);

    String checkAnswer(String username, String question, String answer);

    Integer forgetResetPassword(String username, String passwordNew, String forgetToken);

    Integer resetPassword(String passwordOld, String passwordNew, User user);

    Integer updateInformation(User user);

    User getInformation(Integer id);

    void checkAdminRole(User user);
}