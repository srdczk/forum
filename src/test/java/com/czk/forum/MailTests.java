package com.czk.forum;

import com.czk.forum.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * created by srdczk 2019/11/2
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ForumApplication.class)
public class MailTests {

    @Autowired
    private MailClient client;

    @Autowired
    private TemplateEngine engine;

    @Test
    public void testSendMail() {
        client.sendMail("srdjlj@163.com", "你妈死了", "我草泥马");
    }

    @Test
    public void testHtmlMail() {
        Context context = new Context();
        context.setVariable("username", "司马老狗");
        String content = engine.process("/mail/demo", context);

        client.sendMail("srdjlj@163.com", "可笑的杜狗子", content);
    }

}
