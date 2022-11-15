package com.majorco.config;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xxxiao
 **/
@Component
@Slf4j
public class ConfirmListener implements RabbitTemplate.ConfirmCallback,
    RabbitTemplate.ReturnsCallback {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @PostConstruct
  public void setCallback(RabbitTemplate rabbitTemplate) {
    rabbitTemplate.setConfirmCallback(this);
    rabbitTemplate.setReturnsCallback(this);
  }

  @Override
  public void confirm(CorrelationData correlationData, boolean ack, String cause) {
    String id = correlationData != null ? correlationData.getId() : "";
    String message =
        correlationData != null ? new String(correlationData.getReturned().getMessage().getBody())
            : "";
    if (ack) {
      log.info("confirm ack succeed,id: {},message: {}", id, message);
    } else {
      log.error("confirm ack failed,send to backUp exchange,id: {},message: {},reason: {}", id,
          message, cause);
      final CorrelationData correlationData1 = new CorrelationData();
      correlationData1.setId(String.valueOf(System.currentTimeMillis()));
      rabbitTemplate.convertAndSend(PublishConfirmExchangeConfig.CONFIRM_EXCHANGE_NAME,
          PublishConfirmExchangeConfig.CONFIRM_ROUTING_KEY, message, correlationData1);
    }
  }

  /**
   * 消息传递过程中不可达目的地将消息返回给生产者
   */
  @Override
  public void returnedMessage(ReturnedMessage returned) {
    log.error(
        "message send failed,message: {},replyCode: {},replyText: {},exchange: {},routingKey: {}",
        new String(returned.getMessage().getBody()), returned.getReplyCode(),
        returned.getReplyText(), returned.getExchange(), returned.getRoutingKey());
  }
}











