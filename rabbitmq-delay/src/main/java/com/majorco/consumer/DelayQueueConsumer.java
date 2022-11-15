package com.majorco.consumer;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author xiao
 */
@Component
@Slf4j
public class DelayQueueConsumer {

    /**
     * 接收消息
     */
    @RabbitListener(queues = "${delayed.queue}")
    public void receiveDelayedMessage(Message message) {
        String s = new String(message.getBody());
        log.info("当前时间：{}，收到延迟队列消息：{}", Instant.now().plus(8L, ChronoUnit.HOURS), s);
    }
}









