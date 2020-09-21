package com.example.wuhou.service;

import com.example.wuhou.Dao.DocumentRecordDao;
import com.example.wuhou.entity.DocumentRecord;
import com.example.wuhou.exception.NotExistException;
import com.example.wuhou.util.FileOperationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

@Service
public class DocumentRecordService {
    @Autowired
    DocumentRecordDao documentRecordDao;
    public String addDocumentRecord(DocumentRecord documentRecord) throws Exception {

        if (!documentRecordDao.checkFileName(documentRecord.getFileName())){
            throw new Exception("案卷题目重复！修改后创建创建！");
        }

        String storePath = documentRecord.getRecordGroupNumber() + "\\" + documentRecord.getDocumentCategory() + "\\" + documentRecord.getYear()
                + "\\" + documentRecord.getFileCategory() + "\\" + documentRecord.getBoxNumber() + "\\" + documentRecord.getFileName();
        documentRecord.setStorePath(storePath);
        documentRecord.setDiskPath("E:\\wuhoudocument");
        //创建这条记录对应的文件夹
        String strPath = documentRecord.getDiskPath() + "\\" + storePath;
        File file = new File(strPath);
        if(!file.exists()){
            file.mkdirs();
        }
        //返回文档记录在数据库中的id
        String documentRecordId = documentRecordDao.addDocumentRecord(documentRecord);
        return documentRecordId;
    }
    public void deleteDocumentRecord(String documentRecordId) throws NotExistException {
        String path = documentRecordDao.deleteDocumentRecord(documentRecordId);
        //删除documentrecord的文件夹以及文件夹中的内容
        FileOperationUtil.delFolder(path);
    }
    public void addDocumentRecordFile(String documentRecordId, MultipartFile[] filelist) throws Exception {
        if (filelist.length == 0){
            throw new Exception("服务器没有接收到上传的文件！");
        }
        //获取数据库中对应id的记录内容
        DocumentRecord documentRecord = documentRecordDao.getDocumentRecord(documentRecordId);
        for (MultipartFile multipartFile : filelist) {
            if (multipartFile == null) {
                throw new Exception("存在空文件，上传失败！");
            }
            String path = documentRecord.getDiskPath() + "\\" + documentRecord.getStorePath() + "\\" + multipartFile.getOriginalFilename();
            byte[] bytesfile = multipartFile.getBytes();
//            String path = "C:\\Users\\DF\\Desktop\\test11.jpg";
            FileOutputStream out = new FileOutputStream(new File(path));
            out.write(bytesfile);
            out.flush();
            out.close();
        }
    }
//    public DocumentFile downLoadDocumentRecordFile(String fileId){
//        return documentRecordDao.downLoadDocumentRecordFile(new ObjectId(fileId));
//    }
    public String[] findFileListByDocumentRecordId(String fileCategory) throws Exception {
        return documentRecordDao.findFileListByDocumentRecordId(fileCategory);
    }
    public DocumentRecord getDocumentRecordByDocumentRecordId(String documentRecordId) throws Exception {
        DocumentRecord documentRecord = documentRecordDao.getDocumentRecord(documentRecordId);
        if (documentRecord == null){
            throw new Exception("没有这条记录！");
        }
        return documentRecord;
    }
    public List<DocumentRecord> normalFindDocumentRecord(Map<String, String> findKeyWordMap, String blurryFind, Integer currentPage, Integer pageSize){
        return documentRecordDao.normalFindDocumentRecord(findKeyWordMap, blurryFind, currentPage, pageSize);
    }
}
