package com.mmall.controller.common;

import com.alibaba.fastjson.JSON;
import com.mmall.common.exception.CommonException;
import com.mmall.common.exception.CommonExceptions;
import com.mmall.common.response.ResultBean;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/11/2 10:24
 * 防止SQL注入
 */
@Slf4j
public class AntiSqlInjectionfilter implements Filter {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        // TODO Auto-generated method stub
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {
        log.info("AntiSqlInjectionfilter start");
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        //获得所有请求参数名
        Enumeration params = request.getParameterNames();
        while (params.hasMoreElements()) {
            //得到参数名
            String name = params.nextElement().toString();
            //得到参数对应值
            String[] value = request.getParameterValues(name);
            for (int i = 0; i < value.length; i++) {
                if (!isValid(value[i])) {
                    throw CommonExceptions.SysCommonException.PARAMETER_NOT_ILLEGAL.getCommonException();
                }
            }
        }
        chain.doFilter(request, response);
        log.info("AntiSqlInjectionfilter end");
    }

    //效验
//    protected static boolean sqlValidate(String str) {
//        str = str.toLowerCase();//统一转为小写
//        String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|" +
//                "char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|" +
//                "table|from|grant|use|group_concat|column_name|" +
//                "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|*|" +
//                "chr|mid|master|truncate|char|declare|or|;|-|--|+|,|like|//|/|%|#";//过滤掉的sql关键字，可以手动添加
//        String[] badStrs = badStr.split("\\|");
//        for (int i = 0; i < badStrs.length; i++) {
//            if (str.indexOf(badStrs[i]) >= 0) {
//                return true;
//            }
//        }
//        return false;
//    }

    /**正则表达式**/
    private static String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
            + "(\\b(select|update|union|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";

    private static Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);

    private static boolean isValid(String str)
    {
        if (sqlPattern.matcher(str).find())
        {
            System.out.println("str = " + str);
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Boolean isValid = isValid("2018-11-01' and SLEEP(5) and 'hhvC'='hhvC ");
        System.out.println("isValid = " + isValid);
    }

}
