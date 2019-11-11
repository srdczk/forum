package com.czk.forum;

import com.czk.forum.dao.LoginTicketDAO;
import com.czk.forum.dao.MessageDAO;
import com.czk.forum.dao.UserDAO;
import com.czk.forum.model.LoginTicket;
import com.czk.forum.model.Message;
import com.czk.forum.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * created by srdczk 2019/11/3
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ForumApplication.class)
public class DAOTests {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;


    @Autowired
    private MessageDAO messageDAO;

    @Test
    public void testUserDAO() {
        User user = new User();
        user.setId(2);
        userDAO.activationSuccess(user);
        user.setId(3);
        userDAO.activationSuccess(user);
    }
    @Test
    public void testLoginTicket() {
        LoginTicket loginTicket = loginTicketDAO.getByTicket("JNSIDD");
        System.out.println(loginTicket);
    }

    @Test
    public void testMessageDAO() {
        List<Message> messages = messageDAO.selectConversation(2, 0, 20);
        for (Message message : messages) {
            System.out.println(message);
        }
        messages = messageDAO.selectLetters("2_3", 0, 20);
        for (Message message : messages) {
            System.out.println(message);
        }
        System.out.println(messageDAO.selectLetterCount("2_3"));
        System.out.println(messageDAO.selectConversationCount(2));
        System.out.println(messageDAO.selectConversationUnread(2, "2_3"));
    }

}
