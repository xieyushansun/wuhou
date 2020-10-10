package com.example.wuhou.controller;

import com.example.wuhou.constant.PermissionConstant;
import com.example.wuhou.constant.ResponseConstant;
import com.example.wuhou.entity.DiskManage;
import com.example.wuhou.service.DiskManageService;
import com.example.wuhou.service.DocumentCategoryService;
import com.example.wuhou.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/DiskManage")
@Api(tags = "磁盘管理")
public class DiskManageController {
    @Autowired
    DiskManageService diskManageService;
    @RequiresRoles(value = {PermissionConstant.SUPER_ONLY, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @GetMapping("/getAllDiskNameAndSpace")
    @ApiOperation("获取所有磁盘与磁盘大小")
    public ResultUtil<List<DiskManage>> getAllDiskNameAndSpaceFromComputer(){
        List<DiskManage> diskManageList;
        try {
            diskManageList = diskManageService.getAllDiskNameAndSpaceFromComputer();
        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "获取失败: " + e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "获取成功！", diskManageList);
    }

//    @RequiresRoles(value = {PermissionConstant.SUPER_ONLY, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
//    @PostMapping("/refreshDiskDatabase")
//    @ApiOperation("更新数据库磁盘信息")
//    public ResultUtil refreshDiskDatabase(){
//        try {
//            diskManageService.refreshDiskDatabase();
//        } catch (Exception e) {
//            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "更新失败: " + e.getMessage());
//        }
//        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "更新成功！");
//    }

    @RequiresRoles(value = {PermissionConstant.SUPER_ONLY, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @GetMapping("/getDiskUsedSituation")
    @ApiOperation("获取某磁盘使用状况")
    public ResultUtil<DiskManage> getDiskUsedSituation(
            @ApiParam(value = "磁盘名称", required = true) @RequestParam() String diskName
    ){
        DiskManage diskManage;
        try {
            diskManage = diskManageService.getDiskUsedSituation(diskName);
        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "获取失败: " + e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "获取成功！", diskManage);
    }

    @RequiresRoles(value = {PermissionConstant.SUPER_ONLY, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @GetMapping("/getCurrentDiskNameAndSpace")
    @ApiOperation("获取当前存储磁盘")
    public ResultUtil<DiskManage> getCurrentDiskNameAndSpace(){
        DiskManage diskManage;
        try {
            diskManage = diskManageService.getCurrentDiskNameAndSpace();
        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "获取失败: " + e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "获取成功！", diskManage);
    }
    @RequiresRoles(value = {PermissionConstant.SUPER_ONLY, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @GetMapping("/getUsedDiskNameAndSpace")
    @ApiOperation("获取目前为止已经加入数据库的存储盘")
    public ResultUtil<List<DiskManage>> getUsedDiskNameAndSpace(){
        List<DiskManage> diskManageList;
        try {
            diskManageList = diskManageService.getUsedDiskNameAndSpace();
        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "获取失败: " + e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "获取成功！", diskManageList);
    }
    @RequiresRoles(value = {PermissionConstant.SUPER_ONLY, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @PostMapping("/setCurrentDisk")
    @ApiOperation("设置当前存储磁盘")
    public ResultUtil setCurrentDisk(
            @ApiParam(value = "新存储盘名称", required = true) @RequestParam() String diskName
    ){
        try {
            diskManageService.setCurrentDisk(diskName);
        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "设置失败: " + e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "设置成功！");
    }
}
