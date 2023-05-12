package com.jw_server.blog.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jw_server.blog.service.dao.dto.*;
import com.jw_server.blog.service.dao.entity.BlogArticle;
import com.jw_server.blog.service.dao.entity.BlogArticleTag;
import com.jw_server.blog.service.dao.mapper.BlogArticleMapper;
import com.jw_server.blog.service.dao.mapper.BlogArticleTagMapper;
import com.jw_server.blog.service.dao.mapper.BlogCategoryMapper;
import com.jw_server.blog.service.dao.mapper.BlogCommentMapper;
import com.jw_server.blog.service.dao.vo.*;
import com.jw_server.blog.service.service.IBlogArticleService;
import com.jw_server.common.base.core.constants.ResponseCode;
import com.jw_server.common.base.core.context.MySecurityContext;
import com.jw_server.common.base.dao.LoginUserVO;
import com.jw_server.common.base.utils.IpUtils;
import com.jw_server.common.exception.ServiceException;
import com.jw_server.common.mybatis.page.MyPageVO;
import com.jw_server.common.redis.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Description 博客文章表 服务实现类
 * @author : jingwen
 * Date 2022-12-03 16:11:56
 **/
@Slf4j
@Service
public class BlogArticleServiceImpl extends ServiceImpl<BlogArticleMapper, BlogArticle> implements IBlogArticleService {


    @Resource
    private BlogArticleMapper blogArticleMapper;

    @Resource
    private BlogCategoryMapper blogCategoryMapper;

    @Resource
    private BlogCommentMapper blogCommentMapper;

    @Resource
    private BlogArticleTagMapper blogArticleTagMapper;


    @Resource
    private RedisUtils redisUtils;

    /**
     * /根据文章类别查询文章列表
     **/
    @Override
    public MyPageVO<BlogFrontArticlePageVO> getFrontArticlePage(BlogFrontQueryArticlePageDTO blogFrontQueryArticlePageDTO) {

        /*
         * 先联表blog_article, sys_user, blog_category 查询（筛选）出文章列表
         *
         * 再对每个文章进行遍历, 查询(筛选)文章标签和点赞量
         **/
        IPage<BlogFrontArticlePageVO> articlePage;
        LoginUserVO loginUserVO = MySecurityContext.getLoginUserVO();
        //登录用户，能查看自己的所有文章和其他人的公开文章
        if(ObjectUtil.isNotEmpty(loginUserVO) && loginUserVO != null){
            articlePage = blogArticleMapper.getFrontLoginUserArticlePage(
                    new Page<>(blogFrontQueryArticlePageDTO.getPageNum(), blogFrontQueryArticlePageDTO.getPageSize()),
                    loginUserVO.getId(),
                    blogFrontQueryArticlePageDTO);
        }else { //匿名访问，只能查看公开文章
            articlePage = blogArticleMapper.getFrontPublicArticlePage(
                    new Page<>(blogFrontQueryArticlePageDTO.getPageNum(), blogFrontQueryArticlePageDTO.getPageSize()),
                    blogFrontQueryArticlePageDTO);
        }


        List<BlogFrontArticlePageVO> articleVOList = articlePage.getRecords();
        if(CollectionUtil.isNotEmpty(articleVOList)){
            articleVOList.forEach(articleVO->{
                //封装文章标签
                articleVO.setTagList(blogArticleTagMapper.getArticleTagsByArticleId(articleVO.getArticleId()));
                //封装评论数量
                articleVO.setCommentCounts(blogCommentMapper.getFrontCommentCounts(articleVO.getArticleId()));
                //TODO 封装点赞量
                articleVO.setLikedCounts(0);
            });
        }

        return new MyPageVO<>(articlePage.getPages(),
                articlePage.getCurrent(),
                articlePage.getSize(),
                articlePage.getTotal(),
                articleVOList);
    }


    //TODO 后续可考虑把查询的文章放到缓存里面，修改文章时只用把redis里面的数据修改
    @Override
    @Transactional
    public BlogFrontArticleDetailsVO getFrontArticleDetails(Integer articleId, HttpServletRequest request) {

        BlogFrontArticleDetailsVO frontArticleDetailsVO;
        String viewIpAddress = IpUtils.getIpAddress(request);
        String key = "blogArticle:isViewed:articleId-"+articleId+":"+viewIpAddress;
        if(ObjectUtil.isEmpty(redisUtils.getCacheObject(key))){
            //当前ip第一次浏览文章，更新文章浏览量
            log.info("ip地址:"+viewIpAddress+", 浏览文章 articleId="+articleId);
            log.info("记录到redis中, redis过期时间8小时, 更新数据库:浏览量+1");
            blogArticleMapper.updateArticleViewCounts(articleId);
            frontArticleDetailsVO = blogArticleMapper.getFrontArticleDetails(articleId);
            //封装标签列表
            frontArticleDetailsVO.setTagList(blogArticleTagMapper.getArticleTagsByArticleId(articleId));
            //封装评论数量
            frontArticleDetailsVO.setCommentCounts(blogCommentMapper.getFrontCommentCounts(frontArticleDetailsVO.getArticleId()));
            //TODO 封装点赞量
            frontArticleDetailsVO.setLikedCounts(0);
            redisUtils.setCacheObject(key, "浏览量"+frontArticleDetailsVO.getViewCounts(),8, TimeUnit.HOURS);
        }else {
            //当前ip 重复浏览文章，不更新文章浏览量
            log.info("redis中查询到当前ip已经看过文章，不更新浏览量");
            frontArticleDetailsVO =  blogArticleMapper.getFrontArticleDetails(articleId);
            //封装标签列表
            frontArticleDetailsVO.setTagList(blogArticleTagMapper.getArticleTagsByArticleId(articleId));
            //封装评论数量
            frontArticleDetailsVO.setCommentCounts(blogCommentMapper.getFrontCommentCounts(frontArticleDetailsVO.getArticleId()));
            //TODO 封装点赞量
            frontArticleDetailsVO.setLikedCounts(0);
        }
        return frontArticleDetailsVO;
    }

    /**
     * 博客后台新增文章
     **/
    @Override
    @Transactional
    public void addBlogArticle(BlogAdminAddOrUpdateArticleDTO blogAdminAddArticleDTO) {
        log.info("新增文章——"+blogAdminAddArticleDTO.getArticleTitle());
        BlogArticle addArticle = new BlogArticle();
        BeanUtil.copyProperties(blogAdminAddArticleDTO, addArticle);
        save(addArticle);

        //开始存储文章标签关系
        log.info("开始存储文章标签关系——"+blogAdminAddArticleDTO.getArticleTitle());
        if(ObjectUtil.isEmpty(blogAdminAddArticleDTO.getTagIdList())){
            throw new ServiceException(ResponseCode.CODE_400, "请选择文章标签");
        }
        List<BlogArticleTag> addArticleTagList = new ArrayList<>();
        blogAdminAddArticleDTO.getTagIdList().forEach(tagId->{
            BlogArticleTag addArticleTag = BlogArticleTag.builder()
                    .articleId(addArticle.getArticleId())
                    .tagId(tagId)
                    .build();
            addArticleTagList.add(addArticleTag);
        });
        blogArticleTagMapper.insertBatchArticleTag(addArticleTagList);
        log.info("存储完成——"+blogAdminAddArticleDTO.getArticleTitle());
    }

    /**
     * 博客后台查询文章列表
     **/
    @Override
    public MyPageVO<BlogAdminArticlePageVO> getAdminBlogArticlePage(BlogAdminQueryArticlePageDTO queryArticleDTO) {

        IPage<BlogAdminArticlePageVO> page = blogArticleMapper.getAdminBlogArticlePage(
                new Page<>(queryArticleDTO.getPageNum(), queryArticleDTO.getPageSize()),
                queryArticleDTO);
        List<BlogAdminArticlePageVO> articleVOList = page.getRecords();
        if(CollectionUtil.isNotEmpty(articleVOList)){
            articleVOList.forEach(articleVO->{
                //封装文章标签
                articleVO.setTagList(blogArticleTagMapper.getArticleTagsByArticleId(articleVO.getArticleId()));
                //封装评论数量
                articleVO.setCommentCounts(blogCommentMapper.getFrontCommentCounts(articleVO.getArticleId()));
                //TODO 封装点赞量
                articleVO.setLikedCounts(0);
            });
        }
        return new MyPageVO<>(page);
    }

    /**
     * 后台查询需要编辑的文章信息
     **/
    @Override
    public BlogAdminUpdateArticleVO getUpdateArticle(Integer articleId) {
        BlogAdminUpdateArticleVO updateArticleVO = blogArticleMapper.getUpdateArticle(articleId);
        if (updateArticleVO.getCategoryId() != null){
            updateArticleVO.setCategoryName(
                    blogCategoryMapper.getCategoryNameById(updateArticleVO.getCategoryId()));
        }
        //封装文章标签
        updateArticleVO.setTagList(blogArticleTagMapper.getArticleTagsByArticleId(articleId));
        return updateArticleVO;
    }

    /**
     * 后台更新文章信息
     **/
    @Override
    @Transactional
    public void updateBlogArticle(BlogAdminAddOrUpdateArticleDTO updateArticleDTO) {

        log.info("更新文章——"+updateArticleDTO.getArticleTitle());
        BlogArticle updateArticle = new BlogArticle();
        BeanUtil.copyProperties(updateArticleDTO, updateArticle);
        updateById(updateArticle);

        //开始存储文章标签关系
        log.info("开始存储文章标签关系——"+updateArticleDTO.getArticleTitle());
        if(ObjectUtil.isEmpty(updateArticleDTO.getTagIdList())){
            throw new ServiceException(ResponseCode.CODE_400, "请选择文章标签");
        }
        //清空原有关系
        log.info("清空原有文章标签关系——"+updateArticleDTO.getArticleTitle());
        blogArticleTagMapper.delete(new LambdaQueryWrapper<BlogArticleTag>()
                .eq(BlogArticleTag::getArticleId, updateArticleDTO.getArticleId()));
        List<BlogArticleTag> addArticleTagList = new ArrayList<>();
        updateArticleDTO.getTagIdList().forEach(tagId->{
            BlogArticleTag addArticleTag = BlogArticleTag.builder()
                    .articleId(updateArticleDTO.getArticleId())
                    .tagId(tagId)
                    .build();
            addArticleTagList.add(addArticleTag);
        });
        blogArticleTagMapper.insertBatchArticleTag(addArticleTagList);
        log.info("存储完成——"+updateArticleDTO.getArticleTitle());
        updateById(updateArticle);
    }

    /**
     * 后台修改文章顶置状态
     **/
    @Override
    public void updateArticleTop(BlogAdminUpdateArticleTopDTO updateTopDTO) {
        update(new LambdaUpdateWrapper<BlogArticle>()
                .eq(BlogArticle::getArticleId, updateTopDTO.getArticleId())
                .set(BlogArticle::getIsTop, updateTopDTO.getIsTop()));
    }

    /**
     * 要注意同时删除文章评论和标签
     * 可以改成一次性全部删除
     **/
    @Override
    @Transactional
    public void deleteBatchArticle(List<Integer> ids) {

        ids.forEach(deleteArticleId->{
            //删除评论
            blogCommentMapper.deleteCommentByArticleId(deleteArticleId);
            //删除标签
            blogArticleTagMapper.delete(new LambdaQueryWrapper<BlogArticleTag>()
                    .eq(BlogArticleTag::getArticleId, deleteArticleId));

        });
        //删除文章
        removeBatchByIds(ids);
    }

    /**
     * 后台修改文章审核状态
     **/
    @Override
    public void updateArticleCheck(BlogAdminUpdateArticleCheckDTO updateCheckDTO) {
        update(new LambdaUpdateWrapper<BlogArticle>()
                .eq(BlogArticle::getArticleId, updateCheckDTO.getArticleId())
                .set(BlogArticle::getArticleCheck, updateCheckDTO.getArticleCheck()));
    }

    /**
     * 前台获取热门文章————浏览量最多的3篇文章
     **/
    @Override
    public List<BlogFrontHotArticleVO> getHotArticle(Integer pageNum, Integer pageSize) {

        return blogArticleMapper.getHotArticle(pageNum, pageSize);
    }

    /**
     * 前台获取文章归档
     **/
    @Override
    public MyPageVO<BlogFrontArticleArchiveVO> getArticleArchive(Integer pageNum, Integer pageSize) {
        IPage<BlogFrontArticleArchiveVO> archiveVOIPage;
        LoginUserVO loginUserVO = MySecurityContext.getLoginUserVO();
        //匿名用户，只能查看公开文章
        //登录用户，能查看自己的所有文章和其他人的公开文章
        if(ObjectUtil.isNotEmpty(loginUserVO) && loginUserVO != null){
            archiveVOIPage = blogArticleMapper.getArticleArchivePage(new Page<>(pageNum,pageSize),
                    loginUserVO.getId());

        }else { //匿名用户，只能查看公开文章
            archiveVOIPage = blogArticleMapper.getArticleArchivePage(new Page<>(pageNum,pageSize),
                    null);
        }
        return new MyPageVO<>(archiveVOIPage);
    }

}
