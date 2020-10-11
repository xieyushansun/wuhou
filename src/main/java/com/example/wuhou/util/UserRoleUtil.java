package com.example.wuhou.util;

import com.example.wuhou.entity.Role;
import com.example.wuhou.entity.User;

import java.util.List;

public class UserRoleUtil<T> {
    private T body;
    private int code;
    private String message;

    public UserRoleUtil() {
        super();
    }


    public UserRoleUtil(int code, String message, T body) {
        super();
        this.code = code;
        this.message = message;
        this.body = body;
    }

    public UserRoleUtil(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
