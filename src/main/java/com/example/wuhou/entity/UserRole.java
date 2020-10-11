package com.example.wuhou.entity;

import java.util.List;
import java.util.Set;

public class UserRole {
    String userId;
    String nickName;
    String roleName;
    Set<String> permitions;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<String> getPermitions() {
        return permitions;
    }

    public void setPermitions(Set<String> permitions) {
        this.permitions = permitions;
    }
}
