package com.jw_server.blog.service.controller;

import com.jw_server.blog.service.dao.dto.*;
import com.jw_server.blog.service.dao.vo.*;
import com.jw_server.blog.service.service.IBlogArticleService;
import com.jw_server.common.base.result.ResponseResult;
import com.jw_server.common.log.annotation.SysLog;
import com.jw_server.common.log.constants.LogModuleConst;
import com.jw_server.common.log.constants.LogTypeConst;
import com.jw_server.common.mybatis.page.MyPageVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;



/**
 * @author : jingwen
 * Description 博客文章表 前端控制器
 * Date 2022-12-03 16:11:56
 */
@RestController
@RequestMapping("/blog/blogArticle")
public class BlogArticleController {

    @Resource
    private IBlogArticleService blogArticleService;


    /**
     * Description 前台 分页查询所有文章列表/根据文章类别查询文章列表/根据文章标签id查询文章列表
     * Author jingwen
     * Date 2022-12-03 16:11:56
     **/
    @PostMapping("/front/getFrontArticlePage")
    public ResponseResult<MyPageVO<BlogFrontArticlePageVO>> getFrontArticlePage(@RequestBody BlogFrontQueryArticlePageDTO blogFrontQueryArticlePageDTO) {

        return ResponseResult.success(blogArticleService.getFrontArticlePage(blogFrontQueryArticlePageDTO));
    }

    /**
     * Description 浏览博客文章详细信息，同时设置浏览量+1
     * Author jingwen
     * Date 2022-12-03 16:11:56
     **/
    @GetMapping("/front/getFrontArticleDetails")
    public ResponseResult<BlogFrontArticleDetailsVO> getFrontArticleDetails(@RequestParam Integer articleId, HttpServletRequest request) {

        return ResponseResult.success(blogArticleService.getFrontArticleDetails(articleId,request));
    }

    /**
     * Description: 前台获取热门文章————浏览量最多的3篇文章
     * Author: jingwen
     * Date: 2023/1/28 13:54
     **/
    @GetMapping("/front/getHotArticle")
    public ResponseResult<List<BlogFrontHotArticleVO>> getHotArticle() {

        return ResponseResult.success(blogArticleService.getHotArticle(0, 3));
    }

    /**
     * Description: 前台获取文章归档
     * 匿名访问：只查询公开文章
     * 登录用户访问：查询当前用户的所有文章和其他用户的公开文章
     *
     * Author: jingwen
     * Date: 2023/2/18 21:28
     **/
    @GetMapping("/front/getArticleArchive")
    public ResponseResult<MyPageVO<BlogFrontArticleArchiveVO>> getArticleArchive(@RequestParam Integer pageNum, @RequestParam Integer pageSize){

        return ResponseResult.success(blogArticleService.getArticleArchive(pageNum, pageSize));
    }


    /**
     * Description: 博客后台新增文章
     * Author: jingwen
     * Date: 2023/1/9 21:18
     **/
    @SysLog(logModule= LogModuleConst.BlogArticleModule, logType = LogTypeConst.ADD, logDesc = "博客后台新增文章")
    @PreAuthorize("hasAuthority('blog:blogArticle:publish')")
    @PostMapping("/admin/addBlogArticle")
    public ResponseResult<Object> addBlogArticle(@RequestBody BlogAdminAddOrUpdateArticleDTO blogAdminAddArticleDTO) {
        blogArticleService.addBlogArticle(blogAdminAddArticleDTO);
        return ResponseResult.success();
    }


    /**
     * Description: 博客后台查询文章列表
     * Author: jingwen
     * Date: 2023/1/9 21:18
     **/
    @PreAuthorize("hasAuthority('blog:blogArticle:queryArticle')")
    @PostMapping("/admin/getAdminBlogArticlePage")
    public ResponseResult<MyPageVO<BlogAdminArticlePageVO>> getAdminBlogArticlePage(@RequestBody BlogAdminQueryArticlePageDTO blogAdminQueryArticlePageDTO) {

        return ResponseResult.success(blogArticleService.getAdminBlogArticlePage(blogAdminQueryArticlePageDTO));
    }


    /**
     * Description: 批量删除文章，同时删除该文章评论和标签表关系
     * Author: jingwen
     * Date: 2023/1/11 16:20
     **/
    @SysLog(logModule= LogModuleConst.BlogArticleModule, logType = LogTypeConst.DELETE, logDesc = "批量删除文章,同时删除该文章评论和标签表关系")
    @PreAuthorize("hasAuthority('blog:blogArticle:deleteArticle')")
    @DeleteMapping("/admin/deleteBatch")
    public ResponseResult<Object> deleteBatch(@RequestBody List<Integer> ids) {
        blogArticleService.deleteBatchArticle(ids);
        return ResponseResult.success();
    }

    /**
     * Description: 后台查询需要编辑的文章信息
     * Author: jingwen
     * Date: 2023/1/12 12:18
     **/
    @GetMapping("/admin/getUpdateArticle")
    public ResponseResult<BlogAdminUpdateArticleVO> getUpdateArticle(@RequestParam Integer articleId) {

        return ResponseResult.success(blogArticleService.getUpdateArticle(articleId));
    }

    /**
     * Description: 后台更新文章信息
     * Author: jingwen
     * Date: 2023/1/12 12:18
     **/
    @SysLog(logModule= LogModuleConst.BlogArticleModule, logType = LogTypeConst.UPDATE, logDesc = "后台更新文章信息")
    @PreAuthorize("hasAuthority('blog:blogArticle:updateArticle')")
    @PostMapping("/admin/updateArticle")
    public ResponseResult<Object> updateArticle(@RequestBody BlogAdminAddOrUpdateArticleDTO updateArticleDTO) {
        blogArticleService.updateBlogArticle(updateArticleDTO);
        return ResponseResult.success();
    }


    /**
     * Description: 后台修改文章顶置状态
     * Author: jingwen
     * Date: 2023/1/26 12:48
     **/
    @SysLog(logModule= LogModuleConst.BlogArticleModule, logType = LogTypeConst.UPDATE, logDesc = "后台修改文章顶置状态")
    @PreAuthorize("hasAuthority('blog:blogArticle:editArticleTop')")
    @PostMapping("/admin/updateArticleTop")
    public ResponseResult<Object> updateArticleTop(@RequestBody BlogAdminUpdateArticleTopDTO updateTopDTO) {
        blogArticleService.updateArticleTop(updateTopDTO);
        return ResponseResult.success();
    }

    /**
     * Description: 后台修改文章审核状态
     * Author: jingwen
     * Date: 2023/1/26 12:48
     **/
    @SysLog(logModule= LogModuleConst.BlogArticleModule, logType = LogTypeConst.UPDATE, logDesc = "后台审核文章")
    @PreAuthorize("hasAuthority('blog:blogArticle:checkArticle')")
    @PostMapping("/admin/updateArticleCheck")
    public ResponseResult<Object> updateArticleCheck(@RequestBody BlogAdminUpdateArticleCheckDTO updateCheckDTO) {
        blogArticleService.updateArticleCheck(updateCheckDTO);
        return ResponseResult.success();
    }
}

