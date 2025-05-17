package com.api.orderReceived.infra.gateway.queue;

import com.api.orderReceived.config.RabbitMQConfig;
import com.api.orderReceived.core.gateway.EventPublisher;
import com.api.orderReceived.event.OrderReceivedEvent;
import com.api.orderReceived.infra.gateway.exception.GatewayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMQGateway implements EventPublisher {

  private final RabbitTemplate rabbitTemplate;

  @Override
  public void publish(final OrderReceivedEvent event) {
    try {
      rabbitTemplate.convertAndSend(
          RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ORDER_RECEIVED_QUEUE, event);
    } catch (Exception e) {
      log.error("Failed to publish event: {}", event, e);
      throw new GatewayException("Failed to publish event: " + event);
    }
  }
}
