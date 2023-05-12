package com.jw_server.blog.service.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description 博客评论表
 * Author jingwen
 * Date 2022-12-03 17:17:39
 **/
@Data
@TableName("blog_comment")
@ApiModel(value = "BlogComment对象", description = "博客评论表")
public class BlogComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("评论ID")
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Integer commentId;

    @ApiModelProperty("评论类型")
    private String commentType;

    @ApiModelProperty("评论用户ID")
    private Integer userId;

    @ApiModelProperty("文章ID")
    private Integer articleId;

    @ApiModelProperty("楼层评论ID (0表示为第1级评论)")
    private Integer floorCommentId;

    @ApiModelProperty("回复的评论ID (0表示为第1级评论)")
    private Integer replyCommentId;

    @ApiModelProperty("回复的用户ID (0表示为第1级评论)")
    private Integer replyUserId;

    @ApiModelProperty("评论内容")
    private String commentContent;

    @ApiModelProperty("评论审核状态（1表示通过，0表示未通过）")
    private String commentCheck;

    @ApiModelProperty("是否删除(0代表存在 1代表删除)")
    private String isDeleted;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("更新者")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


}
