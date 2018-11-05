<%@ page import="com.alibaba.fastjson.JSON"%><%@ page import="com.mmall.common.exception.CommonException"%><%@ page import="com.mmall.common.response.ResultBean"%><%@page contentType="application/json;charset=UTF-8" isErrorPage="true"%>
<%
ResultBean resultBean = new ResultBean();
// 获取异常类
Throwable ex = null;
if (request.getAttribute("exception") != null) {
	ex = (Throwable) request.getAttribute("exception");
} else if (request.getAttribute("javax.servlet.error.exception") != null) {
	ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
	CommonException commonException = (CommonException)ex;
	resultBean.setCode(commonException.getErrorCode());
	resultBean.setMsg(commonException.getErrorInfo());
}
%>
<%=ex==null?"未知错误.": JSON.toJSONString(resultBean) %>
