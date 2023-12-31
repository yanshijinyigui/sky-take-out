package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Slf4j
@Api(tags = "文件上传")
public class CommonController {
    @Autowired
    AliOssUtil aliOssUtil;


    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {

        String originalFilename = file.getOriginalFilename();

        //截取原始文件名的后缀   dfdfdf.png
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

       String s= UUID.randomUUID().toString() + extension;

      String S1=  aliOssUtil.upload(file.getBytes(), s);




        return Result.success(S1);

    }




}
