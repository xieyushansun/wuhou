package com.example.wuhou.Dao;

import com.example.wuhou.entity.User;
import com.example.wuhou.entity.FileCategory;
import com.example.wuhou.exception.ExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class FileCategoryDao {
    @Autowired
    MongoTemplate mongoTemplate;
    public void addFileCategory(FileCategory fileCategory) throws ExistException {
        Query query = new Query();
        Criteria criteria = Criteria.where("fileCategory").is(fileCategory.getFileCategory());
        query.addCriteria(criteria);
        FileCategory fileCategory1 = mongoTemplate.findOne(query, FileCategory.class);
        if (fileCategory1 != null) {
            throw new ExistException("该案卷类别已存在!");
        }
        mongoTemplate.insert(fileCategory);
    }
}
