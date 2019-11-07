package com.czk.forum;

import com.czk.forum.dao.CommentDAO;
import com.czk.forum.dao.DiscussPostDAO;
import com.czk.forum.model.Comment;
import com.czk.forum.model.DiscussPost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ForumApplication.class)
public class ForumApplicationTests {
    @Autowired
    private CommentDAO commentDAO;
    @Autowired
    private DiscussPostDAO discussPostDAO;
    @Test
    public void textAddComment() {
        for (int i = 0; i < 50; i++) {
            Comment comment = new Comment();
            comment.setContent("杜狗子,你好" + i);
            comment.setUserId(1);
            comment.setEntityType(2);
            comment.setEntityId((int)(Math.random() * 50) + 1);
            if (i % 2 == 0) comment.setTargetId(0);
            else comment.setTargetId(1);
            comment.setGmtCreate(System.currentTimeMillis());
            commentDAO.add(comment);
        }
    }
    @Test
    public void testupatePost() {
        DiscussPost post = discussPostDAO.getById(1);
        post.setCommentCount(50);
        discussPostDAO.updateCommentCount(post);
    }
}
