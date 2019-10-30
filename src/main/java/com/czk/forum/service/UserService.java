package com.czk.forum.service;

import com.czk.forum.dao.UserDAO;
import com.czk.forum.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * created by srdczk 2019/10/30
 */
@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    public User findUserById(Integer id) {
        return userDAO.getById(id);
    }
}
