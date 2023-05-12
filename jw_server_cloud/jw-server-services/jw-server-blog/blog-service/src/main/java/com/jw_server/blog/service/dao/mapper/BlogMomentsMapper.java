package com.jw_server.blog.service.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jw_server.blog.service.dao.entity.BlogMoments;
import com.jw_server.blog.service.dao.vo.BlogAdminMomentsVO;
import com.jw_server.blog.service.dao.vo.BlogFrontMomentsPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * Description 朋友圈表 Mapper 接口
 * Author jingwen
 * Date 2023-02-11 20:48:10
 **/
@Mapper
public interface BlogMomentsMapper extends BaseMapper<BlogMoments> {

    /**
     * Description: 前台查询朋友圈分页
     * Author: jingwen
     * Date: 2023/2/11 21:20
     **/
    IPage<BlogFrontMomentsPageVO> getFrontAllPublicMomentsPage(Page<BlogFrontMomentsPageVO> objectPage);

    /**
     * Description: 前台登录用户查询所有朋友圈分页
     * 或 前台登录用户查询自己朋友圈分页
     * Author: jingwen
     * Date: 2023/2/11 21:20
     **/
    IPage<BlogFrontMomentsPageVO> getFrontLoginUserMomentsPage(Page<BlogFrontMomentsPageVO> objectPage,
                                                               @Param("userId") Integer userId,
                                                               @Param("viewMe") Boolean viewMe);

    /**
     * Description: 后台查询朋友圈分页
     * Author: jingwen
     * Date: 2023/2/28 17:07
     **/
    IPage<BlogAdminMomentsVO> getAdminMomentsPage(Page<BlogAdminMomentsVO> blogAdminMomentsVOPage,
                                                  @Param("nickname") String nickname);
}
