package com.example.wuhou.util;


public class ResultUtil<T> {
    private int code;
    private String message;
    private T body;
    private int totalElements;


    public ResultUtil() {
        super();
    }

    public ResultUtil(int code, String message, T body) {
        super();
        this.code = code;
        this.message = message;
        this.body = body;
    }

    public ResultUtil(int code, String message) {
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

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    @Override
    public String toString() {
        return "ResultUtil{" +
                "body=" + body +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
