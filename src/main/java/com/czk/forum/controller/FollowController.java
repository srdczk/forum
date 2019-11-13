package com.czk.forum.controller;

import com.czk.forum.annotation.LoginRequired;
import com.czk.forum.dto.FollowDTO;
import com.czk.forum.event.EventProducer;
import com.czk.forum.model.Event;
import com.czk.forum.model.Page;
import com.czk.forum.model.User;
import com.czk.forum.service.FollowService;
import com.czk.forum.service.PageService;
import com.czk.forum.service.UserService;
import com.czk.forum.util.ForumConstant;
import com.czk.forum.util.ForumUtil;
import com.czk.forum.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
public class FollowController implements ForumConstant {

    @Autowired
    private HostHolder hostHolder;


    @Autowired
    private FollowService followService;


    @Autowired
    private UserService userService;

    @Autowired
    private PageService pageService;

    @Autowired
    private EventProducer eventProducer;

    @LoginRequired
    @RequestMapping(value = "/follow", method = RequestMethod.POST)
    @ResponseBody
    public String follow(@RequestBody FollowDTO followDTO) {

        followService.follow(hostHolder.getUser().getId(), followDTO.getEntityType(), followDTO.getEntityId());

        // 关注, 发送通知
        // event 关注发送通知
        // 构建Event
        Event event = new Event()
                .setTopic(TOPIC_FOLLOW)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(3)
                .setEntityId(followDTO.getEntityId())
                .setEntityUserId(followDTO.getEntityId());
        eventProducer.fireEvent(event);
        return ForumUtil.getJSONString(0, "关注成功");
    }

    @LoginRequired
    @RequestMapping(value = "/unfollow", method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(@RequestBody FollowDTO followDTO) {

        followService.unfollow(hostHolder.getUser().getId(), followDTO.getEntityType(), followDTO.getEntityId());

        return ForumUtil.getJSONString(0, "取消关注成功");
    }
    // 关注此人的人, 支持分页儿
    @RequestMapping(value = "/follower/{id}", method = RequestMethod.GET)
    public String getFollower(@PathVariable(value = "id") Integer id,
                              @RequestParam(value = "page", defaultValue = "1") Integer page,
                              Model model) {
        User user = userService.findUserById(id);
        if (user == null) throw new RuntimeException("用户不存在!");
        int sum = (int) followService.followerCount(id);

        model.addAttribute("followerCount", sum);
        model.addAttribute("followeeCount", followService.followeeCount(id));
        model.addAttribute("flrs", followService.findFollowers(id, (page - 1) * 5, 5));

        List<Page> pages = pageService.getDetailPages(page, sum);
        model.addAttribute("pages", pages);
        model.addAttribute("user", user);
        return "/site/follower";
    }
    // 他关注的人, 支持分页儿
    @RequestMapping(value = "/followee/{id}", method = RequestMethod.GET)
    public String getFollowee(@PathVariable(value = "id") Integer id,
                              @RequestParam(value = "page", defaultValue = "1") Integer page,
                              Model model) {
        User user = userService.findUserById(id);
        if (user == null) throw new RuntimeException("用户不存在!");
        int sum = (int) followService.followeeCount(id);
        model.addAttribute("followerCount", followService.followerCount(id));
        model.addAttribute("followeeCount", sum);
        // 用户关注的人
        model.addAttribute("fles", followService.findFollowees(id, (page - 1) * 5, 5));
        List<Page> pages = pageService.getDetailPages(page, sum);
        model.addAttribute("pages", pages);
        model.addAttribute("user", user);

        return "/site/followee";

    }

}
