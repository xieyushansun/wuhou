package com.example.wuhou.controller;

import com.example.wuhou.constant.PathConstant;
import com.example.wuhou.constant.PermissionConstant;
import com.example.wuhou.constant.ResponseConstant;
import com.example.wuhou.entity.Log;
import com.example.wuhou.service.FileCatalogingService;
import com.example.wuhou.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/cataloging")
@Api(tags = "案卷编目")
public class FileCatalogingController {
    @Autowired
    FileCatalogingService fileCatalogingService;

    @RequiresRoles(value = {PermissionConstant.SUPERADMIN, PermissionConstant.FILE_CATALOG}, logical = Logical.OR)
    @GetMapping("/outputFileCatalog")
    @ApiOperation("导出案卷编目")
    public ResultUtil<List<Log>> outputFileCatalog(
            @ApiParam(value = "档号", required = true) @RequestParam() String documentNumber,
            HttpServletResponse response
    ){
        String filename = "编目导出.docx";
        String outputPath = PathConstant.WORD_OUTPUT;
        try {
            //生成文件
            fileCatalogingService.outputFileCatalog(documentNumber);
            InputStream inputStream = new BufferedInputStream(new FileInputStream(outputPath));
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            response.reset();
            response.setHeader("Content-Disposition", "attachment;Filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8));
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        }catch (Exception e){
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "导出失败: " + e.getMessage());
        }
        File file = new File(outputPath);
        file.delete();
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "导出成功！");
    }
}

