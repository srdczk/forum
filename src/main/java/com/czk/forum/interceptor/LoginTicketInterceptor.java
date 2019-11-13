package com.czk.forum.interceptor;

import com.czk.forum.model.User;
import com.czk.forum.service.UserService;
import com.czk.forum.util.CookieUtil;
import com.czk.forum.util.HostHolder;
import com.czk.forum.util.RedisAdapter;
import com.czk.forum.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * created by srdczk 2019/11/3
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private RedisAdapter adapter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String ticket = CookieUtil.getValue(request, "ticket");
        //System.out.println(ticket);
        if (StringUtils.isNotBlank(ticket)) {
            String userId = adapter.get(RedisUtil.getTicketKey(ticket));
            if (StringUtils.isNotBlank(userId)) {
                //登陆成功的状态
                User user = userService.findUserById(Integer.parseInt(userId));
                //在本次请求当中持有User
                hostHolder.setUser(user);
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        User user = hostHolder.getUser();
//        System.out.println(user);
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        hostHolder.clear();
    }
}
