package com.example.wuhou.service;

import com.example.wuhou.Dao.LogDao;
import com.example.wuhou.entity.Log;
import com.example.wuhou.entity.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
