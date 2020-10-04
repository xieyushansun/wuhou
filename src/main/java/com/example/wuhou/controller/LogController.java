package com.example.wuhou.controller;

import com.example.wuhou.constant.PermissionConstant;
import com.example.wuhou.constant.ResponseConstant;
import com.example.wuhou.util.PageUtil;
import com.example.wuhou.service.LogService;
import com.example.wuhou.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
@Api(tags = "日志")
public class LogController {
    @Autowired
    LogService logService;

    @RequiresRoles(value = {PermissionConstant.SUPER_ONLY, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @PostMapping("/getAllLog")
    @ApiOperation("分页获取所有日志")
    public ResultUtil<PageUtil> getAllLog(
            @ApiParam(value = "当前显示页", required = false) @RequestParam(defaultValue = "1") Integer currentPage,
            @ApiParam(value = "页面大小", required = false) @RequestParam(defaultValue = "5") Integer pageSize
    ){
        PageUtil pageUtil;
        try {
            pageUtil = logService.getAllLog(currentPage, pageSize);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "查询成功！", pageUtil);
    }
    @RequiresRoles(value = {PermissionConstant.SUPER_ONLY, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @PostMapping("/deleteAllLog")
    @ApiOperation("清除所有日志")
    public ResultUtil deleteAllLog(){
        try {
            logService.deleteAllLog();
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "删除成功");
    }
    @RequiresRoles(value = {PermissionConstant.SUPER_ONLY, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @PostMapping("deleteAllLogBeforDate")
    @ApiOperation("清除某日期前的日志，不包括该日期")
    public ResultUtil deleteAllLogBeforDate(
            @ApiParam(value = "删除该日期前的所有日志，不包括该日期", required = true) @RequestParam() String date
    ){
        try {
            logService.deleteAllLogBeforDate(date);
        }catch (Exception e){
            return new ResultUtil(ResponseConstant.ResponseCode.SUCCESS, e.getMessage());
        }
        return new ResultUtil(ResponseConstant.ResponseCode.FAILURE, "删除成功");
    }
}
