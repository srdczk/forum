package com.czk.forum;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaTests {
    @Autowired
    private KafkaProducer producer;


    @Test
    public void testKafka() throws InterruptedException {
        // 测试用生产者发消息, 是否能够接受
        for (int i = 0; i < 5; i++) {
            producer.sendMessage("nima", "死了吧" + i);
        }
        Thread.sleep(10000);
    }
}
@Component
class KafkaProducer {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendMessage(String topic, String content) {
        kafkaTemplate.send(topic, content);
    }

}

@Component
class KafkaConsumer {
    @KafkaListener(topics = {"nima"})
    public void handleMessage(ConsumerRecord record) {
        System.out.println(record.value());
    }
}