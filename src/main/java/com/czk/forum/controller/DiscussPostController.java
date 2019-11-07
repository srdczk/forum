package com.czk.forum.controller;

import com.czk.forum.dto.PostDTO;
import com.czk.forum.model.Comment;
import com.czk.forum.model.DiscussPost;
import com.czk.forum.model.Page;
import com.czk.forum.model.User;
import com.czk.forum.service.CommentService;
import com.czk.forum.service.DiscussPostService;
import com.czk.forum.service.PageService;
import com.czk.forum.service.UserService;
import com.czk.forum.util.ForumConstant;
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
 * created by srdczk 2019/11/4
 */
@Controller
@RequestMapping(value = "/discuss")
public class DiscussPostController implements ForumConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PageService pageService;

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

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public String getPage(Model model, @PathVariable(value = "id") Integer id, @RequestParam(value = "page", defaultValue = "1") Integer page) {
        DiscussPost post = discussPostService.findPostById(id);

        if (post == null) {
            model.addAttribute("msg", "抱歉,你要查找的帖子不存在!");
            model.addAttribute("target", "/forum");
            return "/site/operate-result";
        }

        model.addAttribute("post", post);
        model.addAttribute("user", userService.findUserById(post.getUserId()));

        //获得帖子的所有评论和分页信息
        //所有的评论数量
        List<Comment> comments = commentService.findCommentsByEntityType(ENTITY_TYPE_POST, id, (page - 1) * 5, 5);
        List<Page> pages = pageService.getDetailPages(page, post.getCommentCount());

        List<Map<String, Object>> maps = new ArrayList<>();
        int cnt = 1;
        for (Comment comment : comments) {
            Map<String, Object> map = new HashMap<>();
            map.put("comment", comment);
            map.put("user", userService.findUserById(comment.getUserId()));
            map.put("cnt", (page - 1) * 5 + cnt++);
            //回复 -- 一个列表
            List<Comment> ccs = commentService.findCommentsByEntityType(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
            List<Map<String, Object>> replyMaps = new ArrayList<>();
            for (Comment cc : ccs) {
                Map<String, Object> replyMap = new HashMap<>();
                //处理targetId
                replyMap.put("user", userService.findUserById(cc.getUserId()));
                replyMap.put("reply", cc);
                if (!cc.getTargetId().equals(0)) {
                    replyMap.put("pd", true);
                    replyMap.put("tUser", userService.findUserById(cc.getTargetId()));
                } else replyMap.put("pd", false);
                replyMaps.add(replyMap);
            }
            map.put("replyMaps", replyMaps);
            maps.add(map);
        }

        model.addAttribute("maps", maps);
        model.addAttribute("pages", pages);

        return "/site/discuss-detail";
    }

}
