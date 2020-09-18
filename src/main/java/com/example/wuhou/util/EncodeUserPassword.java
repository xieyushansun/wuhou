package com.example.wuhou.util;

import com.example.wuhou.entity.User;

/**
 * 用加密算法加密User.password，返回加密后的字符串。
 * 参数不能弱化为String，因为需要user.alt
 * MD5( MD5(password) + salt)
 */
public class EncodeUserPassword {
    public static String encode(User user) {
        String encodedPassword = MD5Util.MD5Value(user.getPassword()) + user.getSalt();
        return MD5Util.MD5Value(encodedPassword);
    }
}
