package com.suse.hwj.springboot.exception;

import com.suse.hwj.springboot.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice("com.suse.hwj.springboot.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody //返回json串
    public Result error(Exception e) {
        return Result.error();
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody //返回json串
    public Result error(CustomException e) {
        return Result.error(e.getCode(),e.getMsg());
    }

}
