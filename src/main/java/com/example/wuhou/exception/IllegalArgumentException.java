package com.example.wuhou.exception;

import com.example.wuhou.constant.ResponseConstant;
import com.example.wuhou.util.ResultUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
class ArgumentException {
    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public ResultUtil handleShiroException(Exception ex) {
        return new ResultUtil(ResponseConstant.ResponseCode.PARAM_ERROR, "参数异常");
    }
}