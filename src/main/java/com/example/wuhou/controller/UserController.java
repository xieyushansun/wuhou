package com.example.wuhou.controller;

import com.example.wuhou.Dao.UserDao;
import com.example.wuhou.constant.ResponseConstant;
import com.example.wuhou.entity.User;
import com.example.wuhou.exception.NotExistException;
import com.example.wuhou.service.UserService;
import com.example.wuhou.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

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
        User user;
        try {
            user = userService.login(userId, password);
        } catch (NotExistException e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.EXIST_ERROR, "用户不存在");
        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }
//        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "登录成功！");
//        String userame = userfind.getUserName();
        if (user != null){
//            User user = new User();
//            user.setRole("role");
            user.setPassword("");
            user.setSalt("");
//            user.setNickName("nickName");
            ResultUtil<User> resultUtil = new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "登录成功！");
            resultUtil.setBody(user);
            return resultUtil;
        }else{
            ResultUtil<User> resultUtil = new ResultUtil<>(ResponseConstant.ResponseCode.NOT_LOGIN, "用户名或密码错误！");
            resultUtil.setBody(null);
            return resultUtil;
        }
    }


    @PostMapping("/addUser")
    @ApiOperation("新增用户")
    public ResultUtil<String> addUser(
            @ApiParam(value = "用户名", required = true) @RequestParam(defaultValue = "10010") String userId,
            @ApiParam(value = "密码", required = true) @RequestParam(defaultValue = "123") String password,
            @ApiParam(value = "昵称", required = true) @RequestParam(defaultValue = "杉杉") String nickName,
            @ApiParam(value = "角色", required = true) @RequestParam(defaultValue = "guest") String role
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
            @ApiParam(value = "用户名", required = true) @RequestParam(defaultValue = "10010") String userId
    ){
        try {
            userService.deleteUser(userId);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "删除成功！");
    }
}
