package com.example.wuhou.Dao;

import com.example.wuhou.entity.Log;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class testDao {
    @Autowired
    MongoTemplate mongoTemplate;
    public void test(){
//        String strQurey = "";
//        BasicQuery query = new BasicQuery("{ age : { $lt : 50 }, accounts.balance : { $gt : 1000.00 }}");
        BasicDBList basicDBList = new BasicDBList();
        basicDBList.add(new BasicDBObject("operationType", "添加"));
        basicDBList.add(new BasicDBObject("table","user"));

        DBObject obj = new BasicDBObject();
        obj.put("$and", basicDBList);

        Query query = new BasicQuery(obj.toString());

        List<Log> logList = mongoTemplate.find(query, Log.class);

        for (Log log:logList) {
            System.out.println(log.toString());
        }


    }
}
