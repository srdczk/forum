package com.czk.forum.service;

import com.czk.forum.dao.MessageDAO;
import com.czk.forum.model.Message;
import com.czk.forum.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * created by srdczk 2019/11/11
 */
@Service
public class MessageService {
    @Autowired
    private MessageDAO messageDAO;

    @Autowired
    private SensitiveFilter sensitiveFilter;

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

    public void addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        messageDAO.add(message);
    }

    public void addSystemMessage(Message message) {
        messageDAO.add(message);
    }

    public int readMessage(Integer msgId) {
        //修改数据的状态为1
        return messageDAO.updateStatus(msgId, 1);
    }

    // 查询最新的通知
    public Message findRecent(String type, int userId) {
        return messageDAO.getNoticeRecent(type, userId);
    }

    // 查询 通知的未读数量
    public int findNoticeCount(String type, int userId) {
        return messageDAO.getNoticeCount(type, userId);
    }

    public int findNoticeUnreadCount(String type, int userId) {
        return messageDAO.getNoticeUnreadCount(type, userId);
    }



}
