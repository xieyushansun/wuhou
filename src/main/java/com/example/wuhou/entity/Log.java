package com.example.wuhou.entity;

public class Log {
    //id
    String id;
    // 操作时间
    String operationTime;
    // 操作者Id
    String operatorId;
    //操作类型
    String operationType;
    // 操作表
    String table;
    // 操作内容
    String msg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Log{" +
                "operationTime='" + operationTime + '\'' +
                ", operationType='" + operationType + '\'' +
                ", operatorId='" + operatorId + '\'' +
                ", table='" + table + '\'' +
                '}';
    }
}
