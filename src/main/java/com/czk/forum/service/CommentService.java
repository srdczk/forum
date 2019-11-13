package com.czk.forum.service;

import com.czk.forum.dao.CommentDAO;
import com.czk.forum.dao.DiscussPostDAO;
import com.czk.forum.model.Comment;
import com.czk.forum.model.DiscussPost;
import com.czk.forum.model.User;
import com.czk.forum.util.ForumConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * created by srdczk 2019/11/7
 */
@Service
public class CommentService implements ForumConstant {
    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private DiscussPostDAO discussPostDAO;

    public List<Comment> findCommentsByEntityType(Integer entityType, Integer entityId, Integer off, Integer cnt) {
        return commentDAO.selectCommentByEntity(entityType, entityId, off, cnt);
    }

    public Integer findCommentCount(Integer entityType, Integer entityId) {
        return commentDAO.countCommentByEntity(entityType, entityId);
    }


    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void addComment(Integer discussId, User user, Comment comment) {
        DiscussPost post = discussPostDAO.getById(discussId);
        if (post == null || user == null || comment == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        comment.setUserId(user.getId());
        comment.setGmtCreate(System.currentTimeMillis());
        commentDAO.add(comment);
        if (comment.getEntityType().equals(ENTITY_TYPE_POST)) {
            post.setCommentCount(post.getCommentCount() + 1);
            discussPostDAO.updateCommentCount(post);
        }
    }

    public Comment findById(Integer id) {
        return commentDAO.getById(id);
    }

}
