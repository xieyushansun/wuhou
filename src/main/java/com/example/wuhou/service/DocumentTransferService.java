package com.example.wuhou.service;

import com.example.wuhou.Dao.DocumentTransferDao;
import com.example.wuhou.entity.DocumentTransfer;
import com.example.wuhou.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DocumentTransferService {
    @Autowired
    DocumentTransferDao documentTransferDao;
    //添加调卷记录
    public void addDocumentTransfer(String boxNumber, String borrower) throws Exception {
        documentTransferDao.addDocumentTransfer(boxNumber, borrower);
    }
    //删除调卷记录
    public void delteDocumentTransfer(String id) throws Exception {
        documentTransferDao.delteDocumentTransfer(id);
    }
    //归还调卷
    public void returnDocumentTransfer(String id) throws Exception {
        documentTransferDao.returnDocumentTransfer(id);
    }
    //分页查找所有调卷
    public PageUtil findAllDocumentTransfer(Integer currentPage, Integer pageSize){
        return documentTransferDao.findAllDocumentTransfer(currentPage, pageSize);
    }
//    //按调卷人查找所有调卷
//    public List<DocumentTransfer> findDocumentTransferByBorrower(String borrower){
//        return documentTransferDao.findDocumentTransferByBorrower(borrower);
//    }
    //普通查询
    public PageUtil normalFindDocumentTransfer(Map<String, String> findKeyWordMap, Integer currentPage, Integer pageSize){
        return documentTransferDao.normalFindDocumentTransfer(findKeyWordMap, currentPage, pageSize);
    }
    //查找未归还调卷
    public List<DocumentTransfer> findDocumentTransferByNotReturned(){
        return documentTransferDao.findDocumentTransferByNotReturned();
    }
}
