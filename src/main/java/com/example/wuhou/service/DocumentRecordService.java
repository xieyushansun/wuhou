package com.example.wuhou.service;

import com.example.wuhou.Dao.DocumentRecordDao;
import com.example.wuhou.entity.DocumentRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DocumentRecordService {
    @Autowired
    DocumentRecordDao documentRecordDao;
    public String addDocumentRecord(DocumentRecord documentRecord) throws Exception {
        //返回文档记录在数据库中的id
        String documentRecordId = documentRecordDao.addDocumentRecord(documentRecord);
        return documentRecordId;
    }
    public void addDocumentRecordFile(String documentRecordId, MultipartFile[] filelist) throws Exception {
        documentRecordDao.addDocumentRecordFile(documentRecordId, filelist);
    }
}
