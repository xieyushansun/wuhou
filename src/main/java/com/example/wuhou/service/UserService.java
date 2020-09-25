package com.example.wuhou.service;

import com.example.wuhou.Dao.UserDao;
import com.example.wuhou.entity.User;
import com.example.wuhou.exception.NotExistException;
import com.example.wuhou.util.SaltUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserService {
    @Autowired
    UserDao userDao;
    public User login(String userId, String password) throws NotExistException {
//        User user = userDao.findUserByUserId(userId);
//        User user1 = new User();
//        user1.setSalt(user.getSalt());
//        user1.setPassword(password);
//        String dbpassword = user.getPassword();  //从数据库中取出的password
//        String userpassword = EncodeUserPassword.encode(user1);  //用户输入的password
//        if (dbpassword.equals(userpassword)){
//            return user;
//        }
        return null;
    }
    public void addUser(String userId, String password, String nickName) throws Exception {
        User user = new User();
        user.setUserId(userId);
        String salt = SaltUtil.getSalt(8);
        user.setSalt(salt);
        Md5Hash md5Hash = new Md5Hash(password, salt,5);
        user.setPassword(md5Hash.toHex());

        user.setNickName(nickName);
        user.setRoleId("guest");
        userDao.addUser(user);
    }
    public void deleteUser(String userId) throws NotExistException {
        userDao.deleteUser(userId);
    }
    public User findUserByUserId(String userId){
        return userDao.findUserByUserId(userId);
    }


    public User getCurrentUserFromSession() {
        Subject currentUser = SecurityUtils.getSubject();

        if (currentUser.getPrincipals() == null){
            return null;
        }
        String principal = currentUser.getPrincipal().toString();
        User user = userDao.findUserByUserId(principal);
        if (user != null){
            user.setSalt("");
            user.setPassword("");
        }
        return user;
    }
    public void changePassword(String oldPassword, String newPassword, String userId) throws Exception {
        User user = userDao.findUserByUserId(userId);
        String passwordDB = user.getPassword();
        String passwordOld = new Md5Hash(oldPassword, user.getSalt(),5).toHex();
        String passwordNew = new Md5Hash(newPassword, user.getSalt(),5).toHex();

        if (passwordDB.equals(passwordOld)){
            userDao.changePassword(passwordNew, userId);
        }else {
            throw new Exception("旧密码与原密码不匹配！");
        }
    }
    public void resetPassword(String userId) throws Exception {
        User user = userDao.findUserByUserId(userId);
        String resetedPassword = new Md5Hash("12345678", user.getSalt(),5).toHex();

        userDao.resetPassword(userId, resetedPassword);
    }
    public List<User> getAllUser(){
        List<User> userList = userDao.getAllUser();
        for (User user : userList) {
            user.setPassword("");
            user.setSalt("");
        }
        return userList;
    }
}
