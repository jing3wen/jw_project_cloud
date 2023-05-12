package com.jw_server.blog.service.controller;

import com.jw_server.blog.service.dao.entity.BlogTag;
import com.jw_server.blog.service.dao.vo.BlogAdminTagPageVO;
import com.jw_server.blog.service.dao.vo.BlogFrontTagVO;
import com.jw_server.blog.service.service.IBlogTagService;
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
 * Description 博客标签表 前端控制器
 * Date 2023-02-04 15:20:40
 */
@RestController
@RequestMapping("/blog/blogTag")
public class BlogTagController {

    @Resource
    private IBlogTagService blogTagService;


    /**
     * Description: 前台查询所有标签
     * Author: jingwen
     * Date: 2023/2/8 10:03
     **/
    @GetMapping("/front/getAllFrontTag")
    public ResponseResult<List<BlogFrontTagVO>> getAllFrontTag() {
        return ResponseResult.success(blogTagService.getAllFrontTag());
    }


    /**
     * Description: 博客后台新增文章标签
     * Author: jingwen
     * Date: 2023/2/28 12:39
     **/
    @SysLog(logModule= LogModuleConst.BlogTagModule, logType = LogTypeConst.ADD, logDesc = "博客后台新增文章标签")
    @PreAuthorize("hasAuthority('blog:blogTag:add')")
    @PostMapping("/admin/addBlogTag")
    public ResponseResult<Object> addBlogTag(@RequestBody BlogTag blogTag) {
        blogTagService.addOrUpdateBlogTag(blogTag);
        return ResponseResult.success();
    }

    /**
     * Description: 博客后台更新文章标签
     * Author: jingwen
     * Date: 2023/2/28 12:38
     **/
    @SysLog(logModule= LogModuleConst.BlogTagModule, logType = LogTypeConst.UPDATE, logDesc = "博客后台更新文章标签")
    @PreAuthorize("hasAuthority('blog:blogTag:update')")
    @PostMapping("/admin/updateBlogTag")
    public ResponseResult<Object> updateBlogTag(@RequestBody BlogTag blogTag) {
        blogTagService.addOrUpdateBlogTag(blogTag);
        return ResponseResult.success();
    }

    /**
     * Description: 博客后台批量删除文章标签
     * Author: jingwen
     * Date: 2023/2/28 12:39
     **/
    @SysLog(logModule= LogModuleConst.BlogTagModule, logType = LogTypeConst.DELETE, logDesc = "博客后台批量删除文章标签")
    @PreAuthorize("hasAuthority('blog:blogTag:delete')")
    @DeleteMapping("/admin/deleteBatch")
    public ResponseResult<Object> deleteBatch(@RequestBody List<Integer> ids) {
        blogTagService.deleteBlogTagBatch(ids);
        return ResponseResult.success();
    }

    /**
     * Description: 博客后台分页查询文章标签
     * Author: jingwen
     * Date: 2023/2/28 12:42
     **/
    @PreAuthorize("hasAuthority('blog:blogTag:query')")
    @GetMapping("/admin/getBlogTagPage")
    public ResponseResult<MyPageVO<BlogAdminTagPageVO>> getPageList(@RequestParam("pageNum") Integer pageNum,
                                                                    @RequestParam("pageSize") Integer pageSize,
                                                                    @RequestParam("tagName") String tagName) {

        return ResponseResult.success(blogTagService.getAdminTagPage(pageNum, pageSize, tagName));
    }

    /**
     * Description:
     * Author: jingwen
     * Date: 2023/2/28 20:24
     **/
    @GetMapping("/admin/getAllBlogTagList")
    public ResponseResult<List<BlogFrontTagVO>> getAllBlogTagList() {
        return ResponseResult.success(blogTagService.getTagListByTageNameOrNot(null));
    }

    /**
     * Description: 后台根据文章标签名搜索标签列表
     * Author: jingwen
     * Date: 2023/1/25 16:53
     **/
    @GetMapping("/admin/searchBlogTagList")
    public ResponseResult<List<BlogFrontTagVO>> searchBlogCategoryList(@RequestParam String tagName) {
        return ResponseResult.success(blogTagService.getTagListByTageNameOrNot(tagName));
    }

}

