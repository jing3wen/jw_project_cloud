package com.jw_server.blog.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jw_server.blog.service.core.constants.BlogConst;
import com.jw_server.blog.service.dao.dto.BlogAdminUpdateCheckBatchDTO;
import com.jw_server.blog.service.dao.entity.BlogMessage;
import com.jw_server.blog.service.dao.mapper.BlogMessageMapper;
import com.jw_server.blog.service.service.IBlogMessageService;
import com.jw_server.common.base.core.constants.ResponseCode;
import com.jw_server.common.exception.ServiceException;
import com.jw_server.common.mybatis.page.MyPageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * Description  服务实现类
 * Author jingwen
 * Date 2023-02-09 14:24:46
 **/
@Service
public class BlogMessageServiceImpl extends ServiceImpl<BlogMessageMapper, BlogMessage> implements IBlogMessageService {

    @Resource
    private BlogMessageMapper blogMessageMapper;

    /**
     * 查询留言，若数量太多，则限制数量
     **/
    @Override
    public List<BlogMessage> getMessageList() {

        List<BlogMessage> messageList;
        Integer messageCount = Integer.parseInt(
                String.valueOf(
                        count(new LambdaQueryWrapper<BlogMessage>()
                        .eq(BlogMessage::getMessageCheck, BlogConst.MESSAGE_CHECK_PASS))));
        if (messageCount > BlogConst.MAX_MESSAGE_COUNT && BlogConst.MAX_MESSAGE_COUNT != 0){
            messageList =  list(new LambdaQueryWrapper<BlogMessage>()
                    .eq(BlogMessage::getMessageCheck, BlogConst.MESSAGE_CHECK_PASS)
                    .orderByDesc(BlogMessage::getCreateTime)
                   .last("LIMIT ".concat(BlogConst.MAX_MESSAGE_COUNT.toString())));
        }else {
            messageList =  list(new LambdaQueryWrapper<BlogMessage>()
                    .eq(BlogMessage::getMessageCheck, BlogConst.MESSAGE_CHECK_PASS)
                    .orderByDesc(BlogMessage::getCreateTime));
        }
        return messageList;
    }

    /**
     * 后台获取留言版分页
     **/
    @Override
    public MyPageVO<BlogMessage> getAdminMessagePage(Integer pageNum, Integer pageSize, String messageCheck) {
        LambdaQueryWrapper<BlogMessage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(StrUtil.isNotEmpty(messageCheck)) {
            lambdaQueryWrapper.eq(BlogMessage::getMessageCheck, messageCheck);
        }
        return new MyPageVO<>(
                page(new Page<>(pageNum,pageSize),lambdaQueryWrapper)
        );
    }

    /**
     * 后台批量更新留言板审核状态
     **/
    @Override
    public void updateMessageCheckBatch(BlogAdminUpdateCheckBatchDTO updateCheckBatchDTO) {

        if(StrUtil.isEmpty(updateCheckBatchDTO.getCheckStatus())){
            throw new ServiceException(ResponseCode.CODE_400, "更新的状态参数有误");
        }
        if(ObjectUtil.isEmpty(updateCheckBatchDTO.getIds()) || updateCheckBatchDTO.getIds().size()==0){
            throw new ServiceException(ResponseCode.CODE_400, "请选择要更新的数据");
        }
        update(new LambdaUpdateWrapper<BlogMessage>()
                .set(BlogMessage::getMessageCheck, updateCheckBatchDTO.getCheckStatus())
                .in(BlogMessage::getMessageId, updateCheckBatchDTO.getIds()));
    }
}
