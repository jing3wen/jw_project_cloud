package com.jw_server.file.service.service.impl;

import com.jw_server.common.base.core.constants.ResponseCode;
import com.jw_server.common.exception.ServiceException;
import com.jw_server.file.service.core.fileUpload.FileUploadUtils;
import com.jw_server.file.service.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 *
 * Description:
 * Author: jingwen
 * Date: 2023/1/9 21:58
 **/
@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Resource
    private FileUploadUtils fileUploadUtils;

    /**
     * 解析请求路径，构建文件夹
     **/
    @Override
    public String fileUpload(MultipartFile file, String requestUrl) {
        String filePath = "";
        if(requestUrl.contains("/file/fileUpload/")){
            filePath = requestUrl.replace("/file/fileUpload/","");
        }else{
            throw new ServiceException(ResponseCode.CODE_400, "上传文件url有误");
        }
        if (!filePath.endsWith("/")){
            filePath = filePath+"/";
        }
        return fileUploadUtils.fileUpload(file,filePath);
    }

    /**
     * 删除上传文件
     **/
    @Override
    public void deleteUploadFile(String fileUrl) {
        fileUploadUtils.fileDelete(fileUrl);
    }
}
