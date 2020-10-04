package com.example.wuhou.exception;

import com.example.wuhou.constant.ResponseConstant;
import com.example.wuhou.util.ResultUtil;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public ResultUtil<String> defaultExceptionHandler(HttpServletRequest req, Exception e){
        return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "没有权限");
    }
}
