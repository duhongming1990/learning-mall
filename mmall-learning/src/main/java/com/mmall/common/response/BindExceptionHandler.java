package com.mmall.common.response;

import com.mmall.common.exception.CommonException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ControllerAdvice
@ResponseBody
public class BindExceptionHandler {
	@ExceptionHandler(value={BindException.class,CommonException.class})
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
		}else if(e instanceof CommonException){
			CommonException commonException = (CommonException)e;
			result.setCode(commonException.getErrorCode());
			result.setMsg(commonException.getMessage());
		}
        return result;
	}
}
