package com.example.wuhou.controller;

import com.example.wuhou.constant.ResponseConstant;
import com.example.wuhou.entity.DocumentFile;
import com.example.wuhou.entity.DocumentRecord;
import com.example.wuhou.entity.User;
import com.example.wuhou.exception.ExistException;
import com.example.wuhou.service.DocumentRecordService;
import com.example.wuhou.util.FileOperationUtil;
import com.example.wuhou.util.ResultUtil;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.bson.conversions.Bson;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import com.google.gson.JsonObject;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/document")
@Api(tags = "档案")
public class DocumentRecordController {
    @Autowired
    DocumentRecordService documentRecordService;


    @PostMapping("/adddocumentrecord")
    @ApiOperation("新增档案记录")
    public ResultUtil<String> addDocumentRecord(
            @ApiParam(value = "档号", required = true) @RequestParam(defaultValue = "1") String documentNumber,
            @ApiParam(value = "全宗号", required = true) @RequestParam(defaultValue = "1") String recordGroupNumber,
            @ApiParam(value = "目录号", required = true) @RequestParam(defaultValue = "1") String catalogNumber,
            @ApiParam(value = "案卷号", required = true) @RequestParam(defaultValue = "1") String fileNumber,
            @ApiParam(value = "年度", required = true) @RequestParam(defaultValue = "1") String year,
            @ApiParam(value = "保管期限", required = true) @RequestParam(defaultValue = "1") String duration,
            @ApiParam(value = "密级", required = true) @RequestParam(defaultValue = "1") String security,
            @ApiParam(value = "档案类别", required = true) @RequestParam(defaultValue = "1") String documentCategory,
            @ApiParam(value = "案卷类型", required = true) @RequestParam(defaultValue = "2016-2018劳务品牌培训补贴申报资料") String fileCategory,
            @ApiParam(value = "责任者", required = true) @RequestParam(defaultValue = "1") String responsible,
            @ApiParam(value = "案卷题名", required = true) @RequestParam(defaultValue = "1") String fileName,
            @ApiParam(value = "分类号", required = true) @RequestParam(defaultValue = "1") String classNumber,
            @ApiParam(value = "存放位置", required = true) @RequestParam(defaultValue = "1") String storagePath,
            @ApiParam(value = "著录人", required = true) @RequestParam(defaultValue = "1") String recorder,
            @ApiParam(value = "著录时间", required = true) @RequestParam(defaultValue = "1") String recordTime
            ){
        String documentRecordId = "";
        try {
            DocumentRecord documentRecord = new DocumentRecord();

            documentRecord.setDocumentNumber(documentNumber);
            documentRecord.setRecordGroupNumber(recordGroupNumber);
            documentRecord.setCatalogNumber(catalogNumber);
            documentRecord.setFileNumber(fileNumber);
            documentRecord.setYear(year);
            documentRecord.setDuration(duration);
            documentRecord.setSecurity(security);
            documentRecord.setDocumentCategory(documentCategory);
            documentRecord.setFileCategory(fileCategory);
            documentRecord.setResponsible(responsible);
            documentRecord.setFileName(fileName);
            documentRecord.setClassNumber(classNumber);
            documentRecord.setStoragePath(storagePath);
            documentRecord.setRecorder(recorder);
            documentRecord.setRecordTime(recordTime);

            documentRecordId = documentRecordService.addDocumentRecord(documentRecord);
        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }
        List<String> filelist = new ArrayList<>();
        filelist = documentRecordService.findFileListByFileName(fileCategory);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", documentRecordId);
//        jsonObject.addProperty("filelist", filelist);
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < filelist.size(); i++){
            jsonArray.add(filelist.get(i));
        }
        jsonObject.add("filelist", jsonArray);
        ResultUtil<String> resultUtil = new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "添加成功！");
        resultUtil.setBody(jsonObject.toString());  //返回档案在数据库中存储的id
        return resultUtil;

    }

    //删除档案
    @PostMapping("/deletedocumentrecord")
    @ApiOperation("删除档案记录")
    public ResultUtil<String> deleteDocumentRecord(
            @ApiParam(value = "档案记录在数据库中的编号", required = true) @RequestParam() String documentRecordId
    ) throws IOException {
        try {
            documentRecordService.deleteDocumentRecord(documentRecordId);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "删除成功！");
    }

    //添加档案对应的几个文件
    @PostMapping("/adddocumentrecordfile")
    @ApiOperation("新增档案记录关联文件")
    public ResultUtil<String> addDocumentRecordFile(
            @ApiParam(value = "档案记录在数据库中的编号", required = true) @RequestParam() String documentRecordId,
            @ApiParam(value = "文件清单", required = true) @RequestParam("file") MultipartFile[] filelist
    ) throws IOException {
        try {
            documentRecordService.addDocumentRecordFile(documentRecordId, filelist);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "添加成功！");
    }

    //下载档案文件
    @GetMapping("/downLoadDocumentRecordFile")
    @ApiOperation("下载档案文件")
    public void downLoadDocumentRecordFile(
            @ApiParam(value = "下载文件在数据库中的编号", required = true) @RequestParam(defaultValue = "5f657558411e4d7514d2603b") String fileId,
            HttpServletResponse response
    ) throws IOException {
        FileOutputStream fileOutputStream = null;

        DocumentFile documentFile = documentRecordService.downLoadDocumentRecordFile(fileId);
        byte[] buffer = documentFile.getFile();

        //先下载到本地
//        String path = "C:\\Users\\DF\\Desktop\\test11.jpg";
//        FileOutputStream out = new FileOutputStream(new File(path));
//        out.write(buffer);
//        File f = new File(path);
//
//        FileInputStream fileInputStream = new FileInputStream(f);

        //设置Http响应头告诉浏览器下载这个附件,下载的文件名也是在这里设置的
        response.setHeader("Content-Disposition", "attachment;Filename=" + URLEncoder.encode("test3.jpg", "UTF-8"));
        OutputStream outputStream = response.getOutputStream();
//        byte[] bytes = new byte[204800];
        int len = buffer.length;
//        while ((len = fileInputStream.read(bytes))>0){
            outputStream.write(buffer,0,len);
//        }
//        fileInputStream.close();
        outputStream.close();

//        return response;
//      return new ResultUtil<fileOutputStream>(ResponseConstant.ResponseCode.SUCCESS, "下载成功!");
    }

}