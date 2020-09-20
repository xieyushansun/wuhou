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
}
