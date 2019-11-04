package com.czk.forum.controller;

import com.czk.forum.dao.PostDTO;
import com.czk.forum.model.DiscussPost;
import com.czk.forum.model.User;
import com.czk.forum.service.DiscussPostService;
import com.czk.forum.util.ForumUtil;
import com.czk.forum.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * created by srdczk 2019/11/4
 */
@Controller
@RequestMapping(value = "/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(@RequestBody PostDTO postDTO) {
        User user = hostHolder.getUser();

//        System.out.println(postDTO);

        if (user == null) {
            return ForumUtil.getJSONString(403, "您还未登录,不能发帖!");
        }

        discussPostService.addDiscussPost(postDTO, user.getId());

        //报错的情况将来统一处理
        return ForumUtil.getJSONString(0, "发布成功");
    }

}
