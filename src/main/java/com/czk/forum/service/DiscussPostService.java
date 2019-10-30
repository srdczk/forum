package com.czk.forum.service;

import com.czk.forum.dao.DiscussPostDAO;
import com.czk.forum.model.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * created by srdczk 2019/10/30
 */
@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostDAO discussPostDAO;

    public List<DiscussPost> findDiscussPost(Integer userId, Integer off, Integer cnt) {
        if (userId == null) return discussPostDAO.getByPage(off, cnt);
        else return discussPostDAO.getByUserIdAndPage(userId, off, cnt);
    }

    public Integer getSum(Integer userId) {
        if (userId == null) return discussPostDAO.sum();
        else return discussPostDAO.sumOfUser(userId);
    }
}
