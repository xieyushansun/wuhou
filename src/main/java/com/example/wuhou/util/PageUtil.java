package com.example.wuhou.util;

import java.util.List;

// 分页查询
public class PageUtil<T> {
//    List<T> resultList;
    private T body;
    private int totalElement;
    private int code;
    private String message;

    public PageUtil() {
        super();
    }

    public PageUtil(int code, String message, T body) {
        super();
        this.code = code;
        this.message = message;
        this.body = body;
    }

    public PageUtil(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public int getTotalElement() {
        return totalElement;
    }

    public void setTotalElement(int totalElement) {
        this.totalElement = totalElement;
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
