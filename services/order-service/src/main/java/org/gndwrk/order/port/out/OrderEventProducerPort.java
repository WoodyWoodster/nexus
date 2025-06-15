package org.gndwrk.order.port.out;

import org.gndwrk.order.domain.model.Order;

public interface OrderEventProducerPort {
  void sendOrderCreatedEvent(Order order);
}
