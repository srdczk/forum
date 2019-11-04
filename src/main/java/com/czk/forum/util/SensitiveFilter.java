package com.czk.forum.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * created by srdczk 2019/11/4
 */
@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    /**
     * 过滤敏感词
     * @param text 文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) return null;
        char[] chs = text.toCharArray();
        StringBuilder res = new StringBuilder();
        //指针1
        TrieNode node = root;
        //指针2, 3
        int begin = 0, end = 0;

        while (end < chs.length) {
            char c = chs[end];

            // 跳过符号
            if (isSymbol(c)) {
                // 若指针1处于根节点,将此符号计入结果,让指针2向下走一步
                if (node == root) {
                    res.append(c);
                    begin++;
                }
                // 无论符号在开头或中间,指针3都向下走一步
                end++;
                continue;
            }

            // 检查下级节点
            node = node.getSubNode(c);
            if (node == null) {
                // 以begin开头的字符串不是敏感词
                res.append(chs[begin++]);
                // 进入下一个位置
                end = begin;
                // 重新指向根节点
                node = root;
            } else if (node.isKeyWordEnd()) {
                // 发现敏感词,将begin~position字符串替换掉
                res.append(REPLACE);
                // 进入下一个位置
                begin = ++end;
                // 重新指向根节点
                node = root;
            } else {
                // 检查下一个字符
                end++;
            }
        }

        if (node.isKeyWordEnd()) res.append("***");

        res.append(text.substring(begin));

        return res.toString();
    }

    //判断是否为符号
    private boolean isSymbol(Character c) {
        // 0x2e80 - 0x9fff 东亚文字
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2e80 || c > 0x9fff);
    }

    //敏感词替换符号
    private static final String REPLACE = "***";

    //根据敏感词是初始文件

    //根节点
    private TrieNode root = new TrieNode();

    @PostConstruct
    public void init() {
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader bf = new BufferedReader(new InputStreamReader(is))
                ) {
            String keyword;
            while ((keyword = bf.readLine()) != null) {
                addKeyword(keyword);
            }
        } catch (IOException e) {
            logger.error("加载敏感词文件错误:" + e.getMessage());
        }
    }

    //在前缀树中增加一个单词
    private void addKeyword(String keyword) {

        TrieNode tmpNode = root;
        for (char c : keyword.toCharArray()) {
            if (!tmpNode.hasNode(c)) tmpNode.addSubNode(c, new TrieNode());
            tmpNode = tmpNode.getSubNode(c);
        }
        tmpNode.setKeyWordEnd(true);
    }

    //前缀树
    private class TrieNode {
        //关键词标志(是否是结尾)
        private boolean isKeyWordEnd = false;

        //子节点(key是节点)
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }

        //添加子节点
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }
        //获取子节点
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }

        public boolean hasNode(Character c) {
            return subNodes.containsKey(c);
        }

    }

}
