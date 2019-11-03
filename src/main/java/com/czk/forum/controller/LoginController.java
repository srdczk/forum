package com.czk.forum.controller;

import com.czk.forum.model.User;
import com.czk.forum.service.UserService;
import com.czk.forum.util.ForumConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * created by srdczk 2019/11/2
 */
@Controller
public class LoginController implements ForumConstant {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/reg", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }

    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "注册成功，我们已经向您发送了一封邮件，请注意查收");
            model.addAttribute("target", "/forum");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("user", user);
            return "/site/register";
        }
    }

    @RequestMapping(value = "/activation/{id}/{code}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable(value = "id") Integer id, @PathVariable(value = "code") String code) {

        switch (userService.activation(id, code)) {
            case ACTIVATION_SUCCESS:
                //成功跳到登陆页面
                model.addAttribute("msg", "激活成功,您的账号已经可以正常使用了");
                model.addAttribute("target", "/forum/login");
                break;
            case ACTIVATION_REPEAT:
                //失败跳到
                model.addAttribute("msg", "无效的操作,该账号已经激活过");
                model.addAttribute("target", "/forum");
                break;
            case ACTIVATION_FAILUE:
                model.addAttribute("msg", "激活失败,您提供的激活码错误");
                model.addAttribute("target", "/forum");
                break;
        }

        return "/site/operate-result";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage() {
        return "/site/login";
    }
//    @RequestMapping()
//    public Map<String, Object> {
//
//    }

}
