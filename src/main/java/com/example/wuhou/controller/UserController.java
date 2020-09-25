package com.example.wuhou.controller;

import com.example.wuhou.constant.ResponseConstant;
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

    @PostMapping("/login")
    @ApiOperation("登录")
    public ResultUtil<User> Login(
            @ApiParam(value = "用户名", required = true) @RequestParam() String userId,
            @ApiParam(value = "密码", required = true) @RequestParam() String password
    ){
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
        }catch (UnknownAccountException e){ //用户名不存在
            return new ResultUtil<>(ResponseConstant.ResponseCode.EXIST_ERROR, "用户不存在");
        }catch (IncorrectCredentialsException e){ //密码错误
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "密码错误");
        }
        ResultUtil<User> resultUtil = new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "登录成功啦！");
//        resultUtil.setBody(user1);
        return resultUtil;
    }

    @PostMapping("/logout")
    @ApiOperation("退出登录")
    public ResultUtil<String> logout(){
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.logout(); //退出用户
        }catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }

        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "退出登录成功！");
    }

    @PostMapping("/addUser")
    @ApiOperation("新增用户")
    public ResultUtil<String> addUser(
            @ApiParam(value = "用户名", required = true) @RequestParam(defaultValue = "10010") String userId,
            @ApiParam(value = "密码", required = true) @RequestParam(defaultValue = "123") String password,
            @ApiParam(value = "昵称", required = true) @RequestParam(defaultValue = "杉杉") String nickName,
            @ApiParam(value = "角色", required = false) @RequestParam(required = false) String role
    ){
        try {
            userService.addUser(userId, password, nickName);
        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }

        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "添加成功！");
//        String userame = userfind.getUserName();
    }

    @PostMapping("/deleteUser")
    @ApiOperation("删除用户")
    public ResultUtil<String> deleteUser(
            @ApiParam(value = "用户名", required = true) @RequestParam() String userId
    ){
        try {
            userService.deleteUser(userId);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "删除成功！");
    }
    @GetMapping("/getCurrentUser")
    @ApiOperation("获取当前登录用户状态")
    public ResultUtil getCurrentUserFromSession() {
        User user = userService.getCurrentUserFromSession();
        if (user != null) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "", user);
        } else {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "", "登录状态异常!");
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
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "修改成功！");
    }
    @PostMapping("/resetPassword")
    @ApiOperation("重置密码")
    public ResultUtil<String> resetPassword(
            @ApiParam(value = "被重置密码的人的userId", required = true) @RequestParam() String userId
    ){
        try {
            userService.resetPassword(userId);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "重置失败！");
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "重置成功！新密码为：12345678");
    }
    @GetMapping("/getAllUser")
    @ApiOperation("获取所有用户")
    public ResultUtil<List<User>> getAllUser(){
        List<User> userList = new ArrayList<>();
        try {
            userList = userService.getAllUser();
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "获取所有用户失败！");
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "获取成功！", userList);
    }
}
