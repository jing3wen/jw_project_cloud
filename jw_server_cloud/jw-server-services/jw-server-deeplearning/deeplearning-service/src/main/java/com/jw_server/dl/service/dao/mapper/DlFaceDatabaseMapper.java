package com.jw_server.dl.service.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jw_server.dl.service.dao.entity.DlFaceDatabase;
import org.apache.ibatis.annotations.Mapper;


/**
 * Description 人脸库 Mapper 接口
 * Author jingwen
 * Date 2022-09-13 17:21:26
 **/
@Mapper
public interface DlFaceDatabaseMapper extends BaseMapper<DlFaceDatabase> {

    void updateImageAddressWhenDelete(String imageAddress);
}
