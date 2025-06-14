package org.gndwrk.order.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class OrderItem {
  private String productId;
  private int quantity;
  private double price;
}
