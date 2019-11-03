package com.czk.forum.controller;

import com.czk.forum.dto.LoginDTO;
import com.czk.forum.model.User;
import com.czk.forum.service.UserService;
import com.czk.forum.util.ForumConstant;
import com.google.code.kaptcha.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * created by srdczk 2019/11/2
 */
@Controller
public class LoginController implements ForumConstant {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Value("${server.servlet.context-path}")
    private String path;

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
    @Autowired
    private Producer kaptchaProducer;

    @RequestMapping(value = "/kaptcha", method = RequestMethod.GET)
    public void kaptcha(HttpServletResponse response, HttpSession session) {
        //生成验证码
        String text = kaptchaProducer.createText();
        //生成图片
        BufferedImage image = kaptchaProducer.createImage(text);

        //设置本次会话
        session.setAttribute("kaptcha", text);
        //将图片直接输出给浏览器
        response.setContentType("image/png");
        try (OutputStream os = response.getOutputStream()){
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            logger.error("图片错误:" + e.getMessage());
        }
    }

    @RequestMapping(value = "/session/{name}", method = RequestMethod.GET)
    @ResponseBody
    public String session(@PathVariable(value = "name") String name, HttpSession session) {
        return session.getAttribute(name).toString();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(LoginDTO loginDTO, HttpSession session, Model model, HttpServletResponse response) {
        Map<String, Object> map = userService.login(loginDTO, session);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(path);
            if (loginDTO.getRember() != null && loginDTO.getRember().equals("on")) {
                cookie.setMaxAge((int) (REMBER_MILSECONDS / 1000));
            } else cookie.setMaxAge((int) (DEFAULT_MILSECONDS / 1000));
            response.addCookie(cookie);
            return "redirect:/";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("verifycodeMsg", map.get("verifycodeMsg"));
            model.addAttribute("user", loginDTO);
            return "/site/login";
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/login";
    }

}
