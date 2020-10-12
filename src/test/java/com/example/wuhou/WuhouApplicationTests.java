package com.example.wuhou;

import com.example.wuhou.entity.Log;
import com.example.wuhou.util.WorderToNewWordUtils;
import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.internal.MongoClientImpl;
import org.apache.poi.xwpf.usermodel.*;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoActionOperation;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
class WuhouApplicationTests {
    @Autowired
    MongoTemplate mongoTemplate;
    @Test
    void contextLoads() {
        File file = new File(".\\src\\main\\resources\\static\\webapp\\wordtemplate");
        System.out.println(System.getProperty("user.dir"));
        System.out.println(file.isDirectory());
    }
}
