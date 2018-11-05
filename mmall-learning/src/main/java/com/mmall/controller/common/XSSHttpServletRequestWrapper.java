package com.mmall.controller.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/10/23 17:23
 * 防止XSS跨站脚本漏洞
 */
@Slf4j
public class XSSHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XSSHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        String escapeHtml4 = StringEscapeUtils.escapeHtml4(super.getHeader(name));
        log.info("getHeader>>>escapeHtml4:{}",escapeHtml4);
        String cleanXSS = cleanXSS(escapeHtml4);
        log.info("getHeader>>>cleanXSS:{}",cleanXSS);
        return cleanXSS;
    }

    @Override
    public String getQueryString() {
        String escapeHtml4 = StringEscapeUtils.escapeHtml4(super.getQueryString());
        log.info("getQueryString>>>escapeHtml4:{}",escapeHtml4);
        String cleanXSS = cleanXSS(escapeHtml4);
        log.info("getQueryString>>>cleanXSS:{}",cleanXSS);
        return cleanXSS;
    }

    @Override
    public String getParameter(String name) {
        String escapeHtml4 = StringEscapeUtils.escapeHtml4(super.getParameter(name));
        log.info("getParameter>>>escapeHtml4:{}",escapeHtml4);
        String cleanXSS = cleanXSS(escapeHtml4);
        log.info("getParameter>>>cleanXSS:{}",cleanXSS);
        return cleanXSS;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapseValues = new String[length];
            for (int i = 0; i < length; i++) {
                String v = values[i];
                String escapeHtml4 = StringEscapeUtils.escapeHtml4(v);
                log.info("getParameter>>>escapeHtml4:{}",escapeHtml4);
                String cleanXSS = cleanXSS(escapeHtml4);
                log.info("getParameter>>>cleanXSS:{}",cleanXSS);
                escapseValues[i] = cleanXSS;
            }
            return escapseValues;
        }
        return super.getParameterValues(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMap = super.getParameterMap();
        if (parameterMap != null) {
            Map<String, String[]> escapseParameterMap = new HashMap<>();
            for (String key : parameterMap.keySet()) {
                String[] parameterValues = getParameterValues(key);

                int length = parameterValues.length;
                String[] escapseValues = new String[length];
                if (parameterValues != null) {
                    for (int i = 0; i < length; i++) {
                        String v = parameterValues[i];
                        String escapeHtml4 = StringEscapeUtils.escapeHtml4(v);
                        log.info("getParameter>>>escapeHtml4:{}",escapeHtml4);
                        String cleanXSS = cleanXSS(escapeHtml4);
                        log.info("getParameter>>>cleanXSS:{}",cleanXSS);
                        escapseValues[i] = cleanXSS;
                    }
                }

                escapseParameterMap.put(key, escapseValues);
            }
            return escapseParameterMap;
        }
        return super.getParameterMap();
    }

    private static String cleanXSS(String value) {
        if(StringUtils.isBlank(value)){
            return value;
        }
        // 避免空字符串
//        value = value.replaceAll(" ", "");

        // 避免script 标签
        Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
        value = scriptPattern.matcher(value).replaceAll("");

        // 避免src形式的表达式
        scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");

        // 删除单个的 </script> 标签
        scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
        value = scriptPattern.matcher(value).replaceAll("");

        // 删除单个的<script ...> 标签
        scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");

        // 避免 eval(...) 形式表达式
        scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");
        // 避免 e­xpression(...) 表达式
        scriptPattern = Pattern.compile("e­xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");

        // 避免 javascript: 表达式
        scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
        value = scriptPattern.matcher(value).replaceAll("");

        // 避免 vbscript:表达式
        scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
        value = scriptPattern.matcher(value).replaceAll("");

        /** 避免
         * onabort 图像加载被中断
         * onblur  元素失去焦点
         * onchange    用户改变域的内容
         * onclick 鼠标点击某个对象
         * ondblclick  鼠标双击某个对象
         * onerror 当加载文档或图像时发生某个错误
         * onfocus 元素获得焦点3
         * onkeydown   某个键盘的键被按下
         * onkeypress  某个键盘的键被按下或按住
         * onkeyup 某个键盘的键被松开
         * onload  某个页面或图像被完成加载
         * onmousedown 某个鼠标按键被按下
         * onmousemove 鼠标被移动
         * onmouseout  鼠标从某元素移开
         * onmouseover 鼠标被移到某元素之上
         * onmouseup   某个鼠标按键被松开
         * onreset 重置按钮被点击
         * onresize    窗口或框架被调整尺寸
         * onselect    文本被选定
         * onsubmit    提交按钮被点击
         * onunload    用户退出页面
         */
        scriptPattern = Pattern.compile("(onabort|onblur|onchange|onclick|ondblclick|onerror|onfocus|onkeydown|onkeypress|onkeyup|onload|onmousedown|onmousemove|onmouseout|onmouseover|onmouseup|onreset|onresize|onselect|onsubmit|onunload)(.*?)=",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");

        return value;
    }

    public static void main(String[] args) {
        String escapseStr = cleanXSS("http://\">< img src=# onerror=confirm(/xsss/)>111");
        System.out.println("escapseStr = " + escapseStr);
    }
}