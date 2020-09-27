package com.example.wuhou.entity;

import java.util.Arrays;

public class DocumentCategory {
    //数据库中的id
    private String id;
    //档案类别
    private String documentCategory;
    //档案类别缩写
    private String documentCategoryShortName;
    //案卷类别
    private String[] fileCategory;

    public DocumentCategory(){
        this.id = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocumentCategory() {
        return documentCategory;
    }

    public void setDocumentCategory(String documentCategory) {
        this.documentCategory = documentCategory;
    }

    public String getDocumentCategoryShortName() {
        return documentCategoryShortName;
    }

    public void setDocumentCategoryShortName(String documentCategoryShortName) {
        this.documentCategoryShortName = documentCategoryShortName;
    }

    public String[] getFileCategory() {
        return fileCategory;
    }

    public void setFileCategory(String[] fileCategory) {
        this.fileCategory = fileCategory;
    }

    @Override
    public String toString() {
        return "DocumentCategory{" +
                "id='" + id + '\'' +
                ", documentCategory='" + documentCategory + '\'' +
                ", documentCategoryShortName='" + documentCategoryShortName + '\'' +
                ", fileCategory=" + Arrays.toString(fileCategory) +
                '}';
    }
}
