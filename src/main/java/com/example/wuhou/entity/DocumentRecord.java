package com.example.wuhou.entity;

import org.springframework.stereotype.Repository;

@Repository
public class DocumentRecord {
    //记录在数据库中的id
    String id;
    //档号
    String documentNumber;
    //全宗号
    String recordGroupNumber;
    //目录号
    String catalogNumber;
    //案卷号
    String fileNumber;
    //年度
    String year;
    //保管期限
    String duration;
    //密级
    String security;
    //档案类别
    String documentCategory;
    //案卷类型
    String fileCategory;
    //责任者
    String responsible;
    //案卷题名
    String fileName;
    //分类号
    String classNumber;
    //存放位置
    String storagePath;
    //著录人
    String recorder;
    //著录时间
    String recordTime;

    public DocumentRecord(){
        this.id = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getRecordGroupNumber() {
        return recordGroupNumber;
    }

    public void setRecordGroupNumber(String recordGroupNumber) {
        this.recordGroupNumber = recordGroupNumber;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getDocumentCategory() {
        return documentCategory;
    }

    public void setDocumentCategory(String documentCategory) {
        this.documentCategory = documentCategory;
    }

    public String getFileCategory() {
        return fileCategory;
    }

    public void setFileCategory(String fileCategory) {
        this.fileCategory = fileCategory;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(String classNumber) {
        this.classNumber = classNumber;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getRecorder() {
        return recorder;
    }

    public void setRecorder(String recorder) {
        this.recorder = recorder;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }
}
