package com.jw_server.blog.service.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description 
 * Author jingwen
 * Date 2023-02-09 14:24:46
 **/
@Data
@TableName("blog_message")
@ApiModel(value = "BlogMessage对象", description = "弹幕留言类")
public class BlogMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("留言ID")
    @TableId(value = "message_id", type = IdType.AUTO)
    private Integer messageId;

    @ApiModelProperty("昵称")
    private String messageNickname;

    @ApiModelProperty("留言地址")
    private String messageAvatar;

    @ApiModelProperty("留言邮箱")
    private String messageEmail;

    @ApiModelProperty("留言内容")
    private String messageContent;

    @ApiModelProperty("留言审核状态（1表示通过，0表示未审核）")
    private String messageCheck;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


}
