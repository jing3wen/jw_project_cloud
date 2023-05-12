package com.jw_server.blog.service.controller;

import com.jw_server.blog.service.dao.dto.BlogAdminQueryCategoryPageDTO;
import com.jw_server.blog.service.dao.entity.BlogCategory;
import com.jw_server.blog.service.dao.vo.BlogAdminCategoryPageVO;
import com.jw_server.blog.service.dao.vo.BlogFrontCategoryVO;
import com.jw_server.blog.service.service.IBlogCategoryService;
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
 * Description 文章类别表 前端控制器
 * Date 2022-12-03 16:13:45
 */
@RestController
@RequestMapping("/blog/blogCategory")
public class BlogCategoryController {

    @Resource
    private IBlogCategoryService blogCategoryService;



    /**
    * Description 查询所有文章分类
    * Author jingwen
    * Date 2022-12-03 16:13:45
    **/
    @GetMapping("/front/getAllCategory")
    public ResponseResult<List<BlogFrontCategoryVO>> getAllCategory() {
        return ResponseResult.success(blogCategoryService.getAllCategory());
    }


    /**
     * Description: 新增文章类别
     * Author: jingwen
     * Date: 2023/1/12 22:09
     **/
    @SysLog(logModule= LogModuleConst.BlogCategoryModule, logType = LogTypeConst.ADD, logDesc = "后台新增博客文章类别")
    @PreAuthorize("hasAuthority('blog:blogCategory:add')")
    @PostMapping("/admin/addBlogCategory")
    public ResponseResult<Object> addBlogCategory(@RequestBody BlogCategory blogCategory) {
        blogCategoryService.addOrUpdateBlogCategory(blogCategory);
        return ResponseResult.success();
    }

    /**
     * Description: 更新文章类别
     * Author: jingwen
     * Date: 2023/1/12 22:09
     **/
    @SysLog(logModule= LogModuleConst.BlogCategoryModule, logType = LogTypeConst.UPDATE, logDesc = "后台更新博客文章类别")
    @PreAuthorize("hasAuthority('blog:blogCategory:update')")
    @PostMapping("/admin/updateBlogCategory")
    public ResponseResult<Object> updateBlogCategory(@RequestBody BlogCategory blogCategory) {
        blogCategoryService.addOrUpdateBlogCategory(blogCategory);
        return ResponseResult.success();
    }

    /**
     * Description: 后台批量删除博客文章类别
     * Author: jingwen
     * Date: 2023/1/12 22:36
     **/
    @SysLog(logModule= LogModuleConst.BlogCategoryModule, logType = LogTypeConst.DELETE, logDesc = "后台批量删除博客文章类别")
    @PreAuthorize("hasAuthority('blog:blogCategory:delete')")
    @DeleteMapping("/admin/deleteBatch")
    public ResponseResult<Object> deleteBatch(@RequestBody List<Integer> ids) {
        blogCategoryService.deleteCategoryByIds(ids);
        return ResponseResult.success();
    }

    /**
     * Description 后台查询博客类别分页
     * Author jingwen
     * Date 2023/1/12 22:09
     **/
    @PostMapping("/admin/getBlogCategoryPageList")
    @PreAuthorize("hasAuthority('blog:blogCategory:query')")
    public ResponseResult<MyPageVO<BlogAdminCategoryPageVO>> getBlogCategoryPageList(@RequestBody BlogAdminQueryCategoryPageDTO queryCategoryDTO) {
        return ResponseResult.success(blogCategoryService.getBlogCategoryPageList(queryCategoryDTO));
    }


    /**
     * Description: 后台查询所有文章分类
     * Author: jingwen
     * Date: 2023/3/1 9:49
     **/
    @GetMapping("/admin/getAllBlogCategoryList")
    public ResponseResult<List<BlogFrontCategoryVO>> getAllBlogCategoryList() {
        return ResponseResult.success(blogCategoryService.getAllCategoryByCategoryNameOrNot(null));
    }

    /**
     * Description: 后台根据文章类别名搜索类别列表
     * Author: jingwen
     * Date: 2023/3/1 9:49
     **/
    @GetMapping("/admin/searchBlogCategoryList")
    public ResponseResult<List<BlogFrontCategoryVO>> searchBlogCategoryList(@RequestParam String categoryName) {
        return ResponseResult.success(blogCategoryService.getAllCategoryByCategoryNameOrNot(categoryName));
    }

}

