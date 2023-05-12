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
 * Date 2023-02-09 21:59:28
 **/
@Data
  @TableName("blog_friend")
@ApiModel(value = "BlogFriend对象", description = "")
public class BlogFriend implements Serializable {

  private static final long serialVersionUID = 1L;

      @ApiModelProperty("id")
      @TableId(value = "friend_id", type = IdType.AUTO)
      private Integer friendId;

      @ApiModelProperty("标题")
      private String friendTitle;

      @ApiModelProperty("封面")
      private String friendCover;

      @ApiModelProperty("链接")
      private String friendUrl;

      @ApiModelProperty("简介")
      private String friendIntroduction;

      @ApiModelProperty("备注")
      private String remark;

      @ApiModelProperty("状态(1正常 f停用, 0待定)")
      private String status;

      @ApiModelProperty("是否删除(0代表存在 1代表删除)")
      private String isDeleted;

      @ApiModelProperty("创建时间")
      @TableField(fill = FieldFill.INSERT)
      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


}
