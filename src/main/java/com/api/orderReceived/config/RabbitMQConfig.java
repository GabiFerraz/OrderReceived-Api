package com.api.orderReceived.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  public static final String EXCHANGE_NAME = "order.events";
  public static final String ORDER_RECEIVED_QUEUE = "order-received";

  @Bean
  public TopicExchange orderExchange() {
    return new TopicExchange(EXCHANGE_NAME);
  }

  @Bean
  public Queue orderReceivedQueue() {
    return new Queue(ORDER_RECEIVED_QUEUE, true);
  }

  @Bean
  public Binding orderReceivedBinding(final Queue orderReceivedQueue, final TopicExchange orderExchange) {
    return BindingBuilder.bind(orderReceivedQueue).to(orderExchange).with(ORDER_RECEIVED_QUEUE);
  }
}
