package com.example.wuhou.Dao;

import com.example.wuhou.entity.Log;
import com.example.wuhou.util.PageUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class LogDao {
    @Autowired
    MongoTemplate mongoTemplate;

    public String getCurrentUserId() throws Exception {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.getPrincipals() == null){
            throw new Exception("获取当前用户登录状态异常");
        }
        return currentUser.getPrincipals().toString();
    }


    public String getTime(){
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    public void insertLog(String table, String operationType, String msg) throws Exception {
        Log log = new Log();
        log.setOperationTime(getTime());
        log.setOperatorId(getCurrentUserId());
        log.setTable(table);
        log.setOperationType(operationType);
        log.setMsg(msg);

        mongoTemplate.insert(log);
    }
    // 清空日志
    public void deleteAllLog(){
        mongoTemplate.dropCollection("log");
    }

    // 删除某个日志
    public void deleteLog(String id){
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(new ObjectId(id));
        query.addCriteria(criteria);
        mongoTemplate.remove(query, Log.class);
    }

    //分页获取所有日志内容
    public PageUtil getAllLog(Integer currentPage, Integer pageSize){
        Query query = new Query();
        PageUtil pageUtil = new PageUtil();
        pageUtil.setTotalElement((int) mongoTemplate.count(query, Log.class));
        query.skip((currentPage - 1) * pageSize);
        query.limit(pageSize);
        pageUtil.setBody(mongoTemplate.find(query, Log.class));
        return pageUtil;
    }

    public void deleteAllLogBeforDate(String date) {
        //大于$gt  小于$lt 查询  大于等于 gte  小于等于 lte
        Query query = new Query();
        Criteria criteria = Criteria.where("operationTime").lt(date);
        query.addCriteria(criteria);
        mongoTemplate.remove(query, Log.class);
    }

    public PageUtil normalFindLog(Map<String, String> findKeyWordMap, Integer currentPage, Integer pageSize, String blurryFind){
        List<Log> resultList;
        Query query = new Query();
        for (String key : findKeyWordMap.keySet()) {
            if (key.equals("logDateFanwei")){
                // 去空格
//                String temp = findKeyWordMap.get(key).replace(" ", "");
                String date[] = findKeyWordMap.get(key).split(" ~ ");
                String startDate = date[0];
                String endDate = date[1];
                Criteria criteria = new Criteria();
                criteria.andOperator(Criteria.where("operationTime").gte(startDate), Criteria.where("operationTime").lte(endDate));
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
        pageUtil.setTotalElement((int) mongoTemplate.count(query, Log.class));
        query.with(Sort.by(Sort.Order.desc("operationTime")));
        query.skip((currentPage - 1) * pageSize);
        query.limit(pageSize);
        resultList = mongoTemplate.find(query, Log.class);
        pageUtil.setBody(resultList);

        return pageUtil;
    }
//    public static void delteDB(String msg) throws Exception {
//        String addmsg = "用户<" + currentUserId + "> 执行删除操作：" + msg;
//        FileOperationUtil.wirteUserLog(addmsg);
//    }
//    public static void modifyDB(String msg) throws Exception {
//        String addmsg = "用户<" + currentUserId + "> 执行修改操作：" + msg;
//        FileOperationUtil.wirteUserLog(addmsg);
//    }
//    public static void fileAdd(String msg) throws Exception {
//        String addmsg = "用户<" + currentUserId + "> 执行档案记录文件挂载操作：" + msg;
//        FileOperationUtil.wirteUserLog(addmsg);
//    }
//    public static void filedelete(String msg) throws Exception {
//        String addmsg = "用户<" + currentUserId + "> 执行档案记录文件删除操作：" + msg;
//        FileOperationUtil.wirteUserLog(addmsg);
//    }
}
