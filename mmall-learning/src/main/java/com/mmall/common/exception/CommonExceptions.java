package com.mmall.common.exception;

/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/8/31 15:45
 */
public class CommonExceptions {
    public enum SysCommonException{

        PARAMETER_NOT_ILLEGAL(new CommonException(-9999, "传入参数非法！"));
        private CommonException commonException;

        SysCommonException(CommonException commonException){
            this.commonException = commonException;
        }

        public CommonException getCommonException() {
            return commonException;
        }

        public void setCommonException(CommonException commonException) {
            this.commonException = commonException;
        }

        public static void show(){
            for(SysCommonException s : SysCommonException.values()){
                System.out.println(s + "： SysCommonException =" + s.getCommonException());
            }
        }
    }

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
        USER_NOT_ADMIN(new CommonException(1202,"您不是管理员,无权限操作,需要管理员权限！"));



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

    public enum CategoryCommonException{

        CATEGORY_PARAMETER_REEOR(new CommonException(1000,"添加类别参数错误！"));


        private CommonException commonException;

        CategoryCommonException(CommonException commonException){
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
                System.out.println(u + "： CategoryCommonException =" + u.getCommonException());
            }
        }
    }

    public enum ProductCommonException{

        PRODUCT_UNSALE_DELETE(new CommonException(1000,"产品已下架或者删除！")),
        PRODUCT_NOT_FOUND(new CommonException(1001,"产品未找到！"));


        private CommonException commonException;

        ProductCommonException(CommonException commonException){
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
                System.out.println(u + "： ProductCommonException =" + u.getCommonException());
            }
        }
    }

}