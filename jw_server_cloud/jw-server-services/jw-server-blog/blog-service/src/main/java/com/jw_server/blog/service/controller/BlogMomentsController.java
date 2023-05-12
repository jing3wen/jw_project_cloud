package com.jw_server.blog.service.controller;

import com.jw_server.blog.service.dao.dto.BlogFrontMomentsPageDTO;
import com.jw_server.blog.service.dao.entity.BlogMoments;
import com.jw_server.blog.service.dao.vo.BlogAdminMomentsVO;
import com.jw_server.blog.service.dao.vo.BlogFrontMomentsPageVO;
import com.jw_server.blog.service.service.IBlogMomentsService;
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
 * @author : jingwen
 * Description 朋友圈表 前端控制器
 * Date 2023-02-11 20:48:10
 */
@RestController
@RequestMapping("/blog/blogMoments")
public class BlogMomentsController {

    @Resource
    private IBlogMomentsService blogMomentsService;
    /**
     * Description 博客前台发布朋友圈
     * Author jingwen
     * Date 2023-02-11 20:48:10
     **/
    @SysLog(logModule= LogModuleConst.BlogMomentsModule, logType = LogTypeConst.ADD, logDesc = "博客前台发布朋友圈")
    @PostMapping("/front/addMoments")
    public ResponseResult<Object> addMoments(@RequestBody BlogMoments blogMoments) {
        blogMomentsService.save(blogMoments);
        return ResponseResult.success();
    }

    /**
     * Description 博客前台删除朋友圈
     * Author jingwen
     * Date 2023-02-11 20:48:10
     **/
    @SysLog(logModule= LogModuleConst.BlogMomentsModule, logType = LogTypeConst.DELETE, logDesc = "博客前台删除朋友圈")
    @DeleteMapping("/front/deleteBatch")
    public ResponseResult<Object> deleteBatch(@RequestBody List<Integer> ids) {
        blogMomentsService.removeByIds(ids);
        return ResponseResult.success();
    }

    /**
     * Description 前台查询朋友圈分页,
     * 若viewMe=true，则只看登录用户的所有朋友圈
     * 若viewMe=false, 则浏览登录用户的所有朋友圈，和所有用户的公开朋友圈
     *
     * Author jingwen
     * Date 2023-02-11 20:48:10
     **/
    @PostMapping("/front/getFrontMomentsPage")
    public ResponseResult<MyPageVO<BlogFrontMomentsPageVO>> getFrontMomentsPage(@RequestBody BlogFrontMomentsPageDTO frontMomentsPageDTO) {

        return ResponseResult.success(blogMomentsService.getFrontMomentsPage(frontMomentsPageDTO));
    }

    /**
     * Description 博客后台批量删除朋友圈——可以和"博客前台删除朋友圈"合并
     * Author jingwen
     * Date 2023-02-11 20:48:10
     **/
    @SysLog(logModule= LogModuleConst.BlogMomentsModule, logType = LogTypeConst.DELETE, logDesc = "博客后台批量删除朋友圈")
    @PreAuthorize("hasAuthority('blog:blogMoments:delete')")
    @DeleteMapping("/admin/deleteBatch")
    public ResponseResult<Object> deleteAdminBatch(@RequestBody List<Integer> ids) {
        blogMomentsService.removeByIds(ids);
        return ResponseResult.success();
    }

    /**
     * Description: 后台查询朋友圈分页
     * Author: jingwen
     * Date: 2023/2/28 17:00
     **/
    @PreAuthorize("hasAuthority('blog:blogMoments:query')")
    @GetMapping("/admin/getAdminMomentsPage")
    public ResponseResult<MyPageVO<BlogAdminMomentsVO>> getAdminMomentsPage(@RequestParam("pageNum") Integer pageNum,
                                                                            @RequestParam("pageSize") Integer pageSize,
                                                                            @RequestParam("nickname") String nickname) {

        return ResponseResult.success(blogMomentsService.getAdminMomentsPage(pageNum, pageSize, nickname));
    }


}

