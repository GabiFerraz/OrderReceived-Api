package com.api.orderReceived.entrypoint.controller;

import com.api.orderReceived.core.dto.OrderReceivedRequest;
import com.api.orderReceived.core.gateway.EventPublisher;
import com.api.orderReceived.event.OrderReceivedEvent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order-received")
public class OrderReceivedController {

  private final EventPublisher eventPublisher;

  @PostMapping
  public ResponseEntity<String> receiveOrder(
      @RequestBody @Valid final OrderReceivedRequest request) {
    log.info("Received order request for productSku: {}", request.productSku());
    final var event =
        new OrderReceivedEvent(
            request.productSku(),
            request.productQuantity(),
            request.clientCpf(),
            request.paymentMethod(),
            request.cardNumber());

    eventPublisher.publish(event);
    log.info("Order enqueued for processing: {}", event);

    return ResponseEntity.accepted().body("Order received and queued for processing");
  }
}
