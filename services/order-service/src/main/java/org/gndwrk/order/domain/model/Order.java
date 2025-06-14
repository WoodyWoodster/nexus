package org.gndwrk.order.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
@Document(collection = "orders")
public class Order {
  @Id private String id;

  @NotBlank(message = "User ID cannot be blank")
  private String userId;

  @NotEmpty(message = "Order must contain at least 1 item")
  @Valid
  private List<OrderItem> items;
}
