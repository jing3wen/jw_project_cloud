package com.jw_server.dl.service.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jw_server.dl.service.dao.entity.DlFaceDetectFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * Description 检测人脸文件表 Mapper 接口
 * Author jingwen
 * Date 2022-09-22 17:31:09
 **/
@Mapper
public interface DlFaceDetectFileMapper extends BaseMapper<DlFaceDetectFile> {

    /**
     * Description: 获取检测文件状态
     * Author: jingwen
     * Date: 2022/9/22 23:02
     **/
    Integer getFileDetectStatus(Integer id);

    /**
     * Description: 更新文件检测状态
     * Author: jingwen
     * Date: 2022/9/22 23:19
     **/
    void updateFileDetectStatus(@Param("fileId") Integer fileId,
                                @Param("detectStatus") Integer detectStatus);
}
