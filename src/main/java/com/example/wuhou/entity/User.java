package com.example.wuhou.entity;

public class User{
    String userId;  //登录ID
    String password;
    String nickName;
    String salt = "";  //盐值
    String roleId;  //角色

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "User{" +
                "用户编号='" + userId + '\'' +
                ", 用户密码='" + password + '\'' +
                ", 用户昵称='" + nickName + '\'' +
                ", 盐值='" + salt + '\'' +
                ", 角色编号='" + roleId + '\'' +
                '}';
    }
}
