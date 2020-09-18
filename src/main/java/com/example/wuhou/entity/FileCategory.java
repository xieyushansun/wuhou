package com.example.wuhou.entity;

import org.springframework.stereotype.Repository;
import java.util.List;

@Repository

public class FileCategory {
    //案卷类别
    String fileCategory;
    //持续时间
    String duration;
    //涉及科室
    String department;
    //文件清单
    List<String> filelist;
    //档案类别
    String documentCategory;

    public String getFileCategory() {
        return fileCategory;
    }

    public void setFileCategory(String fileCategory) {
        this.fileCategory = fileCategory;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<String> getFilelist() {
        return filelist;
    }

    public void setFilelist(List<String> filelist) {
        this.filelist = filelist;
    }

    public String getDocumentCategory() {
        return documentCategory;
    }

    public void setDocumentCategory(String documentCategory) {
        this.documentCategory = documentCategory;
    }
}
