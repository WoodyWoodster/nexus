package org.gndwrk.order.domain.exception;

public class OrderValidationException extends RuntimeException {
  public OrderValidationException(String message) {
    super(message);
  }
}
