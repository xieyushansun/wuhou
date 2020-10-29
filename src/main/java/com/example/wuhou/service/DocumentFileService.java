package com.example.wuhou.service;

import com.example.wuhou.Dao.DocumentFileDao;
import com.example.wuhou.Dao.DocumentRecordDao;
import com.example.wuhou.Dao.LogDao;
import com.example.wuhou.entity.DocumentRecord;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DocumentFileService {
    @Autowired
    DocumentFileDao documentFileDao;
    @Autowired
    DocumentRecordDao documentRecordDao;
    @Autowired
    LogDao logDao;
    public void addDocumentRecordFile(String documentRecordId, MultipartFile[] filelist) throws Exception {
        if (filelist.length == 0){
            throw new Exception("服务器没有接收到上传的文件！");
        }
        //获取数据库中对应id的记录内容
        DocumentRecord documentRecord = documentRecordDao.getDocumentRecordById(documentRecordId);
        String path = documentRecord.getDiskPath() + ":\\" + documentRecord.getStorePath();
        File fileExsit = new File(path);
        if (!fileExsit.exists()){
            throw new Exception("磁盘: " + documentRecord.getDiskPath() + " 不存在或路径错误！");
        }
        File file = new File(path);
        String[] fileName = file.list();
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(fileName));

        for (MultipartFile multipartFile : filelist) {
            if (multipartFile == null) {
                throw new Exception("存在空文件，上传失败！");
            }
            if (list.contains(multipartFile.getOriginalFilename())){
                throw new Exception("存在重名文件:" + multipartFile.getOriginalFilename());
            }
            String filepath = documentRecord.getDiskPath() + ":\\" + documentRecord.getStorePath() + "\\" + multipartFile.getOriginalFilename();
            byte[] bytesfile = multipartFile.getBytes();
            FileOutputStream out = new FileOutputStream(new File(filepath));
            out.write(bytesfile);
            out.flush();
            out.close();
        }
//        logDao.insertLog("文件操作", "添加", "添加档案记录Id为: " + documentRecordId + " 的挂载文件");
    }
    public List<String> findFileListByDocumentRecordId(String fileCategory) throws Exception {
        return documentFileDao.findFileListByDocumentRecordId(fileCategory);
    }
    public void deleteDocumentRecordFile(File file, String documentRecordId) throws Exception {
        if (file.exists()){
            file.delete();
        }
//        logDao.insertLog("文件操作", "删除", "删除档案记录Id为: " + documentRecordId + " 的挂载文件: " + file.getName());
    }
}
