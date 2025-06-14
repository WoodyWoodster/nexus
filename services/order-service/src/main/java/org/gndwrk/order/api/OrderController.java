package org.gndwrk.order.api;

import org.gndwrk.order.domain.model.Order;
import org.gndwrk.order.port.in.CreateOrderUseCase;
import org.gndwrk.order.port.in.GetOrderUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
  private static final Logger log = LoggerFactory.getLogger(OrderController.class);

  private final CreateOrderUseCase createOrderUseCase;
  private final GetOrderUseCase getOrderUseCase;

  public OrderController(CreateOrderUseCase createOrderUseCase, GetOrderUseCase getOrderUseCase) {
    this.createOrderUseCase = createOrderUseCase;
    this.getOrderUseCase = getOrderUseCase;
  }

  @PostMapping
  public ResponseEntity<Order> createOrder(@RequestBody Order order) {
    log.debug("Creating order: {}", order);
    Order created = createOrderUseCase.createOrder(order);
    log.debug("Created order: {}", created);
    return ResponseEntity.ok(created);
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
