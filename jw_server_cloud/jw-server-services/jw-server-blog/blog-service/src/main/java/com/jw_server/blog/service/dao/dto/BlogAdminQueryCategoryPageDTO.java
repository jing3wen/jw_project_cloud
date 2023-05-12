package com.jw_server.blog.service.dao.dto;

import com.jw_server.common.mybatis.page.MyPageDTO;
import lombok.Data;

/**
 * Description:
 * Author: jingwen
 * DATE: 2023/1/12 22:15
 */
@Data
public class BlogAdminQueryCategoryPageDTO extends MyPageDTO {

    //文章类别名称
    private String categoryName;
}
