package com.jw_server.blog.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jw_server.blog.service.dao.entity.BlogArticleTag;
import com.jw_server.blog.service.dao.entity.BlogTag;
import com.jw_server.blog.service.dao.mapper.BlogArticleTagMapper;
import com.jw_server.blog.service.dao.mapper.BlogTagMapper;
import com.jw_server.blog.service.dao.vo.BlogAdminTagPageVO;
import com.jw_server.blog.service.dao.vo.BlogFrontTagVO;
import com.jw_server.blog.service.service.IBlogTagService;
import com.jw_server.common.base.core.constants.ResponseCode;
import com.jw_server.common.exception.ServiceException;
import com.jw_server.common.mybatis.page.MyPageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description 博客标签表 服务实现类
 * Author jingwen
 * Date 2023-02-04 15:20:40
 **/
@Service
public class BlogTagServiceImpl extends ServiceImpl<BlogTagMapper, BlogTag> implements IBlogTagService {

    @Resource
    private BlogTagMapper blogTagMapper;

    @Resource
    private BlogArticleTagMapper blogArticleTagMapper;

    /**
     * 前台查询所有标签
     **/
    @Override
    public List<BlogFrontTagVO> getAllFrontTag() {
        return blogTagMapper.getAllFrontTag();
    }

    /**
     * 新增文章标签, 要检查同名
     **/
    @Override
    public void addOrUpdateBlogTag(BlogTag blogTag) {
        //查询是否有同名类别
        BlogTag findSameName = blogTagMapper.selectOne(new LambdaQueryWrapper<BlogTag>()
                .select(BlogTag::getTagId)
                .eq(BlogTag::getTagName,blogTag.getTagName()));
        if(ObjectUtil.isNotEmpty(findSameName) && !(findSameName.getTagId().equals(blogTag.getTagId()))){
            throw new ServiceException(ResponseCode.CODE_400,"该名称已存在，请更换名称");
        }
        saveOrUpdate(blogTag);
    }

    /**
     * 后台批量删除博客标签
     **/
    @Override
    public void deleteBlogTagBatch(List<Integer> ids) {
        Long count = blogArticleTagMapper.selectCount(new LambdaQueryWrapper<BlogArticleTag>()
                .in(BlogArticleTag::getTagId, ids));
        if (count > 0){
            throw new ServiceException(ResponseCode.CODE_400, "该标签下还有文章，请先删除文章");
        }
        removeByIds(ids);
    }

    /**
     * 博客后台分页查询文章标签
     **/
    @Override
    public MyPageVO<BlogAdminTagPageVO> getAdminTagPage(Integer pageNum, Integer pageSize, String tagName) {

        return new MyPageVO<>(blogTagMapper.getAdminTagPage(new Page<BlogAdminTagPageVO>(pageNum,pageSize), tagName));
    }


    /**
     * 后台根据文章类别名搜索类别列表/直接搜索所有标签
     **/
    @Override
    public List<BlogFrontTagVO> getTagListByTageNameOrNot(String tagName) {

        return blogTagMapper.getAllTagByTagNameOrNot(tagName);
    }


}
