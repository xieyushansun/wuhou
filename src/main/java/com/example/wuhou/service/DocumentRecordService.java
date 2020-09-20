package com.example.wuhou.service;

import com.example.wuhou.Dao.DocumentRecordDao;
import com.example.wuhou.entity.DocumentFile;
import com.example.wuhou.entity.DocumentRecord;
import com.example.wuhou.exception.NotExistException;
import org.bson.BsonDocument;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentRecordService {
    @Autowired
    DocumentRecordDao documentRecordDao;
    public String addDocumentRecord(DocumentRecord documentRecord) throws Exception {
        //返回文档记录在数据库中的id
        String documentRecordId = documentRecordDao.addDocumentRecord(documentRecord);
        return documentRecordId;
    }
    public void deleteDocumentRecord(String documentRecordId) throws NotExistException {
         documentRecordDao.deleteDocumentRecord(documentRecordId);
    }
    public void addDocumentRecordFile(String documentRecordId, MultipartFile[] filelist) throws Exception {
        documentRecordDao.addDocumentRecordFile(documentRecordId, filelist);
    }
    public DocumentFile downLoadDocumentRecordFile(String fileId){
        return documentRecordDao.downLoadDocumentRecordFile(new ObjectId(fileId));
    }
    public List<String> findFileListByFileName(String fileCategory){
        return documentRecordDao.findFileListByFileNumber(fileCategory);
    }
}
