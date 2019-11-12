package com.czk.forum.controller;

import com.czk.forum.annotation.LoginRequired;
import com.czk.forum.dto.FollowDTO;
import com.czk.forum.service.FollowService;
import com.czk.forum.util.ForumUtil;
import com.czk.forum.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FollowController {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private FollowService followService;

    @LoginRequired
    @RequestMapping(value = "/follow", method = RequestMethod.POST)
    @ResponseBody
    public String follow(@RequestBody FollowDTO followDTO) {

        followService.follow(hostHolder.getUser().getId(), followDTO.getEntityType(), followDTO.getEntityId());

        return ForumUtil.getJSONString(0, "关注成功");
    }

    @LoginRequired
    @RequestMapping(value = "/unfollow", method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(@RequestBody FollowDTO followDTO) {

        followService.unfollow(hostHolder.getUser().getId(), followDTO.getEntityType(), followDTO.getEntityId());

        return ForumUtil.getJSONString(0, "取消关注成功");
    }

}
