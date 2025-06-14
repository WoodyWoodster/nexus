package org.gndwrk.order.api;

import lombok.extern.slf4j.Slf4j;
import org.gndwrk.order.domain.exception.OrderValidationException;
import org.gndwrk.order.domain.model.Order;
import org.gndwrk.order.port.in.CreateOrderUseCase;
import org.gndwrk.order.port.in.GetOrderUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

  private final CreateOrderUseCase createOrderUseCase;
  private final GetOrderUseCase getOrderUseCase;

  public OrderController(CreateOrderUseCase createOrderUseCase, GetOrderUseCase getOrderUseCase) {
    this.createOrderUseCase = createOrderUseCase;
    this.getOrderUseCase = getOrderUseCase;
  }

  @PostMapping
  public ResponseEntity<Order> createOrder(@RequestBody Order order) {
    try {
      log.info("Creating order: {}", order);
      Order created = createOrderUseCase.createOrder(order);
      log.info("Created order: {}", created);
      return ResponseEntity.ok(created);
    } catch (OrderValidationException e) {
      log.error("Order validation failed: {}", e.getMessage());
      return ResponseEntity.badRequest().body(null);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Order> getOrder(@PathVariable String id) {
    log.debug("Getting order with id: {}", id);
    return getOrderUseCase
        .getOrder(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
