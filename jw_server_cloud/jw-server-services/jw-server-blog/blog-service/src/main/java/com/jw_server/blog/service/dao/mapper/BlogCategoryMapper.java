package com.jw_server.blog.service.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jw_server.blog.service.dao.dto.BlogAdminQueryCategoryPageDTO;
import com.jw_server.blog.service.dao.entity.BlogCategory;
import com.jw_server.blog.service.dao.vo.BlogAdminCategoryPageVO;
import com.jw_server.blog.service.dao.vo.BlogFrontCategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * Description 文章类别表 Mapper 接口
 * Author jingwen
 * Date 2022-12-03 16:13:45
 **/
@Mapper
public interface BlogCategoryMapper extends BaseMapper<BlogCategory> {

    /**
     * Description: 查询所有文章分类
     * Author: jingwen
     * Date: 2023/1/8 22:00
     **/
    List<BlogFrontCategoryVO> getAllCategory();

    /**
     * Description: 后台查询博客类别分页
     * Author: jingwen
     * Date: 2023/1/12 22:23
     **/
    IPage<BlogAdminCategoryPageVO> getBlogCategoryPageList(Page<BlogAdminCategoryPageVO> page,
                                                           @Param("queryCategoryDTO") BlogAdminQueryCategoryPageDTO queryCategoryDTO);

    /**
     * Description: 后台根据文章类别名搜索类别列表/直接搜索所有类别
     * Author: jingwen
     * Date: 2023/1/25 17:00
     **/
    List<BlogFrontCategoryVO> getAllCategoryByCategoryNameOrNot(String categoryName);

    /**
     * Description: 根据类别id搜索类别名
     * Author: jingwen
     * Date: 2023/1/26 12:18
     **/
    String getCategoryNameById(Integer categoryId);
}
