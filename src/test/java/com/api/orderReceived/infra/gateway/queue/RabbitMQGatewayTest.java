package com.api.orderReceived.infra.gateway.queue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.api.orderReceived.config.RabbitMQConfig;
import com.api.orderReceived.event.OrderReceivedEvent;
import com.api.orderReceived.infra.gateway.exception.GatewayException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

class RabbitMQGatewayTest {

  private final RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
  private final RabbitMQGateway rabbitMQGateway = new RabbitMQGateway(rabbitTemplate);

  @Test
  void shouldPublishOrderReceivedEventSuccessfully() {
    final var event =
        new OrderReceivedEvent("sku-123", 10, "12345678901", "CREDIT_CARD", "1234567890123456");

    rabbitMQGateway.publish(event);

    final ArgumentCaptor<String> exchangeCaptor = ArgumentCaptor.forClass(String.class);
    final ArgumentCaptor<String> routingKeyCaptor = ArgumentCaptor.forClass(String.class);
    final ArgumentCaptor<OrderReceivedEvent> eventCaptor =
        ArgumentCaptor.forClass(OrderReceivedEvent.class);

    verify(rabbitTemplate)
        .convertAndSend(
            exchangeCaptor.capture(), routingKeyCaptor.capture(), eventCaptor.capture());

    assertThat(exchangeCaptor.getValue()).isEqualTo(RabbitMQConfig.EXCHANGE_NAME);
    assertThat(routingKeyCaptor.getValue()).isEqualTo(RabbitMQConfig.ORDER_RECEIVED_QUEUE);
    assertThat(eventCaptor.getValue()).isEqualTo(event);
    assertThat(eventCaptor.getValue().productSku()).isEqualTo("sku-123");
    assertThat(eventCaptor.getValue().productQuantity()).isEqualTo(10);
    assertThat(eventCaptor.getValue().clientCpf()).isEqualTo("12345678901");
    assertThat(eventCaptor.getValue().paymentMethod()).isEqualTo("CREDIT_CARD");
    assertThat(eventCaptor.getValue().cardNumber()).isEqualTo("1234567890123456");
  }

  @Test
  void shouldThrowGatewayExceptionWhenPublishFails() {
    final var event =
        new OrderReceivedEvent("sku-123", 10, "12345678901", "CREDIT_CARD", "1234567890123456");

    doThrow(new RuntimeException("AMQP error"))
        .when(rabbitTemplate)
        .convertAndSend(any(String.class), any(String.class), any(OrderReceivedEvent.class));

    assertThatThrownBy(() -> rabbitMQGateway.publish(event))
        .isInstanceOf(GatewayException.class)
        .hasMessage("Failed to publish event: " + event);

    verify(rabbitTemplate)
        .convertAndSend(
            eq(RabbitMQConfig.EXCHANGE_NAME), eq(RabbitMQConfig.ORDER_RECEIVED_QUEUE), eq(event));
  }
}
