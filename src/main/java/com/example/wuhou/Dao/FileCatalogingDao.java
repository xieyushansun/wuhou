package com.example.wuhou.Dao;

import com.example.wuhou.entity.DocumentRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class FileCatalogingDao {
    @Autowired
    MongoTemplate mongoTemplate;
    public List<DocumentRecord> getDocumentRecordBydocumentNumber(String documentNumber){
        Query query = new Query();
        Criteria criteria = Criteria.where("documentNumber").is(documentNumber);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, DocumentRecord.class);
    }
}
