package com.example.wuhou.controller;

import com.example.wuhou.constant.PermissionConstant;
import com.example.wuhou.constant.ResponseConstant;
import com.example.wuhou.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;


@RestController
@RequestMapping("/test")
@Api(tags = "角色")
public class testGetImageController {
    @PostMapping("/getImage")
    @ApiOperation("获取图像")
    public String addRole(

            HttpServletRequest request,
            HttpServletResponse response,
            @ApiParam(value = "文件清单", required = true) @RequestParam("imageFile") String file
    ) throws IOException, ServletException {
        String result = "失败";
//        Part part = request.getPart("imageFile");
//        String headImageName = part.getSubmittedFileName(); //获取用户上传图片的图片名
//        part.write("C:\\Users\\DF\\Desktop\\" + headImageName);

        return result;
    }
}
