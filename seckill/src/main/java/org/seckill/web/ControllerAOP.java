package org.seckill.web;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.seckill.dto.ResultBean;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class ControllerAOP {

	private static final Logger logger = LoggerFactory.getLogger(ControllerAOP.class);

	@Pointcut("execution(public org.seckill.dto.ResultBean *(..))")
    public void webLog(){}
	
	@Around("webLog()")
	public Object handlerControllerMethod(ProceedingJoinPoint pjp) {
		
		long startTime = System.currentTimeMillis();
		ResultBean<?> result;
		try {
			result = (ResultBean<?>) pjp.proceed();
			logger.info(pjp.getSignature() + "use time:" + (System.currentTimeMillis() - startTime));
			// 接收到请求，记录请求内容
	        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
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

	@AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.info("RESPONSE : " + ret);
    }
	
	@SuppressWarnings("rawtypes")
	private ResultBean<?> handlerException(ProceedingJoinPoint pjp, Throwable e) {

		ResultBean<?> result = new ResultBean();
		// 已知异常
		if(e instanceof SeckillCloseException) {
			logger.error(pjp.getSignature() + " error ", e);
			result.setMsg("秒杀关闭");
			result.setCode(ResultBean.FAIL);
		} else if(e instanceof RepeatKillException){
			logger.error(pjp.getSignature() + " error ", e);
			result.setMsg("重复秒杀");
			result.setCode(ResultBean.FAIL);
		} else if (e instanceof SeckillException) {
			logger.error(pjp.getSignature() + " error ", e);
			result.setMsg(e.getLocalizedMessage());
			result.setCode(ResultBean.FAIL);
		}
		return result;
	}

}
