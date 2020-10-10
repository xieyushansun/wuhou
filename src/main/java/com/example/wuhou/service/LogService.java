package com.example.wuhou.service;

import com.example.wuhou.Dao.LogDao;
import com.example.wuhou.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LogService {
    @Autowired
    LogDao logDao;
    public PageUtil getAllLog(Integer currentPage, Integer pageSize){
        return logDao.getAllLog(currentPage, pageSize);
    }
    public void deleteAllLog(){
        logDao.deleteAllLog();
    }
    public void deleteAllLogBeforDate(String date){
        logDao.deleteAllLogBeforDate(date);
    }
    public PageUtil normalFindLog(Map<String, String> findKeyWordMap, Integer currentPage, Integer pageSize, String blurryFind){
        return logDao.normalFindLog(findKeyWordMap, currentPage, pageSize, blurryFind);
    }
}
