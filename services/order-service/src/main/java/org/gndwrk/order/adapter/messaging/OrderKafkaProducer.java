package org.gndwrk.order.adapter.messaging;

import lombok.extern.slf4j.Slf4j;
import org.gndwrk.order.config.KafkaTopicConfig;
import org.gndwrk.order.domain.model.Order;
import org.gndwrk.order.port.out.OrderEventProducerPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderKafkaProducer implements OrderEventProducerPort {
  private final KafkaTemplate<String, Order> kafkaTemplate;
  private final KafkaTopicConfig topics;

  public OrderKafkaProducer(KafkaTemplate<String, Order> kafkaTemplate, KafkaTopicConfig topics) {
    this.kafkaTemplate = kafkaTemplate;
    this.topics = topics;
  }

  @Override
  public void sendOrderCreatedEvent(Order order) {
    kafkaTemplate
        .send(topics.getOrderCreated(), order.getId(), order)
        .whenComplete(
            (result, ex) -> {
              if (ex != null) {
                log.error("Failed to send order created event to Kafka: {}", ex.getMessage());
              } else {
                log.info("Sent order created event: {}", result);
              }
            });
  }
}
