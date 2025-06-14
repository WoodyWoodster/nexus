package org.gndwrk.order.port.in;

import org.gndwrk.order.domain.model.Order;

public interface CreateOrderUseCase {
    Order createOrder(Order order);
}
