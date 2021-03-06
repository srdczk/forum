package com.czk.forum.service;

import com.czk.forum.dao.UserDAO;
import com.czk.forum.dto.LoginDTO;
import com.czk.forum.dto.PasswordDTO;
import com.czk.forum.model.User;
import com.czk.forum.util.*;
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

    @Autowired
    private RedisAdapter adapter;

    // 不推荐使用
//    @Autowired
//    private LoginTicketDAO loginTicketDAO;


    public User findUserById(Integer id) {
        // 先从缓存中查询 :
        User user = getCache(id);
        if (user == null) {
            user = initCache(id);
        }
        return user;
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
//        System.out.println(user.getId());
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
        clearCache(userId);
        return ACTIVATION_SUCCESS;
    }

    public Map<String, Object> login(LoginDTO loginDTO, String owner) {
        Map<String, Object> map = new HashMap<>();
        //空值判断
        if (StringUtils.isBlank(loginDTO.getUsername())) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }

        if (StringUtils.isBlank(loginDTO.getPassword())) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }

        if (StringUtils.isBlank(loginDTO.getVerifycode())) {
            map.put("verifycodeMsg", "验证码不能为空");
            return map;
        }

        // 验证码, 和那个不一样
        if (StringUtils.isBlank(owner) || !loginDTO.getVerifycode().equals(adapter.get(RedisUtil.getKaptchaKey(owner)))) {
            map.put("verifycodeMsg", "验证码不正确");
            return map;
        }
        // 不存在session里面, 存在redis 里面

        User user = userDAO.getByName(loginDTO.getUsername());
//        System.out.println(user);
        if (user == null) {
            map.put("usernameMsg", "账号不存在!");
            return map;
        }
        //验证状态
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "该账号未激活!");
            return map;
        }

        //验证密码
        String password = ForumUtil.md5(loginDTO.getPassword() + user.getSalt());
        if (!password.equals(user.getPassword())) {
            map.put("passwordMsg", "密码错误!");
            return map;
        }

        //生成登录凭证
        String ticket = ForumUtil.generateUUID();
        String redisKey = RedisUtil.getTicketKey(ticket);
        if (loginDTO.getRember() != null && loginDTO.getRember().equals("on"))
            adapter.set(redisKey, String.valueOf(user.getId()), REMBER_SECONDS);
        else adapter.set(redisKey, String.valueOf(user.getId()), DEFAULT_SECONDS);

        map.put("ticket", ticket);

        return map;
    }

    public void logout(String ticket) {
        String redisKey = RedisUtil.getTicketKey(ticket);
        adapter.set(redisKey, "");
    }


    public int updateAvatar(Integer userId, String avatar) {
        clearCache(userId);
        return userDAO.updateAvatar(avatar, userId);
    }

    public Map<String, Object> modifyPassword(PasswordDTO passwordDTO, User user) {
        Map<String, Object> map = new HashMap<>();
        //判断是否为空
        if (StringUtils.isBlank(passwordDTO.getOpassword())) {
            map.put("opasswordMsg", "原密码不能为空!");
            return map;
        }
        if (StringUtils.isBlank(passwordDTO.getNpassword())) {
            map.put("npasswordMsg", "新密码不能为空!");
            return map;
        }
        if (StringUtils.isBlank(passwordDTO.getCpassword())) {
            map.put("cpasswordMsg", "确认密码不能为空!");
            return map;
        }
        //两次输入密码不一样
        if (!passwordDTO.getNpassword().equals(passwordDTO.getCpassword())) {
            map.put("cpasswordMsg", "两次输入密码不一样!");
            return map;
        }
        if (passwordDTO.getNpassword().length() < 8) {
            map.put("npasswordMsg", "新密码长度不能少于8位数!");
            return map;
        }
        //确认密码是否正确
        String src = ForumUtil.md5(passwordDTO.getOpassword() + user.getSalt());
        if (!src.equals(user.getPassword())) {
            map.put("opasswordMsg", "密码错误,请检查!");
            return map;
        }

        if (passwordDTO.getNpassword().equals(passwordDTO.getOpassword())) {
            map.put("npasswordMsg", "不能与原密码一致!");
            return map;
        }

        String salt = ForumUtil.generateUUID().substring(0, 5);
        String newPassword = ForumUtil.md5(passwordDTO.getNpassword() + salt);

        user.setSalt(salt);
        user.setPassword(newPassword);

        userDAO.updatePassword(user);

        return map;
    }

    public User findUserByName(String name) {
        return userDAO.getByName(name);
    }

    // 优先从缓存中取值
    private User getCache(int userId) {
        String userKey = RedisUtil.getUserKey(userId);
        return adapter.getUser(userKey);
    }

    // 取不到值的时候初始化数据
    private User initCache(int userId) {
        User user = userDAO.getById(userId);
        adapter.setUser(RedisUtil.getUserKey(userId), user, 3600);
        return user;
    }

    // 数据变更的时候清除缓存数据
    private void clearCache(int userId) {
        String redisKey = RedisUtil.getUserKey(userId);
        adapter.delete(redisKey);
    }

}
