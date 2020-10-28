package com.example.wuhou.entity;

public class DocumentRecord {
    //记录在数据库中的id
    private String id;
    //案卷题名（描述/标题）
    private String fileName;
    //档号
    private String documentNumber;
    //全宗号
    private String recordGroupNumber;
    //盒号
    private String boxNumber;
    //年份
    private String year;
    //保管期限
    private String duration;
    //密级
    private String security;
    //档案类别
    private String documentCategory;
    //案卷类型
    private String fileCategory;
    //责任者
    private String responsible;
    //单位代码
    private String danweiCode;
    //单位名称
    private String danweiName;
    //档案室中的存放位置
    private String position;
    //产生时间
    private String generateTime;
    //著录人
    private String recorder;
    //著录时间
    private String recordTime;
    //所在盘符路径
    private String diskPath;
    //盘符下的存储路径
    private String storePath;
    //序号
    private String order;
    //页码
    private String pageNumber;
    //性别
    private String sex;


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

    public String getDanweiCode() {
        return danweiCode;
    }

    public void setDanweiCode(String danweiCode) {
        this.danweiCode = danweiCode;
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

    public String getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(String generateTime) {
        this.generateTime = generateTime;
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

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "DocumentRecord{" +
                "案卷题名='" + fileName + '\'' +
                ", 档号='" + documentNumber + '\'' +
                ", 全宗号='" + recordGroupNumber + '\'' +
                ", 盒号='" + boxNumber + '\'' +
                ", 年份='" + year + '\'' +
                ", 保管期限='" + duration + '\'' +
                ", 密级='" + security + '\'' +
                ", 档案类别='" + documentCategory + '\'' +
                ", 案卷类型='" + fileCategory + '\'' +
                ", 责任者='" + responsible + '\'' +
                ", 单位代码='" + danweiCode + '\'' +
                ", 单位名称='" + danweiName + '\'' +
                ", 存放位置='" + position + '\'' +
                ", 产生时间='" + generateTime + '\'' +
                ", 著录人='" + recorder + '\'' +
                ", 著录时间='" + recordTime + '\'' +
                ", 所在盘符='" + diskPath + '\'' +
                ", 盘符下的存储路径='" + storePath + '\'' +
                ", 性别='" + sex + '\'' +
                ", 序号='" + order + '\'' +
                ", 页码='" + pageNumber + '\'' +
                '}';
    }
}
