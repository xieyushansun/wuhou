package com.example.wuhou.controller;

import com.example.wuhou.constant.ResponseConstant;
import com.example.wuhou.exception.ExistException;
import com.example.wuhou.service.FileCategoryService;
import com.example.wuhou.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/filecategory")
@Api(tags = "案卷")
public class FileCategoryController {
    @Autowired
    FileCategoryService fileCategoryService;
    @GetMapping("/addfilecategory")
    @ApiOperation("新增案卷类别")
    public ResultUtil<String> addUser(
            @ApiParam(value = "案卷类别", required = true) @RequestParam(defaultValue = "2016-2018劳务品牌培训补贴申报资料") String fileCategory,
            @ApiParam(value = "档案类别", required = true) @RequestParam(defaultValue = "就业创业补助资金") String documentCategory,
            @ApiParam(value = "保管期限", required = true) @RequestParam(defaultValue = "永久") String duration,
            @ApiParam(value = "涉及科室", required = true) @RequestParam(defaultValue = "就业培训科") String department,
            @ApiParam(value = "所需文件清单", required = true) @RequestParam(defaultValue = "") List<String> filelist
    ){
        try {
            fileCategoryService.addfileCategory(fileCategory, duration, department, filelist, documentCategory);
        } catch (ExistException e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, e.getMessage());
        }

        return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "添加成功！");
//        String userame = userfind.getUserName();
    }
}
