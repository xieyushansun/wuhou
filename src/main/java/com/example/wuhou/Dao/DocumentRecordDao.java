package com.example.wuhou.Dao;

import com.example.wuhou.entity.DocumentFile;
import com.example.wuhou.entity.DocumentRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.rmi.server.ExportException;

@Repository
public class DocumentRecordDao {
    @Autowired
    MongoTemplate mongoTemplate;
    public String addDocumentRecord(DocumentRecord documentRecord) throws Exception {
        DocumentRecord documentRecordReturn = mongoTemplate.insert(documentRecord);
        if (documentRecordReturn == null){
            throw new Exception("插入失败");
        }
        return documentRecordReturn.getId();
    }
    public void addDocumentRecordFile(String documentRecordId, MultipartFile[] filelist) throws Exception {
        DocumentFile documentFile = new DocumentFile();
        if (filelist.length == 0){
            throw new Exception("没有文件，上传失败！");
        }
        for (int i = 0; i < filelist.length; i++){
            if (filelist[i] == null){
                throw new Exception("存在空文件，上传失败！");
            }
            if (filelist[i] != null){
                byte[] bytesfile = filelist[i].getBytes();
                documentFile.setFile(bytesfile);
                if (mongoTemplate.insert(documentFile) == null){
                    throw new Exception("数据库插入失败！");
                }
            }
        }
    }
    public void deleteDocumentRecord(){

    }
}
