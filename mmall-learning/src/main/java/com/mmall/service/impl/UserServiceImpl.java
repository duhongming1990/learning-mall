package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.TokenCache;
import com.mmall.common.exception.CommonExceptions;
import com.mmall.dao.UserMapper;
import com.mmall.bean.pojo.User;
import com.mmall.service.IUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("iUserservice")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String username, String password) {
        //用户名是否存在
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            throw CommonExceptions.UserCommonException.USER_NAME_NOT_EXIST.getCommonException();
        }
        //密码是否错误
        User user = userMapper.selectLogin(username, DigestUtils.md5Hex(password));
        if (user == null) {
            throw CommonExceptions.UserCommonException.PASSWORD_ERROR.getCommonException();
        }
        //StringUtils.EMPTY：设置空字符串
        user.setPassword(StringUtils.EMPTY);
        return user;
    }

    @Override
    public Integer register(User user) {

        checkExistValid(user.getUsername(), Const.USERNAME);
        checkExistValid(user.getEmail(), Const.EMAIL);

        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        int resultCount = userMapper.insert(user);

        return resultCount;
    }

    @Override
    public Boolean checkExistValid(String str, String type) {
        Boolean isCheckExistValid = true;
        if (StringUtils.isNotBlank(str)) {
            //用户名是否存在,存在抛异常
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    throw CommonExceptions.UserCommonException.USER_NAME_EXISTED.getCommonException();
                } else {
                    isCheckExistValid = false;
                }
            }
            //邮箱是否存在,存在抛异常
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    throw CommonExceptions.UserCommonException.EMAIl_EXISTED.getCommonException();
                } else {
                    isCheckExistValid = false;
                }
            }
        } else {
            throw CommonExceptions.UserCommonException.PARAMETER_ERROR.getCommonException();
        }
        return isCheckExistValid;
    }

    @Override
    public Boolean CheckNotExistValid(String str, String type) {
        Boolean isCheckNotExistValid = true;
        if (StringUtils.isNotBlank(str)) {
            //用户名是否已存在,不存在抛异常
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount <= 0) {
                    throw CommonExceptions.UserCommonException.USER_NAME_NOT_EXIST.getCommonException();
                } else {
                    isCheckNotExistValid = false;
                }
            }
            //邮箱是否已存在,不存在抛异常
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount <= 0) {
                    throw CommonExceptions.UserCommonException.EMAIl_NOT_EXIST.getCommonException();
                } else {
                    isCheckNotExistValid = false;
                }
            }
        } else {
            throw CommonExceptions.UserCommonException.PARAMETER_ERROR.getCommonException();
        }
        return isCheckNotExistValid;
    }

    @Override
    public String selectQuestion(String username) {
        CheckNotExistValid(username, Const.USERNAME);

        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return question;
        } else {
            throw CommonExceptions.UserCommonException.QUESTION_BLANK.getCommonException();
        }
    }

    @Override
    public String checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return forgetToken;
        } else {
            throw CommonExceptions.UserCommonException.ANSWER_ERROR.getCommonException();
        }
    }

    @Override
    public Integer forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            throw CommonExceptions.UserCommonException.PARAMETER_TOKEN_ERROR.getCommonException();
        }
        CheckNotExistValid(username, Const.USERNAME);

        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            throw CommonExceptions.UserCommonException.TOKEN_INVALID_EXPIRED.getCommonException();
        }

        if (StringUtils.equals(forgetToken, token)) {
            int rowCount = userMapper.updatePasswordByUsername(username, DigestUtils.md5Hex(passwordNew));
            return rowCount;
        } else {
            throw CommonExceptions.UserCommonException.TOKEN_ERROR.getCommonException();
        }
    }

    @Override
    public Integer resetPassword(String passwordOld, String passwordNew, User user) {
        //防止横向越权,要校验一下这个用户的旧密码,一定要指定是这个用户.因为我们会查询一个count(1),如果不指定id,那么结果就是true啦count>0;
        int resultCount = userMapper.checkPassword(DigestUtils.md5Hex(passwordOld), user.getId());
        if (resultCount == 0) {
            throw CommonExceptions.UserCommonException.OLD_PASSWORD_ERROR.getCommonException();
        }

        user.setPassword(DigestUtils.md5Hex(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        return updateCount;
    }

    @Override
    public Integer updateInformation(User user) {
        //username是不能被更新的
        //email也要进行一个校验,校验新的email是不是已经存在,并且存在的email如果相同的话,不能是我们当前的这个用户的.
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            throw CommonExceptions.UserCommonException.EMAIl_EXISTED.getCommonException();
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        return updateCount;
    }

    @Override
    public User getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            throw CommonExceptions.UserCommonException.NOT_FOUND_CURRENT_USR.getCommonException();
        }
        user.setPassword(StringUtils.EMPTY);
        return user;

    }


    //backend

    /**
     * 校验是否是管理员
     *
     * @param user
     * @return
     */
    @Override
    public void checkAdminRole(User user) {
        if (user == null || user.getRole().intValue() != Const.Role.ROLE_ADMIN) {
            throw CommonExceptions.UserCommonException.USER_NOT_ADMIN.getCommonException();
        }
    }


}
