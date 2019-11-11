package com.czk.forum.dto;

public class MessageDTO {

    private String toName;

    private String content;

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "toName='" + toName + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

