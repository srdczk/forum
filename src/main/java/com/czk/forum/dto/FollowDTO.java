package com.czk.forum.dto;

public class FollowDTO {

    private Integer entityType;

    private Integer entityId;

    @Override
    public String toString() {
        return "FollowDTO{" +
                "entityType=" + entityType +
                ", entityId=" + entityId +
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
