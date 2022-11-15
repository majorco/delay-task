package com.majorco.consumer;

import com.majorco.config.PublishConfirmExchangeConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author xxxiao
 **/
@Slf4j
@Component
public class ConfirmQueueConsumer {

  @RabbitListener(queues = PublishConfirmExchangeConfig.CONFIRM_QUEUE_NAME)
  public void receiveMessage(Message message) {
    String msg = new String(message.getBody());
    log.info("接收到confirm队列消息：{}", msg);
  }

}
