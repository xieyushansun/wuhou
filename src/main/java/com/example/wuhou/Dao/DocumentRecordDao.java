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
        List<String> fileList = null;
        if (file.isDirectory()){
//            fileList = list.get(0).getFilelist();
            fileList = Arrays.asList(file.list());
        }
        return fileList;
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
//        PageUtil pageUtil = new PageUtil();
//
//        // 用空格隔开的各个关键字
//        String keyWord[] = multiKeyWord.split(" ");
//        Query query = new Query();
//        // 模糊查询
//        if (blurryFind.compareTo("1") == 0){
//            Criteria criteria = new Criteria();
//            for (String s : keyWord) {
//                criteria.orOperator(
//                        Criteria.where("documentNumber").regex(".*?" + s + ".*?"),
//                        Criteria.where("duration").regex(".*?" + s + ".*?"),
//                        Criteria.where("security").regex(".*?" + s + ".*?"),
//                        Criteria.where("responsible").regex(".*?" + s + ".*?"),
//                        Criteria.where("danwieCode").regex(".*?" + s + ".*?"),
//                        Criteria.where("danweiName").regex(".*?" + s + ".*?"),
//                        Criteria.where("position").regex(".*?" + s + ".*?"),
//                        Criteria.where("recorder").regex(".*?" + s + ".*?"),
//                        Criteria.where("recordTime").regex(".*?" + s + ".*?"),
//                        Criteria.where("storePath").regex(".*?" + s + ".*?")
//                );
//                query.addCriteria(criteria);
//            }
//        }
//        else { // 精确查询
//            for (String s : keyWord) {
//                Criteria criteria = new Criteria();
//                criteria.orOperator(
//                        Criteria.where("documentNumber").is(s),
//                        Criteria.where("duration").is(s),
//                        Criteria.where("security").is(s),
//                        Criteria.where("responsible").is(s),
//                        Criteria.where("danwieCode").is(s),
//                        Criteria.where("danweiName").is(s),
//                        Criteria.where("position").is(s),
//                        Criteria.where("recorder").is(s),
//                        Criteria.where("recordTime").is(s),
//                        Criteria.where("storePath").is(s)
//                );
//                query.addCriteria(criteria);
//            }
//        }
//        pageUtil.setTotalElement((int) mongoTemplate.count(query, DocumentRecord.class));
//
//        query.skip((currentPage - 1) * pageSize);
//        query.limit(pageSize);
//
//        pageUtil.setBody(mongoTemplate.find(query, DocumentRecord.class));
//        return pageUtil;
        PageUtil pageUtil = new PageUtil();

        // 用空格隔开的各个关键字
        String keyWord[] = multiKeyWord.split(" ");
//        Query query = new Query();
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
                        "{danwieCode: /" + s + "/}," +
                        "{danweiName: /" + s + "/}," +
                        "{position: /" + s + "/}," +
                        "{recorder: /" + s + "/}," +
                        "{recordTime: /" + s + "/}," +
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
                        "{danwieCode: " + s + "}," +
                        "{danweiName: " + s + "}," +
                        "{position: " + s + "}," +
                        "{recorder: " + s + "}," +
                        "{recordTime: " + s + "}," +
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
         * filedName : 字段名
         * filedContent : 字段内容
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
            String filedName = jsonObject.get("filedName").toString();
            String filedContent = jsonObject.get("filedContent").toString();
            String operator = jsonObject.get("operator").toString();
            String joiner = jsonObject.get("joiner").toString();
            if (filedName.equals("recordTime") || filedName.compareTo("year") == 0) {
                if (i == 0) {
                    switch (operator) {
                        case "gt": currentSql = String.format("{ %s:{ $gt: \"%s\" }}", filedName, filedContent);break;
                        case "lt": currentSql = String.format("{ %s:{ $lt: \"%s\" }}", filedName, filedContent);break;
                        case "gte": currentSql = String.format("{ %s:{ $gte: \"%s\" }}", filedName, filedContent);break;
                        case "lte": currentSql = String.format("{ %s:{ $lte: \"%s\" }}", filedName, filedContent);break;
                        case "is": currentSql = String.format("{ %s:\"%s\"}", filedName, filedContent);break;
                        default: throw new Exception("操作符传递出错");
                    }
                } else {
                    if (joiner.equals("AND")) {
                        switch (operator) {
                            case "gt": currentSql = "{$and:[" + lastSql + ", " + String.format("{ %s:{ $gt: \"%s\" }}", filedName, filedContent) + "]}";break;
                            case "lt": currentSql = "{$and:[" + lastSql + ", " + String.format("{ %s:{ $lt: \"%s\" }}", filedName, filedContent) + "]}";break;
                            case "gte": currentSql = "{$and:[" + lastSql + ", " + String.format("{ %s:{ $gte: \"%s\" }}", filedName, filedContent) + "]}";break;
                            case "lte": currentSql = "{$and:[" + lastSql + ", " + String.format("{ %s:{ $lte: \"%s\" }}", filedName, filedContent) + "]}";break;
                            case "is": currentSql = "{$and:[" + lastSql + ", " + String.format("{ %s:\"%s\"}", filedName, filedContent) + "]}";break;
                            default: throw new Exception("操作符传递出错");
                        }
                    }else {
                        switch (operator) {
                            case "gt": currentSql = "{$or:[" + lastSql + ", " + String.format("{ %s:{ $gt: \"%s\" }}", filedName, filedContent) + "]}";break;
                            case "lt": currentSql = "{$or:[" + lastSql + ", " + String.format("{ %s:{ $lt: \"%s\" }}", filedName, filedContent) + "]}";break;
                            case "gte": currentSql = "{$or:[" + lastSql + ", " + String.format("{ %s:{ $gte: \"%s\" }}", filedName, filedContent) + "]}";break;
                            case "lte": currentSql = "{$or:[" + lastSql + ", " + String.format("{ %s:{ $lte: \"%s\" }}", filedName, filedContent) + "]}";break;
                            case "is": currentSql = "{$or:[" + lastSql + ", " + String.format("{ %s:\"%s\"}", filedName, filedContent) + "]}";break;
                            default: throw new Exception("操作符传递出错");
                        }
                    }
                }
            } else {
                if (i == 0) {
                    if(blurryFind.equals("1")){
                        currentSql = String.format("{%s: /%s/}", filedName, filedContent);
                    }else {
                        currentSql = String.format("{%s: \"%s\"}", filedName, filedContent);
                    }
                } else {
                    if (joiner.equals("AND")) {
                        if (blurryFind.equals("1")){
                            currentSql = "{$and:[" + lastSql + ", " + String.format("{%s: /%s/}", filedName, filedContent) + "]}";
                        }else {
                            currentSql = "{$and:[" + lastSql + ", " + String.format("{%s: \"%s\"}", filedName, filedContent) + "]}";
                        }

                    } else {
                        if (blurryFind.equals("1")){
                            currentSql = "{$or:[" + lastSql + ", " + String.format("{%s: /%s/}", filedName, filedContent) + "]}";
                        }else {
                            currentSql = "{$or:[" + lastSql + ", " + String.format("{%s: \"%s\"}", filedName, filedContent) + "]}";
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
    public Boolean checkFileName(String fileName){
        Query query = new Query();
        Criteria criteria = Criteria.where("fileName").is(fileName);
        query.addCriteria(criteria);
        List<DocumentRecord> documentRecordList = mongoTemplate.find(query, DocumentRecord.class);
        return documentRecordList.size() == 0;
    }
    public void ModifyDocumentRecord(DocumentRecord documentRecord) throws Exception {
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(new ObjectId(documentRecord.getId()));
        query.addCriteria(criteria);
        mongoTemplate.remove(query, DocumentRecord.class);
        logDao.insertLog("documentRecord", "删除", "删除档案记录:" + documentRecord.toString());
        mongoTemplate.insert(documentRecord);
        logDao.insertLog("documentRecord", "添加", "添加档案记录:" + documentRecord.toString());
    }
}