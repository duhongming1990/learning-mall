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

## 3. jmeter压测

    1.在windows上录好jmx
    2.在linux上执行命令：sh jmeter.sh -n -t XXX.jmx -l result.jtl
    3.把result.jtl导入到jmeter
    
    
## 4. Redis的key集中管理
```java
package com.dhm.seckillplus.common.prefix;

/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/11/1 11:24
 */
public class KeyPrefixs {
    private static final String USER = "user-center:user:";
    private static final String SECKILL_USER = "user-center:seckill-user:";
    private static final String GOODS = "goods-center:goods:";
    private static final int TOKEN_EXPIRE = 3600 * 24 * 2;

    public enum UserKey {
        ID(new BasePrefix("id")),
        NAME(new BasePrefix("name")),
        TOKEN(new BasePrefix(TOKEN_EXPIRE, "token"));

        private BasePrefix basePrefix;

        UserKey(BasePrefix basePrefix) {
            basePrefix.setPrefix(USER + basePrefix.getPrefix());
            this.basePrefix = basePrefix;
        }

        public BasePrefix getBasePrefix() {
            return basePrefix;
        }

        public void setBasePrefix(BasePrefix basePrefix) {
            this.basePrefix = basePrefix;
        }
    }

    public enum SeckillUserKey {
        TOKEN(new BasePrefix(TOKEN_EXPIRE, "token")),
        ID(new BasePrefix("id"));

        private BasePrefix basePrefix;

        SeckillUserKey(BasePrefix basePrefix) {
            basePrefix.setPrefix(SECKILL_USER + basePrefix.getPrefix());
            this.basePrefix = basePrefix;
        }

        public BasePrefix getBasePrefix() {
            return basePrefix;
        }

        public void setBasePrefix(BasePrefix basePrefix) {
            this.basePrefix = basePrefix;
        }
    }


    public enum GoodsKey {
        GOODS_LIST(new BasePrefix(60, "goods-list")),
        GOODS_DETIAL(new BasePrefix(60, "goods-detial")),
        SECKILL_GOODS_STOCK(new BasePrefix("seckill-goods-stock"));

        private BasePrefix basePrefix;

        GoodsKey(BasePrefix basePrefix) {
            basePrefix.setPrefix(GOODS + basePrefix.getPrefix());
            this.basePrefix = basePrefix;
        }

        public BasePrefix getBasePrefix() {
            return basePrefix;
        }

        public void setBasePrefix(BasePrefix basePrefix) {
            this.basePrefix = basePrefix;
        }
    }

}
```

#5. 缓存
## 5.1 页面缓存
```java
@RequestMapping(value = "/to_list",produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model,
                       User user) {
        //取缓存
        String html = (String) redisDao.get(KeyPrefixs.GoodsKey.GOODS_LIST.getBasePrefix(),"",String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        
        SpringWebContext ctx = new SpringWebContext(request,response,
                request.getServletContext(),request.getLocale(), model.asMap(), applicationContext);
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if(!StringUtils.isEmpty(html)) {
            redisDao.set(KeyPrefixs.GoodsKey.GOODS_LIST.getBasePrefix(),"",html);
        }
        return html;
    }
```
## 5.2 对象缓存
## 5.3 静态资源优化
    火狐浏览器：静态化
    304 Not Modified：304 未改变说明无需再次传输请求的内容，也就是说可以使用缓存的内容。
    script：from memory cache
    stylesheet：from disk cache
    webpack:https://www.webpackjs.com/
    tengine:http://tengine.taobao.org/