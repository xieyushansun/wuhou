package com.example.wuhou.Dao;

import com.example.wuhou.entity.DocumentRecord;
import com.example.wuhou.util.PageUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DocumentRecordFindDao {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    LogDao logDao;
    //普通查询
    public PageUtil normalFindDocumentRecord(Map<String, String> findKeyWordMap, String blurryFind, Integer currentPage, Integer pageSize){
        List<DocumentRecord> resultList;
        Query query = new Query();
        if (blurryFind.compareTo("0") == 0){ //精确
            for (String key : findKeyWordMap.keySet()) {
                Criteria criteria = Criteria.where(key).is(findKeyWordMap.get(key));
                query.addCriteria(criteria);
            }
        }else {
            for (String key : findKeyWordMap.keySet()) {
                Criteria criteria = Criteria.where(key).regex(".*?" + findKeyWordMap.get(key) + ".*?");
                query.addCriteria(criteria);
            }
        }
        PageUtil pageUtil = new PageUtil();
        pageUtil.setTotalElement((int) mongoTemplate.count(query, DocumentRecord.class));
        query.skip((currentPage - 1) * pageSize);
        query.limit(pageSize);
        resultList = mongoTemplate.find(query, DocumentRecord.class);
        pageUtil.setBody(resultList);
        return pageUtil;
    }
    // 一般查询
    public PageUtil generalFindDocumentRecord(String multiKeyWord, String blurryFind, Integer currentPage, Integer pageSize){
        PageUtil pageUtil = new PageUtil();

        // 用空格隔开的各个关键字
        String keyWord[] = multiKeyWord.split(" ");
        StringBuilder sqlStart = new StringBuilder("{$and:[");
        String sqlEnd = "]}";
        // 模糊查询
        if (blurryFind.compareTo("1") == 0){
            for (String s : keyWord) {
                String temp = "{$or: [" +
                        "{documentNumber: /" + s + "/}," +
                        "{duration: /" + s + "/}," +
                        "{security: /" + s + "/}," +
                        "{responsible: /" + s + "/}," +
                        "{danweiCode: /" + s + "/}," +
                        "{danweiName: /" + s + "/}," +
                        "{position: /" + s + "/}," +
                        "{generateTime: /" + s + "/}," +
                        "{recorder: /" + s + "/}," +
                        "{recordTime: /" + s + "/}," +
                        "{order: /" + s + "/}," +
                        "{pageNumber: /" + s + "/}," +
                        "{sex: /" + s + "/}," +
                        "{storePath: /" + s + "/}]}";
                sqlStart.append(temp);
            }
            sqlStart.append(sqlEnd);
        }
        else { // 精确查询
            for (String s : keyWord) {
                String temp = "{$or: [" +
                        "{documentNumber: " + s + "}," +
                        "{duration: " + s + "}," +
                        "{security: " + s + "}," +
                        "{responsible: " + s + "}," +
                        "{danweiCode: " + s + "}," +
                        "{danweiName: " + s + "}," +
                        "{position: " + s + "}," +
                        "{recorder: " + s + "}," +
                        "{recordTime: " + s + "}," +
                        "{order: /" + s + "/}," +
                        "{pageNumber: /" + s + "/}," +
                        "{sex: /" + s + "/}," +
                        "{generateTime: /" + s + "/}," +
                        "{storePath: " + s + "}]}";
                sqlStart.append(temp);
            }
            sqlStart.append(sqlEnd);
        }

        BasicQuery basicQuery = new BasicQuery(sqlStart.toString());

        pageUtil.setTotalElement((int) mongoTemplate.count(basicQuery, DocumentRecord.class));

        basicQuery.skip((currentPage - 1) * pageSize);
        basicQuery.limit(pageSize);

        pageUtil.setBody(mongoTemplate.find(basicQuery, DocumentRecord.class));
        return pageUtil;
    }
    // 组合查询
    // keywordList: 字段名，字段内容，运算符，连接符
    public PageUtil combinationFindDocumentRecord(JSONArray jsonArray, String blurryFind, Integer currentPage, Integer pageSize) throws Exception {
        PageUtil pageUtil = new PageUtil();
        /**
         * jsonArray
         * fieldName : 字段名
         * fieldContent : 字段内容
         * operator :
         * {
         *      $gt:大于
         *      $lt:小于
         *      $gte:大于或等于
         *      $lte:小于或等于
         * }
         * joiner : AND/OR
         * */
        String lastSql = "";
        String currentSql = "";

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String fieldName = jsonObject.get("fieldName").toString();
            String fieldContent = jsonObject.get("fieldContent").toString();
            String operator = jsonObject.get("operator").toString();
            String joiner = jsonObject.get("joiner").toString();
            if (fieldName.equals("recordTime") || fieldName.equals("year") || fieldName.equals("generateTime")) {
                if (i == 0) {
                    switch (operator) {
                        case "gt": currentSql = String.format("{ %s:{ $gt: \"%s\" }}", fieldName, fieldContent);break;
                        case "lt": currentSql = String.format("{ %s:{ $lt: \"%s\" }}", fieldName, fieldContent);break;
                        case "gte": currentSql = String.format("{ %s:{ $gte: \"%s\" }}", fieldName, fieldContent);break;
                        case "lte": currentSql = String.format("{ %s:{ $lte: \"%s\" }}", fieldName, fieldContent);break;
                        case "is": currentSql = String.format("{ %s:\"%s\"}", fieldName, fieldContent);break;
                        default: throw new Exception("操作符传递出错");
                    }
                } else {
                    if (joiner.equals("AND")) {
                        switch (operator) {
                            case "gt": currentSql = "{$and:[" + lastSql + ", " + String.format("{ %s:{ $gt: \"%s\" }}", fieldName, fieldContent) + "]}";break;
                            case "lt": currentSql = "{$and:[" + lastSql + ", " + String.format("{ %s:{ $lt: \"%s\" }}", fieldName, fieldContent) + "]}";break;
                            case "gte": currentSql = "{$and:[" + lastSql + ", " + String.format("{ %s:{ $gte: \"%s\" }}", fieldName, fieldContent) + "]}";break;
                            case "lte": currentSql = "{$and:[" + lastSql + ", " + String.format("{ %s:{ $lte: \"%s\" }}", fieldName, fieldContent) + "]}";break;
                            case "is": currentSql = "{$and:[" + lastSql + ", " + String.format("{ %s:\"%s\"}", fieldName, fieldContent) + "]}";break;
                            default: throw new Exception("操作符传递出错");
                        }
                    }else {
                        switch (operator) {
                            case "gt": currentSql = "{$or:[" + lastSql + ", " + String.format("{ %s:{ $gt: \"%s\" }}", fieldName, fieldContent) + "]}";break;
                            case "lt": currentSql = "{$or:[" + lastSql + ", " + String.format("{ %s:{ $lt: \"%s\" }}", fieldName, fieldContent) + "]}";break;
                            case "gte": currentSql = "{$or:[" + lastSql + ", " + String.format("{ %s:{ $gte: \"%s\" }}", fieldName, fieldContent) + "]}";break;
                            case "lte": currentSql = "{$or:[" + lastSql + ", " + String.format("{ %s:{ $lte: \"%s\" }}", fieldName, fieldContent) + "]}";break;
                            case "is": currentSql = "{$or:[" + lastSql + ", " + String.format("{ %s:\"%s\"}", fieldName, fieldContent) + "]}";break;
                            default: throw new Exception("操作符传递出错");
                        }
                    }
                }
            } else {
                if (i == 0) {
                    if(blurryFind.equals("1")){
                        currentSql = String.format("{%s: /%s/}", fieldName, fieldContent);
                    }else {
                        currentSql = String.format("{%s: \"%s\"}", fieldName, fieldContent);
                    }
                } else {
                    if (joiner.equals("AND")) {
                        if (blurryFind.equals("1")){
                            currentSql = "{$and:[" + lastSql + ", " + String.format("{%s: /%s/}", fieldName, fieldContent) + "]}";
                        }else {
                            currentSql = "{$and:[" + lastSql + ", " + String.format("{%s: \"%s\"}", fieldName, fieldContent) + "]}";
                        }

                    } else {
                        if (blurryFind.equals("1")){
                            currentSql = "{$or:[" + lastSql + ", " + String.format("{%s: /%s/}", fieldName, fieldContent) + "]}";
                        }else {
                            currentSql = "{$or:[" + lastSql + ", " + String.format("{%s: \"%s\"}", fieldName, fieldContent) + "]}";
                        }
                    }
                }
            }
            lastSql = currentSql;
        }
//        String s = "{$and:[{$or:[{ year:\"2021\"}, { year:\"2020\"}]}, {recorder: \"杉杉\"}]}";
        BasicQuery basicQuery = new BasicQuery(currentSql);
        // 获取总数
        int n = (int) mongoTemplate.count(basicQuery, DocumentRecord.class);
        pageUtil.setTotalElement(n);
        // 分页查询
        basicQuery.skip((currentPage - 1) * pageSize);
        basicQuery.limit(pageSize);
        List<DocumentRecord> documentRecordList = mongoTemplate.find(basicQuery, DocumentRecord.class);
        pageUtil.setBody(documentRecordList);
        return pageUtil;
    }
    // 判断案卷名是否有重复的
}
