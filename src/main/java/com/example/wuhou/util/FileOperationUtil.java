package com.example.wuhou.util;

import com.example.wuhou.Dao.UserDao;
import com.example.wuhou.constant.PathConstant;
import org.apache.tomcat.jni.User;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FileOperationUtil {
    @Autowired
    static
    MongoTemplate mongoTemplate;

    public static byte[] fileToByte(File file) throws IOException {
        byte[] bytes = null;
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(file);
            bytes = new byte[(int) file.length()];
            fis.read(bytes);
        }catch(IOException e){
            e.printStackTrace();
            throw e;
        }finally{
            fis.close();
        }
        return bytes;
    }
    public static FileOutputStream bytesToFile(byte[] bFile, String fileName) {
        Binary binary = new Binary(bFile);
        binary.getData();
        FileOutputStream fileOuputStream = null;
        try {
            fileOuputStream = new FileOutputStream(fileName);
            fileOuputStream.write(bFile);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOuputStream != null) {
                try {
                    fileOuputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return fileOuputStream;
    }
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }
    //删除文件夹
    //param folderPath 文件夹完整绝对路径
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void wirteUserLog(String msg) throws Exception {
//        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
//        Date date = new Date(System.currentTimeMillis());
//        if (PathConstant.DISKNAME.isEmpty()){
//            throw new Exception("磁盘空间不足");
//        }
//        String path = PathConstant.DISKNAME + "\\" + PathConstant.STORE_FILE_NAME + "\\" + "logfile";
//        String filepath = path + "\\" + formatter.format(date) + ".txt";
//
//        formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
//        String recordTime = formatter.format(date);
//        msg = recordTime + "--------" + msg;
////        FileWriter fileWriter = new FileWriter(filepath, true);
////        fileWriter.write(msg + "\r\n");
////        fileWriter.close();
////        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
////        RandomAccessFile randomFile = new RandomAccessFile(filepath, "rw");
////        long fileLength = randomFile.length();
////        randomFile.seek(fileLength);
////        randomFile.writeBytes(msg + "\r\n");
////        randomFile.close();
//
//
//
//
////        bufferedWriter.write(msg + "\r\n");
////        bufferedWriter.flush();
////        bufferedWriter.close();
//    }
}
