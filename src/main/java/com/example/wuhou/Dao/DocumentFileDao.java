package com.example.wuhou.Dao;

import com.example.wuhou.entity.DocumentRecord;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Repository
public class DocumentFileDao {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    LogDao logDao;
    //查询案卷号对应的文件清单
    public List<String> findFileListByDocumentRecordId(String DocumentRecordId) throws Exception {
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(new ObjectId(DocumentRecordId));
        query.addCriteria(criteria);
        DocumentRecord documentRecord = mongoTemplate.findOne(query, DocumentRecord.class);
        if (documentRecord == null){
            throw new Exception("没有这条记录！");
        }
        String recordPath = documentRecord.getDiskPath() + ":\\" + documentRecord.getStorePath();
        File file = new File(recordPath);
        if (!file.exists()){
            throw new Exception("路径: " + recordPath + " 不存在");
        }
        List<String> fileList = null;
        if (file.isDirectory()){
//            fileList = list.get(0).getFilelist();
            fileList = Arrays.asList(file.list());
        }
        return fileList;
    }
}
