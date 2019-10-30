package com.czk.forum.service;

import com.czk.forum.dao.DiscussPostDAO;
import com.czk.forum.model.DiscussPost;
import com.czk.forum.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * created by srdczk 2019/10/30
 */
@Service
public class PageService {
    @Autowired
    private DiscussPostDAO discussPostDAO;

    public List<Page> getPages(Integer userId, Integer page) {
        Integer sum;
        if (userId == null) sum = discussPostDAO.sum();
        else sum = discussPostDAO.sumOfUser(userId);
        int max = sum % 10 == 0 ? sum / 10 : sum / 10 + 1;
        List<Page> res = new ArrayList<>();
        res.add(new Page("首页", 1, false));
        res.add(new Page("上一页", page < 2 ? 1 : page - 1, false));
        if (page - 2 > 0) res.add(new Page(String.valueOf(page - 2), page - 2, false));
        if (page - 1 > 0) res.add(new Page(String.valueOf(page - 1), page - 1, false));
        res.add(new Page(String.valueOf(page), page, true));
        if (page + 1 < max + 1) res.add(new Page(String.valueOf(page + 1), page + 1, false));
        if (page + 2 < max + 1) res.add(new Page(String.valueOf(page + 2), page + 2, false));
        res.add(new Page("下一页", page >= max ? max : page + 1, false));
        res.add(new Page("末页", max, false));
        return res;
    }

}
