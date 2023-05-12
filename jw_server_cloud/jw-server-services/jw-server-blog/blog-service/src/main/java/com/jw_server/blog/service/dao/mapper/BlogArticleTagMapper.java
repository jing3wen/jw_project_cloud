package com.jw_server.blog.service.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jw_server.blog.service.dao.entity.BlogArticleTag;
import com.jw_server.blog.service.dao.vo.BlogFrontTagVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * Description 博客标签关系表 Mapper 接口
 * Author jingwen
 * Date 2023-02-04 15:21:08
 **/
@Mapper
public interface BlogArticleTagMapper extends BaseMapper<BlogArticleTag> {

    /**
     * Description: 根据文章id查询所有标签名
     * Author: jingwen
     * Date: 2023/2/4 15:29
     **/
    List<BlogFrontTagVO> getArticleTagsByArticleId(Integer articleId);

    /**
     * Description: 批量新增文章标签关系
     * Author: jingwen
     * Date: 2023/2/28 21:29
     **/
    int insertBatchArticleTag(List<BlogArticleTag> addArticleTagList);

}
