package com.example.wuhou.service;

import com.example.wuhou.Dao.RoleDao;
import com.example.wuhou.Dao.UserDao;
import com.example.wuhou.constant.PermissionConstant;
import com.example.wuhou.entity.Role;
import com.example.wuhou.exception.ExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleService {
    @Autowired
    RoleDao roleDao;
    public void addRole(Set<String> permissions, String roleName) throws Exception {
        Role role = new Role();
//        if (permissions.size() == 0){
//            //如果没有添加任何权限，添加一个权限为guest
//            permissions.add(PermissionConstant.GUEST);
//        }
        role.setPermissions(permissions);
        role.setRoleName(roleName);
        roleDao.addRole(role);
    }
    public void deleteRole(String id) throws Exception {
        roleDao.deleteRole(id);
    }
    public void modifyRole(Role role, String id) throws Exception {
        // 如果修改后权限表为空，则赋予guest权限，即没有权限
//        if (role.getPermissions().size() == 0){
//            Set<String> permissions = new HashSet<>();
//            permissions.add(PermissionConstant.GUEST);
//            role.setPermissions(permissions);
//        }
        roleDao.modifyRole(role, id);
    }
    public List<Role> getAllRole(){
        return roleDao.getAllRole();
    }
    public void userRoleAuthorize(String userId, String roleId) throws Exception {
        roleDao.userRoleAuthorize(userId, roleId);
    }
    public void removeUserRoleAuthorize(String userId) throws Exception {
        roleDao.removeUserRoleAuthorize(userId);
    }

    public Set<String> getRolePermissions(String roleId){
        return roleDao.getRolePermissions(roleId);
    }
}
