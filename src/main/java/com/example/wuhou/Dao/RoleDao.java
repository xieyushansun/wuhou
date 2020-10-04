package com.example.wuhou.Dao;

import com.example.wuhou.entity.Role;
import com.example.wuhou.entity.User;
import com.example.wuhou.exception.ExistException;
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

    //添加角色
    public void addRole(Role role) throws Exception {
        Query query = new Query();
        Criteria criteria = Criteria.where("roleName").is(role.getRoleName());
        query.addCriteria(criteria);
        if (mongoTemplate.findOne(query, Role.class) != null){
            throw new ExistException("已经存在该名称的权限，请重新输入！");
        }
        mongoTemplate.insert(role);
//        LogUtil.addDB("添加角色 > " + role.getRoleName());
        logDao.inserLog("role", "添加",  "添加角色 > " + role.getRoleName());

    }
    //删除角色
    public void deleteRole(String id) throws Exception {
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(new ObjectId(id));
        query.addCriteria(criteria);

        Role role = mongoTemplate.findOne(query, Role.class);
        if (role == null){
            throw new ExistException("该角色不存在");
        }
        mongoTemplate.remove(query, Role.class);
//        LogUtil.delteDB("删除角色 > " + role.getRoleName());
        logDao.inserLog("role", "删除", "删除角色 > " + role.getRoleName());
    }
    //修改角色
    public void modifyRole(Role role, String id) throws Exception {
        //首先删除要修改的角色数据
        deleteRole(id);
        addRole(role);
    }
    //获取所有角色
    public List<Role> getAllRole(){
        return mongoTemplate.findAll(Role.class);
    }
    //为用户赋予角色
    public void userRoleAuthorize(String userId, String roleId) throws Exception {
        Update update = new Update();
        Query query = new Query();
//        Criteria criteria = Criteria.where("_id").is(new ObjectId(userId));
//        query.addCriteria(criteria);
        Criteria criteria = Criteria.where("userId").is(userId);
        query.addCriteria(criteria);
        update.set("roleId", new ObjectId(roleId));
        mongoTemplate.updateFirst(query, update, User.class);
        logDao.inserLog("role", "修改", "为用户 " + userId + "添加角色id > " + roleId);
    }
    //删除用户角色，默认回到guest，没有任何权限
    public void removeUserRoleAuthorize(String userId) throws Exception {
        Update update = new Update();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(userId)));
        update.set("roleId", "guest");
        mongoTemplate.updateFirst(query, update, User.class);
        logDao.inserLog("role", "修改", "将用户 " + userId + "角色置为guest");
    }
    //获取角色下所有权限
    public Set<String> getRolePermissions(String roleId){
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(new ObjectId(roleId));
        query.addCriteria(criteria);

        Role role = mongoTemplate.findOne(query, Role.class);

        return role.getPermissions();
    }
}
