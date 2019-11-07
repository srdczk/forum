package com.czk.forum.service;

import com.czk.forum.dao.DiscussPostDAO;
import com.czk.forum.dto.PostDTO;
import com.czk.forum.model.DiscussPost;
import com.czk.forum.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * created by srdczk 2019/10/30
 */
@Service
public class DiscussPostService {

    @Autowired
    private SensitiveFilter sensitiveFilter;

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

    public void addDiscussPost(PostDTO postDTO, Integer userId) {
        if (postDTO == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        postDTO.setTitle(HtmlUtils.htmlEscape(postDTO.getTitle()));
        postDTO.setContent(HtmlUtils.htmlEscape(postDTO.getContent()));


        postDTO.setTitle(sensitiveFilter.filter(postDTO.getTitle()));
        postDTO.setContent(sensitiveFilter.filter(postDTO.getContent()));

        DiscussPost post = new DiscussPost();
        post.setUserId(userId);
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setGmtCreate(System.currentTimeMillis());

        discussPostDAO.add(post);
    }

    public DiscussPost findPostById(Integer id) {
        return discussPostDAO.getById(id);
    }

}
