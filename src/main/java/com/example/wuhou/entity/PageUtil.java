package com.example.wuhou.entity;

import java.util.List;

// 分页查询
public class PageUtil<T> {
//    List<T> resultList;
    private T body;
    private int totalElement;

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
}
