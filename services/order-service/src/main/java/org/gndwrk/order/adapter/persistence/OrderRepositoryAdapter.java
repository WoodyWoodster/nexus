package org.gndwrk.order.adapter.persistence;

import java.util.Optional;
import org.gndwrk.order.domain.model.Order;
import org.gndwrk.order.port.out.OrderRepositoryPort;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryAdapter implements OrderRepositoryPort {

  private final MongoOrderRepository mongoOrderRepository;

  public OrderRepositoryAdapter(MongoOrderRepository mongoOrderRepository) {
    this.mongoOrderRepository = mongoOrderRepository;
  }

  @Override
  public Order save(Order order) {
    return mongoOrderRepository.save(order);
  }

  @Override
  public Optional<Order> findById(String id) {
    return mongoOrderRepository.findById(id);
  }
}
