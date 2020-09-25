package com.example.wuhou.service;

import com.example.wuhou.Dao.DocumentRecordDao;
import com.example.wuhou.constant.PathConstant;
import com.example.wuhou.entity.DocumentRecord;
import com.example.wuhou.exception.NotExistException;
import com.example.wuhou.util.FileOperationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
        documentRecord.setDiskPath(PathConstant.DISKPATH);
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
        DocumentRecord documentRecord = documentRecordDao.getDocumentRecordById(documentRecordId);
        String path = documentRecord.getDiskPath() + "\\" + documentRecord.getStorePath();
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
            String filepath = documentRecord.getDiskPath() + "\\" + documentRecord.getStorePath() + "\\" + multipartFile.getOriginalFilename();
            byte[] bytesfile = multipartFile.getBytes();
            FileOutputStream out = new FileOutputStream(new File(filepath));
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
        DocumentRecord documentRecord = documentRecordDao.getDocumentRecordById(documentRecordId);
        if (documentRecord == null){
            throw new Exception("没有这条记录！");
        }
        return documentRecord;
    }
    public List<DocumentRecord> normalFindDocumentRecord(Map<String, String> findKeyWordMap, String blurryFind, Integer currentPage, Integer pageSize){
        return documentRecordDao.normalFindDocumentRecord(findKeyWordMap, blurryFind, currentPage, pageSize);
    }
    public void deleteDocumentRecordFile(File file){
        if (file.exists()){
            file.delete();
        }
    }

    public void modifyDocumentRecord(DocumentRecord newDocumentRecord) throws Exception {
        //初始化新记录的存储路径set
        String storePath = newDocumentRecord.getRecordGroupNumber() + "\\" + newDocumentRecord.getDocumentCategory() + "\\" + newDocumentRecord.getYear()
                + "\\" + newDocumentRecord.getFileCategory() + "\\" + newDocumentRecord.getBoxNumber() + "\\" + newDocumentRecord.getFileName();
        newDocumentRecord.setStorePath(storePath);
        newDocumentRecord.setDiskPath(PathConstant.DISKPATH);

        String newPath = newDocumentRecord.getDiskPath() + "\\" + storePath;

        DocumentRecord oldDocumentRecord = documentRecordDao.getDocumentRecordById(newDocumentRecord.getId());
        String oldPath = oldDocumentRecord.getDiskPath() + "\\" + oldDocumentRecord.getStorePath();


        // 判断新旧文件夹路径是否一样
        if (!oldPath.equals(newPath)){
            //1.创建新文件夹
            File newFile = new File(newPath);
            if (!newFile.exists()){
                newFile.mkdirs();
            }
            //2.将旧文件夹下的文件移动到新文件夹下
            File oldFile = new File(oldPath);
            String[] fileName = oldFile.list();
            for (String s : fileName) {
                //file中的文件移到file2去
                File file1 = new File(oldPath + "\\" + s);
                File file2 = new File(newPath + "\\" + s);
                if (!file1.renameTo(file2)) {
                    // 删除之前新建的文件夹
                    file2.delete();
                    throw new Exception("文件: " + s + "移动到新路径" + newPath + "失败!");
                }
            }
            //3.删除旧文件夹
            FileOperationUtil.delFolder(oldPath);
        }

        documentRecordDao.ModifyDocumentRecord(newDocumentRecord);
    }
}
