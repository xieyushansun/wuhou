package com.example.wuhou.realm;

import com.example.wuhou.entity.User;
import com.example.wuhou.service.RoleService;
import com.example.wuhou.service.UserService;
import com.example.wuhou.util.ApplicationContextUtils;
import com.example.wuhou.util.SaltUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public class UserRealm extends AuthorizingRealm {
    //授权+认证
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行授权");
        //获取主身份信息(用户名)
        String primaryPrincipal = (String) principalCollection.getPrimaryPrincipal();
        //根据主身份信息获取当前用户的角色信息
        User user = userService.findUserByUserId(primaryPrincipal);
        String roleId = user.getRoleId();
        Set<String> permissionSet = new HashSet<>();
        //如果roleId不是guest，说明其权限表被更新了
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        if (!roleId.equals("guest")){
            permissionSet = roleService.getRolePermissions(roleId);
            // 如果用户所属角色被删除，则查出来为空
            if (permissionSet != null){
                for (String permission:permissionSet){
                    simpleAuthorizationInfo.addRole(permission);
                }
            }
        }
        //根据主身份信息获取角色和权限信息
//        if ("10010".equals(primaryPrincipal)){
//            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//            simpleAuthorizationInfo.addRole("admin1");
//
//            return simpleAuthorizationInfo;
//        }

        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行认证");
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
//      封装用户的登录数据
//        UsernamePasswordToken userToken = (UsernamePasswordToken) authenticationToken;
//
//        User dbuser = userService.findUserByUserId(userToken.getUsername());
//        if (dbuser == null){ //没有这个人
//            return null; //UnknownAccountException
//        }
//
//        String dbsalt = dbuser.getSalt();
//        String userpassword = userToken.getCredentials().toString();
//        String passwordEncoded = MD5Util.MD5Value(MD5Util.MD5Value(userpassword) + dbsalt);
//        userToken.setPassword("cc08f1c3912f71080e36af704cff206a".toCharArray());
//        String principal = (String) userToken.getPrincipal();
//        return new SimpleAuthenticationInfo(principal, dbuser.getPassword(), getName());
        String principal = (String) token.getPrincipal();
        UserService userService = (UserService) ApplicationContextUtils.getBean("userService");
        User user = userService.findUserByUserId(principal);
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                user.getUserId(), user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName()
        );

        return simpleAuthenticationInfo;
    }
}
