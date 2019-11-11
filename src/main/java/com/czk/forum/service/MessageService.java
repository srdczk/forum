package com.czk.forum.service;

import com.czk.forum.dao.MessageDAO;
import com.czk.forum.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * created by srdczk 2019/11/11
 */
@Service
public class MessageService {
    @Autowired
    private MessageDAO messageDAO;

    public Integer findConversationCount(Integer userId) {
        return messageDAO.selectConversationCount(userId);
    }

    public List<Message> findConversation(Integer userId, Integer off, Integer cnt) {
        return messageDAO.selectConversation(userId, off, cnt);
    }

    public Integer findConversationUnread(Integer userId, String conversationId) {
        return messageDAO.selectConversationUnread(userId, conversationId);
    }

    public Integer findLetterCount(String conversationId) {
        return messageDAO.selectLetterCount(conversationId);
    }

    public Integer findUserUnread(Integer userId) {
        return messageDAO.selectUnreadCount(userId);
    }

    public List<Message> findLetters(String conversationId, Integer off, Integer cnt) {
        return messageDAO.selectLetters(conversationId, off, cnt);
    }

}
