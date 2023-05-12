package com.jw_server.blog.service.controller;

import com.jw_server.blog.service.dao.dto.BlogAdminQueryCommentPageDTO;
import com.jw_server.blog.service.dao.dto.BlogAdminUpdateCheckBatchDTO;
import com.jw_server.blog.service.dao.dto.BlogFrontAddCommentDTO;
import com.jw_server.blog.service.dao.dto.BlogFrontCommentPageDTO;
import com.jw_server.blog.service.dao.vo.BlogAdminCommentPageVO;
import com.jw_server.blog.service.dao.vo.BlogFrontCommentVO;
import com.jw_server.blog.service.service.IBlogCommentService;
import com.jw_server.common.base.result.ResponseResult;
import com.jw_server.common.log.annotation.SysLog;
import com.jw_server.common.log.constants.LogModuleConst;
import com.jw_server.common.log.constants.LogTypeConst;
import com.jw_server.common.mybatis.page.MyPageVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;




/**
 * author jingwen
 * Description 博客评论表 前端控制器
 * Date 2022-12-03 17:17:39
 */
@RestController
@RequestMapping("/blog/blogComment")
public class BlogCommentController {

    @Resource
    private IBlogCommentService blogCommentService;

    /**
     * Description: 前台查询文章评论
     * Author: jingwen 
     * Date: 2023/1/4 16:17
     *
     *
     * floorCommentId = 0 表示查询一级评论分页
     * floorCommentId != 0 表示查询二级评论分页
     **/
    @PostMapping ("/front/getFrontComment")
    public ResponseResult<MyPageVO<BlogFrontCommentVO>> getFrontComment(@RequestBody BlogFrontCommentPageDTO frontCommentPageDTO){

        return ResponseResult.success(blogCommentService.getFrontCommentByArticleId(frontCommentPageDTO));
    }


    /**
     * Description: 获取文章评论总数量
     * Author: jingwen
     * Date: 2023/2/6 20:01
     **/
    @GetMapping("/front/getFrontCommentCounts")
    public ResponseResult<Integer> getFrontCommentCounts(@RequestParam Integer articleId){

        return ResponseResult.success(blogCommentService.getFrontCommentCounts(articleId));
    }


    /**
     * Description 前台新增一条评论
     * Author jingwen
     * Date 2022-12-03 17:17:39
     **/
    @SysLog(logModule= LogModuleConst.BlogCommentModule, logType = LogTypeConst.ADD, logDesc = "前台新增一条评论")
    @PostMapping("/front/addComment")
    public ResponseResult<Object> addComment(@RequestBody BlogFrontAddCommentDTO frontAddCommentDTO) {
        blogCommentService.addComment(frontAddCommentDTO);
        return ResponseResult.success();
    }


    /**
     * Description: 后台批量审核博客文章评论
     * Author: jingwen
     * Date: 2023/1/13 10:27
     **/
    @SysLog(logModule= LogModuleConst.BlogCommentModule, logType = LogTypeConst.UPDATE, logDesc = "后台批量审核博客文章评论")
    @PreAuthorize("hasAuthority('blog:blogComment:check')")
    @PostMapping("/admin/updateCheckBatch")
    public ResponseResult<Object> updateCheckBatch(@RequestBody BlogAdminUpdateCheckBatchDTO updateCheckBatchDTO) {
        blogCommentService.updateCommentCheckBatch(updateCheckBatchDTO);
        return ResponseResult.success();
    }


    /**
     * Description 后台批量删除博客文章评论
     * Author jingwen
     * Date 2022-12-03 17:17:39
     **/
    @SysLog(logModule= LogModuleConst.BlogCommentModule, logType = LogTypeConst.DELETE, logDesc = "后台批量删除博客文章评论")
    @PreAuthorize("hasAuthority('blog:blogComment:delete')")
    @DeleteMapping("/admin/deleteBatch")
    public ResponseResult<Object> deleteBatch(@RequestBody List<Integer> ids) {
        blogCommentService.deleteBatchComment(ids);
        return ResponseResult.success();
    }

    /**
     * Description: 后台查询评论分页
     * Author: jingwen
     * Date: 2023/1/13 11:00
     **/
    @PreAuthorize("hasAuthority('blog:blogComment:query')")
    @PostMapping("/admin/getPageList")
    public ResponseResult<MyPageVO<BlogAdminCommentPageVO>> getPageList(@RequestBody BlogAdminQueryCommentPageDTO queryCommentPageDTO) {

        return ResponseResult.success(blogCommentService.getAdminCommentPageList(queryCommentPageDTO));
    }
}

