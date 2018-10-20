package com.mmall.common.response;


import com.google.gson.GsonBuilder;
import com.mmall.common.exception.CommonException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

/**
 *  该类为aop切面类，主要是对所有返回值为resultbean的方法进行入参监控，以及异常处理
 * @author 杜洪明
 * @creation 2018年10月10日
 */
@Aspect
@Component
public class ControllerAOP {
	private static final Logger logger = LoggerFactory.getLogger(ControllerAOP.class);

	@Pointcut("execution(public com.mmall.common.response.ResultBean *(..))")
	public void webLog() {
	}


	/**
	 * 对返回值为resultbean的方法进行切面，获取其入参
	 * @param pjp
	 * @return
	 */
	@Around("webLog()")
	public Object handlerControllerMethod(ProceedingJoinPoint pjp) {

		long startTime = System.currentTimeMillis();
		ResultBean<?> result;
		try {
			result = (ResultBean<?>) pjp.proceed();
			logger.info(pjp.getSignature() + "use time:" + (System.currentTimeMillis() - startTime));
			// 接收到请求，记录请求内容
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = attributes.getRequest();
			// 记录下请求内容
			logger.info("URL : " + request.getRequestURL().toString());
			logger.info("HTTP_METHOD : " + request.getMethod());
			logger.info("IP : " + request.getRemoteAddr());
			logger.info("CLASS_METHOD : " + pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName());
			logger.info("ARGS : " + Arrays.toString(pjp.getArgs()));
		} catch (Throwable e) {
			result = handlerException(pjp, e);
		}
		return result;

	}
    

	/**
	 * 对切面方法进行返回值处理
	 * @param ret
	 * @throws Throwable
	 */
	@AfterReturning(returning = "ret", pointcut = "webLog()")
	public void doAfterReturning(Object ret) throws Throwable {
		// 处理完请求，返回内容
		logger.info("RESPONSE : " + new GsonBuilder().create().toJson(ret));
	}

	@SuppressWarnings("rawtypes")
	/**
	 * 对切面方法进行异常捕获处理
	 * @param pjp
	 * @param e
	 * @return
	 */
	private ResultBean<?> handlerException(ProceedingJoinPoint pjp, Throwable e) {

		ResultBean<?> result = new ResultBean();
		// 已知异常
		if(e instanceof IllegalStateException) {
			result.setMsg(e.getLocalizedMessage()+"非法文件格式");
			result.setCode(ResultBean.FAIL);
		} else if (e instanceof IOException) {
			result.setMsg(e.getLocalizedMessage()+"找不到文件夹");
			result.setCode(ResultBean.FAIL);
		} else if(e instanceof CommonException){
			logger.error(pjp.getSignature() + " exception ", e);
			CommonException commonException = (CommonException)e;
			result.setCode(commonException.getErrorCode());
			result.setMsg(commonException.getErrorInfo());
			// 未知异常是应该重点关注的，这里可以做其他操作，如通知邮件，单独写到某个文件等等。
		}
		result.setMsg(e.getLocalizedMessage());
		result.setCode(ResultBean.FAIL);
		return result;
	}

}