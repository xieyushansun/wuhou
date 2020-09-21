package com.example.wuhou.util;

import org.bson.types.Binary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileOperationUtil {
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
}
