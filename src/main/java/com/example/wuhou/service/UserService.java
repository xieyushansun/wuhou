package com.example.wuhou.service;

import com.example.wuhou.Dao.UserDao;
import com.example.wuhou.entity.User;
import com.example.wuhou.exception.ExistException;
import com.example.wuhou.exception.NotExistException;
import com.example.wuhou.util.EncodeUserPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserDao userDao;
    public User login(String userId, String password) throws NotExistException {
        User user = userDao.findUserByUserId(userId);
        User user1 = new User();
        user1.setSalt(user.getSalt());
        user1.setPassword(password);
        String dbpassword = user.getPassword();  //从数据库中取出的password
        String userpassword = EncodeUserPassword.encode(user1);  //用户输入的password
        if (dbpassword.equals(userpassword)){
            return user;
        }
        return null;
    }
    public void addUser(String userId, String password, String nickName) throws Exception {
        User user = new User();
        user.setUserId(userId);
        user.setPassword(password);
        user.setNickName(nickName);
        user.setRole("guest");
        userDao.addUser(user);
    }
    public void deleteUser(String userId) throws NotExistException {
        userDao.deleteUser(userId);
    }
}
