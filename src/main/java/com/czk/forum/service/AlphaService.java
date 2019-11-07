package com.czk.forum.service;

import com.czk.forum.dao.DiscussPostDAO;
import com.czk.forum.dao.UserDAO;
import com.czk.forum.model.DiscussPost;
import com.czk.forum.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * created by srdczk 2019/11/7
 */
@Service
public class AlphaService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private DiscussPostDAO discussPostDAO;

    @Autowired
    private TransactionTemplate transactionTemplate;

    //REQUIRED: 支持当前事务(外部事物), 如果不存在, 则创建新事物
    //REQUIREDS_NEW: 创建一个新的事物,暂停外部事物
    //NESTED: 如果当前存在外部事物, 则嵌套在外部事物中执行(独立的提交和回滚), 否则会和REQUIRED一样
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Object save() {
        User user = new User();
        user.setUsername("alpha");
        user.setSalt("NIMASILE");
        user.setPassword("ANSIAASAS");
        user.setEmail("SIMALEBALAOWU");
        user.setGmtCreate(System.currentTimeMillis());
        user.setActivationCode("DSDS");
        user.setAvatar("SDSD");
        userDAO.add(user);
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setGmtCreate(System.currentTimeMillis());
        post.setTitle("SIMA");
        post.setContent("SDSSDD");
        discussPostDAO.add(post);
        //模仿错误, 会在此报错
        Integer.valueOf("SDKJSD");

        return "OK";
    }

    public Object save2() {
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_UNCOMMITTED);
        //
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionTemplate.execute((t) -> {
            User user = new User();
            user.setUsername("alpha");
            user.setSalt("NIMASILE");
            user.setPassword("ANSIAASAS");
            user.setEmail("SIMALEBALAOWU");
            user.setGmtCreate(System.currentTimeMillis());
            user.setActivationCode("DSDS");
            user.setAvatar("SDSD");
            userDAO.add(user);
            DiscussPost post = new DiscussPost();
            post.setUserId(user.getId());
            post.setGmtCreate(System.currentTimeMillis());
            post.setTitle("SIMA");
            post.setContent("SDSSDD");
            discussPostDAO.add(post);
            //模仿错误, 会在此报错
            Integer.valueOf("SDKJSD");
            return "OK";
        });
        return "OK";
    }
}
