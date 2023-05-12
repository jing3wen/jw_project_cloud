package com.jw_server.blog.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jw_server.blog.service.dao.entity.BlogArticleTag;
import com.jw_server.blog.service.dao.mapper.BlogArticleTagMapper;
import com.jw_server.blog.service.service.IBlogArticleTagService;
import org.springframework.stereotype.Service;

/**
 * Description 博客标签关系表 服务实现类
 * Author jingwen
 * Date 2023-02-04 15:21:08
 **/
@Service
public class BlogArticleTagServiceImpl extends ServiceImpl<BlogArticleTagMapper, BlogArticleTag> implements IBlogArticleTagService {

}
