package com.example.wuhou.entity;

import org.springframework.stereotype.Repository;

@Repository
public class DocumentFile {
    String documentRecordId;
    String documentName;
    byte[] file;
    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
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
