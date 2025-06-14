package org.gndwrk.order.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Optional;
import java.util.stream.Collectors;
import org.gndwrk.order.domain.exception.OrderValidationException;
import org.gndwrk.order.domain.model.Order;
import org.gndwrk.order.port.in.CreateOrderUseCase;
import org.gndwrk.order.port.in.GetOrderUseCase;
import org.gndwrk.order.port.out.OrderRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class OrderCommandService implements CreateOrderUseCase, GetOrderUseCase {

  private final OrderRepositoryPort orderRepositoryPort;
  private final Validator validator;

  public OrderCommandService(OrderRepositoryPort orderRepositoryPort, Validator validator) {
    this.orderRepositoryPort = orderRepositoryPort;
    this.validator = validator;
  }

  @Override
  public Order createOrder(Order order) {
    validateOrder(order);
    return orderRepositoryPort.save(order);
  }

  @Override
  public Optional<Order> getOrder(String id) {
    return orderRepositoryPort.findById(id);
  }

  private void validateOrder(Order order) {
    if (order == null) {
      throw new OrderValidationException("Order cannot be null");
    }

    var violations = validator.validate(order);
    if (!violations.isEmpty()) {
      String errorMessage =
          violations.stream()
              .map(ConstraintViolation::getMessage)
              .collect(Collectors.joining(", "));
      throw new OrderValidationException("Order validation failed: " + errorMessage + ".");
    }
  }
}
