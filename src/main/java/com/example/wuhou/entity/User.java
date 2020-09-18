package com.example.wuhou.entity;

import java.io.Serializable;

public class User implements Serializable {
    String userId;  //登录ID
    String password;
    String nickName;
    String salt = "";  //盐值
    String role;  //角色

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", nickName='" + nickName + '\'' +
                ", salt='" + salt + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
