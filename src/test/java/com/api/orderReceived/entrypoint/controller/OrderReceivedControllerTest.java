package com.api.orderReceived.entrypoint.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.api.orderReceived.core.dto.OrderReceivedRequest;
import com.api.orderReceived.core.gateway.EventPublisher;
import com.api.orderReceived.event.OrderReceivedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class OrderReceivedControllerTest {

  private static final String BASE_URL = "/api/order-received";

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private EventPublisher eventPublisher;

  @Test
  void shouldReceiveOrderSuccessfully() throws Exception {
    final var request =
        new OrderReceivedRequest("sku-123", 10, "12345678901", "CREDIT_CARD", "1234567890123456");
    final var expectedEvent =
        new OrderReceivedEvent("sku-123", 10, "12345678901", "CREDIT_CARD", "1234567890123456");

    mockMvc
        .perform(
            post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isAccepted())
        .andExpect(content().string("Order received and queued for processing"));

    final ArgumentCaptor<OrderReceivedEvent> eventCaptor =
        ArgumentCaptor.forClass(OrderReceivedEvent.class);
    verify(eventPublisher).publish(eventCaptor.capture());

    final var capturedEvent = eventCaptor.getValue();
    assertThat(capturedEvent).isEqualTo(expectedEvent);
    assertThat(capturedEvent.productSku()).isEqualTo(request.productSku());
    assertThat(capturedEvent.productQuantity()).isEqualTo(request.productQuantity());
    assertThat(capturedEvent.clientCpf()).isEqualTo(request.clientCpf());
    assertThat(capturedEvent.paymentMethod()).isEqualTo(request.paymentMethod());
    assertThat(capturedEvent.cardNumber()).isEqualTo(request.cardNumber());
  }
}
