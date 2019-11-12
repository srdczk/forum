package com.czk.forum.controller;

import com.czk.forum.model.DiscussPost;
import com.czk.forum.model.Page;
import com.czk.forum.service.DiscussPostService;
import com.czk.forum.service.LikeService;
import com.czk.forum.service.PageService;
import com.czk.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by srdczk 2019/10/30
 */

@Controller
public class IndexController {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserService userService;

    @Autowired
    private PageService pageService;

    @RequestMapping(value = "/", method = {RequestMethod.POST, RequestMethod.GET})
    public String index(@RequestParam(value = "page", defaultValue = "1") Integer page, Model model, @RequestParam(value = "userId", required = false) Integer userId) {

        List<DiscussPost> posts = discussPostService.findDiscussPost(userId, (page - 1) * 10, 10);
        List<Map<String, Object>> dis = new ArrayList<>();
        for (DiscussPost post : posts) {
            Map<String, Object> map = new HashMap<>();
            map.put("post", post);
            map.put("user", userService.findUserById(post.getUserId()));
            map.put("likeCount", likeService.findEntityCount(1, post.getId()));
            dis.add(map);
        }
        model.addAttribute("dis", dis);
        List<Page> pages = pageService.getPages(userId, page);
        model.addAttribute("pages", pages);
        if (userId != null) model.addAttribute("userId", userId);
        model.addAttribute("cur", page);
        return "index";
    }
    //session 在分布式下出现的问题,nigix负载均衡

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String getErrorPage() {
        return "/error/500";
    }

}
