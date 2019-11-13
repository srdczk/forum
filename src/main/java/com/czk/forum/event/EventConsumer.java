package com.czk.forum.event;

import com.alibaba.fastjson.JSONObject;
import com.czk.forum.model.Event;
import com.czk.forum.model.Message;
import com.czk.forum.service.MessageService;
import com.czk.forum.util.ForumConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EventConsumer implements ForumConstant {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    // 系统的消息发布
    // 发布消息
    // 评论时候, 点赞的时候, 都发送一个通知

    @Autowired
    private MessageService messageService;

    @KafkaListener(topics = {TOPIC_COMMENT, TOPIC_FOLLOW, TOPIC_LIKE})
    public void handleMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息的内容为空");
            return;
        }

        Event event = JSONObject.parseObject((String) record.value(), Event.class);

        if (event == null) {
            logger.error("消息格式错误!");
            return;
        }
        // 1 作为系统用户, 将 1 作为系统用户
        // 系统消息 conversation_id -> topic,
        // 发送站内信
        // 发送站内通知
        Message message = new Message();
        // 系统用户id
        message.setFromId(SYSTEM_USER_ID);
        // 需要通知的userId
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        // content 实体的操作
        Map<String, Object> map = new HashMap<>();
        map.put("userId", event.getUserId());
        map.put("entityType", event.getEntityType());
        map.put("entityId", event.getEntityId());

        if (!event.getData().isEmpty()) {
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        // 消费者, 被动触发
        message.setContent(JSONObject.toJSONString(map));
        message.setContent(JSONObject.toJSONString(map));
        message.setGmtCreate(System.currentTimeMillis());
        messageService.addSystemMessage(message);
    }

}
