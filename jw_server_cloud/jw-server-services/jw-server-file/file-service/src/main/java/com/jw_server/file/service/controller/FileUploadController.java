package com.jw_server.file.service.controller;

import com.jw_server.common.base.result.ResponseResult;
import com.jw_server.common.log.annotation.SysLog;
import com.jw_server.common.log.constants.LogModuleConst;
import com.jw_server.common.log.constants.LogTypeConst;
import com.jw_server.file.service.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;



/**
 * Description: 文件上传controller类
 * @author : jingwen
 * DATE: 2023/1/9 21:51
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Resource
    private FileUploadService fileUploadService;

    /**
     * Description: 文件上传
     * @author : jingwen
     * Date: 2023/1/9 21:54
     **/
    @SysLog(logModule= LogModuleConst.FileUploadModule, logType = LogTypeConst.UPLOAD, logDesc = "上传文件", saveRequestParam = false)
    @PreAuthorize("hasAuthority('file:uploadFile')")
    @PostMapping("/fileUpload/**")
    public ResponseResult<String> fileUpload(HttpServletRequest request, @RequestParam MultipartFile file){
        String requestUrl = request.getRequestURI();
        log.info("--上传文件--请求url为: "+requestUrl);
        String fileUrl= fileUploadService.fileUpload(file, requestUrl);
        return ResponseResult.success(fileUrl);
    }


    /**
     * Description: 删除上传文件
     * @author : jingwen
     * Date: 2023/1/13 18:27
     **/
    @SysLog(logModule= LogModuleConst.FileUploadModule, logType = LogTypeConst.DELETE, logDesc = "删除上传文件")
    @PreAuthorize("hasAuthority('file:deleteUploadFile')")
    @DeleteMapping("/deleteUploadFile/**")
    public ResponseResult<String> deleteUploadFile(@RequestBody String fileUrl){
        log.info("--删除上传文件--文件存储地址: "+fileUrl);
        fileUploadService.deleteUploadFile(fileUrl);
        return ResponseResult.success();
    }






}
