package com.czk.forum.controller;

import com.czk.forum.annotation.LoginRequired;
import com.czk.forum.dto.LikeDTO;
import com.czk.forum.event.EventProducer;
import com.czk.forum.model.Event;
import com.czk.forum.model.User;
import com.czk.forum.service.LikeService;
import com.czk.forum.util.ForumConstant;
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
public class LikeController implements ForumConstant {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    // 异步请求点赞
    @LoginRequired
    @RequestMapping(value = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String like(@RequestBody LikeDTO likeDTO) {
        Integer entityType = likeDTO.getEntityType();
        Integer entityId = likeDTO.getEntityId();
        Integer entityUserId = likeDTO.getEntityUserId();
        User user = hostHolder.getUser();
        // 实现点赞
        likeService.like(user.getId(), entityType, entityId, entityUserId);
        // 获取数量
        long likeCount = likeService.findEntityCount(entityType, entityId);

        int status = likeService.findStatus(user.getId(), entityType, entityId);
        // 返回json
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", status);
        if (status == 1) {
            Event event = new Event()
                    .setTopic(TOPIC_LIKE)
                    .setUserId(hostHolder.getUser().getId())
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setData("postId", likeDTO.getPostId());
            // 设置帖子的详情
            eventProducer.fireEvent(event);
        }
        return ForumUtil.getJSONString(0, null, map);
    }

}
