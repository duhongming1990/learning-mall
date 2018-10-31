package com.dhm.seckillplus.common.exception;

/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/8/31 15:45
 */
public class CommonExceptions {

    //通用的错误码
    public enum GlobalCommonException{

        SERVER_ERROR(new CommonException(500100,"服务端异常")),
        BIND_ERROR(new CommonException(500101,"参数校验异常：%s"));
        private CommonException commonException;

        GlobalCommonException(CommonException commonException){
            this.commonException = commonException;
        }

        public CommonException getCommonException() {
            return commonException;
        }

        public void setCommonException(CommonException commonException) {
            this.commonException = commonException;
        }

        public static void show(){
            for(GlobalCommonException g : GlobalCommonException.values()){
                System.out.println(g + "： GlobalCommonException =" + g.getCommonException());
            }
        }
    }

    //登录模块 5002XX
    public enum UserCommonException{

        SESSION_ERROR(new CommonException(500210,"Session不存在或者已经失效")),
        PASSWORD_EMPTY(new CommonException(500211,"登录密码不能为空")),
        MOBILE_EMPTY(new CommonException(500212,"手机号不能为空")),
        MOBILE_ERROR(new CommonException(500213,"手机号格式错误")),
        MOBILE_NOT_EXIST(new CommonException(500214,"手机号不存在")),
        PASSWORD_ERROR(new CommonException(500215,"密码错误"));


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

    //商品模块 5003XX

    //订单模块 5004XX

    //秒杀模块 5005XX
    public enum SeckillCommonException{
        SECKILL_OVER(new CommonException(500500,"商品已经秒杀完毕")),
        REPEATE_SECKILL(new CommonException(500501,"不能重复秒杀"));


        private CommonException commonException;

        SeckillCommonException(CommonException commonException){
            this.commonException = commonException;
        }

        public CommonException getCommonException() {
            return commonException;
        }

        public void setCommonException(CommonException commonException) {
            this.commonException = commonException;
        }

        public static void show(){
            for(SeckillCommonException s : SeckillCommonException.values()){
                System.out.println(s + "： UserCommonException =" + s.getCommonException());
            }
        }

//        public CodeMsg fillArgs(Object... args) {
//            int code = this.code;
//            String message = String.format(this.msg, args);
//            return new CodeMsg(code, message);
//        }
    }

}