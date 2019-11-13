package com.czk.forum.controller;

import com.czk.forum.annotation.LoginRequired;
import com.czk.forum.event.EventConsumer;
import com.czk.forum.event.EventProducer;
import com.czk.forum.model.Comment;
import com.czk.forum.model.DiscussPost;
import com.czk.forum.model.Event;
import com.czk.forum.model.User;
import com.czk.forum.service.CommentService;
import com.czk.forum.service.DiscussPostService;
import com.czk.forum.util.ForumConstant;
import com.czk.forum.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * created by srdczk 2019/11/7
 */
@Controller
@RequestMapping(value = "/comment")
public class CommentController implements ForumConstant {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private DiscussPostService discussPostService;


    @RequestMapping(value = "/add/{id}", method = RequestMethod.POST)
    @LoginRequired
    public String addComment(@PathVariable(value = "id") Integer id, Comment comment) {
        User user = hostHolder.getUser();
        commentService.addComment(id, user, comment);
//        System.out.println(comment);
        // comment 1
        // like 2
        // follow 3
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(user.getId())
                .setEntityType(1)
                .setEntityId(comment.getId())
                .setData("postId", id);
                // 不一定所有都有帖子
        // 如果是帖子的回帖
        if (comment.getEntityType().equals(ENTITY_TYPE_POST)) {
            event.setEntityUserId(discussPostService.findPostById(id).getUserId());
            // 如果是评论的回帖
        } else if (comment.getEntityType().equals(ENTITY_TYPE_COMMENT)) {
            event.setEntityUserId(commentService.findById(comment.getId()).getUserId());
        }
        // 生产者开始, 投给 消费者
        eventProducer.fireEvent(event);
        return "redirect:/discuss/detail/" + id;
    }

}
