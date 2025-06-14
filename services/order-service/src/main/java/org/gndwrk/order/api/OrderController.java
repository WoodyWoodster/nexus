package org.gndwrk.order.api;

import org.gndwrk.order.domain.model.Order;
import org.gndwrk.order.port.in.CreateOrderUseCase;
import org.gndwrk.order.port.in.GetOrderUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    Order created = createOrderUseCase.createOrder(order);
    return ResponseEntity.ok(created);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Order> getOrder(@PathVariable String id) {
    return getOrderUseCase
        .getOrder(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
