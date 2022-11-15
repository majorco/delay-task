package com.majorco.config;

import java.util.Collections;
import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xxxiao
 **/
@Configuration
@Data
@ConfigurationProperties(prefix = "delayed")
public class DelayedQueueConfiguration {

  private String queue;
  private String exchange;
  private String routingKey;

  @Bean
  public CustomExchange delayExchange() {
    return new CustomExchange(exchange, "x-delayed-message", true, false,
        Collections.singletonMap("x-delayed-type", "direct"));
  }

  @Bean
  public Queue delayQueue() {
    return new Queue(queue);
  }

  @Bean
  public Binding binding(Queue delayQueue, CustomExchange delayExchange) {
    return BindingBuilder.bind(delayQueue).to(delayExchange).with(routingKey).noargs();
  }

  @Bean
  public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(
      ConnectionFactory connectionFactory) {
    final SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();
    simpleRabbitListenerContainerFactory.setConnectionFactory(connectionFactory);
    simpleRabbitListenerContainerFactory.setMessageConverter(new Jackson2JsonMessageConverter());
    return simpleRabbitListenerContainerFactory;
  }

  /**
   * see {@link org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration}
   * springboot 自动配置 template 时会自动设置
   *
   * @return Jackson2JsonMessageConverter
   */
  @Bean
  public MessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
  }


}
