package org.gndwrk.order.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.gndwrk.order.annotation.IntegrationTest;
import org.gndwrk.order.domain.model.Order;
import org.gndwrk.order.domain.model.OrderItem;
import org.gndwrk.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@IntegrationTest
@ContextConfiguration(initializers = OrderServiceIntegrationTest.Initializer.class)
class OrderServiceIntegrationTest {

  static final Network network = Network.newNetwork();

  @Container
  static final MongoDBContainer mongoDBContainer =
      new MongoDBContainer("mongo:latest").withNetwork(network).withNetworkAliases("mongodb");

  @Container
  static final KafkaContainer kafkaContainer =
      new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"))
          .withNetwork(network)
          .withNetworkAliases("kafka");

  static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      TestPropertyValues.of(
              "spring.data.mongodb.uri=" + mongoDBContainer.getReplicaSetUrl(),
              "spring.data.mongodb.database=test",
              "spring.kafka.bootstrap-servers=" + kafkaContainer.getBootstrapServers(),
              "spring.kafka.producer.bootstrap-servers=" + kafkaContainer.getBootstrapServers(),
              "spring.kafka.consumer.bootstrap-servers=" + kafkaContainer.getBootstrapServers(),
              "spring.kafka.topics.order-created=test-order-created",
              "spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer",
              "spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer")
          .applyTo(configurableApplicationContext.getEnvironment());
    }
  }

  @Autowired private OrderService orderService;

  @Autowired private MongoTemplate mongoTemplate;

  @BeforeEach
  void cleanup() {
    mongoTemplate.getCollection("orders").drop();
  }

  @Test
  @DisplayName("should create and retrieve an order successfully")
  void shouldCreateAndRetrieveOrder() {
    OrderItem item = new OrderItem("product1", 2, 29.99);
    Order order = new Order(null, "user123", List.of(item));

    Order createdOrder = orderService.createOrder(order);
    Optional<Order> retrievedOrder = orderService.getOrder(createdOrder.getId());

    assertThat(retrievedOrder)
        .isPresent()
        .hasValueSatisfying(
            o -> {
              assertThat(o.getId()).isNotNull();
              assertThat(o.getUserId()).isEqualTo("user123");
              assertThat(o.getItems())
                  .hasSize(1)
                  .first()
                  .satisfies(
                      i -> {
                        assertThat(i.getProductId()).isEqualTo("product1");
                        assertThat(i.getQuantity()).isEqualTo(2);
                        assertThat(i.getPrice()).isEqualTo(29.99);
                      });
            });
  }

  @Test
  @DisplayName("should return empty when retrieving non-existent order")
  void shouldReturnEmptyForNonExistentOrder() {
    Optional<Order> result = orderService.getOrder("non-existent-id");

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should create multiple orders for the same user")
  void shouldCreateMultipleOrdersForSameUser() {
    OrderItem item1 = new OrderItem("product1", 1, 10.0);
    OrderItem item2 = new OrderItem("product2", 2, 20.0);
    Order order1 = new Order(null, "user123", List.of(item1));
    Order order2 = new Order(null, "user123", List.of(item2));

    Order createdOrder1 = orderService.createOrder(order1);
    Order createdOrder2 = orderService.createOrder(order2);

    assertThat(createdOrder1.getId()).isNotNull();
    assertThat(createdOrder2.getId()).isNotNull();
    assertThat(createdOrder1.getId()).isNotEqualTo(createdOrder2.getId());

    Optional<Order> retrievedOrder1 = orderService.getOrder(createdOrder1.getId());
    Optional<Order> retrievedOrder2 = orderService.getOrder(createdOrder2.getId());

    assertThat(retrievedOrder1).isPresent();
    assertThat(retrievedOrder2).isPresent();
  }
}
