package com.jw_server.blog.service.dao.dto;

import com.jw_server.common.mybatis.page.MyPageDTO;
import lombok.Data;

/**
 * Description: 博客后台查询文章列表DTO
 * Author: jingwen
 * DATE: 2023/1/10 11:20
 */
@Data
public class BlogAdminQueryArticlePageDTO extends MyPageDTO {

    //文章类别名称
    private String categoryName;

    //文章审核状态
    private String articleCheck;
}
