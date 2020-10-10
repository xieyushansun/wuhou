package com.example.wuhou.Dao;

import com.example.wuhou.entity.DocumentTransfer;
import com.example.wuhou.util.PageUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class DocumentTransferDao {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    LogDao logDao;
    //添加调卷记录
    public void addDocumentTransfer(String boxNumber, String borrower) throws Exception {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.getPrincipals() == null){
            throw new Exception("获取当前用户登录状态异常");
        }
        //设置登记者
        DocumentTransfer documentTransfer = new DocumentTransfer();
        documentTransfer.setBorrowRecorder(currentUser.getPrincipals().toString());
        //设置借阅时间
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        documentTransfer.setBorrowDate(formatter.format(date));

        documentTransfer.setBoxNumber(boxNumber);
        documentTransfer.setBorrower(borrower);

        documentTransfer.setReturnDate("");
        documentTransfer.setReturnRecorder("");

        mongoTemplate.insert(documentTransfer);
        logDao.insertLog("documentTransfer", "添加", "添加调卷记录:" + documentTransfer.toString());
    }
    //删除调卷记录
    public void delteDocumentTransfer(String id) throws Exception {
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(new ObjectId(id));
        query.addCriteria(criteria);
        DocumentTransfer documentTransfer = mongoTemplate.findOne(query, DocumentTransfer.class);
        if (documentTransfer == null){
            throw new Exception("没有id为: " + id + " 的记录！");
        }
        mongoTemplate.remove(query, DocumentTransfer.class);
        logDao.insertLog("documentTransfer", "删除", "删除调卷记录:" + documentTransfer.toString());
    }
    //归还调卷
    public void returnDocumentTransfer(String id) throws Exception {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String returnDate = formatter.format(date);

        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.getPrincipals() == null){
            throw new Exception("获取当前用户登录状态异常");
        }
        String returnRecorder  = currentUser.getPrincipals().toString();

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));

        DocumentTransfer documentTransfer = mongoTemplate.findOne(query, DocumentTransfer.class);
        if (documentTransfer == null){
            throw new Exception("没有id为: " + id + " 的记录！");
        }

        Update update = new Update();
        update.set("returnDate", returnDate);
        update.set("returnRecorder", returnRecorder);
        mongoTemplate.updateMulti(query, update, DocumentTransfer.class);
        logDao.insertLog("documentTransfer", "修改", "归还调卷记录:" + documentTransfer.toString());

    }
    //分页查找所有调卷
    public PageUtil findAllDocumentTransfer(Integer currentPage, Integer pageSize){
        PageUtil pageUtil = new PageUtil();
        Query query = new Query();
        query.with(Sort.by(Sort.Order.desc("borrowDate")));
        pageUtil.setTotalElement((int) mongoTemplate.count(query, DocumentTransfer.class));

        query.skip((currentPage - 1) * pageSize);
        query.limit(pageSize);
        pageUtil.setBody(mongoTemplate.find(query, DocumentTransfer.class));
        return pageUtil;
    }
//    //按调卷人查找所有调卷
//    public List<DocumentTransfer> findDocumentTransferByBorrower(String borrower){
//        Query query = new Query();
//        query.addCriteria(Criteria.where("borrower").regex(".*?" + borrower + ".*?"));
//        return mongoTemplate.find(query, DocumentTransfer.class);
//    }
    //一般查询调卷
    public PageUtil normalFindDocumentTransfer(Map<String, String> findKeyWordMap, Integer currentPage, Integer pageSize, String blurryFind){
        List<DocumentTransfer> resultList;
        Query query = new Query();
        for (String key : findKeyWordMap.keySet()) {
            if (key.equals("borrowDateFanwei")){
                // 去空格
                String temp = findKeyWordMap.get(key).replace(" ", "");
                String date[] = temp.split("-");
                String startDate = date[0].replace("/", "-");
                String endDate = date[1].replace("/", "-");
                Criteria criteria = new Criteria();
                criteria.andOperator(Criteria.where("borrowDate").gte(startDate), Criteria.where("borrowDate").lte(endDate));
                query.addCriteria(criteria);
            }else {
                Criteria criteria;
                if (blurryFind.equals("1")){
                    criteria = Criteria.where(key).regex(".*?" + findKeyWordMap.get(key) + ".*?");
                }else {
                    criteria = Criteria.where(key).is(findKeyWordMap.get(key));
                }
                query.addCriteria(criteria);
            }
        }

        PageUtil pageUtil = new PageUtil();
        pageUtil.setTotalElement((int) mongoTemplate.count(query, DocumentTransfer.class));
        query.with(Sort.by(Sort.Order.desc("borrowDate")));
        query.skip((currentPage - 1) * pageSize);
        query.limit(pageSize);
        resultList = mongoTemplate.find(query, DocumentTransfer.class);
        pageUtil.setBody(resultList);

        return pageUtil;

    }
    //查找未归还调卷
    public List<DocumentTransfer> findDocumentTransferByNotReturned(){
        Query query = new Query();
        // 归还日期为空，说明还没有归还
        query.addCriteria(Criteria.where("returnDate").is(""));
        query.with(Sort.by(Sort.Order.desc("borrowDate")));
        return mongoTemplate.find(query, DocumentTransfer.class);
    }
}
