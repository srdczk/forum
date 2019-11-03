package com.czk.forum;

import com.czk.forum.dao.LoginTicketDAO;
import com.czk.forum.model.LoginTicket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * created by srdczk 2019/11/3
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ForumApplication.class)
public class DAOTests {
    @Autowired
    private LoginTicketDAO loginTicketDAO;
    @Test
    public void testLoginTicket() {
        LoginTicket loginTicket = loginTicketDAO.getByTicket("JNSIDD");
        System.out.println(loginTicket);
    }
}
