package com.example.wuhou.controller;

import com.example.wuhou.constant.ResponseConstant;
import com.example.wuhou.entity.DocumentCategory;

import com.example.wuhou.service.DocumentCategoryService;
import com.example.wuhou.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/DocumentCategory")
@Api(tags = "档案")
public class DocumentCategoryController {
    @Autowired
    DocumentCategoryService documentCategoryService;

    @PostMapping("/addDocumentCategory")
    @ApiOperation("新增案卷类别")
    public ResultUtil<String> addDocumentCategoryService(
            @ApiParam(value = "档案类别", required = true) @RequestParam(defaultValue = "就业创业补助资金") String documentCategory,
            @ApiParam(value = "档案类别缩写", required = true) @RequestParam() String documentCategoryShortName,
            @ApiParam(value = "案卷类别", required = true) @RequestParam() String[] fileCategory
    ){
        try {
            documentCategoryService.addDocumentCategory(documentCategory, documentCategoryShortName, fileCategory);
        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }

        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "添加成功！");
    }
    @PostMapping("/deleteDocumentCategory")
    @ApiOperation("删除档案类别")
    public ResultUtil<String> deleteDocumentCategory(
            @ApiParam(value = "档案id", required = true) @RequestParam() String id
    ){
        try {
            documentCategoryService.deleteDocumentCategory(id);
        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }

        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "删除成功！");
    }
    @PostMapping("/modifyDocumentCategory")
    @ApiOperation("修改档案类别数据")
    public ResultUtil<String> modifyDocumentCategory(
            @ApiParam(value = "档案id", required = true) @RequestParam() String id,
            @ApiParam(value = "档案类别", required = true) @RequestParam(defaultValue = "就业创业补助资金") String documentCategory,
            @ApiParam(value = "档案类别缩写", required = true) @RequestParam() String documentCategoryShortName,
            @ApiParam(value = "案卷类别", required = true) @RequestParam() String[] fileCategory
    ){
        try {
            documentCategoryService.modifyDocumentCategory(id, documentCategory, documentCategoryShortName, fileCategory);;
        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }
        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "修改成功！");
    }

    @GetMapping("/FindAllDocumentCategory")
    @ApiOperation("查找所有档案类别数据")
    public ResultUtil<List<DocumentCategory>> FindAllDocumentCategory(){
        List<DocumentCategory> documentCategories;
        try {
            documentCategories = documentCategoryService.findAllDocumentCategory();
        } catch (Exception e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }
        ResultUtil<List<DocumentCategory>> resultUtil = new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "返回成功！");
        resultUtil.setBody(documentCategories);
        return resultUtil;
    }
}
