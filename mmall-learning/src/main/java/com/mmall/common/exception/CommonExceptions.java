package com.mmall.common.exception;

/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/8/31 15:45
 */
public class CommonExceptions {

    public enum UserCommonException{

        USER_NAME_NOT_EXIST(new CommonException(1000,"用户名不存在！")),
        USER_NAME_EXISTED(new CommonException(1001,"用户名已存在！")),
        EMAIl_NOT_EXIST(new CommonException(1002,"邮箱不存在！")),
        EMAIl_EXISTED(new CommonException(1003,"邮箱已存在！")),
        QUESTION_BLANK(new CommonException(1004,"找回密码的问题是空的！")),
        NOT_FOUND_CURRENT_USR(new CommonException(1005,"找不到当前用户！")),


        PASSWORD_ERROR(new CommonException(1100,"密码错误！")),
        OLD_PASSWORD_ERROR(new CommonException(1100,"旧密码错误！")),
        PARAMETER_ERROR(new CommonException(1101,"参数错误！")),
        PARAMETER_TOKEN_ERROR(new CommonException(1102,"参数错误,token需要传递！")),
        TOKEN_INVALID_EXPIRED(new CommonException(1103,"token无效或者过期")),
        TOKEN_ERROR(new CommonException(1104,"token错误,请重新获取重置密码的token")),
        ANSWER_ERROR(new CommonException(1005,"问题的答案错误！")),


        USER_NOT_LOGIN(new CommonException(1201,"用户未登录！")),
        USER_NOT_ADMIN(new CommonException(1202," 不是管理员,无法登录！"));



        private CommonException commonException;

        UserCommonException(CommonException commonException){
            this.commonException = commonException;
        }

        public CommonException getCommonException() {
            return commonException;
        }

        public void setCommonException(CommonException commonException) {
            this.commonException = commonException;
        }

        public static void show(){
            for(UserCommonException u : UserCommonException.values()){
                System.out.println(u + "： UserCommonException =" + u.getCommonException());
            }
        }
    }

}