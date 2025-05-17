package com.api.orderReceived.event;

public record OrderReceivedEvent(
    String productSku,
    int productQuantity,
    String clientCpf,
    String paymentMethod,
    String cardNumber) {}
