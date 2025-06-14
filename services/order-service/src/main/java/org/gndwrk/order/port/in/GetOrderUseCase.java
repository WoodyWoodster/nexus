package org.gndwrk.order.port.in;

import java.util.Optional;
import org.gndwrk.order.domain.model.Order;

public interface GetOrderUseCase {
  Optional<Order> getOrder(String id);
}
