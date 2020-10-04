package com.example.wuhou.entity;

import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class Role {
    // 初始化用戶roleId为guest
    // 当第一次给用户赋角色以后，用户roleId就是角色表里角色的id

    private String id;
    private String roleName;
    private Set<String> permissions;

    public Role(){
        id = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }
}
