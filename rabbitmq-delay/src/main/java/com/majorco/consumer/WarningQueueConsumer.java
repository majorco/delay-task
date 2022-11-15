package com.majorco.consumer;

import com.majorco.config.PublishConfirmExchangeConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WarningQueueConsumer {

  @RabbitListener(queues = PublishConfirmExchangeConfig.WARNING_QUEUE_NAME)
  public void receiveMsg(Message message) {
    String msg = new String(message.getBody());
    log.info("报警发现不可路由消息：{}", msg);
  }

  @RabbitListener(queues = PublishConfirmExchangeConfig.BACKUP_QUEUE_NAME)
  public void receiveMessage(Message message) {
    String msg = new String(message.getBody());
    log.info("发现不可路由消息：{}", msg);
  }
}