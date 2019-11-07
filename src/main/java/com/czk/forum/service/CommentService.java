package com.czk.forum.service;

import com.czk.forum.dao.CommentDAO;
import com.czk.forum.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * created by srdczk 2019/11/7
 */
@Service
public class CommentService {
    @Autowired
    private CommentDAO commentDAO;

    public List<Comment> findCommentsByEntityType(Integer entityType, Integer entityId, Integer off, Integer cnt) {
        return commentDAO.selectCommentByEntity(entityType, entityId, off, cnt);
    }

    public Integer findCommentCount(Integer entityType, Integer entityId) {
        return commentDAO.countCommentByEntity(entityType, entityId);
    }

}
