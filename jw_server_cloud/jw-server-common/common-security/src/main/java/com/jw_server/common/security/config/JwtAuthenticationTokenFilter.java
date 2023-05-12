package com.jw_server.common.security.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.jw_server.common.base.core.constants.BaseConfigConst;
import com.jw_server.common.base.core.constants.FeignInnerConst;
import com.jw_server.common.base.core.constants.SecurityContextConst;
import com.jw_server.common.base.core.context.MySecurityContext;
import com.jw_server.common.base.dao.LoginUserVO;
import com.jw_server.common.redis.utils.RedisUtils;
import com.jw_server.common.security.core.utils.JwtUtils;
import com.jw_server.common.security.dao.UserDetailsVO;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Description: JWT过滤器
 * Author: jingwen
 * DATE: 2022/8/30 15:27
 */
@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedisUtils redisUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //检查外部请求访问的接口是否为内部接口
        if(request.getRequestURI().contains("bumanz")){
            if(StrUtil.isEmpty(request.getHeader(FeignInnerConst.HEADER_INNER_NAME)) ||
                    !(FeignInnerConst.HEADER_INNER_VALUE.equals(request.getHeader(FeignInnerConst.HEADER_INNER_NAME)))){
                log.warn("jwt过滤器发现有外部请求直接访问内部接口");
                //此处抛出RuntimeException会被spring security的认证失败处理器AuthenticationEntryPointImpl捕获，
                // message写什么都没啥意义
                throw new RuntimeException("不可访问内部接口");

            }
        }

        String token = request.getHeader(BaseConfigConst.TOKEN_HEADER);
        log.info("jwt过滤器发现请求: "+request.getRequestURI()+"——开始解析token————"+token);
        //获取token
        if (StrUtil.isBlank(token)) {
            //放行, 交给SpringSecurity来处理, 进行登录
            filterChain.doFilter(request, response);
            return;
        }
        //解析token
        String userId;
        try {
            Claims claims = JwtUtils.parseJWT(token);
            userId = claims.getSubject();
        }catch (Exception e) {
            //此处抛出RuntimeException会被spring security的认证失败处理器AuthenticationEntryPointImpl捕获，
            // message写什么都没啥意义
            throw new RuntimeException("用户已过期，请重新登录");
        }
        //从redis中获取用户信息
        String subToken = token.substring(token.lastIndexOf(".")+1);
        String redisKey = "login-tokens:"+ subToken;
        UserDetailsVO userDetailsVO = redisUtils.getCacheObject(redisKey);
        if(Objects.isNull(userDetailsVO)){
            //此处抛出RuntimeException会被spring security的认证失败处理器AuthenticationEntryPointImpl捕获，
            // message写什么都没啥意义
            throw new RuntimeException("用户已过期，请重新登录");
        }
        log.info("jwt过滤器将token认证信息写入到spring security上下文 和 自定义上下文MySecurityContext");
        //获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetailsVO, null, userDetailsVO.getAuthorities());
        //存入SecurityContextHolder上下文
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //用户认证的userDetailsVO复制成LoginUserVO, 然后写入到MySecurityContext
        MySecurityContext.setLoginUserVO(copyBeanFromUserDetailsVO(userDetailsVO));
        //放行
        filterChain.doFilter(request, response);
    }

    /**
     * Description: 将userDetailsVO信息复制到LoginUserVO
     * @author : jingwen
     * Date: 2022/9/2 15:05
     **/
    public LoginUserVO copyBeanFromUserDetailsVO(UserDetailsVO userDetailsVO) {
        return LoginUserVO.builder()
                .id(userDetailsVO.getId())
                .username(userDetailsVO.getUsername())
                .nickname(userDetailsVO.getNickname())
                .avatar(userDetailsVO.getAvatar())
                .roleList(userDetailsVO.getRoleList())
                .token(userDetailsVO.getToken())
                .permissionList(userDetailsVO.getPermissionList())
                .email(userDetailsVO.getEmail())
                .phone(userDetailsVO.getPhone())
                .sex(userDetailsVO.getSex())
                .remark(userDetailsVO.getRemark())
                .build();
    }

}
