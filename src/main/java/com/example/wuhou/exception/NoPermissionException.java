package com.example.wuhou.exception;

import com.example.wuhou.constant.ResponseConstant;
import com.example.wuhou.util.ResultUtil;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class NoPermissionException {
    @ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    public ResultUtil handleShiroException(Exception ex) {
        return new ResultUtil(ResponseConstant.ResponseCode.FORBIDDEN_ERROR, "没有权限");
    }
    @ResponseBody
    @ExceptionHandler(AuthorizationException.class)
    public ResultUtil AuthorizationException(Exception ex) {
        return new ResultUtil(ResponseConstant.ResponseCode.UNAUTHORIZED_RESOURCES, "没有认证");
    }
}

