package com.czk.forum.controller;

import com.czk.forum.annotation.LoginRequired;
import com.czk.forum.dao.UserDAO;
import com.czk.forum.dto.MessageDTO;
import com.czk.forum.model.Message;
import com.czk.forum.model.User;
import com.czk.forum.service.MessageService;
import com.czk.forum.service.PageService;
import com.czk.forum.service.UserService;
import com.czk.forum.util.ForumUtil;
import com.czk.forum.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by srdczk 2019/11/11
 */
@Controller
public class MessageController {

    @Autowired
    private PageService pageService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    //私信列表的请求
    @LoginRequired
    @RequestMapping(value = "/letter/list", method = RequestMethod.GET)
    public String getLetterList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                Model model) {
        //获得ConversationList;
        User user = hostHolder.getUser();
        Integer sum = messageService.findConversationCount(user.getId());
        List<Message> messages = messageService.findConversation(user.getId(), (page - 1) * 5, 5);
        List<Map<String, Object>> conversations = new ArrayList<>();
        for (Message message : messages) {
            Map<String, Object> map = new HashMap<>();
            map.put("conversation", message);
            map.put("unreadCount", messageService.findConversationUnread(user.getId(), message.getConversationId()));
            map.put("letterCount", messageService.findLetterCount(message.getConversationId()));
            //还要显示相对的用户的头像
            Integer targetId = message.getFromId();
            if (targetId.equals(user.getId())) targetId = message.getToId();
            map.put("target", userService.findUserById(targetId));
            conversations.add(map);
        }
        model.addAttribute("conversations", conversations);
        model.addAttribute("unread", messageService.findUserUnread(user.getId()));
        model.addAttribute("pages", pageService.getDetailPages(page, sum));

        //页面
        return "/site/letter";
    }

    @LoginRequired
    @RequestMapping(value = "/letter/detail/{conversationId}", method = RequestMethod.GET)
    public String getLetterDetail(@PathVariable(value = "conversationId") String conversationId,
                                  @RequestParam(value = "page", defaultValue = "1") Integer page,
                                  Model model) {
        User user = hostHolder.getUser();
        Integer sum = messageService.findLetterCount(conversationId);
        List<Message> messages = messageService.findLetters(conversationId, (page - 1) * 5, 5);

        List<Map<String, Object>> letters = new ArrayList<>();
        for (Message message : messages) {
            Map<String, Object> map = new HashMap<>();
            if (message.getToId().equals(user.getId()) && message.getStatus().equals(0)) {
                messageService.readMessage(message.getId());
            }
            map.put("letter", message);
            map.put("fromUser", userService.findUserById(message.getFromId()));
            letters.add(map);
        }
        model.addAttribute("letters", letters);
        model.addAttribute("cId", conversationId);
        model.addAttribute("target", getLetterTarget(conversationId));
        model.addAttribute("pages", pageService.getDetailPages(page, sum));

        // 提取出未读的消息, 然后将其设置为已经读取
        return "/site/letter-detail";
    }
    private User getLetterTarget(String conversationId) {
        String[] ss = conversationId.split("_");
        Integer a = Integer.parseInt(ss[0]), b = Integer.parseInt(ss[1]);
        if (hostHolder.getUser().getId().equals(a)) {
            return userService.findUserById(b);
        } else return userService.findUserById(a);
    }

    @LoginRequired
    @RequestMapping(value = "/letter/send", method = RequestMethod.POST)
    @ResponseBody
    public String sendLetter(@RequestBody MessageDTO messageDTO) {
//        System.out.println(messageDTO);
        User toUser = userService.findUserByName(messageDTO.getToName());
        if (toUser == null) return ForumUtil.getJSONString(1, "目标用户不存在");
        User user = hostHolder.getUser();
        //构造要插入的对象
        Message message = new Message();
        message.setFromId(user.getId());
        message.setToId(toUser.getId());
        String conversationId = Math.min(user.getId(), toUser.getId()) + "_" + Math.max(user.getId(), toUser.getId());
        message.setConversationId(conversationId);
        message.setContent(messageDTO.getContent());
        message.setGmtCreate(System.currentTimeMillis());

        messageService.addMessage(message);


//        System.out.println(message);

        return ForumUtil.getJSONString(0);
    }

}
