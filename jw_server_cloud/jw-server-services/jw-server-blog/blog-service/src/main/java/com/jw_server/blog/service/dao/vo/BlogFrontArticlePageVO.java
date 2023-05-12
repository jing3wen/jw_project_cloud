package com.jw_server.blog.service.dao.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description: 前台博客分页查询
 * Author: jingwen
 * DATE: 2022/12/3 16:49
 */
@Data
public class BlogFrontArticlePageVO {

    //文章id
    private Integer articleId;

    //用户id
    private Integer userId;

    //用户昵称
    private String nickname;

    //类别ID
    private Integer categoryId;

    //类别名
    private String categoryName;

    //标签名
    private List<BlogFrontTagVO> tagList;

    //是否顶置
    private String isTop;

    //文章封面
    private String articleCover;

    //文章标题
    private String articleTitle;

    //文章简介
    private String articleSummary;

    //浏览量
    private Integer viewCounts;

    //评论量
    private Integer commentCounts;

    //点赞量
    private Integer likedCounts;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
