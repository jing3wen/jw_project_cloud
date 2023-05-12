package com.jw_server.blog.service.controller;

import com.jw_server.blog.service.dao.dto.BlogAdminUpdateCheckBatchDTO;
import com.jw_server.blog.service.dao.entity.BlogFriend;
import com.jw_server.blog.service.service.IBlogFriendService;
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
 * Description  前端控制器
 * Date 2023-02-09 21:59:28
 */
@RestController
@RequestMapping("/blog/blogFriend")
public class BlogFriendController {

    @Resource
    private IBlogFriendService blogFriendService;


    /**
     * Description 前台申请友链
     * Author jingwen
     * Date 2023-02-09 21:59:28
     **/
    @SysLog(logModule= LogModuleConst.BlogFriendModule, logType = LogTypeConst.ADD, logDesc = "前台申请友链")
    @PostMapping("/front/addFriend")
    public ResponseResult<Object> add(@RequestBody BlogFriend blogFriend) {
        blogFriend.setStatus("0");
        blogFriendService.save(blogFriend);
        return ResponseResult.success();
    }

    /**
     * Description 前台查询所有友链
     * Author jingwen
     * Date 2023-02-09 21:59:28
     **/
    @GetMapping("/front/getAllFriend")
    public ResponseResult<List<BlogFriend>> getAllFriend() {
        return ResponseResult.success(blogFriendService.getAllFriend());
    }

    /**
     * Description: 后台批量更新友链审核状态
     * Author: jingwen
     * Date: 2023/2/28 17:18
     **/
    @SysLog(logModule= LogModuleConst.BlogFriendModule, logType = LogTypeConst.UPDATE, logDesc = "后台批量更新友链审核状态")
    @PreAuthorize("hasAuthority('blog:blogFriend:check')")
    @PostMapping("/admin/updateCheckBatch")
    public ResponseResult<Object> updateCheckBatch(@RequestBody BlogAdminUpdateCheckBatchDTO updateCheckBatchDTO) {
        blogFriendService.updateFriendCheckBatch(updateCheckBatchDTO);
        return ResponseResult.success();
    }

    /**
     * Description: 后台批量删除友链
     * Author: jingwen
     * Date: 2023/2/28 17:21
     **/
    @SysLog(logModule= LogModuleConst.BlogFriendModule, logType = LogTypeConst.DELETE, logDesc = "后台批量删除友链")
    @PreAuthorize("hasAuthority('blog:blogFriend:delete')")
    @DeleteMapping("/admin/deleteBatch")
    public ResponseResult<Object> deleteBatch(@RequestBody List<Integer> ids) {
        blogFriendService.removeByIds(ids);
        return ResponseResult.success();
    }

    /**
     * Description 后台分页查询友链
     * Author jingwen
     * Date 2023-02-09 21:59:28
     **/
    @PreAuthorize("hasAuthority('blog:blogFriend:query')")
    @GetMapping("/admin/getBlogFriendPage")
    public ResponseResult<MyPageVO<BlogFriend>> getPageList(@RequestParam("pageNum") Integer pageNum,
                                                            @RequestParam("pageSize") Integer pageSize,
                                                            @RequestParam("status") String status) {

        return ResponseResult.success(blogFriendService.getAdminFriendPage(pageNum, pageSize, status));
    }

}

