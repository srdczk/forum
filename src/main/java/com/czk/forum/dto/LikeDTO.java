package com.czk.forum.dto;

public class LikeDTO {

    private Integer entityType;

    private Integer entityId;

    private Integer entityUserId;

    public Integer getEntityUserId() {
        return entityUserId;
    }

    public void setEntityUserId(Integer entityUserId) {
        this.entityUserId = entityUserId;
    }

    @Override
    public String toString() {
        return "LikeDTO{" +
                "entityType=" + entityType +
                ", entityId=" + entityId +
                ", entityUserId=" + entityUserId +
                '}';
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
