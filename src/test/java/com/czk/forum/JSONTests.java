package com.czk.forum;

import com.czk.forum.dao.MessageDAO;
import com.czk.forum.model.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ForumApplication.class)
public class JSONTests {

    @Autowired
    private MessageDAO messageDAO;

    @Test
    public void testJSON() {
    }

}
