package com.czk.forum.model;

/**
 * created by srdczk 2019/10/30
 */
public class Page {
    private String sign;

    private Integer page;

    private Boolean cur;

    public Boolean getCur() {
        return cur;
    }

    public void setCur(Boolean cur) {
        this.cur = cur;
    }

    public Page(String sign, Integer page, Boolean cur) {
        this.cur = cur;

        this.sign = sign;
        this.page = page;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
