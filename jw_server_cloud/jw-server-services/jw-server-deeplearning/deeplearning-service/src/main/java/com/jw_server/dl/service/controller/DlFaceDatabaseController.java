package com.jw_server.dl.service.controller;

import com.jw_server.common.base.core.constants.ResponseCode;
import com.jw_server.common.base.result.ResponseResult;
import com.jw_server.common.exception.ServiceException;
import com.jw_server.common.log.annotation.SysLog;
import com.jw_server.common.log.constants.LogModuleConst;
import com.jw_server.common.log.constants.LogTypeConst;
import com.jw_server.common.mybatis.page.MyPageVO;
import com.jw_server.dl.service.core.fileUpload.FileUploadUtils;
import com.jw_server.dl.service.core.utils.socketToPython.DetectFileUtils;
import com.jw_server.dl.service.dao.dto.QueryDlFaceDataDTO;
import com.jw_server.dl.service.dao.entity.DlFaceDatabase;
import com.jw_server.dl.service.service.IDlFaceDatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;


/**
 * author jingwen
 * Description 人脸库 前端控制器
 * Date 2022-09-13 17:21:26
 */
@Slf4j
@RestController
@RequestMapping("/deeplearning//dlFaceDatabase")
public class DlFaceDatabaseController {

    @Resource
    private IDlFaceDatabaseService dlFaceDatabaseService;

    @Resource
    private FileUploadUtils fileUploadUtils;

    @Resource
    private DetectFileUtils detectFileUtils;


    /**
     * Description 新增
     * Author jingwen
     * Date 2022-09-13 17:21:26
     **/
    @SysLog(logModule=LogModuleConst.DlFaceDatabaseModule, logType = LogTypeConst.ADD, logDesc = "新增人脸库")
    @PreAuthorize("hasAuthority('dl:dlFaceDatabase:addFace')")
    @PostMapping("/add")
    public ResponseResult<Object> add(@RequestBody DlFaceDatabase dlFaceDatabase) {
        dlFaceDatabaseService.addFaceDatabase(dlFaceDatabase);
        return ResponseResult.success();
    }

    /**
     * Description 更新
     * Author jingwen
     * Date 2022-09-13 17:21:26
     **/
    @SysLog(logModule=LogModuleConst.DlFaceDatabaseModule, logType = LogTypeConst.UPDATE, logDesc = "更新人脸库")
    @PreAuthorize("hasAuthority('dl:dlFaceDatabase:update')")
    @PostMapping("/update")
    public ResponseResult<Object> update(@RequestBody DlFaceDatabase dlFaceDatabase) {
        dlFaceDatabaseService.updateFace(dlFaceDatabase);
        return ResponseResult.success();
    }

    /**
     * Description 批量删除
     * Author jingwen
     * Date 2022-09-13 17:21:26
     **/
    @SysLog(logModule=LogModuleConst.DlFaceDatabaseModule, logType = LogTypeConst.DELETE, logDesc = "删除人脸库")
    @PreAuthorize("hasAuthority('dl:dlFaceDatabase:delete')")
    @PostMapping("/deleteBatch")
    public ResponseResult<Object> deleteBatch(@RequestBody List<Integer> ids) {
        dlFaceDatabaseService.deleteFaceData(ids);
        return ResponseResult.success();
    }

    /**
     * Description 分页查询
     * Author jingwen
     * Date 2022-09-13 17:21:26
     **/
    @PreAuthorize("hasAuthority('dl:dlFaceDatabase:query')")
    @PostMapping("/getPageList")
    public ResponseResult<MyPageVO<DlFaceDatabase>> getPageList(@RequestBody QueryDlFaceDataDTO queryDlFaceDataDTO) {
        return ResponseResult.success(dlFaceDatabaseService.getFacePageList(queryDlFaceDataDTO));
    }


    /**
     * Description: 上传人脸图片
     * Author: jingwen
     * Date: 2022/9/13 17:23
     **/
    @SysLog(logModule=LogModuleConst.DlFaceDatabaseModule, logType = LogTypeConst.UPLOAD, logDesc = "上传人脸图片", saveRequestParam = false)
    @PreAuthorize("hasAuthority('dl:dlFaceDatabase:uploadFaceImage')")
    @PostMapping("/uploadFaceDatabase")
    public ResponseResult<Object> uploadFaceImage(@RequestParam MultipartFile file){
        log.info(LogModuleConst.DlFaceDatabaseModule+"--上传人脸图片");
        return ResponseResult.success(dlFaceDatabaseService.uploadFaceImage(file));
    }

    /**
     * Description: 删除上传的人脸图片
     * 检测要删除的人脸是不是写入了数据库，若写入了数据库需要更新数据库中的数据
     * Author: jingwen
     * Date: 2022/9/14 11:48
     **/
    @SysLog(logModule=LogModuleConst.DlFaceDatabaseModule, logType = LogTypeConst.DELETE, logDesc = "删除上传的人脸图片")
    @PreAuthorize("hasAuthority('dl:dlFaceDatabase:deleteUploadFaceImage')")
    @DeleteMapping("/deleteFaceImage")
    public ResponseResult<Object> deleteFaceImage(@RequestBody String fileUrl){
        log.info(LogModuleConst.DlFaceDatabaseModule+"--删除上传的人脸图片:"+fileUrl);
        dlFaceDatabaseService.updateImageAddressWhenDeleteFile(fileUrl);
        fileUploadUtils.fileDelete(fileUrl);
        return ResponseResult.success();
    }

    /**
     * Description: 手动更新人脸库
     * Author: jingwen
     * Date: 2022/9/22 22:10
     **/
    @SysLog(logModule= LogModuleConst.DlFaceDatabaseModule, logType = LogTypeConst.UPDATE, logDesc = "手动更新检测服务器人脸库")
    @PreAuthorize("hasAuthority('dl:dlFaceDatabase:updateFaceDatabase')")
    @GetMapping("/updateFaceDatabase")
    public ResponseResult<Object> updateFaceDatabase(){
        try{
            detectFileUtils.remoteCall_update_face_dataset();
        }catch(Exception e){
            throw new ServiceException(ResponseCode.CODE_400, "调用检测服务器异常");
        }
        return ResponseResult.success();
    }
}

