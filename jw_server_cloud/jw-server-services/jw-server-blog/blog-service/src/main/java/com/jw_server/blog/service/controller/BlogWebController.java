package com.jw_server.blog.service.controller;

import cn.hutool.core.util.ObjectUtil;
import com.jw_server.blog.service.core.constants.BlogConst;
import com.jw_server.blog.service.dao.entity.BlogWeb;
import com.jw_server.blog.service.service.IBlogWebService;
import com.jw_server.common.base.result.ResponseResult;
import com.jw_server.common.log.annotation.SysLog;
import com.jw_server.common.log.constants.LogModuleConst;
import com.jw_server.common.log.constants.LogTypeConst;
import com.jw_server.common.redis.utils.RedisUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;



/**
 * @author : jingwen
 * Description 网站配置表 前端控制器
 * Date 2023-02-04 15:21:28
 */
@RestController
@RequestMapping("/blog/blogWeb")
public class BlogWebController {

    @Resource
    private IBlogWebService blogWebService;

    @Resource
    private RedisUtils redisUtils;

    /**
     * Description 获取网站配置
     * Author jingwen
     * Date 2023-02-04 15:21:28
     **/
    @GetMapping("/front/getWebInfo")
    public ResponseResult<BlogWeb> findAll() {
        return ResponseResult.success(blogWebService.getWebInfo());
    }


    /**
     * Description 后台更新网站配置
     * Author jingwen
     * Date 2023-02-04 15:21:28
     **/
    @SysLog(logModule= LogModuleConst.BlogWebModule, logType = LogTypeConst.UPDATE, logDesc = "后台更新网站配置")
    @PreAuthorize("hasAuthority('blog:blogWeb:update')")
    @PostMapping("/admin/updateBlogWeb")
    public ResponseResult<Object> update(@RequestBody BlogWeb blogWeb) {
        blogWebService.updateById(blogWeb);
        //删除缓存
        if(ObjectUtil.isNotEmpty(redisUtils.getCacheObject(BlogConst.BLOG_WEB))){
            System.out.println("删除缓存");
            redisUtils.deleteObject(BlogConst.BLOG_WEB);
        }
        return ResponseResult.success();
    }
    /**
     * Description 后台获取网站配置
     * Author jingwen
     * Date 2023-02-04 15:21:28
     **/
    @GetMapping("/admin/getWebInfo")
    public ResponseResult<BlogWeb> getAdminWebInfo() {
        return ResponseResult.success(blogWebService.getWebInfo());
    }


}

