package com.example.wuhou.Dao;

import com.example.wuhou.entity.Role;
import com.example.wuhou.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class RoleDao {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    LogDao logDao;

    //获取当前用户
    public String getCurrentUserId() throws Exception {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.getPrincipals() == null){
            throw new Exception("获取当前用户登录状态异常");
        }
        return currentUser.getPrincipals().toString();
    }

    //添加角色
    public void addRole(Role role) throws Exception {
        Query query = new Query();
        Criteria criteria = Criteria.where("roleName").is(role.getRoleName());
        query.addCriteria(criteria);
        if (mongoTemplate.findOne(query, Role.class) != null){
            throw new Exception("已经存在该名称的权限，请重新输入！");
        }
        mongoTemplate.insert(role);
//        LogUtil.addDB("添加角色 > " + role.getRoleName());
        logDao.insertLog("role", "添加",  "添加角色 > " + role.getRoleName());

    }
    //


    //问题修改！

    public void deleteRole(String id) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));

        Role role = mongoTemplate.findOne(query, Role.class);
        if (role == null){
            throw new Exception("该角色不存在");
        }
        if(role.getRoleName().equals("超级管理员")){
            throw new Exception("不可以删除超级管理员角色！");
        }
        mongoTemplate.remove(query, Role.class);
//        LogUtil.delteDB("删除角色 > " + role.getRoleName());

        Query query1 = new Query();
        query1.addCriteria(Criteria.where("roleId").is(new ObjectId(id)));
        Update update = new Update();
        update.set("roleId", "guest");
        mongoTemplate.updateMulti(query1, update, User.class);

        logDao.insertLog("role", "删除", "删除角色 > " + role.getRoleName());
    }
    //修改角色
    public void modifyRole(Role role, String roleId) throws Exception {
        //首先删除要修改的角色数据
//        deleteRole(id);
//        role.setId(id);
//        addRole(role);
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(roleId)));
        Update update = Update.update("roleName", role.getRoleName()).set("permissions", role.getPermissions());
        mongoTemplate.updateMulti(query, update, Role.class);
        logDao.insertLog("role", "修改", "修改角色: " + role.getRoleName());

    }
    //获取所有角色
    public List<Role> getAllRole(){
        return mongoTemplate.findAll(Role.class);
    }
    //为用户赋予角色
    public void userRoleAuthorize(String userId, String roleId) throws Exception {
        if (userId.equals("admin")){
            throw new Exception("不可以修改超级管理员的角色！");
        }
        if (getCurrentUserId().equals(userId)){
            throw new Exception("不可以修改自己的角色！");
        }
        Update update = new Update();
        Query query = new Query();
//        Criteria criteria = Criteria.where("_id").is(new ObjectId(userId));
//        query.addCriteria(criteria);
        Criteria criteria = Criteria.where("userId").is(userId);
        query.addCriteria(criteria);
        update.set("roleId", new ObjectId(roleId));
        mongoTemplate.updateFirst(query, update, User.class);
        logDao.insertLog("role", "修改", "为用户 " + userId + " 添加角色id > " + roleId);
    }
    //删除用户角色，默认回到guest，没有任何权限
    public void removeUserRoleAuthorize(String userId) throws Exception {
        if (userId.equals("admin")){
            throw new Exception("不可以移除超级管理员的角色！");
        }
        if (getCurrentUserId().equals(userId)){
            throw new Exception("不可以修改自己的角色！");
        }
        Update update = new Update();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(userId)));
        update.set("roleId", "guest");
        mongoTemplate.updateFirst(query, update, User.class);
        logDao.insertLog("role", "修改", "将用户 " + userId + "角色置为guest");
    }
    //获取角色下所有权限
    public Set<String> getRolePermissions(String roleId){
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(new ObjectId(roleId));
        query.addCriteria(criteria);

        Role role = mongoTemplate.findOne(query, Role.class);
        if (role == null){
            return null;
        }
        return role.getPermissions();
    }
    public Role findRoleByRoleId(String roleId){
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(roleId)));
        return mongoTemplate.findOne(query, Role.class);
    }
}