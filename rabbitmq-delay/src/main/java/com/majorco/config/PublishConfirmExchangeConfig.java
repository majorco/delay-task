package com.majorco.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xxxiao
 **/
@Configuration
public class PublishConfirmExchangeConfig {

  /**
   * 确认交换机
   */
  public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";

  public static final String CONFIRM_QUEUE_NAME = "confirm_queue";

  public static final String CONFIRM_ROUTING_KEY = "confirm_key";

  /**
   * 备份交换机 队列
   */
  public static final String BACKUP_EXCHANGE_NAME = "backup_exchange";

  public static final String BACKUP_QUEUE_NAME = "backup_queue";
  /**
   * 报警队列
   */
  public static final String WARNING_QUEUE_NAME = "warning_queue";

  /**
   * fanout 发送给所有跟此交换机绑定的队列
   *
   * @return FanoutExchange
   */
  @Bean
  public FanoutExchange backupExchange() {
    return ExchangeBuilder.fanoutExchange(BACKUP_EXCHANGE_NAME).build();
  }

  @Bean
  public Queue backupQueue() {
    return new Queue(BACKUP_QUEUE_NAME);
  }

  @Bean
  public Binding backupBinding(Queue backupQueue, FanoutExchange backupExchange) {
    return BindingBuilder.bind(backupQueue).to(backupExchange);
  }

  /**
   * alternate 候补
   *
   * @return DirectExchange
   */
  @Bean
  public DirectExchange confirmExchange() {
    return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).alternate(BACKUP_EXCHANGE_NAME)
        .build();
  }

  @Bean
  public Queue confirmQueue() {
    return new Queue(CONFIRM_QUEUE_NAME);
  }

  @Bean
  public Binding confirmBinding(Queue confirmQueue, DirectExchange confirmExchange) {
    return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
  }

  /**
   * 告警队列
   *
   * @return Queue
   */

  @Bean
  public Queue warningQueue() {
    return new Queue(WARNING_QUEUE_NAME);
  }


}
