package com.jw_server.blog.service.core.constants;

/**
 * Description:
 * @author : jingwen
 * DATE: 2023/2/9 20:54
 */
public class BlogConst {

    /**
     * 缓存到redis中的网站信息 key
     **/
    public static final String BLOG_WEB="blog:blog_web";

    /**
     * 查询留言数量上线
     **/
    public static final Integer MAX_MESSAGE_COUNT = 200;

    /**
     * 评论类型——文章评论
     **/
    public static final String ARTICLE_COMMENT_TYPE="article";

    /**
     * 评论类型——留言
     **/
    public static final String MESSAGE_COMMENT_TYPE="message";


    /**
     * 留言审核通过
     **/
    public static final String MESSAGE_CHECK_PASS = "1";
}
