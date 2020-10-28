package com.example.wuhou.controller;

import com.example.wuhou.constant.PermissionConstant;
import com.example.wuhou.constant.ResponseConstant;
import com.example.wuhou.service.DocumentFileService;
import com.example.wuhou.service.DocumentRecordFindService;
import com.example.wuhou.service.DocumentRecordService;
import com.example.wuhou.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONArray;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/document")
@Api(tags = "档案记录")
public class DocumentRecordFindController {
    @Autowired
    DocumentRecordService documentRecordService;
    @Autowired
    DocumentRecordFindService documentRecordFindService;
    //普通查询
    @RequiresRoles(value = {PermissionConstant.DOCUMENT_RECORD_CHECK, PermissionConstant.FILE_CATALOG, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
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
            @ApiParam(value = "单位代码") @RequestParam(required = false, defaultValue = "") String danweiCode,
            @ApiParam(value = "单位名称") @RequestParam(required = false, defaultValue = "") String danweiName,
            @ApiParam(value = "档案室中的存放位置") @RequestParam(required = false, defaultValue = "") String position,
            @ApiParam(value = "产生时间") @RequestParam(required = false, defaultValue = "") String generateTime,
            @ApiParam(value = "著录人") @RequestParam(required = false, defaultValue = "") String recorder,
            @ApiParam(value = "著录时间") @RequestParam(required = false, defaultValue = "") String recordTime,
            @ApiParam(value = "是否模糊查询", required = true) @RequestParam(defaultValue = "1") String blurryFind,
            @ApiParam(value = "序号") @RequestParam(required = false, defaultValue = "") String order,
            @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "") String pageNumber,
            @ApiParam(value = "性别") @RequestParam(required = false, defaultValue = "") String sex,
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
        if (!danweiCode.isEmpty()){
            findKeyWordMap.put("danweiCode", danweiCode);
        }
        if (!danweiName.isEmpty()){
            findKeyWordMap.put("danweiName", danweiName);
        }
        if (!position.isEmpty()){
            findKeyWordMap.put("position", position);
        }
        if (!generateTime.isEmpty()){
            findKeyWordMap.put("generateTime", generateTime);
        }
        if (!recorder.isEmpty()){
            findKeyWordMap.put("recorder", recorder);
        }
        if (!recordTime.isEmpty()){
            findKeyWordMap.put("recordTime", recordTime);
        }
        if (!order.isEmpty()){
            findKeyWordMap.put("order", order);
        }
        if (!pageNumber.isEmpty()){
            findKeyWordMap.put("pageNumber", pageNumber);
        }
        if(!sex.isEmpty()){
            findKeyWordMap.put("sex", sex);
        }
        PageUtil pageUtil;
        try {
            pageUtil = documentRecordFindService.normalFindDocumentRecord(findKeyWordMap, blurryFind, currentPage, pageSize);
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
            pageUtil = documentRecordFindService.generalFindDocumentRecord(multiKeyWord, blurryFind, currentPage, pageSize);
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
            pageUtil = documentRecordFindService.combinationFindDocumentRecord(jsonArray, blurryFind, currentPage, pageSize);
        }catch (Exception e){
            return new PageUtil(ResponseConstant.ResponseCode.FAILURE, "查找失败: " + e.getMessage());
        }
        pageUtil.setCode(ResponseConstant.ResponseCode.SUCCESS);
        pageUtil.setMessage("查询成功！");
        return pageUtil;
    }
}
