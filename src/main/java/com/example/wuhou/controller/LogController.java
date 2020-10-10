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
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/log")
@Api(tags = "日志")
public class LogController {
    @Autowired
    LogService logService;

    @RequiresRoles(value = {PermissionConstant.SUPER_ONLY, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @GetMapping("/getAllLog")
    @ApiOperation("分页获取所有日志")
    public PageUtil getAllLog(
            @ApiParam(value = "当前显示页") @RequestParam(defaultValue = "1") Integer currentPage,
            @ApiParam(value = "页面大小") @RequestParam(defaultValue = "5") Integer pageSize
    ){
        PageUtil pageUtil;
        try {
            pageUtil = logService.getAllLog(currentPage, pageSize);
        }catch (Exception e){
            return new PageUtil(ResponseConstant.ResponseCode.FAILURE, "获取失败: " + e.getMessage());
        }
        pageUtil.setCode(ResponseConstant.ResponseCode.SUCCESS);
        pageUtil.setMessage("查询成功！");
        return pageUtil;
    }
    @RequiresRoles(value = {PermissionConstant.SUPER_ONLY, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @PostMapping("/deleteAllLog")
    @ApiOperation("清除所有日志")
    public ResultUtil deleteAllLog(){
        try {
            logService.deleteAllLog();
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE,"删除失败: " +  e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "删除成功");
    }
    @RequiresRoles(value = {PermissionConstant.SUPER_ONLY, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @PostMapping("/deleteAllLogBeforDate")
    @ApiOperation("清除某日期前的日志，不包括该日期")
    public ResultUtil deleteAllLogBeforDate(
            @ApiParam(value = "删除该日期前的所有日志，不包括该日期", required = true) @RequestParam() String date
    ){
        try {
            logService.deleteAllLogBeforDate(date);
        }catch (Exception e){
            return new ResultUtil(ResponseConstant.ResponseCode.FAILURE, "删除失败: " + e.getMessage());
        }
        return new ResultUtil(ResponseConstant.ResponseCode.SUCCESS, "删除成功");
    }

    @RequiresRoles(value = {PermissionConstant.SUPER_ONLY, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @GetMapping("/normalFindLog")
    @ApiOperation("普通查询日志")
    public PageUtil normalFindLog(
            @ApiParam(value = "操作者ID") @RequestParam(defaultValue = "") String operatorId,
            @ApiParam(value = "操作类型") @RequestParam(defaultValue = "") String operationType,
            @ApiParam(value = "操作描述") @RequestParam(defaultValue = "") String msg,
            @ApiParam(value = "日期范围, 包括日期开始和结束天") @RequestParam(defaultValue = "") String logDateFanwei,
            @ApiParam(value = "是否模糊查询") @RequestParam(defaultValue = "1") String blurryFind,
            @ApiParam(value = "当前显示页") @RequestParam(defaultValue = "1") Integer currentPage,
            @ApiParam(value = "页面大小") @RequestParam(defaultValue = "5") Integer pageSize
    ){
        Map<String, String> findKeyWordMap = new HashMap<>();
        if (!operatorId.isEmpty()){
            findKeyWordMap.put("operatorId", operatorId);
        }
        if (!operationType.isEmpty()){
            findKeyWordMap.put("operationType", operationType);
        }
        if (!msg.isEmpty()){
            findKeyWordMap.put("msg", msg);
        }
        if (!logDateFanwei.isEmpty()){
            findKeyWordMap.put("logDateFanwei", logDateFanwei);
        }
        PageUtil pageUtil;
        try {
            pageUtil = logService.normalFindLog(findKeyWordMap, currentPage, pageSize, blurryFind);
        }catch (Exception e){
            return new PageUtil(ResponseConstant.ResponseCode.FAILURE, "查询失败: " + e.getMessage());
        }
        pageUtil.setMessage("查询成功！");
        pageUtil.setCode(ResponseConstant.ResponseCode.SUCCESS);
        return pageUtil;
    }
}
