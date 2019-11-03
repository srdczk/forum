package com.czk.forum.util;

import com.czk.forum.model.User;
import org.springframework.stereotype.Component;

/**
 * created by srdczk 2019/11/3
 */
@Component
public class HostHolder {

    private static ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public User getUser() {
        return threadLocal.get();
    }

    public void setUser(User user) {
        threadLocal.set(user);
    }

    public void clear() {
        threadLocal.remove();
    }

}
