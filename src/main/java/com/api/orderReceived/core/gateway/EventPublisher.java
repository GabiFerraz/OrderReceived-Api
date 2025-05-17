package com.api.orderReceived.core.gateway;

import com.api.orderReceived.event.OrderReceivedEvent;

public interface EventPublisher {

  void publish(final OrderReceivedEvent event);
}
