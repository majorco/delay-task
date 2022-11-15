package com.majorco;

import com.majorco.config.DelayedQueueConfiguration;
import com.majorco.delay.RabbitDelayTask;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author xxxiao
 **/
@Component
public class Init implements ApplicationRunner {

  @Autowired
  private DelayedQueueConfiguration configuration;
  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Override
  public void run(ApplicationArguments args) {
    for (int i = 0; i < 100; i++) {
      rabbitTemplate.convertAndSend(configuration.getExchange(), configuration.getRoutingKey(),
          RabbitDelayTask.builder().taskId(String.valueOf(i)).build(), message -> {
            message.getMessageProperties().setDelay(5000);
            return message;
          });
    }
  }
}








