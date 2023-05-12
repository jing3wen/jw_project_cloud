package com.jw_server.common.security.config;


import com.jw_server.common.security.core.handler.AccessDeniedHandlerImpl;
import com.jw_server.common.security.core.handler.AuthenticationEntryPointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;


/**
 * Description: SpringSecurity配置类(针对于springboot2.7新版本)
 *
 * prePostEnabled = true  开启@PreAuthorize等注解
 * jsr250Enabled=true 开启@PermitAll等注解
 * Author: jingwen
 * DATE: 2022/8/29 21:20
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    //认证失败处理
    @Resource
    private AuthenticationEntryPointImpl authenticationEntryPoint;
    // 授权失败处理
    @Resource
    private AccessDeniedHandlerImpl accessDeniedHandler;


    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //开始线程传递用户认证信息
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        return httpSecurity
                .csrf().disable()  //关闭csrf
                .cors() //开启跨域
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  //不通过Session获取SecurityContext
                .and()
                .authorizeRequests()  // 请求认证
                /*
                 * 对于登录接口 允许匿名访问, .anonymous()用户登录认证后(请求头带token)就不能访问
                 * .permitAll()用户登录认证，未登录认证(请求头带不带token)都能访问
                 **/
                .antMatchers("/auth/login/userLogin").anonymous()
                .antMatchers("/**/inner/**").permitAll()  //内部调用
                .antMatchers("/system/sysUser/register").permitAll()
                .antMatchers("/system/sysUser/getCodeForType").permitAll()
                .antMatchers("/system/sysUser/updateForgetPassword").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/**/front/**").permitAll()
                .anyRequest().authenticated()  //任意请求认证后都能访问
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)  //认证失败处理
                .accessDeniedHandler(accessDeniedHandler)  // 授权失败处理
                .and()
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
//
//    //用户登录请求放行
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().antMatchers("/login/userLogin", "/login/getCode");
//    }

    // 密码加密 PasswordEncoder注入容器
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    //获取AuthenticationManager（认证管理器），登录时认证使用
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }



}
