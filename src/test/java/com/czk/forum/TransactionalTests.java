package com.czk.forum;

import com.czk.forum.service.AlphaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * created by srdczk 2019/11/7
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ForumApplication.class)
public class TransactionalTests {

    @Autowired
    private AlphaService alphaService;

    @Test
    public void testTransactional() {
        alphaService.save();
    }
    @Test
    public void testSave2() {
        alphaService.save2();
    }
}
