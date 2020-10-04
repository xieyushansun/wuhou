package com.example.wuhou.Dao;

import com.example.wuhou.constant.DataConstant;
import com.example.wuhou.entity.DocumentRecord;
import com.example.wuhou.entity.PageUtil;
import com.example.wuhou.exception.NotExistException;
import com.example.wuhou.util.FileOperationUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
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
        logDao.inserLog("documentRecord", "添加", "添加档案记录:" + documentRecord.toString());
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

        String recordPath = documentRecord.getDiskPath() + "\\" + documentRecord.getStorePath();
        File file = new File(recordPath);
        List<String> fileList = null;
        if (file.isDirectory()){
//            fileList = list.get(0).getFilelist();
            fileList = Arrays.asList(file.list());
        }

        //把filelist所有元素都加上路径
//        for (int i = 0; i < fileList.size(); i++){
//            String filename = fileList.get(i);
//            String filepath = recordPath + "\\" + filename;
//            fileList.set(i, filepath);
//        }
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
        logDao.inserLog("documentRecord", "删除", "删除档案记录:" + documentRecord.toString());
        //返回档案记录对应的文件，在service层删除
        return documentRecord.getDiskPath() + "\\" + documentRecord.getStorePath();
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
    //下载档案文件
//    public DocumentFile downLoadDocumentRecordFile(ObjectId fileId) {
//        Query query = new Query();
//        Criteria criteria = Criteria.where("_id").is(fileId);
//        query.addCriteria(criteria);
//        DocumentFile documentFile = mongoTemplate.findOne(query, DocumentFile.class);
//        return documentFile;
////        FileOutputStream fileOutputStream = FileOperationUtil.bytesToFile(documentFile.getFile(), documentFile.getDocumentName());
////        return fileOutputStream;
//    }
    public PageUtil normalFindDocumentRecord(Map<String, String> findKeyWordMap, String blurryFind, Integer currentPage, Integer pageSize){
//        List<List<DocumentRecord>> lists = new ArrayList<>();
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
//        int n = (int) mongoTemplate.count(query, DocumentRecord.class);
        resultList = mongoTemplate.find(query, DocumentRecord.class);
        pageUtil.setBody(resultList);
        // 将二维lists转为一维resultList
//        for (List<DocumentRecord> list : lists) {
//            resultList.addAll(list);
//        }
        return pageUtil;
    }
    // 一般查询
    //
    public PageUtil generalFindDocumentRecord(String multiKeyWord, String blurryFind, Integer currentPage, Integer pageSize){
        PageUtil pageUtil = new PageUtil();

        // 用空格隔开的各个关键字
        String keyWord[] = multiKeyWord.split(" ");
        Query query = new Query();
        // 模糊查询
        if (blurryFind.compareTo("1") == 0){
            for (String s : keyWord) {
                Criteria criteria = new Criteria();
                criteria.orOperator(
                        Criteria.where("documentNumber").regex(".*?" + s + ".*?"),
                        Criteria.where("duration").regex(".*?" + s + ".*?"),
                        Criteria.where("security").regex(".*?" + s + ".*?"),
                        Criteria.where("responsible").regex(".*?" + s + ".*?"),
                        Criteria.where("danwieCode").regex(".*?" + s + ".*?"),
                        Criteria.where("danweiName").regex(".*?" + s + ".*?"),
                        Criteria.where("position").regex(".*?" + s + ".*?"),
                        Criteria.where("recorder").regex(".*?" + s + ".*?"),
                        Criteria.where("recordTime").regex(".*?" + s + ".*?"),
                        Criteria.where("storePath").regex(".*?" + s + ".*?")
                );
                query.addCriteria(criteria);
            }
        }
        else { // 精确查询
            for (String s : keyWord) {
                Criteria criteria = new Criteria();
                criteria.orOperator(
                        Criteria.where("documentNumber").is(s),
                        Criteria.where("duration").is(s),
                        Criteria.where("security").is(s),
                        Criteria.where("responsible").is(s),
                        Criteria.where("danwieCode").is(s),
                        Criteria.where("danweiName").is(s),
                        Criteria.where("position").is(s),
                        Criteria.where("recorder").is(s),
                        Criteria.where("recordTime").is(s),
                        Criteria.where("storePath").is(s)
                );
                query.addCriteria(criteria);
            }
        }
        pageUtil.setTotalElement((int) mongoTemplate.count(query, DocumentRecord.class));

        query.skip((currentPage - 1) * pageSize);
        query.limit(pageSize);

        pageUtil.setBody(mongoTemplate.find(query, DocumentRecord.class));
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
        logDao.inserLog("documentRecord", "删除", "删除档案记录:" + documentRecord.toString());
        mongoTemplate.insert(documentRecord);
        logDao.inserLog("documentRecord", "添加", "添加档案记录:" + documentRecord.toString());
    }
}