package com.jw_server.dl.service.controller;

import com.jw_server.common.base.result.ResponseResult;
import com.jw_server.common.log.annotation.SysLog;
import com.jw_server.common.log.constants.LogModuleConst;
import com.jw_server.common.log.constants.LogTypeConst;
import com.jw_server.common.mybatis.page.MyPageVO;
import com.jw_server.dl.service.core.fileUpload.FileUploadUtils;
import com.jw_server.dl.service.dao.dto.QueryDlFaceDetectFileDTO;
import com.jw_server.dl.service.dao.entity.DlFaceDetectFile;
import com.jw_server.dl.service.service.IDlFaceDetectFileService;
import com.jw_server.dl.service.service.impl.DetectFileTaskManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;



/**
 * author jingwen
 * Description 检测人脸文件表 前端控制器
 * Date 2022-09-22 17:31:09
 */
@Slf4j
@RestController
@RequestMapping("/deeplearning//dlFaceDetectFile")
public class DlFaceDetectFileController {

    @Resource
    private IDlFaceDetectFileService dlFaceDetectFileService;

    @Resource
    private FileUploadUtils fileUploadUtils;

    @Resource
    private DetectFileTaskManager detectFileTaskManager;


    /**
     * Description 新增
     * Author jingwen
     * Date 2022-09-22 17:31:09
     **/
    @SysLog(logModule= LogModuleConst.DlFaceDetectFileModule, logType = LogTypeConst.ADD, logDesc = "新增")
    @PreAuthorize("hasAuthority('dl:dlFaceDetectFile:addDetectFile')")
    @PostMapping("/add")
    public ResponseResult<Object> add(@RequestBody DlFaceDetectFile dlFaceDetectFile) {
        dlFaceDetectFileService.addDetectFile(dlFaceDetectFile);
        return ResponseResult.success();
    }

    /**
     * Description 更新
     * Author jingwen
     * Date 2022-09-22 17:31:09
     **/
    @SysLog(logModule= LogModuleConst.DlFaceDetectFileModule, logType = LogTypeConst.UPDATE, logDesc = "更新")
    @PreAuthorize("hasAuthority('dl:dlFaceDetectFile:update')")
    @PostMapping("/update")
    public ResponseResult<Object> update(@RequestBody DlFaceDetectFile dlFaceDetectFile) {
        dlFaceDetectFileService.updateById(dlFaceDetectFile);
        return ResponseResult.success();
    }

    /**
     * Description 批量删除
     * Author jingwen
     * Date 2022-09-22 17:31:09
     **/
    @SysLog(logModule= LogModuleConst.DlFaceDetectFileModule, logType = LogTypeConst.DELETE, logDesc = "删除")
    @PreAuthorize("hasAuthority('dl:dlFaceDetectFile:delete')")
    @PostMapping("/deleteBatch")
    public ResponseResult<Object> deleteBatch(@RequestBody List<Integer> ids) {
        dlFaceDetectFileService.deleteFaceDetectFile(ids);
        return ResponseResult.success();
    }


    /**
     * Description 分页查询
     * Author jingwen
     * Date 2022-09-22 17:31:09
     **/
    @PreAuthorize("hasAuthority('dl:dlFaceDetectFile:query')")
    @PostMapping("/getPageList")
    public ResponseResult<MyPageVO<DlFaceDetectFile>> getPageList(@RequestBody QueryDlFaceDetectFileDTO dlFaceDetectFileDTO) {
//        System.out.println(dlFaceDetectFileDTO);
        return ResponseResult.success(dlFaceDetectFileService.getDetectFilePageList(dlFaceDetectFileDTO));
    }

    /**
     * Description: 上传检测图片
     * Author: jingwen
     * Date: 2022/9/13 17:23
     **/
    @SysLog(logModule= LogModuleConst.DlFaceDetectFileModule, logType = LogTypeConst.UPLOAD, logDesc = "上传检测图片", saveRequestParam = false)
    @PreAuthorize("hasAuthority('dl:dlFaceDetectFile:uploadDetectFile')")
    @PostMapping("/uploadFaceDetectFile")
    public ResponseResult<String> uploadFaceDetectFile(@RequestParam MultipartFile file){
        log.info(LogModuleConst.DlFaceDetectFileModule+"--上传检测文件");
        return ResponseResult.success(dlFaceDetectFileService.uploadDetectFile(file));
    }


    /**
     * Description: 删除上传的检测文件
     * Author: jingwen
     * Date: 2022/9/22 19:13
     **/
    @SysLog(logModule = LogModuleConst.DlFaceDetectFileModule, logType = LogTypeConst.DELETE, logDesc = "删除上传的检测文件")
    @PreAuthorize("hasAuthority('dl:dlFaceDetectFile:deleteUploadDetectFile')")
    @DeleteMapping("/deleteFaceDetectFile")
    public ResponseResult<Object> deleteFaceDetectFile(@RequestBody String fileUrl){
        log.info(LogModuleConst.DlFaceDetectFileModule+"--删除上传的检测文件:"+fileUrl);
        fileUploadUtils.fileDelete(fileUrl);
        return ResponseResult.success();
    }


    /**
     * Description: 异步检测文件——线程池监控版本
     * Author: jingwen
     * Date: 2023/3/9 20:16
     **/
    @SysLog(logModule = LogModuleConst.DlFaceDetectFileModule, logType = LogTypeConst.UPDATE, logDesc = "检测文件")
    @PreAuthorize("hasAuthority('dl:dlFaceDetectFile:detecting')")
    @PostMapping("/detectFaceFile")
    public ResponseResult<Object> submitDetectTask(@RequestBody DlFaceDetectFile detectFile){
        detectFileTaskManager.submitDetectTask(detectFile);
        return ResponseResult.success();
    }

}

