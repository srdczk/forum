package com.czk.forum.service;

import com.czk.forum.dao.UserDAO;
import com.czk.forum.model.User;
import com.czk.forum.util.ForumConstant;
import com.czk.forum.util.ForumUtil;
import com.czk.forum.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * created by srdczk 2019/10/30
 */
@Service
public class UserService implements ForumConstant {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private TemplateEngine engine;

    @Autowired
    private MailClient client;

    @Value("${forum.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public User findUserById(Integer id) {
        return userDAO.getById(id);
    }

    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();
        //空值处理
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }

        //验证账号
        User sUser = userDAO.getByName(user.getUsername());
        if (sUser != null) {
            map.put("usernameMsg", "账号已存在");
            return map;
        }

        //验证邮箱
        sUser = userDAO.getByEmail(user.getEmail());
        if (sUser != null) {
            map.put("emailMsg", "邮箱已经存在");
            return map;
        }
        //注册用户
        user.setSalt(ForumUtil.generateUUID().substring(0, 5));
        user.setPassword(ForumUtil.md5(user.getPassword() + user.getSalt()));
        user.setGmtCreate(System.currentTimeMillis());
        user.setAvatar(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setActivationCode(ForumUtil.generateUUID());

        userDAO.add(user);
        user = userDAO.getByName(user.getUsername());
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        //激活路径设计：activation/101/code
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = engine.process("/mail/activation", context);

        client.sendMail(user.getEmail(), "欢迎注册杜狗网", content);
        return map;

    }

    public int activation(Integer userId, String code) {
        User user = userDAO.getById(userId);
        if (user == null) return ACTIVATION_FAILUE;
        if (user.getStatus().equals(1)) return ACTIVATION_REPEAT;
        if (!user.getActivationCode().equals(code)) return ACTIVATION_FAILUE;
        userDAO.activationSuccess(user);
        return ACTIVATION_SUCCESS;
    }

}
