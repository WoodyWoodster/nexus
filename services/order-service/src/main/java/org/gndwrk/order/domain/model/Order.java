package org.gndwrk.order.domain.model;

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
  private String product;
  private int quantity;
}
