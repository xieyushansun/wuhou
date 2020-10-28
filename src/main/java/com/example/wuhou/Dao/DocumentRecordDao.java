package com.example.wuhou.Dao;

import com.example.wuhou.entity.DocumentRecord;
import com.example.wuhou.util.PageUtil;
import com.example.wuhou.exception.NotExistException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Repository
public class DocumentRecordDao {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    LogDao logDao;
    //添加档案记录
    public String addDocumentRecord(DocumentRecord documentRecord) throws Exception {
        DocumentRecord documentRecordReturn = mongoTemplate.insert(documentRecord);
        if (documentRecordReturn == null){
            throw new Exception("插入失败");
        }
        logDao.insertLog("documentRecord", "添加", "添加档案记录:" + documentRecord.toString());
        return documentRecordReturn.getId();
    }
    //删除文件记录
    public String deleteDocumentRecord(String documentRecordId) throws Exception {
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(new ObjectId(documentRecordId));
        query.addCriteria(criteria);
        DocumentRecord documentRecord = mongoTemplate.findOne(query, DocumentRecord.class);
        if (documentRecord == null){
            throw new NotExistException(documentRecordId + ":没有该条档案记录\n");
        }
        mongoTemplate.remove(query, DocumentRecord.class);
        logDao.insertLog("documentRecord", "删除", "删除档案记录:" + documentRecord.toString());
        //返回档案记录对应的文件，在service层删除
        return documentRecord.getDiskPath() + ":\\" + documentRecord.getStorePath();
    }
    //添加档案文件
    public DocumentRecord getDocumentRecordById(String documentRecordId){
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
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(new ObjectId(documentRecordId));
        query.addCriteria(criteria);
        DocumentRecord documentRecord = mongoTemplate.findOne(query, DocumentRecord.class);
        return documentRecord;
    }

    public String checkFileName(String fileName){
        Query query = new Query();
        Criteria criteria = Criteria.where("fileName").is(fileName);
        query.addCriteria(criteria);
        DocumentRecord documentRecord = mongoTemplate.findOne(query, DocumentRecord.class);
        if (documentRecord == null){
            return "";
        }else {
            return documentRecord.getId();
        }
    }
    public void ModifyDocumentRecord(DocumentRecord documentRecord) throws Exception {
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(new ObjectId(documentRecord.getId()));
        query.addCriteria(criteria);
        Update update = Update.update("fileName", documentRecord.getFileName())
                .set("documentNumber", documentRecord.getDocumentNumber())
                .set("recordGroupNumber", documentRecord.getRecordGroupNumber())
                .set("boxNumber", documentRecord.getBoxNumber())
                .set("year", documentRecord.getYear())
                .set("duration", documentRecord.getDuration())
                .set("security", documentRecord.getSecurity())
                .set("documentCategory", documentRecord.getDocumentCategory())
                .set("fileCategory", documentRecord.getFileCategory())
                .set("responsible", documentRecord.getResponsible())
                .set("danweiCode", documentRecord.getDanweiCode())
                .set("danweiName", documentRecord.getDanweiName())
                .set("position", documentRecord.getPosition())
                .set("recorder", documentRecord.getRecorder())
                .set("recordTime", documentRecord.getRecordTime())
                .set("generateTime", documentRecord.getGenerateTime())
                .set("diskPath", documentRecord.getDiskPath())
                .set("storePath", documentRecord.getStorePath())
                .set("order", documentRecord.getOrder())
                .set("pageNumber", documentRecord.getPageNumber())
                .set("sex", documentRecord.getSex());
        mongoTemplate.updateFirst(query, update, DocumentRecord.class);
        logDao.insertLog("documentRecord", "修改", "修改案卷题名为" + documentRecord.getFileName() + " 的档案记录内容");
    }
}