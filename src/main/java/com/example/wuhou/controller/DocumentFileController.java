package com.example.wuhou.controller;

import com.example.wuhou.constant.PathConstant;
import com.example.wuhou.constant.PermissionConstant;
import com.example.wuhou.constant.ResponseConstant;
import com.example.wuhou.entity.DocumentRecord;
import com.example.wuhou.service.DocumentFileService;
import com.example.wuhou.service.DocumentRecordService;
import com.example.wuhou.util.FileOperationUtil;
import com.example.wuhou.util.ImgToPDFUtil;
import com.example.wuhou.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/document")
@Api(tags = "档案记录")
public class DocumentFileController {
    @Autowired
    DocumentRecordService documentRecordService;
    @Autowired
    DocumentFileService documentFileService;
    //添加档案对应的几个文件
    @RequiresRoles(value = {PermissionConstant.DOCUMENT_RECORD_ADD, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @PostMapping("/adddocumentrecordfile")
    @ApiOperation("新增档案记录关联文件")
    public ResultUtil<String> addDocumentRecordFile(
            @ApiParam(value = "档案记录在数据库中的编号", required = true) @RequestParam() String documentRecordId,
            @ApiParam(value = "文件清单", required = true) @RequestParam("file") MultipartFile[] filelist
    ){
        try {
            documentFileService.addDocumentRecordFile(documentRecordId, filelist);
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
            fileList = documentFileService.findFileListByDocumentRecordId(documentRecordId);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "查找失败: " + e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "查找成功！", fileList);
    }

    @RequiresRoles(value = {PermissionConstant.DOCUMENT_RECORD_FILE_DOWNLOAD, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @GetMapping("/downLoadDocumentFileToPDF")
    @ApiOperation("将同一案卷题名下的所有图片打包成一个PDF")
    public ResultUtil<String> downLoadDocumentFileToPDF(
        @ApiParam(value = "档案记录在数据库中的编号", required = true) @RequestParam() String documentRecordId,
        HttpServletResponse response
    ){
        try {
            DocumentRecord documentRecord = documentRecordService.getDocumentRecordByDocumentRecordId(documentRecordId);
            String filepath = documentRecord.getDiskPath() + ":\\" + documentRecord.getStorePath() + "\\";
            //将filepath目录下的所有图片生成为一个pdf
            String pdfFileName = documentRecord.getFileName() + ".pdf";
            String outPdfFilePath = PathConstant.PDF_OUTPUT + pdfFileName;
            //生成pdf
            ImgToPDFUtil.toPdf(filepath, outPdfFilePath);
            InputStream inputStream = new BufferedInputStream(new FileInputStream(outPdfFilePath));
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            response.reset();
            response.setHeader("Content-Disposition", "attachment;Filename=" + URLEncoder.encode(pdfFileName, StandardCharsets.UTF_8));
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            File file = new File(outPdfFilePath);
            if (file.exists()){
                file.delete();
            }
        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "生成pdf失败：" + e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "生成pdf成功");
    }
    @RequiresRoles(value={PermissionConstant.DOCUMENT_RECORD_ADD, PermissionConstant.SUPERADMIN}, logical = Logical.OR)
    @GetMapping("/deleteAllFile")
    @ApiOperation("删除某档案记录下所有文件")
    public ResultUtil<String> deleteAllFileByDocumentRecordId(
            @ApiParam(value = "档案记录在数据库中的编号", required = true) @RequestParam() String documentRecordId
    ){
        try {
            DocumentRecord documentRecord = documentRecordService.getDocumentRecordByDocumentRecordId(documentRecordId);
            String filepath = documentRecord.getDiskPath() + ":\\" + documentRecord.getStorePath() + "\\";
            FileOperationUtil.delAllFile(filepath);
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "删除所有文件失败：" + e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "删除所有文件成功");
    }
}
