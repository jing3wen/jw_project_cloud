package com.jw_server.blog.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jw_server.blog.service.dao.dto.BlogAdminQueryCommentPageDTO;
import com.jw_server.blog.service.dao.dto.BlogAdminUpdateCheckBatchDTO;
import com.jw_server.blog.service.dao.dto.BlogFrontAddCommentDTO;
import com.jw_server.blog.service.dao.dto.BlogFrontCommentPageDTO;
import com.jw_server.blog.service.dao.entity.BlogComment;
import com.jw_server.blog.service.dao.mapper.BlogArticleMapper;
import com.jw_server.blog.service.dao.mapper.BlogCommentMapper;
import com.jw_server.blog.service.dao.vo.BlogAdminCommentPageVO;
import com.jw_server.blog.service.dao.vo.BlogFrontCommentVO;
import com.jw_server.blog.service.service.IBlogCommentService;
import com.jw_server.common.base.core.constants.ResponseCode;
import com.jw_server.common.exception.ServiceException;
import com.jw_server.common.mybatis.page.MyPageVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description 博客评论表 服务实现类
 * Author jingwen
 * Date 2022-12-03 17:17:39
 **/
@Service
public class BlogCommentServiceImpl extends ServiceImpl<BlogCommentMapper, BlogComment> implements IBlogCommentService {

    @Resource
    private BlogCommentMapper blogCommentMapper;
    @Resource
    private BlogArticleMapper blogArticleMapper;

    Log logger = LogFactory.getLog(BlogCommentServiceImpl.class);


    /**
     * floorCommentId = 0 表示查询一级评论分页
     *  首先查询文章的第一级评论，(floorCommentId=0的评论)
     *  再分别查询该一级评论下的二级评论分页
     *
     * floorCommentId != 0 表示查询二级评论分页
     **/
    @Override
    public MyPageVO<BlogFrontCommentVO> getFrontCommentByArticleId(BlogFrontCommentPageDTO frontCommentPageDTO) {

        IPage<BlogFrontCommentVO> commentIPage = blogCommentMapper.getFrontCommentPageVO(
                frontCommentPageDTO.getCommentType(),
                frontCommentPageDTO.getArticleId(),
                frontCommentPageDTO.getFloorCommentId(),
                new Page<>(frontCommentPageDTO.getPageNum(),frontCommentPageDTO.getPageSize()));

        //前台查询二级评论, 直接返回
        if (frontCommentPageDTO.getFloorCommentId()!=0){
            return new MyPageVO<>(commentIPage);
        }else{
            //前台查询一级评论,需要封装一级评论下的二级评论, 其实这个else可以省去,但是为了方便后续查看我还是写上了
            List<BlogFrontCommentVO> parentList= commentIPage.getRecords();
            /*
             * 开始查询二级评论
             **/
            parentList.forEach(parent -> {
                IPage<BlogFrontCommentVO> childPage = blogCommentMapper.getFrontCommentPageVO(
                        frontCommentPageDTO.getCommentType(),
                        frontCommentPageDTO.getArticleId(),
                        parent.getCommentId(),
                        new Page<>(0, 5));
                parent.setReplyCommentCounts(childPage.getTotal());
                parent.setReplyCommentList(childPage.getRecords());
            });

            return new MyPageVO<>(commentIPage.getPages(),
                    commentIPage.getCurrent(),
                    commentIPage.getSize(),
                    commentIPage.getTotal(),
                    parentList);
        }
    }

    /**
     * 获取文章评论总数量
     **/
    @Override
    public Integer getFrontCommentCounts(Integer articleId) {

        return blogCommentMapper.getFrontCommentCounts(articleId);
    }

    /**
     * 新增评论
     **/
    @Override
    public void addComment(BlogFrontAddCommentDTO frontAddCommentDTO) {
        //新增一条评论
        BlogComment addComment = new BlogComment();
        BeanUtil.copyProperties(frontAddCommentDTO, addComment);
        save(addComment);
    }


    /**
     * 后台审核博客文章评论
     **/
    @Override
    public void updateCommentCheckBatch(BlogAdminUpdateCheckBatchDTO updateCheckBatchDTO) {

        if(StrUtil.isEmpty(updateCheckBatchDTO.getCheckStatus())){
            throw new ServiceException(ResponseCode.CODE_400, "更新的状态参数有误");
        }
        if(ObjectUtil.isEmpty(updateCheckBatchDTO.getIds()) || updateCheckBatchDTO.getIds().size()==0){
            throw new ServiceException(ResponseCode.CODE_400, "请选择要更新的数据");
        }
        update(new LambdaUpdateWrapper<BlogComment>()
                .set(BlogComment::getCommentCheck, updateCheckBatchDTO.getCheckStatus())
                .in(BlogComment::getCommentId, updateCheckBatchDTO.getIds()));
    }

    /**
     * 后台批量删除博客文章评论, 子评论也会被删除
     **/
    @Override
    public void deleteBatchComment(List<Integer> ids) {
        ids.forEach(deleteId->{
            blogCommentMapper.deleteComment(deleteId);
        });
    }

    /**
     * 后台查询评论分页
     **/
    @Override
    public MyPageVO<BlogAdminCommentPageVO> getAdminCommentPageList(BlogAdminQueryCommentPageDTO queryCommentPageDTO) {

        IPage<BlogAdminCommentPageVO> page = blogCommentMapper.getAdminCommentPageList(
                new Page<>(queryCommentPageDTO.getPageNum(),queryCommentPageDTO.getPageSize()),
                queryCommentPageDTO);
        return new MyPageVO<>(page);
    }
}
