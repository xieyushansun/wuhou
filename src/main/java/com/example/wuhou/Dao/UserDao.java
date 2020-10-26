package com.example.wuhou.Dao;

import com.example.wuhou.entity.User;
import com.example.wuhou.exception.ExistException;
import com.example.wuhou.exception.NotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao {
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    LogDao logDao;

    public User findUserByUserId(String userId) {
        Query query = new Query();
        Criteria criteria = Criteria.where("userId").is(userId);
        query.addCriteria(criteria);
        User user = mongoTemplate.findOne(query, User.class);
//        if (user == null) {
//            throw new NotExistException("用户不存在");
//        }
        return user;
    }
    public void addUser(User user) throws Exception {
        Query query = new Query();
        Criteria criteria = Criteria.where("userId").is(user.getUserId());
        query.addCriteria(criteria);

        User dbuser=mongoTemplate.findOne(query, User.class);
        if (dbuser != null){
            throw new ExistException(user.getUserId() + ":用户Id重复\n");
        }

        if (mongoTemplate.insert(user) == null){
            throw new Exception("插入失败");
        }

        logDao.insertLog("user", "添加", "添加userId为：" + user.getUserId() + " 的用户");

//        LogUtil.addDB("插入用户 " + user.getUserId());

    }
    public void deleteUser(String userId) throws Exception {
        Query query = new Query();
        Criteria criteria = Criteria.where("userId").is(userId);
        query.addCriteria(criteria);
        User dbuser = mongoTemplate.findOne(query, User.class);
        if (dbuser == null){
            throw new NotExistException(userId + ":没有该用户\n");
        }
        mongoTemplate.remove(query, User.class);
//        LogUtil.delteDB("删除用户 " + userId);
        logDao.insertLog("user", "删除", "删除用户 " + userId);
    }

    public void changePassword(String newPassword, String userId) throws Exception {
        Query query = Query.query(Criteria.where("userId").is(userId));
        Update update = Update.update("password", newPassword);
        try {
            mongoTemplate.updateFirst(query, update, User.class);
//            LogUtil.modifyDB("修改用户 " + userId + " 的密码");
            logDao.insertLog("user", "修改", "修改用户 " + userId + " 的密码");
        }catch (Exception e){
            throw new Exception("修改失败!");
        }
    }

    public void resetPassword(String userId, String resetedPassword) throws Exception {
        Query query = Query.query(Criteria.where("userId").is(userId));
        Update update = Update.update("password", resetedPassword);
        try {
            mongoTemplate.updateFirst(query, update, User.class);
//            LogUtil.modifyDB("重置用户 " + userId + " 的密码为12345678");
            logDao.insertLog("user", "修改", "重置用户 " + userId + " 的密码为12345678" );
        }catch (Exception e){
            throw new Exception("重置失败!");
        }
    }
    public List<User> getAllUser(){
        return mongoTemplate.findAll(User.class);
    }
}
