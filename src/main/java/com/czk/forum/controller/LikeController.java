package com.czk.forum.controller;

import com.czk.forum.annotation.LoginRequired;
import com.czk.forum.dto.LikeDTO;
import com.czk.forum.model.User;
import com.czk.forum.service.LikeService;
import com.czk.forum.util.ForumUtil;
import com.czk.forum.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;


    // 异步请求点赞
    @LoginRequired
    @RequestMapping(value = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String like(@RequestBody LikeDTO likeDTO) {
        Integer entityType = likeDTO.getEntityType();
        Integer entityId = likeDTO.getEntityId();
        User user = hostHolder.getUser();
        // 实现点赞
        likeService.like(user.getId(), entityType, entityId);
        // 获取数量
        long likeCount = likeService.findEntityCount(entityType, entityId);

        int status = likeService.findStatus(user.getId(), entityType, entityId);
        // 返回json
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", status);
        return ForumUtil.getJSONString(0, null, map);
    }

}
