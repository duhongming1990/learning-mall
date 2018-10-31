# 1.使用JSR303数据校验
## 1.1 引入依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```
## 1.2 在Controller方法中：@Valid
```java
@RequestMapping("/do_login")
@ResponseBody
public ResultBean<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
    //登录
    String token = userService.login(response, loginVo);
    return new ResultBean<>(token);
}
```

## 1.3 LoginVo定义注解、使用注解
```java
@Data
public class LoginVo {
	
	@NotNull
	@IsMobile
	private String mobile;
	
	@NotNull
	@Length(min=32)
	private String password;
}
```

## 1.4 validator自定义注解实现
```java
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {IsMobileValidator.class })
public @interface  IsMobile {
	
	boolean required() default true;
	
	String message() default "手机号码格式错误";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

	private boolean required = false;
	
	public void initialize(IsMobile constraintAnnotation) {
		required = constraintAnnotation.required();
	}

	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(required) {
			return isMobile(value);
		}
		return true;
	}

	private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");

	private static boolean isMobile(String src) {
		if(StringUtils.isEmpty(src)) {
			return false;
		}
		Matcher m = mobile_pattern.matcher(src);
		return m.matches();
	}

}
```

1.5 绑定异常绑定ResultBean统一返回
```java
@ControllerAdvice
@ResponseBody
public class BindExceptionHandler {
	@ExceptionHandler(value=Exception.class)
	public ResultBean<String> exceptionHandler(Exception e){
        e.printStackTrace();
	    ResultBean result = new ResultBean();
        result.setCode(ResultBean.FAIL);
		if(e instanceof BindException) {
			BindException ex = (BindException)e;
			List<ObjectError> errors = ex.getAllErrors();
			ObjectError error = errors.get(0);
			String msg = error.getDefaultMessage();
            result.setMsg(msg);
		}
        return result;
	}
}
```

2 全局异常码表
```java
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
```