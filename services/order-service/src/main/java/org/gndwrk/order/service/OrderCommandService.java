package org.gndwrk.order.service;

import org.gndwrk.order.domain.model.Order;
import org.gndwrk.order.port.in.CreateOrderUseCase;
import org.gndwrk.order.port.in.GetOrderUseCase;
import org.gndwrk.order.port.out.OrderRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderCommandService implements CreateOrderUseCase, GetOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    public OrderCommandService(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepositoryPort.save(order);
    }

    @Override
    public Optional<Order> getOrder(String id) {
        return orderRepositoryPort.findById(id);
    }
}
