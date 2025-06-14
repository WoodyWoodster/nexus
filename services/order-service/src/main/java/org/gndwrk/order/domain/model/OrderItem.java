package org.gndwrk.order.domain.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class OrderItem {
  @NotBlank(message = "Product ID cannot be blank")
  private String productId;

  @Min(value = 1, message = "Quantity must be greater than 0")
  private int quantity;

  @Min(value = 0, message = "Price can't be negative")
  private double price;
}
