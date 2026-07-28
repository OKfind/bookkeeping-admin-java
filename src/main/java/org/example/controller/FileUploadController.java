package org.example.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.pojo.Result;
import org.example.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * File upload API.
 */
@RestController
@Tag(name = "文件上传OSS")
public class FileUploadController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        // 获取文件的名称
        String originFileName = file.getOriginalFilename();
        // 避免上传同一个文件导致名称一样而上传不成功的问题
        String fileName = UUID.randomUUID().toString()+originFileName.substring(originFileName.lastIndexOf("."));
        String url = aliOssUtil.uploadFile(fileName,file.getInputStream());
        return Result.success(url);
    }
}
