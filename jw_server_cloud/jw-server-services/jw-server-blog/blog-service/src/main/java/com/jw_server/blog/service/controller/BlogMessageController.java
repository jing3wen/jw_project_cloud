package com.jw_server.blog.service.controller;

import com.jw_server.blog.service.dao.dto.BlogAdminUpdateCheckBatchDTO;
import com.jw_server.blog.service.dao.entity.BlogMessage;
import com.jw_server.blog.service.service.IBlogMessageService;
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
 * Description  前端控制器
 * Date 2023-02-09 14:24:46
 */
@RestController
@RequestMapping("/blog/blogMessage")
public class BlogMessageController {

    @Resource
    private IBlogMessageService blogMessageService;
    /**
     * Description 前台新增一条留言
     * Author jingwen
     * Date 2023-02-09 14:24:46
     **/
    @SysLog(logModule= LogModuleConst.BlogMessageModule, logType = LogTypeConst.ADD, logDesc = "前台新增一条留言")
    @PostMapping("/front/addMessage")
    public ResponseResult<Object> add(@RequestBody BlogMessage blogMessage) {
        blogMessageService.save(blogMessage);
        return ResponseResult.success();
    }

    /**
     * Description 前台获取所有留言
     * Author jingwen
     * Date 2023-02-09 14:24:46
     **/
    @GetMapping("/front/getMessageList")
    public ResponseResult<List<BlogMessage>> getMessageList() {
        return ResponseResult.success(blogMessageService.getMessageList());
    }

    /**
     * Description 后台批量更新留言板审核状态
     * Author jingwen
     * Date 2023-02-09 14:24:46
     **/
    @SysLog(logModule= LogModuleConst.BlogMessageModule, logType = LogTypeConst.UPDATE, logDesc = "后台批量更新留言板审核状态")
    @PreAuthorize("hasAuthority('blog:blogMessage:check')")
    @PostMapping("/admin/updateCheckBatch")
    public ResponseResult<Object> updateCheckBatch(@RequestBody BlogAdminUpdateCheckBatchDTO updateCheckBatchDTO) {
        blogMessageService.updateMessageCheckBatch(updateCheckBatchDTO);
        return ResponseResult.success();
    }

    /**
     * Description 后台批量更新留言板留言
     * Author jingwen
     * Date 2023-02-09 14:24:46
     **/
    @SysLog(logModule= LogModuleConst.BlogMessageModule, logType = LogTypeConst.DELETE, logDesc = "后台批量更新留言板留言")
    @PreAuthorize("hasAuthority('blog:blogMessage:delete')")
    @DeleteMapping("/admin/deleteBatch")
    public ResponseResult<Object> deleteBatch(@RequestBody List<Integer> ids) {
        blogMessageService.removeByIds(ids);
        return ResponseResult.success();
    }

    /**
     * Description: 后台获取留言版分页
     * Author: jingwen
     * Date: 2023/2/28 16:35
     **/
    @PreAuthorize("hasAuthority('blog:blogMessage:query')")
    @GetMapping("/admin/getBlogMessagePage")
    public ResponseResult<MyPageVO<BlogMessage>> getPageList(@RequestParam("pageNum") Integer pageNum,
                                                             @RequestParam("pageSize") Integer pageSize,
                                                             @RequestParam("messageCheck") String messageCheck) {

        return ResponseResult.success(blogMessageService.getAdminMessagePage(pageNum, pageSize, messageCheck));
    }




}

