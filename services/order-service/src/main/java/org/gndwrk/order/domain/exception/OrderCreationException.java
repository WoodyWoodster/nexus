package org.gndwrk.order.domain.exception;

public class OrderCreationException extends RuntimeException {
  public OrderCreationException(String message, Throwable cause) {
    super(message, cause);
  }
}
