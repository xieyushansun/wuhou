package com.example.wuhou.controller;

import com.example.wuhou.constant.PermissionConstant;
import com.example.wuhou.constant.ResponseConstant;

import com.example.wuhou.entity.DocumentRecord;
import com.example.wuhou.service.DocumentFileService;
import com.example.wuhou.util.PageUtil;
import com.example.wuhou.service.DocumentRecordService;
import com.example.wuhou.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/document")
@Api(tags = "档案记录")
public class DocumentRecordController {
    @Autowired
    DocumentRecordService documentRecordService;
    @Autowired
    DocumentFileService documentFileService;
    @RequiresRoles(value = {PermissionConstant.DOCUMENT_RECORD_ADD, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @PostMapping("/adddocumentrecord")
    @ApiOperation("新增档案记录")
    public ResultUtil<String> addDocumentRecord(
            @ApiParam(value = "案卷题名", required = true) @RequestParam(defaultValue = "fileName") String fileName,
            @ApiParam(value = "档号", required = true) @RequestParam(defaultValue = "documentNumber") String documentNumber,
            @ApiParam(value = "全宗号", required = true) @RequestParam(defaultValue = "129") String recordGroupNumber,
            @ApiParam(value = "盒号", required = true) @RequestParam(defaultValue = "boxNumber") String boxNumber,
            @ApiParam(value = "年份", required = true) @RequestParam(defaultValue = "2020") String year,
            @ApiParam(value = "保管期限", required = true) @RequestParam(defaultValue = "duration") String duration,
            @ApiParam(value = "密级") @RequestParam(required = false, defaultValue = "") String security,
            @ApiParam(value = "档案类别", required = true) @RequestParam(defaultValue = "documentCategory") String documentCategory,
            @ApiParam(value = "案卷类型", required = true) @RequestParam(defaultValue = "fileCategory") String fileCategory,
            @ApiParam(value = "责任者") @RequestParam(required = false, defaultValue = "") String responsible,
            @ApiParam(value = "单位代码") @RequestParam(required = false, defaultValue = "") String danweiCode,
            @ApiParam(value = "单位名称") @RequestParam(required = false, defaultValue = "") String danweiName,
            @ApiParam(value = "档案室中的存放位置") @RequestParam(required = false, defaultValue = "") String position,
            @ApiParam(value = "产生时间") @RequestParam(required = false, defaultValue = "") String generateTime,
            @ApiParam(value = "序号", required = true) @RequestParam(defaultValue = "1") String order,
            @ApiParam(value = "页码", required = true) @RequestParam(defaultValue = "1") String pageNumber,
            @ApiParam(value = "性别", required = false) @RequestParam(defaultValue = "1") String sex,
            @ApiParam(value = "著录人", required = true) @RequestParam(defaultValue = "1") String recorder,
            @ApiParam(value = "著录时间", required = true) @RequestParam(defaultValue = "1") String recordTime
            ){
        String documentRecordId;
        try {
            DocumentRecord documentRecord = new DocumentRecord();

            documentRecord.setFileName(fileName);
            documentRecord.setDocumentNumber(documentNumber);
            documentRecord.setRecordGroupNumber(recordGroupNumber);
            documentRecord.setBoxNumber(boxNumber);
            documentRecord.setYear(year);
            documentRecord.setDuration(duration);
            documentRecord.setSecurity(security);
            documentRecord.setDocumentCategory(documentCategory);
            documentRecord.setFileCategory(fileCategory);
            documentRecord.setResponsible(responsible);
            documentRecord.setDanweiCode(danweiCode);
            documentRecord.setDanweiName(danweiName);
            documentRecord.setPosition(position);
            documentRecord.setGenerateTime(generateTime);
            documentRecord.setOrder(order);
            documentRecord.setPageNumber(pageNumber);
            documentRecord.setSex(sex);
            documentRecord.setRecorder(recorder);
            documentRecord.setRecordTime(recordTime);

            documentRecordId = documentRecordService.addDocumentRecord(documentRecord);

        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "添加失败: " + e.getMessage());
        }
        ResultUtil<String> resultUtil = new ResultUtil<String>(ResponseConstant.ResponseCode.SUCCESS, "添加成功！");
        //返回档案记录Id
        resultUtil.setBody(documentRecordId);
        return resultUtil;
    }

    //删除档案
    @RequiresRoles(value = {PermissionConstant.DOCUMENT_RECORD_DELETE, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @PostMapping("/deletedocumentrecord")
    @ApiOperation("删除档案记录")
    public ResultUtil<String> deleteDocumentRecord(
            @ApiParam(value = "档案记录在数据库中的编号", required = true) @RequestParam() String documentRecordId
    ) throws IOException {
        try {
            documentRecordService.deleteDocumentRecord(documentRecordId);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "删除失败: " + e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "删除成功！");
    }

    //删除档案记录对应文件
    @RequiresRoles(value = {PermissionConstant.DOCUMENT_RECORD_DELETE, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @PostMapping("/deleteDocumentRecordFile")
    @ApiOperation("删除档案记录文件")
    public ResultUtil<String> deleteDocumentRecordFile(
            @ApiParam(value = "档案记录在数据库中的编号", required = true) @RequestParam() String documentRecordId,
            @ApiParam(value = "删除文件名称", required = true) @RequestParam() String fileName
    ) {
        try {
            DocumentRecord documentRecord = documentRecordService.getDocumentRecordByDocumentRecordId(documentRecordId);
            String filepath = documentRecord.getDiskPath() + ":\\" + documentRecord.getStorePath() + "\\" + fileName;
            File file = new File(filepath);
            documentFileService.deleteDocumentRecordFile(file, documentRecordId);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "删除失败: " + e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "删除成功！");
    }

    @RequiresRoles(value = {PermissionConstant.DOCUMENT_RECORD_MODIFY, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @PostMapping("/modifyDocumentRecord")
    @ApiOperation("修改档案记录")
    public ResultUtil<String> modifyDocumentRecord(
            @ApiParam(value = "案卷题名", required = true) @RequestParam() String fileName,
            @ApiParam(value = "档号", required = true) @RequestParam() String documentNumber,
            @ApiParam(value = "全宗号", required = true) @RequestParam() String recordGroupNumber,
            @ApiParam(value = "盒号", required = true) @RequestParam() String boxNumber,
            @ApiParam(value = "年份", required = true) @RequestParam() String year,
            @ApiParam(value = "保管期限", required = true) @RequestParam() String duration,
            @ApiParam(value = "密级") @RequestParam(required = false, defaultValue = "") String security,
            @ApiParam(value = "档案类别", required = true) @RequestParam() String documentCategory,
            @ApiParam(value = "案卷类型", required = true) @RequestParam() String fileCategory,
            @ApiParam(value = "责任者") @RequestParam(required = false, defaultValue = "") String responsible,
            @ApiParam(value = "单位代码") @RequestParam(required = false, defaultValue = "") String danweiCode,
            @ApiParam(value = "单位名称") @RequestParam(required = false, defaultValue = "") String danweiName,
            @ApiParam(value = "档案室中的存放位置") @RequestParam(required = false, defaultValue = "") String position,
            @ApiParam(value = "产生时间") @RequestParam(required = false, defaultValue = "") String generateTime,
            @ApiParam(value = "著录人", required = true) @RequestParam() String recorder,
            @ApiParam(value = "著录时间", required = true) @RequestParam() String recordTime,
            @ApiParam(value = "磁盘名", required = true) @RequestParam() String diskPath,
            @ApiParam(value = "磁盘名", required = true) @RequestParam() String storePath,
            @ApiParam(value = "序号", required = true) @RequestParam() String order,
            @ApiParam(value = "页码", required = true) @RequestParam() String pageNumber,
            @ApiParam(value = "性别", required = false) @RequestParam() String sex,
            @ApiParam(value = "需要修改的档案记录的id", required = true) @RequestParam(defaultValue = "1") String documentRecordId
    ){
        String newStorePath = "";
        try {
            DocumentRecord newDocumentRecord = new DocumentRecord();

            newDocumentRecord.setId(documentRecordId);
            newDocumentRecord.setFileName(fileName);
            newDocumentRecord.setDocumentNumber(documentNumber);
            newDocumentRecord.setRecordGroupNumber(recordGroupNumber);
            newDocumentRecord.setBoxNumber(boxNumber);
            newDocumentRecord.setYear(year);
            newDocumentRecord.setDuration(duration);
            newDocumentRecord.setSecurity(security);
            newDocumentRecord.setDocumentCategory(documentCategory);
            newDocumentRecord.setFileCategory(fileCategory);
            newDocumentRecord.setResponsible(responsible);
            newDocumentRecord.setDanweiCode(danweiCode);
            newDocumentRecord.setDanweiName(danweiName);
            newDocumentRecord.setPosition(position);
            newDocumentRecord.setGenerateTime(generateTime);
            newDocumentRecord.setRecorder(recorder);
            newDocumentRecord.setRecordTime(recordTime);
            newDocumentRecord.setDiskPath(diskPath);
            newDocumentRecord.setStorePath(storePath);
            newDocumentRecord.setOrder(order);
            newDocumentRecord.setPageNumber(pageNumber);
            newDocumentRecord.setSex(sex);

            newStorePath = documentRecordService.modifyDocumentRecord(newDocumentRecord);

        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "修改失败: " + e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "档案记录修改成功！", newStorePath);
    }
}