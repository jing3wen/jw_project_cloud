package com.jw_server.blog.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jw_server.blog.service.dao.dto.BlogAdminUpdateCheckBatchDTO;
import com.jw_server.blog.service.dao.entity.BlogMessage;
import com.jw_server.common.mybatis.page.MyPageVO;

import java.util.List;


/**
 * Description  服务类
 * Author jingwen
 * Date 2023-02-09 14:24:46
 **/
public interface IBlogMessageService extends IService<BlogMessage> {

    /**
     * Description: 查询留言，若数量太多，则限制数量
     * Author: jingwen
     * Date: 2023/2/9 20:42
     **/
    List<BlogMessage> getMessageList();

    /**
     * Description: 后台获取留言版分页
     * Author: jingwen
     * Date: 2023/2/28 16:35
     **/
    MyPageVO<BlogMessage> getAdminMessagePage(Integer pageNum, Integer pageSize, String messageCheck);

    /**
     * Description: 后台批量更新留言板审核状态
     * Author: jingwen
     * Date: 2023/2/28 16:43
     **/
    void updateMessageCheckBatch(BlogAdminUpdateCheckBatchDTO updateCheckBatchDTO);
}
