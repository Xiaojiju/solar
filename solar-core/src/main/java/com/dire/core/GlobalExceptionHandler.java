package com.dire.core;

import com.dire.core.context.exceptions.ServiceException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler({Exception.class})
//    public HttpResult exception(Exception e) {
//        e.printStackTrace();
//        return HttpResult.error(e.getMessage());
//    }
//
//    @ExceptionHandler({ServiceException.class})
//    public HttpResult serviceException(ServiceException e) {
//        return HttpResult.error(e.getMessage(), e.getCode());
//    }
//
//    @ExceptionHandler({BindException.class })
//    public HttpResult bindException(BindException e) {
//        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
//        return parseError(fieldErrors);
//    }
//
//    @ExceptionHandler({MethodArgumentNotValidException.class })
//    public HttpResult paramsException(MethodArgumentNotValidException e) {
//        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
//        return parseError(fieldErrors);
//    }
//
//    @ExceptionHandler({HttpMessageNotReadableException.class })
//    public HttpResult httpMessageNotReadableException(HttpMessageNotReadableException e) {
//        return HttpResult.error(e.getMessage());
//    }
//
//    @ExceptionHandler({ MissingServletRequestParameterException.class })
//    public HttpResult paramsMissingException(MissingServletRequestParameterException missingException) {
//        return HttpResult.error("参数 " + missingException.getParameterName() + " 不能为空");
//    }
//
//    private HttpResult parseError(List<FieldError> fieldErrors) {
//        StringBuilder msg = new StringBuilder();
//        for (FieldError error : fieldErrors) {
//            msg.append(error.getDefaultMessage()).append(";");
//        }
//        return HttpResult.error(msg.toString());
//    }
}
