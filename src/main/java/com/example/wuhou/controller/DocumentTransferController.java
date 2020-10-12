package com.example.wuhou.controller;

import com.example.wuhou.constant.PermissionConstant;
import com.example.wuhou.constant.ResponseConstant;
import com.example.wuhou.entity.DocumentTransfer;
import com.example.wuhou.service.DocumentTransferService;
import com.example.wuhou.util.PageUtil;
import com.example.wuhou.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/DocumentTransfer")
@Api(tags = "调卷登记")
public class DocumentTransferController {
    @Autowired
    DocumentTransferService documentTransferService;

    @RequiresRoles(value = {PermissionConstant.SUPERADMIN, PermissionConstant.DOCUMENT_TRANSFER_MANAGE}, logical = Logical.OR)
    @PostMapping("/addDocumentTransfer")
    @ApiOperation("新增调卷记录")
    public ResultUtil addDocumentTransfer(
            @ApiParam(value = "盒号", required = true) @RequestParam() String boxNumber,
            @ApiParam(value = "借阅人", required = true) @RequestParam() String borrower
//            @ApiParam(value = "借阅日期", required = true) @RequestParam() String borrowDate
//            @ApiParam(value = "归还日期", required = true) @RequestParam() String returnDate
    ){
        try {
            documentTransferService.addDocumentTransfer(boxNumber, borrower);
        } catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "添加失败: " + e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "添加成功！");
    }
    @RequiresRoles(value = {PermissionConstant.SUPERADMIN, PermissionConstant.DOCUMENT_TRANSFER_MANAGE}, logical = Logical.OR)
    @PostMapping("/delteDocumentTransfer")
    @ApiOperation("删除调卷记录")
    public ResultUtil delteDocumentTransfer(
            @ApiParam(value = "调卷记录id", required = true) @RequestParam() String id
    ){
        try {
            documentTransferService.delteDocumentTransfer(id);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "删除失败: " + e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "删除成功！");
    }
    @RequiresRoles(value = {PermissionConstant.SUPERADMIN, PermissionConstant.DOCUMENT_TRANSFER_MANAGE}, logical = Logical.OR)
    @PostMapping("/returnDocumentTransfer")
    @ApiOperation("归还调卷")
    public ResultUtil returnDocumentTransfer(
            @ApiParam(value = "调卷记录id", required = true) @RequestParam() String id
    ) throws Exception {
        try {
            documentTransferService.returnDocumentTransfer(id);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "归还失败: " + e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "归还成功！");
    }
    @RequiresRoles(value = {PermissionConstant.SUPERADMIN, PermissionConstant.DOCUMENT_TRANSFER_MANAGE}, logical = Logical.OR)
    @GetMapping("/findAllDocumentTransfer")
    @ApiOperation("分页查找所有调卷")
    public PageUtil findAllDocumentTransfer(
            @ApiParam(value = "当前显示页") @RequestParam(defaultValue = "1") Integer currentPage,
            @ApiParam(value = "页面大小", required = true) @RequestParam(defaultValue = "5") Integer pageSize
    ){
        PageUtil pageUtil;
        try {
            pageUtil = documentTransferService.findAllDocumentTransfer(currentPage, pageSize);
        }catch (Exception e){
            return new PageUtil(ResponseConstant.ResponseCode.FAILURE, "查询失败: " + e.getMessage());
        }
        pageUtil.setMessage("查找成功！");
        pageUtil.setCode(ResponseConstant.ResponseCode.SUCCESS);
        return pageUtil;
    }
    //普通查询调卷记录
    @RequiresRoles(value = {PermissionConstant.SUPERADMIN, PermissionConstant.DOCUMENT_TRANSFER_MANAGE}, logical = Logical.OR)
    @GetMapping("/normalFindDocumentTransfer")
    @ApiOperation("普通查询调卷记录")
    public PageUtil normalFindDocumentTransfer(
            @ApiParam(value = "盒号") @RequestParam(defaultValue = "") String boxNumber,
            @ApiParam(value = "借阅人") @RequestParam(defaultValue = "") String borrower,
            @ApiParam(value = "借阅记录者") @RequestParam(defaultValue = "") String borrowRecorder,
            @ApiParam(value = "归还记录者") @RequestParam(defaultValue = "") String returnRecorder,
            @ApiParam(value = "日期范围, 包括日期开始和结束天") @RequestParam(defaultValue = "") String borrowDateFanwei,
            @ApiParam(value = "是否模糊查询") @RequestParam(defaultValue = "1") String blurryFind,
            @ApiParam(value = "当前显示页") @RequestParam(defaultValue = "1") Integer currentPage,
            @ApiParam(value = "页面大小", required = true) @RequestParam(defaultValue = "5") Integer pageSize
    ){
        Map<String, String> findKeyWordMap = new HashMap<>();
        if (!boxNumber.isEmpty()){
            findKeyWordMap.put("boxNumber", boxNumber);
        }
        if (!borrower.isEmpty()){
            findKeyWordMap.put("borrower", borrower);
        }
        if (!borrowRecorder.isEmpty()){
            findKeyWordMap.put("borrowRecorder", borrowRecorder);
        }
        if (!returnRecorder.isEmpty()){
            findKeyWordMap.put("returnRecorder", returnRecorder);
        }
        if (!borrowDateFanwei.isEmpty()){
            findKeyWordMap.put("borrowDateFanwei", borrowDateFanwei);
        }
        PageUtil pageUtil;
        try {
            pageUtil = documentTransferService.normalFindDocumentTransfer(findKeyWordMap, currentPage, pageSize, blurryFind);
        }catch (Exception e){
            return new PageUtil<>(ResponseConstant.ResponseCode.FAILURE, "查询失败: " + e.getMessage());
        }
        pageUtil.setCode(ResponseConstant.ResponseCode.SUCCESS);
        pageUtil.setMessage("查询成功！");
        return pageUtil;
    }
    //查找未归还调卷
    @RequiresRoles(value = {PermissionConstant.SUPERADMIN, PermissionConstant.DOCUMENT_TRANSFER_MANAGE}, logical = Logical.OR)
    @GetMapping("/findDocumentTransferByNotReturned")
    @ApiOperation("查找未归还调卷")
    public ResultUtil findDocumentTransferByNotReturned(){
        List<DocumentTransfer> documentTransferList;
        try {
            documentTransferList = documentTransferService.findDocumentTransferByNotReturned();
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "查询成功！", documentTransferList);
    }

}
