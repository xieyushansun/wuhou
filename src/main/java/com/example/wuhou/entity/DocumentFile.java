package com.example.wuhou.entity;

import org.springframework.stereotype.Repository;

@Repository
public class DocumentFile {
    String documentRecordId;
    byte[] file;

    public DocumentFile(){
        documentRecordId = null;
    }

    public String getDocumentRecordId() {
        return documentRecordId;
    }

    public void setDocumentRecordId(String documentRecordId) {
        this.documentRecordId = documentRecordId;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}
