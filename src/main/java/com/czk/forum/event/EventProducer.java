package com.czk.forum.event;

import com.alibaba.fastjson.JSONObject;
import com.czk.forum.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {
    // 生产者 发送消息, 需要kafkaTemplate
    @Autowired
    private KafkaTemplate kafkaTemplate;
    // 处理事件
    // 外界调用事件
    public void fireEvent(Event event) {
        // 将事件发送指定的主题
        // 将 event 转换为一个 json 字符串儿
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }
}
