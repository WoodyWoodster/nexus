package org.gndwrk.order.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.gndwrk.order.domain.exception.OrderValidationException;
import org.gndwrk.order.domain.model.Order;
import org.gndwrk.order.domain.model.OrderItem;
import org.gndwrk.order.port.in.CreateOrderUseCase;
import org.gndwrk.order.port.in.GetOrderUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private CreateOrderUseCase createOrderUseCase;

  @MockBean private GetOrderUseCase getOrderUseCase;

  @Nested
  @DisplayName("POST /orders")
  class CreateOrder {

    @Test
    @DisplayName("should create order successfully")
    void shouldCreateOrderSuccessfully() throws Exception {
      OrderItem item = new OrderItem("product1", 1, 10.0);
      Order order = new Order(null, "user1", List.of(item));
      Order createdOrder = new Order("1", "user1", List.of(item));

      when(createOrderUseCase.createOrder(any(Order.class))).thenReturn(createdOrder);

      mockMvc
          .perform(
              post("/orders")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(order)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value("1"))
          .andExpect(jsonPath("$.userId").value("user1"));
    }

    @Test
    @DisplayName("should return bad request when validation fails")
    void shouldReturnBadRequestWhenValidationFails() throws Exception {
      OrderItem item = new OrderItem("product1", 1, 10.0);
      Order order = new Order(null, "", List.of(item));

      when(createOrderUseCase.createOrder(any(Order.class)))
          .thenThrow(new OrderValidationException("User ID cannot be blank"));

      mockMvc
          .perform(
              post("/orders")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(order)))
          .andExpect(status().isBadRequest());
    }
  }

  @Nested
  @DisplayName("GET /orders/{id}")
  class GetOrder {

    @Test
    @DisplayName("should return order when found")
    void shouldReturnOrderWhenFound() throws Exception {
      String orderId = "1";
      OrderItem item = new OrderItem("product1", 1, 10.0);
      Order order = new Order(orderId, "user1", List.of(item));

      when(getOrderUseCase.getOrder(orderId)).thenReturn(Optional.of(order));

      mockMvc
          .perform(get("/orders/{id}", orderId))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(orderId))
          .andExpect(jsonPath("$.userId").value("user1"));
    }

    @Test
    @DisplayName("should return not found when order doesn't exist")
    void shouldReturnNotFoundWhenOrderDoesNotExist() throws Exception {
      String orderId = "1";
      when(getOrderUseCase.getOrder(orderId)).thenReturn(Optional.empty());

      mockMvc.perform(get("/orders/{id}", orderId)).andExpect(status().isNotFound());
    }
  }
}
