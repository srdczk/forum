package com.czk.forum;

import com.czk.forum.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * created by srdczk 2019/11/4
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ForumApplication.class)
public class FilterTests {
    @Autowired
    private SensitiveFilter filter;

    @Test
    public void testFilter() {
        String text = "fabcd";
        System.out.println(filter.filter(text));
    }

}
