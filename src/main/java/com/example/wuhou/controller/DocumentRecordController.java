package com.example.wuhou.controller;

import com.example.wuhou.constant.PermissionConstant;
import com.example.wuhou.constant.ResponseConstant;

import com.example.wuhou.entity.DocumentRecord;
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
            @ApiParam(value = "单位代码", required = true) @RequestParam(defaultValue = "danwieCode") String danwieCode,
            @ApiParam(value = "单位名称", required = true) @RequestParam(defaultValue = "danweiName") String danweiName,
            @ApiParam(value = "档案室中的存放位置") @RequestParam(required = false, defaultValue = "") String position,
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
            documentRecord.setDanwieCode(danwieCode);
            documentRecord.setDanweiName(danweiName);
            documentRecord.setPosition(position);
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


    //添加档案对应的几个文件
    @RequiresRoles(value = {PermissionConstant.DOCUMENT_RECORD_ADD, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @PostMapping("/adddocumentrecordfile")
    @ApiOperation("新增档案记录关联文件")
    public ResultUtil<String> addDocumentRecordFile(
            @ApiParam(value = "档案记录在数据库中的编号", required = true) @RequestParam() String documentRecordId,
            @ApiParam(value = "文件清单", required = true) @RequestParam("file") MultipartFile[] filelist
    ){
        try {
            documentRecordService.addDocumentRecordFile(documentRecordId, filelist);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "新增失败: " + e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "添加成功！");
    }
    //下载档案文件
    @RequiresRoles(value = {PermissionConstant.DOCUMENT_RECORD_FILE_DOWNLOAD, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @GetMapping("/downLoadDocumentRecordFile")
    @ApiOperation("下载档案文件")
    public ResultUtil<String> downLoadDocumentRecordFile(
            @ApiParam(value = "档案记录在数据库中的编号", required = true) @RequestParam() String documentRecordId,
            @ApiParam(value = "下载文件名称", required = true) @RequestParam() String fileName,
            HttpServletResponse response
    ) {
        try {
            DocumentRecord documentRecord = documentRecordService.getDocumentRecordByDocumentRecordId(documentRecordId);
            String filepath = documentRecord.getDiskPath() + ":\\" + documentRecord.getStorePath() + "\\" + fileName;
//            File file = new File(filepath);
            InputStream inputStream = new BufferedInputStream(new FileInputStream(filepath));
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            response.reset();
            response.setHeader("Content-Disposition", "attachment;Filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "下载失败: " + e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "下载成功！");
    }

    //预览档案文件
    @RequiresRoles(value = {PermissionConstant.DOCUMENT_RECORD_CHECK, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @GetMapping("/previewDocumentRecordFile")
    @ApiOperation("预览文件")
    public ResultUtil<String> previewDocumentRecordFile(
            @ApiParam(value = "档案记录在数据库中的编号", required = true) @RequestParam() String documentRecordId,
            @ApiParam(value = "预览文件名称", required = true) @RequestParam() String fileName,
            HttpServletResponse response
    ) {
        try {
            DocumentRecord documentRecord = documentRecordService.getDocumentRecordByDocumentRecordId(documentRecordId);
            String filepath = documentRecord.getDiskPath() + ":\\" + documentRecord.getStorePath() + "\\" + fileName;
            File file = new File(filepath);
            InputStream inputStream = new BufferedInputStream(new FileInputStream(filepath));
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            response.reset();
            response.setHeader("Content-Disposition", "Filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "预览失败: " + e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "预览成功！");
    }
    //根据档案记录Id查询路径下对应的文件清单名
    @RequiresRoles(value = {PermissionConstant.DOCUMENT_RECORD_CHECK, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @GetMapping("/findFileListByDocumentRecordId")
    @ApiOperation("查找档案记录关联文件")
    public ResultUtil<List<String>> findFileListByDocumentRecordId(
            @ApiParam(value = "档案记录在数据库中的编号", required = true) @RequestParam() String documentRecordId
    ){
        List<String> fileList;
        try {
            fileList = documentRecordService.findFileListByDocumentRecordId(documentRecordId);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "查找失败: " + e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "查找成功！", fileList);
    }

    //普通查询
    @RequiresRoles(value = {PermissionConstant.DOCUMENT_RECORD_CHECK, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @GetMapping("/normalFindDocumentRecord")
    @ApiParam("普通查询档案记录, 0:默认是精确，1:模糊")
    public PageUtil normalFindDocumentRecord(
            @ApiParam(value = "案卷题名1") @RequestParam(required = false, defaultValue = "") String fileName,
            @ApiParam(value = "档号") @RequestParam(required = false, defaultValue = "") String documentNumber,
            @ApiParam(value = "全宗号1") @RequestParam(required = false, defaultValue = "") String recordGroupNumber,
            @ApiParam(value = "盒号1") @RequestParam(required = false, defaultValue = "") String boxNumber,
            @ApiParam(value = "年份1") @RequestParam(required = false, defaultValue = "") String year,
            @ApiParam(value = "保管期限") @RequestParam(required = false, defaultValue = "") String duration,
            @ApiParam(value = "密级") @RequestParam(required = false, defaultValue = "") String security,
            @ApiParam(value = "档案类别1") @RequestParam(required = false, defaultValue = "") String documentCategory,
            @ApiParam(value = "案卷类型1") @RequestParam(required = false, defaultValue = "") String fileCategory,
            @ApiParam(value = "责任者") @RequestParam(required = false, defaultValue = "") String responsible,
            @ApiParam(value = "单位代码") @RequestParam(required = false, defaultValue = "") String danwieCode,
            @ApiParam(value = "单位名称") @RequestParam(required = false, defaultValue = "") String danweiName,
            @ApiParam(value = "档案室中的存放位置") @RequestParam(required = false, defaultValue = "") String position,
            @ApiParam(value = "著录人") @RequestParam(required = false, defaultValue = "") String recorder,
            @ApiParam(value = "著录时间") @RequestParam(required = false, defaultValue = "") String recordTime,
            @ApiParam(value = "是否模糊查询", required = true) @RequestParam(defaultValue = "1") String blurryFind,
            @ApiParam(value = "当前显示页") @RequestParam(defaultValue = "1") Integer currentPage,
            @ApiParam(value = "页面大小", required = true) @RequestParam(defaultValue = "5") Integer pageSize
    ){
        Map<String, String> findKeyWordMap = new HashMap<>();
        if (!fileName.isEmpty()){
            findKeyWordMap.put("fileName", fileName);
        }
        if (!documentNumber.isEmpty()){
            findKeyWordMap.put("documentNumber", documentNumber);
        }
        if (!recordGroupNumber.isEmpty()){
            findKeyWordMap.put("recordGroupNumber", recordGroupNumber);
        }
        if (!boxNumber.isEmpty()){
            findKeyWordMap.put("boxNumber", boxNumber);
        }
        if (!year.isEmpty()){
            findKeyWordMap.put("year", year);
        }
        if (!duration.isEmpty()){
            findKeyWordMap.put("duration", duration);
        }
        if (!security.isEmpty()){
            findKeyWordMap.put("security", security);
        }
        if (!documentCategory.isEmpty()){
            findKeyWordMap.put("documentCategory", documentCategory);
        }
        if (!fileCategory.isEmpty()){
            findKeyWordMap.put("fileCategory", fileCategory);
        }
        if (!responsible.isEmpty()){
            findKeyWordMap.put("responsible", responsible);
        }
        if (!danwieCode.isEmpty()){
            findKeyWordMap.put("danwieCode", danwieCode);
        }
        if (!danweiName.isEmpty()){
            findKeyWordMap.put("danweiName", danweiName);
        }
        if (!position.isEmpty()){
            findKeyWordMap.put("position", position);
        }
        if (!recorder.isEmpty()){
            findKeyWordMap.put("recorder", recorder);
        }
        if (!recordTime.isEmpty()){
            findKeyWordMap.put("recordTime", recordTime);
        }
        PageUtil pageUtil;
        try {
            pageUtil = documentRecordService.normalFindDocumentRecord(findKeyWordMap, blurryFind, currentPage, pageSize);
        }catch (Exception e){
            return new PageUtil(ResponseConstant.ResponseCode.FAILURE, "查找失败: " + e.getMessage());
        }
        pageUtil.setCode(ResponseConstant.ResponseCode.SUCCESS);
        pageUtil.setMessage("查询成功");
        return pageUtil;
    }

    //一般查询
    @RequiresRoles(value = {PermissionConstant.DOCUMENT_RECORD_CHECK, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @GetMapping("/generalFindDocumentRecord")
    @ApiParam("一般查询档案记录, 0:默认是精确，1:模糊")
    public PageUtil generalFindDocumentRecord(
            @ApiParam(value = "需要查询的多个关键字，空格隔开") @RequestParam(defaultValue = "") String multiKeyWord,
            @ApiParam(value = "是否模糊查询", required = true) @RequestParam(defaultValue = "1") String blurryFind,
            @ApiParam(value = "当前显示页") @RequestParam(defaultValue = "1") Integer currentPage,
            @ApiParam(value = "页面大小", required = true) @RequestParam(defaultValue = "5") Integer pageSize
    ){
        PageUtil pageUtil;
        try {
            pageUtil = documentRecordService.generalFindDocumentRecord(multiKeyWord, blurryFind, currentPage, pageSize);
        }catch (Exception e){
            return new PageUtil(ResponseConstant.ResponseCode.FAILURE, "查找失败: " + e.getMessage());
        }
        pageUtil.setCode(ResponseConstant.ResponseCode.SUCCESS);
        pageUtil.setMessage("查询成功！");
        return pageUtil;
    }

    //组合查询
    @RequiresRoles(value = {PermissionConstant.DOCUMENT_RECORD_CHECK, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @GetMapping("/combinationFindDocumentRecord")
    @ApiParam("一般查询档案记录, 0:默认是精确，1:模糊")
    public PageUtil combinationFindDocumentRecord(
            @ApiParam(value = "需要查询的多内容") @RequestParam() String list,
            @ApiParam(value = "是否模糊查询", required = true) @RequestParam(defaultValue = "1") String blurryFind,
            @ApiParam(value = "当前显示页") @RequestParam(defaultValue = "1") Integer currentPage,
            @ApiParam(value = "页面大小", required = true) @RequestParam(defaultValue = "5") Integer pageSize
    ){
        PageUtil pageUtil;
        try {
            JSONArray jsonArray = JSONArray.fromObject(list);
//            for (int i = 0; i < jsonArray.size(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                System.out.println("json数组传递过来的参数为:" + "第" + i + "条:" + "\n" + jsonObject.get("id"));
//            }
            pageUtil = documentRecordService.combinationFindDocumentRecord(jsonArray, blurryFind, currentPage, pageSize);
        }catch (Exception e){
            return new PageUtil(ResponseConstant.ResponseCode.FAILURE, "查找失败: " + e.getMessage());
        }
        pageUtil.setCode(ResponseConstant.ResponseCode.SUCCESS);
        pageUtil.setMessage("查询成功！");
        return pageUtil;
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
            documentRecordService.deleteDocumentRecordFile(file, documentRecordId);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "删除失败: " + e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "删除成功！");
    }

    @RequiresRoles(value = {PermissionConstant.DOCUMENT_RECORD_MODIFY, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @PostMapping("/modifyDocumentRecord")
    @ApiOperation("修改档案记录")
    public ResultUtil<String> modifyDocumentRecord(
            @ApiParam(value = "案卷题名", required = true) @RequestParam(defaultValue = "1") String fileName,
            @ApiParam(value = "档号", required = true) @RequestParam(defaultValue = "1") String documentNumber,
            @ApiParam(value = "全宗号", required = true) @RequestParam(defaultValue = "129") String recordGroupNumber,
            @ApiParam(value = "盒号", required = true) @RequestParam(defaultValue = "1") String boxNumber,
            @ApiParam(value = "年份", required = true) @RequestParam(defaultValue = "1") String year,
            @ApiParam(value = "保管期限", required = true) @RequestParam(defaultValue = "1") String duration,
            @ApiParam(value = "密级") @RequestParam(required = false, defaultValue = "") String security,
            @ApiParam(value = "档案类别", required = true) @RequestParam(defaultValue = "1") String documentCategory,
            @ApiParam(value = "案卷类型", required = true) @RequestParam(defaultValue = "1") String fileCategory,
            @ApiParam(value = "责任者") @RequestParam(required = false, defaultValue = "") String responsible,
            @ApiParam(value = "单位代码", required = true) @RequestParam(defaultValue = "1") String danwieCode,
            @ApiParam(value = "单位名称", required = true) @RequestParam(defaultValue = "1") String danweiName,
            @ApiParam(value = "档案室中的存放位置") @RequestParam(required = false, defaultValue = "") String position,
            @ApiParam(value = "著录人", required = true) @RequestParam(defaultValue = "1") String recorder,
            @ApiParam(value = "著录时间", required = true) @RequestParam(defaultValue = "1") String recordTime,
            @ApiParam(value = "需要修改的档案记录的id", required = true) @RequestParam(defaultValue = "1") String documentRecordId
    ){
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
            newDocumentRecord.setDanwieCode(danwieCode);
            newDocumentRecord.setDanweiName(danweiName);
            newDocumentRecord.setPosition(position);
            newDocumentRecord.setRecorder(recorder);
            newDocumentRecord.setRecordTime(recordTime);

            documentRecordService.modifyDocumentRecord(newDocumentRecord);

        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "修改失败: " + e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "档案记录修改成功！");
    }
}