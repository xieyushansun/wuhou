package com.example.wuhou.entity;

public class Log {
    //id
    private String id;
    // 操作时间
    private String operationTime;
    // 操作者Id
    private String operatorId;
    //操作类型
    private String operationType;
    // 操作表
    private String table;
    // 操作内容
    private String msg;

    public Log(){
        this.id = null;
    }

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
