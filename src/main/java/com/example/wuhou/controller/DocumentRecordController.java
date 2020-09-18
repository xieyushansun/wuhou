package com.example.wuhou.controller;

import com.example.wuhou.constant.ResponseConstant;
import com.example.wuhou.entity.DocumentRecord;
import com.example.wuhou.entity.User;
import com.example.wuhou.exception.ExistException;
import com.example.wuhou.service.DocumentRecordService;
import com.example.wuhou.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/document")
@Api(tags = "档案")
public class DocumentRecordController {
    @Autowired
    DocumentRecordService documentRecordService;
    @GetMapping("/adddocumentrecord")
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
            @ApiParam(value = "案卷类型", required = true) @RequestParam(defaultValue = "1") String fileCategory,
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
        ResultUtil<String> resultUtil = new ResultUtil<>(ResponseConstant.ResponseCode.NOT_LOGIN, "添加成功！");
        resultUtil.setBody(documentRecordId);  //返回档案在数据库中存储的id
        return resultUtil;
//        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "添加成功！");
//        String userame = userfind.getUserName();
    }

    //添加档案对应的几个文件
    @PostMapping("/adddocumentrecordfile")
    @ApiOperation("新增档案记录")
    public ResultUtil<String> addDocumentRecordFile(
            @ApiParam(value = "档案记录在数据库中的编号", required = true) @RequestParam() String documentRecordId,
            @ApiParam(value = "文件清单", required = true) @RequestParam() MultipartFile[] filelist
    ) throws IOException {
        try {
            documentRecordService.addDocumentRecordFile(documentRecordId, filelist);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "添加成功！");
    }

}
