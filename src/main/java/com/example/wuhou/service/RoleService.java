package com.example.wuhou.service;

import com.example.wuhou.Dao.RoleDao;
import com.example.wuhou.Dao.UserDao;
import com.example.wuhou.entity.Role;
import com.example.wuhou.exception.ExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RoleService {
    @Autowired
    RoleDao roleDao;
    public void addRole(Set<String> permissions, String roleName) throws Exception {
        Role role = new Role();
        role.setPermissions(permissions);
        role.setRoleName(roleName);
        roleDao.addRole(role);
    }
    public void deleteRole(String id) throws ExistException {
        roleDao.deleteRole(id);
    }
    public void modifyRole(Role role, String id) throws Exception {
        roleDao.modifyRole(role, id);
    }
    public List<Role> getAllRole(){
        return roleDao.getAllRole();
    }
    public void userRoleAuthorize(String userId, String roleId){
        roleDao.userRoleAuthorize(userId, roleId);
    }
    public void removeUserRoleAuthorize(String userId){
        roleDao.removeUserRoleAuthorize(userId);
    }
}
