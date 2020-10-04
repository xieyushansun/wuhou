package com.example.wuhou.controller;

import com.example.wuhou.constant.PermissionConstant;
import com.example.wuhou.constant.ResponseConstant;
import com.example.wuhou.entity.Role;
import com.example.wuhou.service.RoleService;
import com.example.wuhou.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/role")
@Api(tags = "角色")
public class RoleController {
    @Autowired
    RoleService roleService;

    @RequiresRoles(value = {PermissionConstant.ROLE_MANAGE, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @PostMapping("/addRole")
    @ApiOperation("添加角色")
    public ResultUtil<String> addRole( //Set<String> permissions, String roleName
            @ApiParam(value = "权限列表", required = true) @RequestParam() List<String> permissionList,
            @ApiParam(value = "角色名", required = true) @RequestParam() String roleName
    ){
        try {
            Set<String> roleSet = new HashSet<>(permissionList);
            roleService.addRole(roleSet, roleName);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "添加成功！");
    }

    @RequiresRoles(value = {PermissionConstant.ROLE_MANAGE, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @PostMapping("/deleteRole")
    @ApiOperation("删除角色")
    public ResultUtil<String> deleteRole(
            @ApiParam(value = "角色id", required = true) @RequestParam() String roleId
    ){
        try {
            roleService.deleteRole(roleId);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "删除成功！");
    }

    @RequiresRoles(value = {PermissionConstant.ROLE_MANAGE, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @PostMapping("/modifyRole")
    @ApiOperation("修改角色")
    public ResultUtil<String> modifyRole(
            @ApiParam(value = "要修改的角色id", required = true) @RequestParam() String roleId,
            @ApiParam(value = "修改后的权限列表", required = true) @RequestParam() List<String> permissionList,
            @ApiParam(value = "修改后的角色名称", required = true) @RequestParam() String roleName
    ){
        try {
            Role role = new Role();
            Set<String> roleSet = new HashSet<>(permissionList);
            role.setPermissions(roleSet);
            role.setRoleName(roleName);
            roleService.modifyRole(role, roleId);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "修改成功！");
    }
    @RequiresRoles(value = {PermissionConstant.ROLE_MANAGE, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @GetMapping("/getAllRole")
    @ApiOperation("获取所有角色内容")
    public ResultUtil<List<Role>> getAllRole(){
        List<Role> roleList;
        try {
            roleList = roleService.getAllRole();
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }
        ResultUtil<List<Role>> resultUtil = new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "查询成功！");
        resultUtil.setBody(roleList);
        return resultUtil;
    }
    @RequiresRoles(value = {PermissionConstant.ROLE_MANAGE, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @PostMapping("/userRoleAuthorize")
    @ApiOperation("为用户授权")
    public ResultUtil<String>userRoleAuthorize(String userId, String roleId){
        try {
            roleService.userRoleAuthorize(userId, roleId);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "角色授权成功！");
    }

    @RequiresRoles(value = {PermissionConstant.ROLE_MANAGE, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @PostMapping("/removeUserRoleAuthorize")
    @ApiOperation("删除用户当前角色，默认为guest")
    public ResultUtil<String>removeUserRoleAuthorize(String userId){
        try {
            roleService.removeUserRoleAuthorize(userId);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "删除角色成功！");
    }

}
