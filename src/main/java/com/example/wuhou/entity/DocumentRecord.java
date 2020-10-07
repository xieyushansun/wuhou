package com.example.wuhou.entity;

public class DocumentRecord {
    //记录在数据库中的id
    String id;
    //案卷题名（描述/标题）
    String fileName;
    //档号
    String documentNumber;
    //全宗号
    String recordGroupNumber;
    //盒号
    String boxNumber;
    //年份
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
    //单位代码
    String danwieCode;
    //单位名称
    String danweiName;
    //档案室中的存放位置
    String position;
    //著录人
    String recorder;
    //著录时间
    String recordTime;
    //所在盘符路径
    String diskPath;
    //盘符下的存储路径
    String storePath;

    public DocumentRecord(){
        this.id = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public String getBoxNumber() {
        return boxNumber;
    }

    public void setBoxNumber(String boxNumber) {
        this.boxNumber = boxNumber;
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

    public String getDanwieCode() {
        return danwieCode;
    }

    public void setDanwieCode(String danwieCode) {
        this.danwieCode = danwieCode;
    }

    public String getDanweiName() {
        return danweiName;
    }

    public void setDanweiName(String danweiName) {
        this.danweiName = danweiName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

    public String getDiskPath() {
        return diskPath;
    }

    public void setDiskPath(String diskPath) {
        this.diskPath = diskPath;
    }

    public String getStorePath() {
        return storePath;
    }

    public void setStorePath(String storePath) {
        this.storePath = storePath;
    }

    @Override
    public String toString() {
        return "DocumentRecord{" +
                "id='" + id + '\'' +
                ", fileName='" + fileName + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", recordGroupNumber='" + recordGroupNumber + '\'' +
                ", boxNumber='" + boxNumber + '\'' +
                ", year='" + year + '\'' +
                ", duration='" + duration + '\'' +
                ", security='" + security + '\'' +
                ", documentCategory='" + documentCategory + '\'' +
                ", fileCategory='" + fileCategory + '\'' +
                ", responsible='" + responsible + '\'' +
                ", danwieCode='" + danwieCode + '\'' +
                ", danweiName='" + danweiName + '\'' +
                ", position='" + position + '\'' +
                ", recorder='" + recorder + '\'' +
                ", recordTime='" + recordTime + '\'' +
                ", diskPath='" + diskPath + '\'' +
                ", storePath='" + storePath + '\'' +
                '}';
    }
}
