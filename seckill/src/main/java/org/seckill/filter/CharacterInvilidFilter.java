package org.seckill.filter;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import java.io.IOException;
import java.util.Enumeration;

/*
 * CharacterInvilidFilter：过滤request请求中的非法字符，防止脚本攻击
 * CharacterInvilidFilter继承了Spring框架的CharacterEncodingFilter过滤器，
 * 当然，我们也可以自己实现这样一个过滤器
 */
public class CharacterInvilidFilter implements Filter{

    public void destroy() {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("CharacterInvilidFilter启动中......");
        String parameterName = null;
        String parameterValue = null;
        // 获取请求的参数
        @SuppressWarnings("unchecked")
        Enumeration<String> allParameter = request.getParameterNames();
        while(allParameter.hasMoreElements()){
            parameterName = allParameter.nextElement();
            parameterValue = request.getParameter(parameterName);
            if(null != parameterValue){
                for(String str : invalidCharacter){
                    if (StringUtils.containsIgnoreCase(parameterValue, str)){
                        request.setAttribute("errorMessage", "非法字符：" + str);
                        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/error.jsp");
                        requestDispatcher.forward(request, response);
                        return;
                    }
                }
            }
        }
        filterChain.doFilter(request, response); // 执行目标资源，放行
        System.out.println("CharacterInvilidFilter结束，执行目标资源，放行！");
    }

    public void init(FilterConfig filterConfig) throws ServletException {

    }
    // 需要过滤的非法字符
    private static String[] invalidCharacter = new String[]{
            "script","select","insert","document","window","function",
            "delete","update","prompt","alert","create","alter",
            "drop","iframe","link","where","replace","function","onabort",
            "onactivate","onafterprint","onafterupdate","onbeforeactivate",
            "onbeforecopy","onbeforecut","onbeforedeactivateonfocus",
            "onkeydown","onkeypress","onkeyup","onload",
            "expression","applet","layer","ilayeditfocus","onbeforepaste",
            "onbeforeprint","onbeforeunload","onbeforeupdate",
            "onblur","onbounce","oncellchange","oncontextmenu",
            "oncontrolselect","oncopy","oncut","ondataavailable",
            "ondatasetchanged","ondatasetcomplete","ondeactivate",
            "ondrag","ondrop","onerror","onfilterchange","onfinish","onhelp",
            "onlayoutcomplete","onlosecapture","onmouse","ote",
            "onpropertychange","onreadystatechange","onreset","onresize",
            "onresizeend","onresizestart","onrow","onscroll",
            "onselect","onstaronsubmit","onunload","IMgsrc","infarction"
    };

}
