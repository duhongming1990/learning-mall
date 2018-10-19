# 单点登录

## 1.单Tomcat应用

    CookieName：JSESSIONID
    存储在Tomcat应用服务器内

## 2.集群Tomcat应用，多个Tomcat共享session问题

    将sessionId放在URL当参数传递或者放入本地cookie中。eg：http://192.168.100.246:8080/system/system_manage/org_manage.action?token=o::02BF6CB44A0B4D1886B5EE9DF3D1640F
    将session信息存入分布式Redis中。

## 2.1 自定义Cookie具体实现
```java
@Slf4j
public class CookieUtil {

    private final static String COOKIE_DOMAIN = ".happymmall.com";
    private final static String COOKIE_NAME = "mmall_login_token";


    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cks = request.getCookies();
        if(cks != null){
            for(Cookie ck : cks){
                log.info("read cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                if(StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    log.info("return cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                    return ck.getValue();
                }
            }
        }
        return null;
    }

    //X:domain=".happymmall.com"
    //a:A.happymmall.com            cookie:domain=A.happymmall.com;path="/"
    //b:B.happymmall.com            cookie:domain=B.happymmall.com;path="/"
    //c:A.happymmall.com/test/cc    cookie:domain=A.happymmall.com;path="/test/cc"
    //d:A.happymmall.com/test/dd    cookie:domain=A.happymmall.com;path="/test/dd"
    //e:A.happymmall.com/test       cookie:domain=A.happymmall.com;path="/test"

    public static void writeLoginToken(HttpServletResponse response,String token){
        Cookie ck = new Cookie(COOKIE_NAME,token);
        ck.setDomain(COOKIE_DOMAIN);
        ck.setPath("/");//代表设置在根目录
        ck.setHttpOnly(true);
        //单位是秒。
        //如果这个maxage不设置的话，cookie就不会写入硬盘，而是写在内存。只在当前页面有效。
        ck.setMaxAge(60 * 60 * 24 * 365);//如果是-1，代表永久
        log.info("write cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
        response.addCookie(ck);
    }


    public static void delLoginToken(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cks = request.getCookies();
        if(cks != null){
            for(Cookie ck : cks){
                if(StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    ck.setDomain(COOKIE_DOMAIN);
                    ck.setPath("/");
                    ck.setMaxAge(0);//设置成0，代表删除此cookie。
                    log.info("del cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                    response.addCookie(ck);
                    return;
                }
            }
        }
    }
    
    
}
```
## 2.2 UserController具体使用
```java
//login:
CookieUtil.writeLoginToken(httpServletResponse,session.getId());
redisPoolUtil.setEx(session.getId(), JsonUtil.obj2String(user),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
        
//logout:
CookieUtil.delLoginToken(request,response);
redisPoolUtil.del(session.getId());

//getUserInfo:
String loginToken = CookieUtil.readLoginToken(request);
String userJsonStr = redisPoolUtil.get(loginToken);
User user = JsonUtil.string2Obj(userJsonStr,User.class);
```

## 2.3 重置session时间的filter
```xml
    <!-- 二期新增重置session时间的filter -->
    <filter>
        <filter-name>sessionExpireFilter</filter-name>
        <filter-class>com.mmall.controller.common.SessionExpireFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>sessionExpireFilter</filter-name>
        <url-pattern>*.do</url-pattern>
    </filter-mapping>
```
```java
public class SessionExpireFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        RedisPoolUtil redisPoolUtil = SpringContextHolder.getBean(RedisPoolUtil.class);
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String loginToken = CookieUtil.readLoginToken(httpServletRequest);

        if (StringUtils.isNotEmpty(loginToken)) {
            //判断logintoken是否为空或者""；
            //如果不为空的话，符合条件，继续拿user信息

            String userJsonStr = redisPoolUtil.get(loginToken);
            User user = JsonUtil.string2Obj(userJsonStr, User.class);
            if (user != null) {
                //如果user不为空，则重置session的时间，即调用expire命令
                redisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }


    @Override
    public void destroy() {

    }
}
```