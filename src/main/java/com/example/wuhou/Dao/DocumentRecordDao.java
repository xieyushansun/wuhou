package com.example.wuhou.Dao;

import com.example.wuhou.entity.DocumentFile;
import com.example.wuhou.entity.DocumentRecord;
import com.example.wuhou.entity.FileCategory;
import com.example.wuhou.exception.NotExistException;
import com.example.wuhou.util.FileOperationUtil;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.*;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class DocumentRecordDao {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    private GridFsTemplate gridFsTemplate;

    //添加档案记录
    public String addDocumentRecord(DocumentRecord documentRecord) throws Exception {
        DocumentRecord documentRecordReturn = mongoTemplate.insert(documentRecord);
        if (documentRecordReturn == null){
            throw new Exception("插入失败");
        }
        return documentRecordReturn.getId();
    }
    //查询案卷号对应的文件清单
    public List<String> findFileListByFileNumber(String fileCategory){
        List<String> fileList = new ArrayList<>();
        Query query = new Query();
        Criteria criteria = Criteria.where("fileCategory").is(fileCategory);
        List<FileCategory> list = mongoTemplate.find(query, FileCategory.class);
        if (list.get(0) != null){
            fileList = list.get(0).getFilelist();
        }
        return fileList;
    }
    //删除文件记录
    public void deleteDocumentRecord(String documentRecordId) throws NotExistException {
        Query query1 = new Query();
        Criteria criteria1 = Criteria.where("_id").is(new ObjectId(documentRecordId));
        query1.addCriteria(criteria1);
        DocumentRecord documentRecord = mongoTemplate.findOne(query1, DocumentRecord.class);
        if (documentRecord == null){
            throw new NotExistException(documentRecordId + ":没有该条档案记录\n");
        }
        mongoTemplate.remove(query1, DocumentRecord.class);
        //删除档案记录对应的文件
        Query query2 = new Query();
        Criteria criteria2 = Criteria.where("documentRecordId").is(documentRecordId);
        query2.addCriteria(criteria2);
        mongoTemplate.remove(query2, DocumentFile.class);
    }
    //添加档案文件
    public void addDocumentRecordFile(HttpServletRequest request, String documentRecordId, MultipartFile[] filelist) throws Exception {
//        DocumentFile documentFile = new DocumentFile();
//        if (filelist.length == 0){
//            throw new Exception("没有文件，上传失败！");
//        }
//        for (MultipartFile multipartFile : filelist) {
//            if (multipartFile == null) {
//                throw new Exception("存在空文件，上传失败！");
//            }
//            byte[] bytesfile = multipartFile.getBytes();
//            documentFile.setFile(bytesfile);
//            documentFile.setDocumentName(multipartFile.getOriginalFilename());
//            documentFile.setDocumentRecordId(documentRecordId);
//            if (mongoTemplate.insert(documentFile) == null) {
//                throw new Exception("数据库插入失败！");
//            }
//        }


    }
    //下载档案文件
    public DocumentFile downLoadDocumentRecordFile(ObjectId fileId) {
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(fileId);
        query.addCriteria(criteria);
        DocumentFile documentFile = mongoTemplate.findOne(query, DocumentFile.class);
        return documentFile;
//        FileOutputStream fileOutputStream = FileOperationUtil.bytesToFile(documentFile.getFile(), documentFile.getDocumentName());
//        return fileOutputStream;
    }
}
