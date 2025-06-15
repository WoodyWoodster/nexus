package org.gndwrk.order.port.out;

import java.util.Optional;
import org.gndwrk.order.domain.model.Order;

public interface OrderRepositoryPort {
  Order save(Order order);

  Optional<Order> findById(String id);
}
