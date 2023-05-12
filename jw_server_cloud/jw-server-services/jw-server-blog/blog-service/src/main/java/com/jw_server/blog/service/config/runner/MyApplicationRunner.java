package com.jw_server.blog.service.config.runner;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jw_server.blog.service.core.constants.BlogConst;
import com.jw_server.blog.service.dao.entity.BlogWeb;
import com.jw_server.blog.service.dao.mapper.BlogWebMapper;
import com.jw_server.common.redis.utils.RedisUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Description: 项目启动时预先查询一些数据到redis中
 * Author: jingwen
 * DATE: 2023/2/25 19:12
 */
@Component
public class MyApplicationRunner implements ApplicationRunner {


    @Resource
    private BlogWebMapper blogWebMapper;

    @Resource
    private RedisUtils redisUtils;


    @Override
    public void run(ApplicationArguments args) throws Exception {

        //存储博客网站配置
        BlogWeb blogWeb = blogWebMapper.selectOne(new LambdaQueryWrapper<BlogWeb>().orderByDesc(BlogWeb::getUpdateTime));
        if(ObjectUtil.isNotEmpty(blogWeb)){
            redisUtils.setCacheObject(BlogConst.BLOG_WEB, blogWeb);
        }

    }
}
