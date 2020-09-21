package com.example.wuhou.Dao;

import com.example.wuhou.entity.Role;
import com.example.wuhou.exception.ExistException;
import com.example.wuhou.exception.NotExistException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class RoleDao {
    @Autowired
    MongoTemplate mongoTemplate;
    //添加角色
    public void addRole(Role role) throws Exception {
        Query query = new Query();
        Criteria criteria = Criteria.where("roleName").is(role.getRoleName());
        query.addCriteria(criteria);
        if (mongoTemplate.findOne(query, Role.class) != null){
            throw new ExistException("已经存在该名称的权限，请重新输入！");
        }
        mongoTemplate.insert(role);
    }
    //删除角色
    public void deleteRole(String id) throws ExistException {
        Query query = new Query();
        Criteria criteria = Criteria.where("id").is(new ObjectId(id));
        query.addCriteria(criteria);

        if (mongoTemplate.findOne(query, Role.class) == null){
            throw new ExistException("该角色不存在");
        }
        mongoTemplate.remove(query, Role.class);
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
}
