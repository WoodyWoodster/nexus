package org.gndwrk.order.integration;

import org.gndwrk.order.domain.model.Order;
import org.gndwrk.order.domain.model.OrderItem;
import org.gndwrk.order.port.in.CreateOrderUseCase;
import org.gndwrk.order.port.in.GetOrderUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false"
})
@Testcontainers
class OrderServiceIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

    @Autowired
    private CreateOrderUseCase createOrderUseCase;

    @Autowired
    private GetOrderUseCase getOrderUseCase;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void cleanup() {
        mongoTemplate.getCollection("orders").drop();
    }

    @Test
    @DisplayName("should create and retrieve an order successfully")
    void shouldCreateAndRetrieveOrder() {
        OrderItem item = new OrderItem("product1", 2, 29.99);
        Order order = new Order(null, "user123", List.of(item));

        Order createdOrder = createOrderUseCase.createOrder(order);
        Optional<Order> retrievedOrder = getOrderUseCase.getOrder(createdOrder.getId());

        assertThat(retrievedOrder)
                .isPresent()
                .hasValueSatisfying(o -> {
                    assertThat(o.getId()).isNotNull();
                    assertThat(o.getUserId()).isEqualTo("user123");
                    assertThat(o.getItems())
                            .hasSize(1)
                            .first()
                            .satisfies(i -> {
                                assertThat(i.getProductId()).isEqualTo("product1");
                                assertThat(i.getQuantity()).isEqualTo(2);
                                assertThat(i.getPrice()).isEqualTo(29.99);
                            });
                });
    }

    @Test
    @DisplayName("should return empty when retrieving non-existent order")
    void shouldReturnEmptyForNonExistentOrder() {
        Optional<Order> result = getOrderUseCase.getOrder("non-existent-id");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should create multiple orders for the same user")
    void shouldCreateMultipleOrdersForSameUser() {
        OrderItem item1 = new OrderItem("product1", 1, 10.0);
        OrderItem item2 = new OrderItem("product2", 2, 20.0);
        Order order1 = new Order(null, "user123", List.of(item1));
        Order order2 = new Order(null, "user123", List.of(item2));

        Order createdOrder1 = createOrderUseCase.createOrder(order1);
        Order createdOrder2 = createOrderUseCase.createOrder(order2);

        assertThat(createdOrder1.getId()).isNotNull();
        assertThat(createdOrder2.getId()).isNotNull();
        assertThat(createdOrder1.getId()).isNotEqualTo(createdOrder2.getId());

        Optional<Order> retrievedOrder1 = getOrderUseCase.getOrder(createdOrder1.getId());
        Optional<Order> retrievedOrder2 = getOrderUseCase.getOrder(createdOrder2.getId());

        assertThat(retrievedOrder1).isPresent();
        assertThat(retrievedOrder2).isPresent();
    }
}

