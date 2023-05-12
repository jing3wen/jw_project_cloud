package com.jw_server.blog.service.dao.dto;

import com.jw_server.common.mybatis.page.MyPageDTO;
import lombok.Data;

/**
 * Description: 前台浏览朋友圈分页
 * Author: jingwen
 * DATE: 2023/2/11 20:57
 */
@Data
public class BlogFrontMomentsPageDTO extends MyPageDTO {

    private Boolean viewMe;
}
