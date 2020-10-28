package com.example.wuhou.service;

import com.example.wuhou.Dao.DiskManageDao;
import com.example.wuhou.Dao.DocumentRecordDao;
import com.example.wuhou.Dao.DocumentRecordFindDao;
import com.example.wuhou.Dao.LogDao;
import com.example.wuhou.util.PageUtil;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DocumentRecordFindService {
    @Autowired
    DocumentRecordFindDao documentRecordFindDao;
    @Autowired
    LogDao logDao;
    @Autowired
    DiskManageDao diskManageDao;
    // 普通查询
    public PageUtil normalFindDocumentRecord(Map<String, String> findKeyWordMap, String blurryFind, Integer currentPage, Integer pageSize){
        return documentRecordFindDao.normalFindDocumentRecord(findKeyWordMap, blurryFind, currentPage, pageSize);
    }
    // 一般查询
    public PageUtil generalFindDocumentRecord(String multiKeyWord, String blurryFind, Integer currentPage, Integer pageSize){
        return documentRecordFindDao.generalFindDocumentRecord(multiKeyWord, blurryFind, currentPage, pageSize);
    }
    //组合查询
    public PageUtil combinationFindDocumentRecord(JSONArray jsonArray, String blurryFind, Integer currentPage, Integer pageSize) throws Exception {
        return documentRecordFindDao.combinationFindDocumentRecord(jsonArray, blurryFind, currentPage, pageSize);
    }
}
