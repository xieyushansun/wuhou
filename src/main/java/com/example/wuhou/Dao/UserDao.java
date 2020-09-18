package com.example.wuhou.Dao;

import com.example.wuhou.constant.PasswordConstant;
import com.example.wuhou.entity.User;
import com.example.wuhou.exception.ExistException;
import com.example.wuhou.exception.NotExistException;
import com.example.wuhou.util.EncodeUserPassword;
import com.example.wuhou.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
    @Autowired
    MongoTemplate mongoTemplate;
    public User findUserByUserId(String userId) throws NotExistException {
        Query query = new Query();
        Criteria criteria = Criteria.where("userId").is(userId);
        query.addCriteria(criteria);
        User user = mongoTemplate.findOne(query, User.class);
        if (user == null) {
            throw new NotExistException("用户不存在");
        }
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
        user.setSalt(RandomUtil.getStringRandom(PasswordConstant.SALT_LENGTH));
        user.setPassword(EncodeUserPassword.encode(user));

        if (mongoTemplate.insert(user) == null){
            throw new Exception("插入失败");
        }
    }
    public void deleteUser(String userId) throws NotExistException {
        Query query = new Query();
        Criteria criteria = Criteria.where("userId").is(userId);
        query.addCriteria(criteria);

        User dbuser = mongoTemplate.findOne(query, User.class);
        if (dbuser == null){
            throw new NotExistException(userId + ":没有该用户\n");
        }
        mongoTemplate.remove(query, User.class);
    }
}
