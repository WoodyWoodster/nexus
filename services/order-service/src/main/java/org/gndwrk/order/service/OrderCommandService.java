package org.gndwrk.order.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.gndwrk.order.domain.exception.OrderCreationException;
import org.gndwrk.order.domain.exception.OrderValidationException;
import org.gndwrk.order.domain.model.Order;
import org.gndwrk.order.port.in.CreateOrderUseCase;
import org.gndwrk.order.port.in.GetOrderUseCase;
import org.gndwrk.order.port.out.OrderEventProducerPort;
import org.gndwrk.order.port.out.OrderRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class OrderCommandService implements CreateOrderUseCase, GetOrderUseCase {

  private final OrderRepositoryPort orderRepositoryPort;
  private final OrderEventProducerPort orderEventProducer;
  private final Validator validator;

  public OrderCommandService(
      OrderRepositoryPort orderRepositoryPort,
      OrderEventProducerPort orderEventProducer,
      Validator validator) {
    this.orderRepositoryPort = orderRepositoryPort;
    this.orderEventProducer = orderEventProducer;
    this.validator = validator;
  }

  @Override
  @Transactional
  public Order createOrder(Order order) {
    validateOrder(order);
    try {
      Order savedOrder = orderRepositoryPort.save(order);
      orderEventProducer.sendOrderCreatedEvent(savedOrder);
      return savedOrder;
    } catch (Exception e) {
      log.error("Failed to create order: {}", e.getMessage(), e);
      throw new OrderCreationException("Failed to create order", e);
    }
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
