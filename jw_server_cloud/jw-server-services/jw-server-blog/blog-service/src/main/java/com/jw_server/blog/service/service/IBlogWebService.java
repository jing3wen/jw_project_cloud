package com.jw_server.blog.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jw_server.blog.service.dao.entity.BlogWeb;


/**
 * Description 网站配置表 服务类
 * Author jingwen
 * Date 2023-02-04 15:21:28
 **/
public interface IBlogWebService extends IService<BlogWeb> {

    /**
     * Description: 获取网站配置
     * Author: jingwen
     * Date: 2023/2/8 23:15
     **/
    BlogWeb getWebInfo();

}
