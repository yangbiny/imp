package com.impassive.imp.exception.serialize;

public class SerializeException extends RuntimeException {

  public SerializeException(String message) {
    super(message);
  }

  public SerializeException(Throwable cause) {
    super(cause);
  }

  public SerializeException(String message, Throwable cause) {
    super(message, cause);
  }
}
