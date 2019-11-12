package com.czk.forum.controller;

import com.czk.forum.annotation.LoginRequired;
import com.czk.forum.dto.PasswordDTO;
import com.czk.forum.model.User;
import com.czk.forum.service.FollowService;
import com.czk.forum.service.LikeService;
import com.czk.forum.service.UserService;
import com.czk.forum.util.ForumUtil;
import com.czk.forum.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * created by srdczk 2019/11/4
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger  = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private FollowService followService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Value("${forum.path.upload}")
    private String uploadPath;

    @Value("${forum.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @LoginRequired
    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }

    @LoginRequired
    //需要user
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadAvatar(MultipartFile image, Model model) {
        if (image == null) {
            model.addAttribute("error", "您还未选择图片");
            return "/site/setting";
        }
        String fileName = image.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf('.'));

        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件的格式不正确");
            return "/site/setting";
        }
        fileName = ForumUtil.generateUUID() + suffix;

        File file = new File(uploadPath + "/" + fileName);
        try {
            image.transferTo(file);
        } catch (IOException e) {
            logger.error("上传文件错误:" + e.getMessage());
            throw new RuntimeException("上传文件错误:服务器发生异常!", e);
        }

        //更新当前用户的头像路径
        //http://localhost:8888/forum/user/avatar/xxx.png
        String newPath = domain + contextPath + "/user/avatar/" + fileName;

        User user = hostHolder.getUser();

        userService.updateAvatar(user.getId(), newPath);

        return "redirect:/";
    }

    //不用登录
    //获取头像
    @RequestMapping(value = "/avatar/{name}", method = RequestMethod.GET)
    public void getImage(@PathVariable(value = "name") String name, HttpServletResponse response) {

        File file = new File(uploadPath + "/" + name);
        //文件后缀
        String suffix = name.substring(name.lastIndexOf('.'));
        //相应图片
        response.setContentType("image/" + suffix);
        try (OutputStream outputStream = response.getOutputStream();
             FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int b;
            while ((b = fis.read(buffer)) != -1) {
                outputStream.write(buffer, 0, b);
            }

        } catch (IOException e) {
            logger.error("输出头像失败:" + e.getMessage());
        }
    }


    //修改密码
    @LoginRequired
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public String modify(PasswordDTO passwordDTO, Model model) {
        Map<String, Object> map = userService.modifyPassword(passwordDTO, hostHolder.getUser());
        if (map == null || map.isEmpty()) {
            return "redirect:/logout";
        } else {
            model.addAttribute("opasswordMsg", map.get("opasswordMsg"));
            model.addAttribute("npasswordMsg", map.get("npasswordMsg"));
            model.addAttribute("cpasswordMsg", map.get("cpasswordMsg"));
            model.addAttribute("password", passwordDTO);
            return "/site/setting";
        }
    }

    // 个人主页
    @RequestMapping(value = "/profile/{id}", method = RequestMethod.GET)
    public String profile(@PathVariable(value = "id") Integer id,
                          Model model) {

        User user = userService.findUserById(id);

        if (user == null) throw new RuntimeException("该用户不存在!");

        // 用户的基本信息
        model.addAttribute("user", user);

        model.addAttribute("likeCount", likeService.findUserLikeCount(user.getId()));

        model.addAttribute("followerCount", followService.followerCount(id));

        model.addAttribute("followeeCount", followService.followeeCount(id));

        if (hostHolder.getUser() != null && followService.hasFollowed(hostHolder.getUser().getId(), 1, id)) {
            model.addAttribute("followed", true);
        } else model.addAttribute("followed", false);

        return "/site/profile";
    }
}
