package com.example.wuhou.Dao;

import com.example.wuhou.entity.DocumentCategory;
import com.example.wuhou.exception.ExistException;
import com.example.wuhou.exception.NotExistException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DocumentCategoryDao {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    LogDao logDao;
    public void addDocumentCategory(DocumentCategory documentCategory) throws Exception {
        Query query = new Query();
        Criteria criteria = Criteria.where("DocumentCategory").is(documentCategory.getDocumentCategory());
        query.addCriteria(criteria);
        DocumentCategory documentCategory1 = mongoTemplate.findOne(query, DocumentCategory.class);
        if (documentCategory1 != null) {
            throw new ExistException("该档案类别已存在!");
        }
        mongoTemplate.insert(documentCategory);
        logDao.insertLog("documentCategory", "添加", "添加档案类别:" + documentCategory.toString());
    }
    public void deleteDocumentCategory(String id) throws Exception {
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(new ObjectId(id));
        query.addCriteria(criteria);
        DocumentCategory documentCategory = mongoTemplate.findOne(query, DocumentCategory.class);
        if (documentCategory == null) {
            throw new NotExistException("该档案类别不存在!");
        }
        mongoTemplate.remove(documentCategory);
        logDao.insertLog("documentCategory", "删除", "删除档案类别 " + documentCategory.toString());
    }
    public void modifyDocumentCategory(DocumentCategory documentCategory) throws Exception {
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(new ObjectId(documentCategory.getId()));
        query.addCriteria(criteria);
        DocumentCategory DBdocumentCategory = mongoTemplate.findOne(query, DocumentCategory.class);
        if (DBdocumentCategory == null){
            throw new NotExistException("该档案类别不存在!");
        }
        //将数据库中对应id的数据删除
        mongoTemplate.remove(DBdocumentCategory);
        logDao.insertLog("documentCategory", "删除", "删除档案类别id为 " + documentCategory.getId());
        //重新插入修改后的
        mongoTemplate.insert(documentCategory);
        logDao.insertLog("documentCategory", "添加", "添加档案类别:" + documentCategory.toString());
    }
    public List<DocumentCategory> findAllDocumentCategory(){
        List<DocumentCategory> documentCategories;
        documentCategories = mongoTemplate.findAll(DocumentCategory.class);
        return documentCategories;
    }
}
