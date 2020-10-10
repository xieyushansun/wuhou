package com.example.wuhou.controller;

import com.example.wuhou.Dao.DiskManageDao;
import com.example.wuhou.Dao.LogDao;
import com.example.wuhou.constant.PathConstant;
import com.example.wuhou.constant.PermissionConstant;
import com.example.wuhou.constant.ResponseConstant;
import com.example.wuhou.entity.DiskManage;
import com.example.wuhou.entity.User;
import com.example.wuhou.service.UserService;
import com.example.wuhou.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@Api(tags = "用户")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    LogDao logDao;
    @Autowired
    DiskManageDao diskManageDao;
    @PostMapping("/login")
    @ApiOperation("登录")
    public ResultUtil<User> Login(
            @ApiParam(value = "用户名", required = true) @RequestParam() String userId,
            @ApiParam(value = "密码", required = true) @RequestParam() String password
    ) throws Exception {
//        Boolean isLogin = false;
//        User user;
//        try {
//            user = userService.login(userId, password);
//        } catch (NotExistException e) {
//            return new ResultUtil<>(ResponseConstant.ResponseCode.EXIST_ERROR, "用户不存在");
//        } catch (Exception e) {
//            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
//        }
////        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "登录成功！");
////        String userame = userfind.getUserName();
//        if (user != null){
////            User user = new User();
////            user.setRole("role");
//            user.setPassword("");
//            user.setSalt("");
////            user.setNickName("nickName");
//            ResultUtil<User> resultUtil = new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "登录成功！");
//            resultUtil.setBody(user);
//            return resultUtil;
//        }else{
//            ResultUtil<User> resultUtil = new ResultUtil<>(ResponseConstant.ResponseCode.NOT_LOGIN, "用户名或密码错误！");
//            resultUtil.setBody(null);
//            return resultUtil;
//        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userId, password);
        try {
            subject.login(token);
            //设置30分钟后登陆超时
            SecurityUtils.getSubject().getSession().setTimeout(1800000);


            logDao.insertLog("登录操作", "登录", "用户名为: " + userId + " 的用户登录系统！");
        }catch (UnknownAccountException e){ //用户名不存在
            return new ResultUtil<>(ResponseConstant.ResponseCode.EXIST_ERROR, "用户不存在！");
        }catch (IncorrectCredentialsException e){ //密码错误
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "密码错误！");
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "登录失败: " + e.getMessage());
        }

        // 用来触发登录界面
        if (subject.isAuthenticated()){
            subject.hasRole("test");
//            System.out.println(subject.hasRole("role1"));
//            System.out.println(subject.hasAllRoles(Arrays.asList("role1", "role2")));
        }
        ResultUtil<User> resultUtil = new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "登录成功啦！");
        DiskManage diskManage = diskManageDao.getCurrentDiskNameAndSpace();
        if (diskManage != null){
            PathConstant.DISK_NAME = diskManage.getDiskName();
        }
        return resultUtil;
    }

    @PostMapping("/logout")
    @ApiOperation("退出登录")
    public ResultUtil<String> logout(){
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.logout(); //退出登录
        }catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "退出登录失败: " + e.getMessage());
        }

        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "退出登录成功！");
    }

    @RequiresRoles(value = {PermissionConstant.USER_MANAGE, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @PostMapping("/addUser")
    @ApiOperation("新增用户")
//    @RequiresRoles(value = {"ss"}, logical = Logical.OR)  //OR表示满足一个条件即可，AND表示所有条件都需要满足
    public ResultUtil<String> addUser(
            @ApiParam(value = "用户名", required = true) @RequestParam(defaultValue = "10010") String userId,
            @ApiParam(value = "密码", required = true) @RequestParam(defaultValue = "123") String password,
            @ApiParam(value = "昵称", required = true) @RequestParam(defaultValue = "杉杉") String nickName,
            @ApiParam(value = "角色", required = false) @RequestParam(required = false) String roleId
    ){
        try {
            userService.addUser(userId, password, nickName, roleId);
        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "添加失败: " + e.getMessage());
        }

        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "添加成功！");
//        String userame = userfind.getUserName();
    }

    @RequiresRoles(value = {PermissionConstant.USER_MANAGE, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @PostMapping("/deleteUser")
    @ApiOperation("删除用户")
    public ResultUtil<String> deleteUser(
            @ApiParam(value = "用户名", required = true) @RequestParam() String userId
    ){
        try {
            if (userId.equals("admin")){
                return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "不可以删除超级管理员！");
            }
            userService.deleteUser(userId);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "删除失败: " +e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "删除成功！");
    }
    @GetMapping("/getCurrentUser")
    @ApiOperation("获取当前登录用户状态")
    public ResultUtil getCurrentUserFromSession() {
        User user = userService.getCurrentUserFromSession();
        if (user != null) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "获取成功！", user);
        } else {
            return new ResultUtil<>(ResponseConstant.ResponseCode.UNAUTHORIZED_RESOURCES, "获取失败", "登录状态异常!");
        }
    }

    @PostMapping("/changePassword")
    @ApiOperation("修改密码")
    public ResultUtil<String> changePassword(
            @ApiParam(value = "原来的密码", required = true) @RequestParam() String oldPassword,
            @ApiParam(value = "新密码", required = true) @RequestParam() String newPassword
    ){
        try {
            User user = userService.getCurrentUserFromSession();
            String userId = user.getUserId();
            userService.changePassword(oldPassword, newPassword, userId);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "修改失败: " +e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "修改成功！");
    }

    @RequiresRoles(value = {PermissionConstant.USER_MANAGE, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @PostMapping("/resetPassword")
    @ApiOperation("重置密码")
    public ResultUtil<String> resetPassword(
            @ApiParam(value = "被重置密码的人的userId", required = true) @RequestParam() String userId
    ){
        try {
            userService.resetPassword(userId);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "重置失败: " + e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "重置成功！新密码为：12345678");
    }
    @RequiresRoles(value = {PermissionConstant.USER_MANAGE, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @GetMapping("/getAllUser")
    @ApiOperation("获取所有用户")
    public ResultUtil<List<User>> getAllUser(){
        List<User> userList;
        try {
            userList = userService.getAllUser();
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "获取所有用户失败: " + e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "获取成功！", userList);
    }
}
