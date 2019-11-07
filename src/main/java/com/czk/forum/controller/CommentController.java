package com.czk.forum.controller;

import com.czk.forum.annotation.LoginRequired;
import com.czk.forum.model.Comment;
import com.czk.forum.model.User;
import com.czk.forum.service.CommentService;
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
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(value = "/add/{id}", method = RequestMethod.POST)
    @LoginRequired
    public String addComment(@PathVariable(value = "id") Integer id, Comment comment) {
        User user = hostHolder.getUser();
        commentService.addComment(id, user, comment);
//        System.out.println(comment);
        return "redirect:/discuss/detail/" + id;
    }

}
