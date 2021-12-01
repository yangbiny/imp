package com.impassive.imp.exception;

public class ImpNettyException extends RuntimeException {

  public ImpNettyException(String message) {
    super(message);
  }

  public ImpNettyException(String message, Throwable cause) {
    super(message, cause);
  }
}
