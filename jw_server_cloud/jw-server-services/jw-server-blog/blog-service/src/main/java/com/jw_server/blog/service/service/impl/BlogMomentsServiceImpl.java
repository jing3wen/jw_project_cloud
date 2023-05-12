package com.jw_server.blog.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jw_server.blog.service.dao.dto.BlogFrontMomentsPageDTO;
import com.jw_server.blog.service.dao.entity.BlogMoments;
import com.jw_server.blog.service.dao.mapper.BlogMomentsMapper;
import com.jw_server.blog.service.dao.vo.BlogAdminMomentsVO;
import com.jw_server.blog.service.dao.vo.BlogFrontMomentsPageVO;
import com.jw_server.blog.service.service.IBlogMomentsService;
import com.jw_server.common.base.core.context.MySecurityContext;
import com.jw_server.common.base.dao.LoginUserVO;
import com.jw_server.common.mybatis.page.MyPageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description 朋友圈表 服务实现类
 * Author jingwen
 * Date 2023-02-11 20:48:10
 **/
@Service
public class BlogMomentsServiceImpl extends ServiceImpl<BlogMomentsMapper, BlogMoments> implements IBlogMomentsService {

    @Resource
    private BlogMomentsMapper blogMomentsMapper;

    /**
     * 前台查询朋友圈分页
     **/
    @Override
    public MyPageVO<BlogFrontMomentsPageVO> getFrontMomentsPage(BlogFrontMomentsPageDTO frontMomentsPageDTO) {
        IPage<BlogFrontMomentsPageVO> page;

        LoginUserVO loginUserVO = MySecurityContext.getLoginUserVO();
        //当前用户为登录用户，可以查询当前用户的所有朋友圈和其他用户的公开朋友圈
        if(ObjectUtil.isNotEmpty(loginUserVO)) {
            page = blogMomentsMapper.getFrontLoginUserMomentsPage(
                    new Page<>(frontMomentsPageDTO.getPageNum(), frontMomentsPageDTO.getPageSize()),
                    loginUserVO.getId(),
                    frontMomentsPageDTO.getViewMe());
        }else {
            //当前用户为匿名访问，只查询当前所有用户的公开朋友圈
            page= blogMomentsMapper.getFrontAllPublicMomentsPage(
                    new Page<>(frontMomentsPageDTO.getPageNum(), frontMomentsPageDTO.getPageSize()));
        }
        return  new MyPageVO<>(page);
    }


    /**
     * 后台查询朋友圈分页
     **/
    @Override
    public MyPageVO<BlogAdminMomentsVO> getAdminMomentsPage(Integer pageNum, Integer pageSize, String nickname) {

        return new MyPageVO<>(
                blogMomentsMapper.getAdminMomentsPage(new Page<BlogAdminMomentsVO>(pageNum, pageSize),
                        nickname));
    }
}
