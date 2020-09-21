package com.example.wuhou.entity;

import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class Role {
    String id;
    String roleName;
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
