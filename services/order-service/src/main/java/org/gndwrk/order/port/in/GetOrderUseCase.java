package org.gndwrk.order.port.in;

import org.gndwrk.order.domain.model.Order;

import java.util.Optional;

public interface GetOrderUseCase {
    Optional<Order> getOrder(String id);
}
