package com.czk.forum.dao;

/**
 * created by srdczk 2019/11/4
 */
public class PostDTO {

    private String title;

    private String content;

    @Override
    public String toString() {
        return "PostDTO{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
