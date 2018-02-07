/*  
 * 文件名：MyInterceptor.java  
 * 版权：Copyright by www.youkeshu.com  
 * 描述：代码注释以及格式化示例
 * 创建人：孙伸  
 * 创建时间：2018/2/6    
 * 修改理由：  
 * 修改内容：  
 */
package com.yks.oms.interceptor;

import com.yks.oms.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 * @author 孙伸
 * @version 1.0
 * @date 2018/2/6
 * @see MyInterceptor
 * @since JDK1.8
 */
public class MyInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        // 强转两个参数
        System.out.println("进来了");

        User user = (User) httpServletRequest.getSession().getAttribute("user");
        String ticket = httpServletRequest.getHeader("ticket");
        if (StringUtils.isNotBlank(ticket) && ticket.equals(user.getTicket())){
            return true;
        }
        System.out.println("被过滤了");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
