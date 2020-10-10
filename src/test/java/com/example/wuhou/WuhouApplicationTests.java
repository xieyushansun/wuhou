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
//        String inputUrl = "C:\\Users\\DF\\Desktop\\附录B模板_B2.docx";
//        String outputUrl = "C:\\Users\\DF\\Desktop\\test6.docx";
////        try {
////            //解析docx模板并获取document对象
////            XWPFDocument document = new XWPFDocument(POIXMLDocument.openPackage(inputUrl));
////            //获取整个文本对象
////            List<XWPFParagraph> allParagraph = document.getParagraphs();
////
////            //获取XWPFRun对象输出整个文本内容
////            StringBuffer tempText = new StringBuffer();
////            for (XWPFParagraph xwpfParagraph : allParagraph) {
////                List<XWPFRun> runList = xwpfParagraph.getRuns();
////                for (XWPFRun xwpfRun : runList) {
////                    tempText.append(xwpfRun.toString());
////                }
////            }
////            System.out.println(tempText.toString());
////
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        try {
////            StringBuffer tableText = new StringBuffer();
////            //解析docx模板并获取document对象
////            XWPFDocument document = new XWPFDocument(POIXMLDocument.openPackage(inputUrl));
////            //获取全部表格对象
////            List<XWPFTable> allTable = document.getTables();
////
////            for (XWPFTable xwpfTable : allTable) {
////                //获取表格行数据
////                List<XWPFTableRow> rows = xwpfTable.getRows();
////                for (XWPFTableRow xwpfTableRow : rows) {
////                    //获取表格单元格数据
////                    List<XWPFTableCell> cells = xwpfTableRow.getTableCells();
////                    for (XWPFTableCell xwpfTableCell : cells) {
////                        List<XWPFParagraph> paragraphs = xwpfTableCell.getParagraphs();
////                        for (XWPFParagraph xwpfParagraph : paragraphs) {
////                            List<XWPFRun> runs = xwpfParagraph.getRuns();
////                            for(int i = 0; i < runs.size();i++){
////                                XWPFRun run = runs.get(i);
////                                tableText.append(run.toString());
////                            }
////                        }
////                    }
////                }
////            }
////
////            System.out.println(tableText.toString());
////
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//        //替换word中的文字
////        try {
////            //获取word文档解析对象
////            XWPFDocument doucument = new XWPFDocument(POIXMLDocument.openPackage(inputUrl));
////            //获取段落文本对象
////            List<XWPFParagraph> paragraph = doucument.getParagraphs();
////            //获取首行run对象
////            XWPFRun run = paragraph.get(0).getRuns().get(0);
////            //设置文本内容
////            run.setText("修改了的word");
////            //生成新的word
////            File file = new File(outputUrl);
////
////            FileOutputStream stream = new FileOutputStream(file);
////            doucument.write(stream);
////            stream.close();
////
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//
//        Map<String, String> testMap = new HashMap<String, String>();
//        testMap.put("danwieCode", "111");
//        testMap.put("danweiName", "222");
//        testMap.put("fileName", "333");
//        testMap.put("recordTime", "444");
//        testMap.put("jian", "777");
//        testMap.put("page", "888");
//
//
//        List<String[]> testList = new ArrayList<String[]>();
//        testList.add(new String[]{"1","1AA","1BB","1CC"});
//        testList.add(new String[]{"2","2AA","2BB","2CC"});
//        testList.add(new String[]{"3","3AA","3BB","3CC"});
//        testList.add(new String[]{"4","4AA","4BB","4CC"});
//
//        WorderToNewWordUtils.changWord(inputUrl, outputUrl, testMap, testList);

//        /*BasicDBList basicDBList = new BasicDBList();
//        basicDBList.add(new BasicDBObject("operationType", "添加"));
//        basicDBList.add(new BasicDBObject("table","user"));
//
//        DBObject obj = new BasicDBObject();
//        obj.put("$and", basicDBList);
//
//        Query query = new BasicQuery(obj.toString());
//        System.out.println(obj.toString());
//        List<Log> logList = mongoTemplate.find(query, Log.class);
//
//        for (Log log:logList) {
//            System.out.println(log.toString());
//        }*/
        //测试1——通过
//        String sql1 = "{ operationTime:{ $lt: \"2020-09-30 10:37:02\" }, operationTime : { $gt : \"2020-09-25 10:37:02\" }}";
//        String sql2 = "{ \"$and\":[{\"operationTime\":{ \"$lt\": \"2020-09-30 10:37:02\" }}, {\"operationTime\" : { \"$gt\" : \"2020-09-25 10:37:02\" }}]}";
//        String sql3 = "{" +
//                "$and:["+
//                    "{$and: [{operationType: \"添加加\"}, {table: /操作/}]}, " +
//                    "{$and:[{operationTime:{ $lt: \"2020-10-03 10:37:02\" }}, {operationTime : { $gt: \"2020-09-25 10:37:02\" }}]}" +
//                "]"+
//                "}";
//
//        BasicQuery query = new BasicQuery(sql3);
//        List<Log> result = mongoTemplate.find(query, Log.class);
//        System.out.println(result.size());
//        for (Log log:result) {
//            System.out.println(log.toString());
//        }
//        String cmd = "mongodump -u root -p root -d wuhou2 -o C:\\Users\\DF\\Desktop";
//        Runtime rt = Runtime.getRuntime();
//        String[] command = new String[]{"cmd.exe" , "/C" , cmd};
//        try {
//            rt.exec(command);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        File file = new File(".\\src\\main\\resources\\static\\webapp\\wordtemplate");
        System.out.println(System.getProperty("user.dir"));
        System.out.println(file.isDirectory());
    }
}
