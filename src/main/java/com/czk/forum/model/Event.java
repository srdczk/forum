package com.czk.forum.model;

import java.util.HashMap;
import java.util.Map;

public class Event {
    // 事件的类型
    private String topic;
    // 做这件事儿的人
    private int userId;
    // 事件作用的实体
    private int entityType;

    private int entityId;
    // 实体的事件
    private int entityUserId;
    // 处理其他的事件, 可能会有其他需要的事件字段
    private Map<String, Object> data = new HashMap<>();

    // 流模式 --> set -> 返回引用

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key, Object val) {
        data.put(key, val);
        return this;
    }
}
