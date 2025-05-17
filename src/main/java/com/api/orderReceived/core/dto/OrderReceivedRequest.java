package com.api.orderReceived.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record OrderReceivedRequest(
    @NotBlank String productSku,
    @Positive Integer productQuantity,
    @NotBlank String clientCpf,
    @NotBlank String paymentMethod,
    @NotBlank String cardNumber) {}
