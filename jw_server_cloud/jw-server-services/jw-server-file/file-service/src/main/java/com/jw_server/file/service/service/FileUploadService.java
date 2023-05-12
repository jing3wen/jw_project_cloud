package com.jw_server.file.service.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Description: 文件上传服务
 * @author : jingwen
 * Date: 2023/4/30 21:38
 **/
public interface FileUploadService {

    /**
     * 解析请求路径，构建文件夹
     **/
    String fileUpload(MultipartFile file, String requestUrl);

    /**
     * 删除上传文件
     **/
    void deleteUploadFile(String fileUrl);
}
