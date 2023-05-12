package com.jw_server.common.mybatis.handler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.jw_server.common.base.core.constants.SecurityContextConst;
import com.jw_server.common.base.core.context.MySecurityContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * Description: 数据库字段自动填充
 * @author : jingwen
 * DATE: 2022/8/31 18:15
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    private static String CREATE_USER = "createBy";
    private static String CREATE_DATE = "createTime";

    private static String UPDATE_USER = "updateBy";
    private static String UPDATE_DATE = "updateTime";

    /**
     * 插入时的填充策略
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.hasSetter(CREATE_DATE)){
            //实现填充业务逻辑
            this.setFieldValByName(CREATE_DATE, LocalDateTime.now(), metaObject);
            this.setFieldValByName(UPDATE_DATE, LocalDateTime.now(), metaObject);
        }
        if (metaObject.hasSetter(CREATE_USER)){
            //实现填充业务逻辑
            String loginUsername = MySecurityContext.getLoginUserUsername();
            // authentication.getPrincipal() = "anonymousUser"时一般为用户注册
            this.setFieldValByName(CREATE_USER, loginUsername, metaObject);
            this.setFieldValByName(UPDATE_USER, loginUsername, metaObject);
        }

    }

    /**
     * 更新时的填充策略
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasSetter(UPDATE_DATE)) {
            //实现填充业务逻辑
            this.setFieldValByName(UPDATE_DATE, LocalDateTime.now(), metaObject);
        }
        if (metaObject.hasSetter(UPDATE_USER)){
            String loginUsername = MySecurityContext.getLoginUserUsername();
            this.setFieldValByName(UPDATE_USER, loginUsername, metaObject);
        }
    }
}
