package com.czk.forum.dto;

public class LikeDTO {

    private Integer entityType;

    private Integer entityId;

    private Integer entityUserId;

    private Integer postId;

    @Override
    public String toString() {
        return "LikeDTO{" +
                "entityType=" + entityType +
                ", entityId=" + entityId +
                ", entityUserId=" + entityUserId +
                ", postId=" + postId +
                '}';
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getEntityUserId() {
        return entityUserId;
    }

    public void setEntityUserId(Integer entityUserId) {
        this.entityUserId = entityUserId;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

}
